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
 */package com.jdom.util.j2ee;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

public final class MockContext {

    private final static Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);

        }
    };

    private final static InitialContext instance = context.mock(InitialContext.class);

    public static InitialContext getInstance() {
        return instance;
    }

    public static void registerInJndi(final String lookup, final Object object) {
        try {
            context.checking(new Expectations() {
                {
                    allowing(instance).lookup(lookup);
                    will(returnValue(object));
                }
            });
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }
}
