/** 
 *  Copyright (C) 2012  Just Do One More
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */package com.jdom.util.serialization;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.jdom.util.reflection.ReflectionUtils;
import com.jdom.util.serialization.SerializationContext.SerializationFieldWriterAdapter;

public class DynamicSerializationManager {

	private final SerializationContext serializationContext;

	private final DeserializationContext deserializationContext;

	public DynamicSerializationManager(
			SerializationContext serializationContext,
			DeserializationContext deserializationContext) {
		this.serializationContext = serializationContext;
		this.deserializationContext = deserializationContext;
	}

	// TODO: Still to handle: Collections other than list, user classes, nested
	// classes
	@SuppressWarnings("unchecked")
	public <T> String serialize(T obj) {
		Class<?> clazz = obj.getClass();
		DynamicSerialize annotation = clazz
				.getAnnotation(DynamicSerialize.class);
		if (annotation == null) {
			throw new IllegalArgumentException(clazz.getName()
					+ " is not annotated with DynamicSerialize!");
		}

		serializationContext.writeStart();
		serializationContext.writeClassName(clazz.getName());

		Class<? extends DynamicSerializer<?>> serializerClass = annotation
				.value();
		// Custom serializer
		if (DynamicSerializer.NullSerializer.class != serializerClass) {
			DynamicSerializer<T> serializer = (DynamicSerializer<T>) ReflectionUtils
					.newInstance(serializerClass);
			serializer.serialize(new SerializationFieldWriterAdapter(
					serializationContext), obj);
		} else {
			// Normal serialization
			List<java.lang.reflect.Field> fields = getFields(obj);
			for (java.lang.reflect.Field field : fields) {
				Object propertyValue = ReflectionUtils.getPropertyValue(obj,
						field.getName());
				Class<?> fieldType = field.getType();
				FieldType type = FieldType.getForClass(fieldType);
				type.writeField(serializationContext, field.getName(),
						propertyValue);
				if (fieldType.isArray() && propertyValue != null) {
					Object[] array = (Object[]) propertyValue;
					if (array.length > 0) {
						Class<?> itemType = array[0].getClass();
						type = FieldType.getForClass(itemType);
						for (int i = 0; i < array.length; i++) {
							type.writeField(serializationContext,
									"arrayElement[" + i + "]", array[i]);
						}
					}
				} else if (List.class.isAssignableFrom(fieldType)
						&& propertyValue != null) {
					List<?> list = (List<?>) propertyValue;
					if (!list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							Object listObj = list.get(i);
							Class<?> itemType = listObj.getClass();
							type = FieldType.getForClass(itemType);
							type.writeField(serializationContext, "list[" + i
									+ "]", listObj);
						}
					}
				}
			}
		}

		serializationContext.writeEnd();

		return serializationContext.getOutput();
	}

	public List<java.lang.reflect.Field> getFields(Object obj) {
		List<java.lang.reflect.Field> fields = new ArrayList<java.lang.reflect.Field>();
		Class<?> clazz = obj.getClass();

		while (clazz != Object.class) {
			java.lang.reflect.Field[] classFields = clazz.getDeclaredFields();
			for (java.lang.reflect.Field field : classFields) {
				int fieldModifiers = field.getModifiers();
				if (java.lang.reflect.Modifier.isTransient(fieldModifiers)) {
					continue;
				}
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}

		return fields;
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(Class<T> clazz, String string) {
		try {

			deserializationContext.init(string);

			String className = deserializationContext.readClassName();
			Class<?> serializedClassType = ReflectionUtils.forName(className);

			// Custom serializer
			DynamicSerialize annotation = serializedClassType
					.getAnnotation(DynamicSerialize.class);
			Class<? extends DynamicSerializer<?>> customSerializer = annotation
					.value();
			if (DynamicSerializer.NullSerializer.class != customSerializer) {
				DynamicSerializer<T> serializer = (DynamicSerializer<T>) ReflectionUtils
						.newInstance(customSerializer);
				return serializer.deserialize(deserializationContext);
			}
			// End custom serializer

			T obj = ReflectionUtils.newInstanceOfAssignableType(clazz,
					className);

			Field field = deserializationContext.readField();
			while (field != Field.NULL) {
				Object value = field.fieldType.getValue(field.fieldValue,
						field.fieldClass);

				if (value != null) {
					if (field.fieldType == FieldType.ARRAY) {
						Object[] arrayInformation = (Object[]) value;
						Class<?> arrayElementType = (Class<?>) arrayInformation[0];
						int numberOfElements = ((Integer) arrayInformation[1])
								.intValue();

						Object array = Array.newInstance(arrayElementType,
								numberOfElements);
						if (numberOfElements > 0) {
							for (int i = 0; i < numberOfElements; i++) {
								Field arrayElementField = deserializationContext
										.readField();
								Array.set(array, i, arrayElementField.fieldType
										.getValue(arrayElementField.fieldValue,
												arrayElementField.fieldClass));
							}
						}
						ReflectionUtils.setPropertyValue(obj, field.fieldName,
								array);
					} else if (field.fieldType == FieldType.LIST) {
						Object[] arrayInformation = (Object[]) value;
						Class<?> arrayElementType = (Class<?>) arrayInformation[0];
						int numberOfElements = ((Integer) arrayInformation[1])
								.intValue();

						List<Object> list = ReflectionUtils
								.newInstanceOfAssignableType(List.class,
										arrayElementType.getName());
						if (numberOfElements > 0) {
							for (int i = 0; i < numberOfElements; i++) {
								Field arrayElementField = deserializationContext
										.readField();
								list.add(arrayElementField.fieldType.getValue(
										arrayElementField.fieldValue,
										arrayElementField.fieldClass));
							}
						}
						ReflectionUtils.setPropertyValue(obj, field.fieldName,
								list);
					} else {
						ReflectionUtils.setPropertyValue(obj, field.fieldName,
								value);
					}
				}
				field = deserializationContext.readField();
			}

			return obj;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
