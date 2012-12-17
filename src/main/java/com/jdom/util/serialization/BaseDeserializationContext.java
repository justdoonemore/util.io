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

public abstract class BaseDeserializationContext implements
		DeserializationContext {

	protected StringBuilder text;

	@Override
	public final void init(String value) {
		this.text = new StringBuilder(value);

		initInternal();
	}

	@Override
	public final Field readField() {
		if (!isFieldsRemaining()) {
			return Field.NULL;
		}

		return readFieldInternal();
	}

	@Override
	public String readClassName() {
		return extractField("className");
	}

	@Override
	public int readInt() {
		Field field = readField();
		return ((Integer) field.fieldType.getValue(field.fieldValue,
				field.fieldClass)).intValue();
	}

	@Override
	public byte readByte() {
		Field field = readField();
		return ((Byte) field.fieldType.getValue(field.fieldValue,
				field.fieldClass)).byteValue();
	}

	@Override
	public String readString() {
		Field field = readField();
		return ((String) field.fieldType.getValue(field.fieldValue,
				field.fieldClass));
	}

	protected abstract String extractField(String fieldName);

	protected abstract boolean isFieldsRemaining();

	protected abstract Field readFieldInternal();

	protected abstract void initInternal();
}
