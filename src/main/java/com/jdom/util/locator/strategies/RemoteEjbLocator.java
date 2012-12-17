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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.jdom.util.locator.ServiceLocatorException;

public class RemoteEjbLocator implements Locator {

    ContextLookupStrategy contextLookupStrategy = new ServerContextLookupStrategy();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T locate(Class<T> interfaceClass) {

        try {
            Object lookedUpObject = lookupObject(interfaceClass);
            return (T) PortableRemoteObject.narrow(lookedUpObject, interfaceClass);
        } catch (NamingException e) {
            throw new ServiceLocatorException(e);
        }
    }

    Object lookupObject(Class<?> interfaceClass) throws NamingException {
        String jndiLookup = getJndiLookup(interfaceClass);

        InitialContext ctx = contextLookupStrategy.getInitialContext();

        return ctx.lookup(jndiLookup);
    }

    protected String getJndiLookup(Class<?> interfaceClass) {
        return interfaceClass.getName();
    }
}
