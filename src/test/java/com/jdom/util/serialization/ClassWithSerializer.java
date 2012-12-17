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

import com.jdom.util.serialization.DeserializationFieldReader;
import com.jdom.util.serialization.DynamicSerialize;
import com.jdom.util.serialization.DynamicSerializer;
import com.jdom.util.serialization.SerializationFieldWriter;
import com.jdom.util.serialization.ClassWithSerializer.CustomSerializer;

@DynamicSerialize(CustomSerializer.class)
public class ClassWithSerializer {

	public static class CustomSerializer implements
			DynamicSerializer<ClassWithSerializer> {

		@Override
		public void serialize(SerializationFieldWriter serializer,
				ClassWithSerializer obj) {
			serializer.writeString(obj.getStringField());
			serializer.writeInt(obj.getIntField());
		}

		@Override
		public ClassWithSerializer deserialize(
				DeserializationFieldReader deserializer) {
			return new ClassWithSerializer(deserializer.readString(),
					deserializer.readInt());
		}
	}

	private final String stringField;

	private final int intField;

	public ClassWithSerializer(String stringField, int intField) {
		this.stringField = stringField;
		this.intField = intField;
	}

	public String getStringField() {
		return stringField;
	}

	public int getIntField() {
		return intField;
	}
}
