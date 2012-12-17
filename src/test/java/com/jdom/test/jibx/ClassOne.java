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
 */package com.jdom.test.jibx;

import com.jdom.test.jibx.dep.ClassTwo;

public class ClassOne {

    private String testString;

	private Long testLong;
    
    private ClassTwo classTwo;

    public String getTestString() {
		return testString;
	}

	public void setTestString(String testString) {
		this.testString = testString;
	}

	public Long getTestLong() {
		return testLong;
	}

	public void setTestLong(Long testLong) {
		this.testLong = testLong;
	}

	public ClassTwo getClassTwo() {
		return classTwo;
	}

	public void setClassTwo(ClassTwo classTwo) {
		this.classTwo = classTwo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classTwo == null) ? 0 : classTwo.hashCode());
		result = prime * result
				+ ((testLong == null) ? 0 : testLong.hashCode());
		result = prime * result
				+ ((testString == null) ? 0 : testString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassOne other = (ClassOne) obj;
		if (classTwo == null) {
			if (other.classTwo != null)
				return false;
		} else if (!classTwo.equals(other.classTwo))
			return false;
		if (testLong == null) {
			if (other.testLong != null)
				return false;
		} else if (!testLong.equals(other.testLong))
			return false;
		if (testString == null) {
			if (other.testString != null)
				return false;
		} else if (!testString.equals(other.testString))
			return false;
		return true;
	}
}
