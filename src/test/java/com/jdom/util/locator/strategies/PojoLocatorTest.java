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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jdom.util.locator.interfaces.TestImplPackageService;
import com.jdom.util.locator.interfaces.TestSamePackageImpl;
import com.jdom.util.locator.interfaces.TestSamePackageImplService;
import com.jdom.util.locator.interfaces.impl.TestImplPackage;
import com.jdom.util.locator.strategies.PojoLocator;

public class PojoLocatorTest {

    private final PojoLocator locator = new PojoLocator();

    @Test
    public void testServiceWithImplInSamePackageCanBeConstructed() {
        TestSamePackageImplService service = locator.locate(TestSamePackageImplService.class);

        assertTrue(service instanceof TestSamePackageImpl);
    }

    @Test
    public void testServiceWithImplInImplPackageCanBeConstructed() {
        TestImplPackageService service = locator.locate(TestImplPackageService.class);

        assertTrue(service instanceof TestImplPackage);
    }
}
