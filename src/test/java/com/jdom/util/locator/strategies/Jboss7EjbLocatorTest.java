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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.jdom.util.locator.interfaces.TestSamePackageImpl;
import com.jdom.util.locator.interfaces.TestSamePackageImplService;
import com.jdom.util.locator.strategies.ApplicationLookup;
import com.jdom.util.locator.strategies.Jboss7EjbLocator;

public class Jboss7EjbLocatorTest {

	@Test
	public void testlookupApplicationInformationFromClasspathReadsFile() {
		ApplicationLookup lookup = Jboss7EjbLocator
				.lookupApplicationInformationFromClasspath();
		assertEquals("testApplicationName", lookup.applicationName);
		assertEquals("1.0.1", lookup.applicationVersion);
	}

	@Test
	public void testGetApplicationInformationOnlyReadsFileFromClasspathOnce() {
		ApplicationLookup firstRead = Jboss7EjbLocator
				.getApplicationInformation();
		ApplicationLookup secondRead = Jboss7EjbLocator
				.getApplicationInformation();

		assertSame(firstRead, secondRead);
	}

	@Test
	public void testGetJndiLookupCreatesProperEarAndEjbPath() {
		assertEquals("Incorrect jndi name resolved for lookup!",
				"java:global/testApplicationName.ear-1.0.1/testApplicationName.ejb-1.0.1/"
						+ TestSamePackageImpl.class.getSimpleName() + "!"
						+ TestSamePackageImplService.class.getName(),
				new Jboss7EjbLocator()
						.getJndiLookup(TestSamePackageImplService.class));
	}
}
