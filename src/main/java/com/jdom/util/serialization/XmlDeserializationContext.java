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

public class XmlDeserializationContext extends BaseDeserializationContext {

	public XmlDeserializationContext() {
	}

	@Override
	public void initInternal() {
		this.text.delete(0, "<serialized>".length());
		this.text.delete(this.text.length() - "</serialized>".length(),
				this.text.length());
	}

	@Override
	public Field readFieldInternal() {
		this.text.delete(0, "<field>".length());
		String fieldName = extractField("fieldName");
		String fieldValue = extractField("fieldValue");
		FieldType type = FieldType.valueOf(extractField("fieldType"));
		String className = extractField("fieldClass");
		this.text.delete(0, "</field>".length());

		return new Field(fieldName, fieldValue, type, className);
	}

	@Override
	protected String extractField(String fieldName) {
		this.text.delete(0, ("<" + fieldName + ">").length());
		int endIndex = this.text.indexOf("</" + fieldName + ">");
		String value = this.text.substring(0, endIndex);
		this.text.delete(0, endIndex);
		this.text.delete(0, ("</" + fieldName + ">").length());

		return value;
	}

	@Override
	protected boolean isFieldsRemaining() {
		return this.text.indexOf("<field>") != -1;
	}
}
