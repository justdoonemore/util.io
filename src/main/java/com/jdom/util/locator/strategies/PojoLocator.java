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
 */package com.jdom.util.locator.strategies;

import java.util.regex.Pattern;

import com.jdom.util.locator.ServiceLocatorException;
import com.jdom.util.reflection.ReflectionException;
import com.jdom.util.reflection.ReflectionUtils;

public class PojoLocator implements Locator {

	private static final Pattern REPLACE_SERVICE_PATTERN = Pattern
			.compile("Service$");

	@Override
	public <T> T locate(Class<T> interfaceClass) {
		String implClassPath = REPLACE_SERVICE_PATTERN.matcher(
				interfaceClass.getName()).replaceFirst("");

		try {
			return ReflectionUtils.newInstanceOfAssignableType(interfaceClass,
					implClassPath);
		} catch (ReflectionException e) {
			implClassPath = REPLACE_SERVICE_PATTERN.matcher(
					interfaceClass.getPackage().getName() + ".impl."
							+ interfaceClass.getSimpleName()).replaceFirst("");

			try {
				return ReflectionUtils.newInstanceOfAssignableType(
						interfaceClass, implClassPath);
			} catch (ReflectionException e2) {
				throw new ServiceLocatorException(e);
			}
		}
	}

}
