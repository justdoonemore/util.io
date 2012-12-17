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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jdom.util.serialization.DynamicSerializationManager;
import com.jdom.util.serialization.XmlDeserializationContext;
import com.jdom.util.serialization.XmlSerializationContext;
import com.jdom.util.serialization.JavaObjectClass.ENUM;

public class DynamicSerializationManagerTest {

	private final DynamicSerializationManager manager = new DynamicSerializationManager(
			new XmlSerializationContext(), new XmlDeserializationContext());

	private final AnnotatedClass annotatedClass = new AnnotatedClass(
			"someString", 2);

	@Test(expected = IllegalArgumentException.class)
	public void testThrowsExceptionOnNonAnnotatedClass() {
		manager.serialize(new NonAnnotatedClass());
	}

	@Test
	public void testDoesNotThrowExceptionOnAnnotatedClass() {
		manager.serialize(annotatedClass);
	}

	@Test
	public void testFindsFieldsNotMarkedTransient() throws SecurityException,
			NoSuchFieldException {
		List<Field> fields = manager.getFields(annotatedClass);
		assertEquals(2, fields.size());
	}

	@Test
	public void testFindsFieldsFromSuperClass() throws SecurityException,
			NoSuchFieldException {
		AnnotatedSubClass subclass = new AnnotatedSubClass("someString", 2,
				"subClass");
		List<Field> fields = manager.getFields(subclass);
		assertEquals(3, fields.size());
	}

	@Test
	public void testSerializeWritesOutEntireObject() {
		AnnotatedSubClass subclass = new AnnotatedSubClass("someString", 2,
				"subClass");
		String serialized = manager.serialize(subclass);

		assertEquals(
				"<serialized>"
						+ "<className>com.jdom.util.serialization.AnnotatedSubClass</className>"
						+ "<field><fieldName>subField</fieldName><fieldValue>subClass</fieldValue><fieldType>STRING</fieldType><fieldClass>java.lang.String</fieldClass></field>"
						+ "<field><fieldName>stringField</fieldName><fieldValue>someString</fieldValue><fieldType>STRING</fieldType><fieldClass>java.lang.String</fieldClass></field>"
						+ "<field><fieldName>intField</fieldName><fieldValue>2</fieldValue><fieldType>INT</fieldType><fieldClass>java.lang.Integer</fieldClass></field>"
						+ "</serialized>",

				serialized);
	}

	@Test
	public void testDeserializeRecreatesBeanObject() {
		BeanClass bean = new BeanClass("someString", 2);
		BeanClass restored = manager.deserialize(BeanClass.class,
				manager.serialize(bean));
		assertEquals("someString", restored.getStringField());
		assertEquals(2, restored.getIntField());
	}

	@Test
	public void testNullValuesAreHandled() {
		BeanClass bean = new BeanClass(null, 2);
		BeanClass restored = manager.deserialize(BeanClass.class,
				manager.serialize(bean));
		assertNull(restored.getStringField());
		assertEquals(2, restored.getIntField());
	}

	@Test
	public void testDeserializeRecreatesSubclassOfBeanObject() {
		SubBeanClass bean = new SubBeanClass("someString", 2, (byte) 8);
		SubBeanClass restored = manager.deserialize(SubBeanClass.class,
				manager.serialize(bean));
		assertEquals("someString", restored.getStringField());
		assertEquals(2, restored.getIntField());
		assertEquals(8, restored.getByteField());
	}

	@Test
	public void testBeanCanSpecifySerializer() {
		ClassWithSerializer bean = new ClassWithSerializer("someString", 2);
		ClassWithSerializer restored = manager.deserialize(
				ClassWithSerializer.class, manager.serialize(bean));
		assertEquals("someString", restored.getStringField());
		assertEquals(2, restored.getIntField());
	}

	@Test
	public void testSerializerHandlesNull() {
		ClassWithSerializer bean = new ClassWithSerializer(null, 2);
		ClassWithSerializer restored = manager.deserialize(
				ClassWithSerializer.class, manager.serialize(bean));
		assertNull(restored.getStringField());
		assertEquals(2, restored.getIntField());
	}

