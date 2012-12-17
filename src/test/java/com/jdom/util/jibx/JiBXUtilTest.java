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
 */package com.jdom.util.jibx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import com.jdom.test.jibx.ClassOne;
import com.jdom.test.jibx.dep.ClassTwo;
import com.jdom.util.jibx.JiBXUtil;

public class JiBXUtilTest {

    private static final String MARSHALLED = "<ClassOne><TestString>thisisatest</TestString><TestLong>1003</TestLong>"
            + "<ClassTwo><ItsString>anotherfield</ItsString></ClassTwo></ClassOne>";

    private final ClassOne classOne = new ClassOne();

    @Before
    public void setUp() {
        ClassTwo classTwo = new ClassTwo();
        classTwo.setItsString("anotherfield");

        classOne.setTestLong(1003L);
        classOne.setTestString("thisisatest");
        classOne.setClassTwo(classTwo);
    }

    @Test
    public void testUnmarshallingMarshalEqualsOriginalObject() {
        String marshalled = JiBXUtil.marshalObject(classOne);
        ClassOne unmarshalled = JiBXUtil.unmarshalObject(ClassOne.class, marshalled);

        assertNotSame(classOne, unmarshalled);
        assertEquals(classOne, unmarshalled);
    }

    @Test
    public void testUnmarshallingObjectAndRemarshallingEqualsOriginalXml() {
        ClassOne unmarshalled = JiBXUtil.unmarshalObject(ClassOne.class, MARSHALLED);
        String newMarshall = JiBXUtil.marshalObject(unmarshalled);
        assertEquals(MARSHALLED, newMarshall);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmarshallingObjectOfWrongClassWillThrowIllegalArgumentException() {
        JiBXUtil.unmarshalObject(ClassTwo.class, MARSHALLED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmarshallingObjectThatGetsJibxExceptionThrowsIllegalArgumentException() {
        JiBXUtil.unmarshalObject(ClassTwo.class, MARSHALLED + "fjkasljkfs");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarshallingClassWithoutBindingThrowsIllegalArgumentException() {
        JiBXUtil.marshalObject(String.class);
    }
}
