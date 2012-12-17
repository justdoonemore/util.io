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

import com.jdom.util.locator.ServiceFactory;
import com.jdom.util.locator.strategies.LocalEjbLocator;
import com.jdom.util.locator.strategies.Locator;
import com.jdom.util.locator.strategies.PojoLocator;
import com.jdom.util.locator.strategies.RemoteEjbLocator;

public class ServiceFactoryUtil {

    public static <T> void addLocator(Class<T> serviceInterface, T mockImpl) {
        ServiceFactory.getConfig().addLocator(serviceInterface, new MockLocator(mockImpl));
    }

    public static void setPojoDefault() {
        ServiceFactory.getConfig().defaultLocator = new PojoLocator();
    }

    public static void setLocalEjbDefault() {
        ServiceFactory.getConfig().defaultLocator = new LocalEjbLocator();
    }

    public static void setRemoteEjbDefault() {
        ServiceFactory.getConfig().defaultLocator = new RemoteEjbLocator();
    }

    public static void setDefaults() {
        ServiceFactory.setDefaults();
    }

    private static class MockLocator implements Locator {

        private final Object mock;

        private MockLocator(Object mock) {
            this.mock = mock;
        }

        @Override
        public <T> T locate(Class<T> interfaceClass) {
            return interfaceClass.cast(mock);
        }

    }

}
