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

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import com.jdom.logging.api.LogFactory;import com.jdom.logging.api.Logger;

import com.jdom.util.io.IOUtil;
import com.jdom.util.properties.PropertiesUtil;

/**
 * Used to find EJBs within a JBoss 7 server that are contained inside of an EJB
 * module in an EAR. Requires a file to be on the root of the classpath named
 * <code>com.jdom.util.locator.strategies.Jboss7EjbLocator.properties</code>
 * with two properties, <code>applicationName</code> which should match the name
 * of the ear (minus a .ear suffix) file and ejb (minus a .ejb suffix), and
 * <code>applicationVersion</code> which should match the versions suffix at the
 * end of both archives.
 * 
 * @author djohnson
 * 
 */
public class Jboss7EjbLocator extends LocalEjbLocator {
	private static final Class<Jboss7EjbLocator> CLASS = Jboss7EjbLocator.class;

	private static final Logger LOG = LogFactory.getLogger(CLASS);

	private static final Pattern REPLACE_SERVICE_PATTERN = Pattern
			.compile("Service$");

	private static final String PROPERTIES_FILE = CLASS.getName()
			+ ".properties";

	private static final String NAME_KEY = "applicationName";

	private static final String VERSION_KEY = "applicationVersion";

	private static volatile ApplicationLookup applicationInformation;

	@Override
	protected String getJndiLookup(Class<?> interfaceClass) {
		String implClassSimpleName = REPLACE_SERVICE_PATTERN.matcher(
				interfaceClass.getSimpleName()).replaceFirst("");
		ApplicationLookup lookupInfo = getApplicationInformation();
		return "java:global/" + lookupInfo.applicationName + ".ear-"
				+ lookupInfo.applicationVersion + "/"
				+ lookupInfo.applicationName + ".ejb-"
				+ lookupInfo.applicationVersion + "/" + implClassSimpleName
				+ "!" + interfaceClass.getName();
	}

	static ApplicationLookup getApplicationInformation() {
		ApplicationLookup result = applicationInformation;
		if (result == null) {
			synchronized (CLASS) {
				result = applicationInformation;
				if (result == null) {
					applicationInformation = result = lookupApplicationInformationFromClasspath();
				}
			}
		}
		return result;
	}

	static ApplicationLookup lookupApplicationInformationFromClasspath() {
		InputStream is = null;
		try {
			is = CLASS.getResourceAsStream("/" + PROPERTIES_FILE);
			Properties properties = PropertiesUtil.readPropertiesFile(is);
			String versionValue = properties.getProperty(VERSION_KEY);
			String nameValue = properties.getProperty(NAME_KEY);

			if (nameValue == null || versionValue == null) {
				throw new IllegalStateException(
						"Unable to find the version.properties file on the classpath!  "
								+ "This is required to look up EJB services!");
			} else if (LOG.isInfoEnabled()) {
				LOG.info(String.format(
						"Retrieved application [%s] and version [%s].",
						nameValue, versionValue));
			}
			ApplicationLookup lookup = new ApplicationLookup();
			lookup.applicationName = nameValue;
			lookup.applicationVersion = versionValue;
			return lookup;
		} finally {
			IOUtil.close(is);
		}
	}
}
