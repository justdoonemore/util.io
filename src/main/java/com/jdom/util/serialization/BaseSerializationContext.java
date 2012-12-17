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

public abstract class BaseSerializationContext implements SerializationContext {

	protected final StringBuilder output = new StringBuilder();

	@Override
	public String getOutput() {
		return output.toString();
	}

	@Override
	public void writeInt(String name, int value) {
		writeField(new Field(name, Integer.toString(value), FieldType.INT,
				int.class.getName()));
	}

	@Override
	public void writeByte(String name, byte value) {
		writeField(new Field(name, Byte.toString(value), FieldType.BYTE,
				byte.class.getName()));
	}

	@Override
	public void writeString(String name, String value) {
		writeField(new Field(name, value, FieldType.STRING,
				String.class.getName()));
	}
}
