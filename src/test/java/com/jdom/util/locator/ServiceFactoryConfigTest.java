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
 */package com.jdom.util.locator;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jdom.util.jibx.JiBXUtil;
import com.jdom.util.locator.ServiceFactory;
import com.jdom.util.locator.ServiceFactoryConfig;
import com.jdom.util.locator.interfaces.TestImplPackageService;
import com.jdom.util.locator.strategies.LocalEjbLocator;
import com.jdom.util.locator.strategies.Locator;
import com.jdom.util.locator.strategies.RemoteEjbLocator;

public class ServiceFactoryConfigTest {

    @Test
    public void testWhenNoLocatorInStoreFallsBackOnDefaultLocator() {

        Locator locator = ServiceFactory.getConfig().getLocator(TestImplPackageService.class.getName());

        assertSame(locator, ServiceFactory.getConfig().defaultLocator);
    }

    @Test
    public void testExplicitLocatorIsReturnedWhenInConfig() {
        Locator remoteLocator = new RemoteEjbLocator();

        ServiceFactory.getConfig().addLocator(TestImplPackageService.class, remoteLocator);

        Locator locator = ServiceFactory.getConfig().getLocator(TestImplPackageService.class.getName());

        assertSame(remoteLocator, locator);

        ServiceFactoryUtil.setDefaults();
    }

    @Test
    public void testUnmarshallingFromXml() {
        String xml = "<ServiceFactoryConfig default-locator=\"com.jdom.util.locator.strategies.RemoteEjbLocator\"><Services>"
                + "<Service name=\"com.jdom.util.locator.interfaces.TestImplPackageService\" locator=\"com.jdom.util.locator.strategies.LocalEjbLocator\" />"
                + "</Services>" + "</ServiceFactoryConfig>";

        ServiceFactoryConfig serviceFactoryConfig = JiBXUtil.unmarshalObject(ServiceFactoryConfig.class, xml);

        assertTrue("Did not get the correct type of default locator!",
                serviceFactoryConfig.defaultLocator instanceof RemoteEjbLocator);

        assertTrue("Did not find the locator for the service configured via xml!", serviceFactoryConfig
                .getLocator(TestImplPackageService.class.getName()) instanceof LocalEjbLocator);
    }
}
