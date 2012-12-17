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

public interface SerializationContext {

	public static class SerializationFieldWriterAdapter implements
			SerializationFieldWriter {

		private static final String EMPTY_FIELD_NAME = "no_field_name";

		private final SerializationContext serializer;

		public SerializationFieldWriterAdapter(SerializationContext context) {
			this.serializer = context;
		}

		@Override
		public void writeInt(int value) {
			FieldType.INT.writeField(serializer, EMPTY_FIELD_NAME, value);
		}

		@Override
		public void writeByte(byte value) {
			FieldType.BYTE.writeField(serializer, EMPTY_FIELD_NAME, value);
		}

		@Override
		public void writeString(String value) {
			FieldType.STRING.writeField(serializer, EMPTY_FIELD_NAME, value);
		}
	}

	void writeStart();

	void writeEnd();

	String getOutput();

	void writeClassName(String simpleName);

	void writeInt(String name, int value);

	void writeByte(String name, byte value);

	void writeString(String name, String value);

	void writeField(Field field);
}
