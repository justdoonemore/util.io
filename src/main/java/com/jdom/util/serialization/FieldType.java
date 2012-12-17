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
import java.util.Date;
import java.util.List;

import com.jdom.util.reflection.ReflectionUtils;

public enum FieldType {
	STRING {
		@Override
		String getStringRepresentation(Object value) {
			return value.toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return string;
		}
	},
	INT {
		@Override
		String getStringRepresentation(Object value) {
			return ((Integer) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Integer.valueOf(string);
		}
	},
	BYTE {
		@Override
		String getStringRepresentation(Object value) {
			return ((Byte) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Byte.valueOf(string);
		}
	},
	SHORT {
		@Override
		String getStringRepresentation(Object value) {
			return ((Short) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Short.valueOf(string);
		}
	},
	LONG {
		@Override
		String getStringRepresentation(Object value) {
			return ((Long) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Long.valueOf(string);
		}
	},
	FLOAT {
		@Override
		String getStringRepresentation(Object value) {
			return ((Float) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Float.valueOf(string);
		}
	},
	DOUBLE {
		@Override
		String getStringRepresentation(Object value) {
			return ((Double) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Double.valueOf(string);
		}
	},
	CHAR {
		@Override
		String getStringRepresentation(Object value) {
			return ((Character) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Character.valueOf(string.charAt(0));
		}
	},
	BOOL {
		@Override
		String getStringRepresentation(Object value) {
			return ((Boolean) value).toString();
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return Boolean.valueOf(string);
		}
	},
	ARRAY {
		@Override
		String getStringRepresentation(Object value) {
			return value.getClass().getComponentType().getName() + "["
					+ Integer.toString(Array.getLength(value)) + "]";
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			String[] split = string.split("\\[");
			String numberOfElements = split[1].substring(0,
					split[1].indexOf("]"));
			return new Object[] { ReflectionUtils.forName(split[0]),
					Integer.valueOf(numberOfElements) };
		}
	},
	ENUM {
		@Override
		String getStringRepresentation(Object value) {
			return ((Enum<?>) value).name();
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Object getValueRepresentation(String string, String fieldClass) {
			Class enumClass = ReflectionUtils.forName(fieldClass);
			return Enum.valueOf(enumClass, string);
		}
	},
	JAVA_DATE {
		@Override
		String getStringRepresentation(Object value) {
			return Long.toString(((Date) value).getTime());
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			return new Date(Long.parseLong(string));
		}
	},
	LIST {
		@Override
		String getStringRepresentation(Object value) {
			return value.getClass().getName() + "["
					+ Integer.toString(((List<?>) value).size()) + "]";
		}

		@Override
		Object getValueRepresentation(String string, String fieldClass) {
			String[] split = string.split("\\[");
			String numberOfElements = split[1].substring(0,
					split[1].indexOf("]"));
			return new Object[] { ReflectionUtils.forName(split[0]),
					Integer.valueOf(numberOfElements) };
		}
	};

	public void writeField(SerializationContext serializationContext,
			String name, Object value) {
		String fieldValue = (value == null) ? "null_value"
				: getStringRepresentation(value);
		String fieldClass = (value == null) ? "null_value" : value.getClass()
				.getName();
		serializationContext.writeField(new Field(name, fieldValue, this,
				fieldClass));
	}

	public Object getValue(String string, String fieldClass) {
		return ("null_value".equals(string)) ? null : getValueRepresentation(
				string, fieldClass);
	}

	abstract String getStringRepresentation(Object value);

	abstract Object getValueRepresentation(String string, String fieldClass);

	public static <T> FieldType getForClass(Class<T> fieldType) {
		if (fieldType == int.class || fieldType == Integer.class) {
			return FieldType.INT;
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			return FieldType.BYTE;
		} else if (fieldType == String.class) {
			return FieldType.STRING;
		} else if (fieldType == short.class || fieldType == Short.class) {
			return FieldType.SHORT;
		} else if (fieldType == long.class || fieldType == Long.class) {
			return FieldType.LONG;
		} else if (fieldType == float.class || fieldType == Float.class) {
			return FieldType.FLOAT;
		} else if (fieldType == double.class || fieldType == Double.class) {
			return FieldType.DOUBLE;
		} else if (fieldType == char.class || fieldType == Character.class) {
			return FieldType.CHAR;
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			return FieldType.BOOL;
		} else if (fieldType.isArray()) {
			return FieldType.ARRAY;
		} else if (fieldType.isEnum()) {
			return FieldType.ENUM;
		} else if (Date.class.isAssignableFrom(fieldType)) {
			return FieldType.JAVA_DATE;
		} else if (List.class.isAssignableFrom(fieldType)) {
			return FieldType.LIST;
		} else {
			throw new IllegalArgumentException(
					"Don't know how to serialize field type ["
							+ fieldType.getName() + "]");
		}
	}
}