	@Test
	public void testPrimitiveDataTypes() {
		byte byteField = Byte.MAX_VALUE;
		short shortField = Short.MAX_VALUE;
		int intField = Integer.MAX_VALUE;
		long longField = Long.MAX_VALUE;
		float floatField = Float.MAX_VALUE;
		double doubleField = Double.MAX_VALUE;
		char charField = Character.MAX_VALUE;
		String stringField = "someString";
		boolean booleanField = true;

		PrimitiveDataTypeBean bean = new PrimitiveDataTypeBean();
		bean.setByteField(byteField);
		bean.setShortField(shortField);
		bean.setIntField(intField);
		bean.setLongField(longField);
		bean.setFloatField(floatField);
		bean.setDoubleField(doubleField);
		bean.setCharField(charField);
		bean.setStringField(stringField);
		bean.setBooleanField(booleanField);

		PrimitiveDataTypeBean restored = manager.deserialize(
				PrimitiveDataTypeBean.class, manager.serialize(bean));
		assertEquals(byteField, restored.getByteField());
		assertEquals(shortField, restored.getShortField());
		assertEquals(intField, restored.getIntField());
		assertEquals(longField, restored.getLongField());
		assertEquals(floatField, restored.getFloatField(), 0.00001);
		assertEquals(doubleField, restored.getDoubleField(), 0.00001);
		assertEquals(charField, restored.getCharField());
		assertEquals(stringField, restored.getStringField());
		assertEquals(booleanField, restored.isBooleanField());
	}

	@Test
	public void testWrapperDataTypes() {
		Byte byteField = Byte.MAX_VALUE;
		Short shortField = Short.MAX_VALUE;
		Integer intField = Integer.MAX_VALUE;
		Long longField = Long.MAX_VALUE;
		Float floatField = Float.MAX_VALUE;
		Double doubleField = Double.MAX_VALUE;
		Character charField = Character.MAX_VALUE;
		Boolean booleanField = true;

		WrapperDataTypeBean bean = new WrapperDataTypeBean();
		bean.setByteField(byteField);
		bean.setShortField(shortField);
		bean.setIntegerField(intField);
		bean.setLongField(longField);
		bean.setFloatField(floatField);
		bean.setDoubleField(doubleField);
		bean.setCharacterField(charField);
		bean.setBooleanField(booleanField);

		WrapperDataTypeBean restored = manager.deserialize(
				WrapperDataTypeBean.class, manager.serialize(bean));
		assertEquals(byteField, restored.getByteField());
		assertEquals(shortField, restored.getShortField());
		assertEquals(intField, restored.getIntegerField());
		assertEquals(longField, restored.getLongField());
		assertEquals(floatField, restored.getFloatField(), 0.00001);
		assertEquals(doubleField, restored.getDoubleField(), 0.00001);
		assertEquals(charField, restored.getCharacterField());
		assertEquals(booleanField, restored.isBooleanField());
	}

	@Test
	public void testArrayType() {

		ArrayBean bean = new ArrayBean();
		String[] array = new String[] { "one", "two" };
		bean.setArray(array);

		ArrayBean restored = manager.deserialize(ArrayBean.class,
				manager.serialize(bean));

		assertTrue("Expected the arrays to be equal!",
				Arrays.equals(array, restored.getArray()));
	}

	@Test
	public void testNullArrayType() {

		ArrayBean bean = new ArrayBean();
		String[] array = null;
		bean.setArray(array);

		ArrayBean restored = manager.deserialize(ArrayBean.class,
				manager.serialize(bean));

		assertNull("Expected null array!", restored.getArray());
	}

	@Test
	public void testEmptyArrayType() {

		ArrayBean bean = new ArrayBean();
		String[] array = new String[0];
		bean.setArray(array);

		ArrayBean restored = manager.deserialize(ArrayBean.class,
				manager.serialize(bean));

		assertTrue("Expected the arrays to be equal!",
				Arrays.equals(array, restored.getArray()));
	}

	@Test
	public void testEnumType() {
		JavaObjectClass bean = new JavaObjectClass();
		bean.setEnumValue(ENUM.TWO);

		String serialized = manager.serialize(bean);
		JavaObjectClass restored = manager.deserialize(JavaObjectClass.class,
				serialized);

		assertEquals("Expected the enum to be equal!", ENUM.TWO,
				restored.getEnumValue());
	}

	@Test
	public void testDateType() {
		JavaObjectClass bean = new JavaObjectClass();
		Date date = new Date();
		bean.setDate(date);

		String serialized = manager.serialize(bean);
		JavaObjectClass restored = manager.deserialize(JavaObjectClass.class,
				serialized);

		assertEquals("Expected the date to be equal!", date.getTime(), restored
				.getDate().getTime());
	}

	@Test
	public void testListType() {
		JavaCollectionClass bean = new JavaCollectionClass();
		List<String> list = new ArrayList<String>();
		list.add("one");
		list.add("two");
		bean.setStringList(list);

		String serialized = manager.serialize(bean);
		JavaCollectionClass restored = manager.deserialize(
				JavaCollectionClass.class, serialized);

		assertTrue(restored.getStringList().contains("one"));
		assertTrue(restored.getStringList().contains("two"));
	}
}
