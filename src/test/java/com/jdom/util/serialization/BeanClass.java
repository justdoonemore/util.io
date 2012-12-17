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

import com.jdom.util.serialization.DynamicSerialize;

@DynamicSerialize
public class BeanClass {

	private String stringField;

	private int intField;

	private transient int transientField;

	public BeanClass() {

	}

	public BeanClass(String stringField, int intField) {
		this.stringField = stringField;
		this.intField = intField;
		this.transientField = intField + 1;
	}

	public String getStringField() {
		return stringField;
	}

	public int getIntField() {
		return intField;
	}

	public int getTransientField() {
		return transientField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public void setIntField(int intField) {
		this.intField = intField;
	}
}
