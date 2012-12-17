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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Looks up resources in Spring. Expects a Spring file named
 * <code>applicationContext.xml</code> to be on the classpath.
 * 
 * @author djohnson
 * 
 */
public class SpringLocator implements Locator {
	private static final Logger LOG = Logger.getLogger(SpringLocator.class);

	private static final String APPLICATION_CONTEXT_XML = "applicationContext.xml";
	private static volatile ApplicationContext appContext;

	@Override
	public <T> T locate(Class<T> interfaceClass) {
		return interfaceClass.cast(getApplicationContext().getBean(
				interfaceClass.getName()));
	}

	private static ApplicationContext getApplicationContext() {
		ApplicationContext result = appContext;
		if (result == null) {
			synchronized (SpringLocator.class) {
				result = appContext;
				if (result == null) {
					appContext = result = lookupApplicationContext();
					if (LOG.isInfoEnabled()) {
						LOG.info(String.format(
								"Read %s, initialization complete.",
								APPLICATION_CONTEXT_XML));
					}
				}
			}
		} else if (LOG.isDebugEnabled()) {
			LOG.debug(String.format(
					"Not reading %s, initialization already completed.",
					APPLICATION_CONTEXT_XML));
		}
		return result;
	}

	private static ApplicationContext lookupApplicationContext() {
		return new ClassPathXmlApplicationContext(
				new String[] { APPLICATION_CONTEXT_XML });
	}
}
