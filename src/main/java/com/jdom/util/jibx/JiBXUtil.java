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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * Provides marshalling facilities.
 */
public final class JiBXUtil {
    /**
     * private Constructor.
     */
    private JiBXUtil() {

    }

    /**
     * Marshalls an object to a string.
     * 
     * @param objectToMarshal
     *            the object to marshal
     * @return the marshaled string
     * @throws JiBXException
     *             runtime exception that occurs on errors with marshaling
     */
    public static String marshalObject(Object objectToMarshal) {
        StringWriter writer = new StringWriter();
        IBindingFactory bindingFactory;
        try {
            bindingFactory = BindingDirectory.getFactory(objectToMarshal.getClass());
            IMarshallingContext mctx = bindingFactory.createMarshallingContext();

            mctx.setOutput(writer);
            mctx.marshalDocument(objectToMarshal);

        } catch (JiBXException e) {
            throw new IllegalArgumentException("Exception marshalling the object!", e);
        }

        return writer.toString();
    }

    /**
     * Unmarshal the object of the specified class from the input stream.
     * 
     * @param <T>
     *            class type
     * @param classOfObjectToUnmarshall
     *            the class reference
     * @param inputStream
     *            the input stream
     * @return the unmarshaled object
     * @throws JiBXException
     *             runtime exception that occurs on errors with unmarshaling
     */
    public static <T> T unmarshalObject(Class<T> classOfObjectToUnmarshall, InputStream inputStream) {

        try {
            IBindingFactory bfact = BindingDirectory.getFactory(classOfObjectToUnmarshall);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
            Object unmarshalledObject = uctx.unmarshalDocument(inputStream, null);

            T castedObject = classOfObjectToUnmarshall.cast(unmarshalledObject);

            return castedObject;
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("The object cannot be cast to " + classOfObjectToUnmarshall.getName(),
                    cce);
        } catch (JiBXException e) {
            throw new IllegalArgumentException("Unable to unmarshal the xml!", e);
        }
    }

    /**
     * Unmarshal the object of the specified class from the string.
     * 
     * @param <T>
     *            class type
     * @param classOfObjectToUnmarshall
     *            the class reference
     * @param string
     *            the marshalled string
     * @return the unmarshaled object
     * @throws JiBXException
     *             runtime exception that occurs on errors with unmarshaling
     */
    public static <T> T unmarshalObject(Class<T> classOfObjectToUnmarshall, String string) {
        return unmarshalObject(classOfObjectToUnmarshall, string.getBytes());
    }

    /**
     * Unmarshal the object of the specified class from the string.
     * 
     * @param <T>
     *            class type
     * @param classOfObjectToUnmarshall
     *            the class reference
     * @param rawBytes
     *            the marshalled bytes
     * @return the unmarshaled object
     * @throws JiBXException
     *             runtime exception that occurs on errors with unmarshaling
     */
    public static <T> T unmarshalObject(Class<T> classOfObjectToUnmarshall, byte[] rawBytes) {
        return unmarshalObject(classOfObjectToUnmarshall, new ByteArrayInputStream(rawBytes));
    }
}
