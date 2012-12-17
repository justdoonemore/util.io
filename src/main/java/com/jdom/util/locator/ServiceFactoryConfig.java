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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.jdom.util.locator.strategies.LocalEjbLocator;
import com.jdom.util.locator.strategies.Locator;

class ServiceFactoryConfig {

    static final Locator DEFAULT_LOCATOR = new LocalEjbLocator();

    Locator defaultLocator = DEFAULT_LOCATOR;

    private final Map<String, Locator> locators = new HashMap<String, Locator>();

    // Used by jibx to store locators
    private transient final Collection<LocatorLookup> locatorLookups = new HashSet<LocatorLookup>();

    public Locator getLocator(String name) {

        Locator locator = locators.get(name);

        if (locator == null) {
            locator = defaultLocator;
        }

        return locator;
    }

    void addLocator(Class<?> interfaceClass, Locator locator) {
        locators.put(interfaceClass.getName(), locator);
    }

    /**
     * Used by JiBX to prepare for serialization.
     */
    @SuppressWarnings("unused")
    private void prepareLocatorLookups() {
        for (String lookupKey : locators.keySet()) {
            locatorLookups.add(new LocatorLookup(lookupKey, locators.get(lookupKey)));
        }
    }

    /**
     * Used by JiBX to restore from serialization.
     */
    @SuppressWarnings("unused")
    private void restoreFromLocatorLookups() {
        for (LocatorLookup locatorLookup : locatorLookups) {
            locators.put(locatorLookup.getClassName(), locatorLookup.getLocator());
        }
    }
}
