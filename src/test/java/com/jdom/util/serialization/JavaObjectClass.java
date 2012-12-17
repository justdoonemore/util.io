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

import java.util.Date;

import com.jdom.util.serialization.DynamicSerialize;

@DynamicSerialize
public class JavaObjectClass {

	public static enum ENUM {
		ONE, TWO, THREE;
	}

	private ENUM enumValue;

	private Date date;

	public ENUM getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(ENUM enumValue) {
		this.enumValue = enumValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
