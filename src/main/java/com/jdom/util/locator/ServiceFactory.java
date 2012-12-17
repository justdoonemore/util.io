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

import java.io.InputStream;

import org.apache.commons.io.IOUtils;


import com.jdom.util.jibx.JiBXUtil;
import com.jdom.util.locator.strategies.Locator;

public final class ServiceFactory {
    private static final String CONFIG_FILE = "/service-factory.xml";

    private static ServiceFactoryConfig config;

    static {
        setDefaults();
    }

    /**
     * Default Constructor.
     */
    private ServiceFactory() {
        super();
    }

    /**
     * Create an instance of the service associated with the input interface. The default naming
     * convention is used to determine the name of the concrete service implementation.
     * 
     * @param <I>
     *            the expected interface return value
     * @param serviceInterface
     *            the interface that defines the expected return value
     * @return new instance of the service
     * @throws ServiceFactoryException
     *             on failure to create the service
     */
    public static <I> I create(Class<I> serviceInterface) throws ServiceFactoryException {
        if ((serviceInterface == null) || !serviceInterface.isInterface()) {
            throw new IllegalArgumentException("Argument serviceInterface " + serviceInterface
                    + " must be an interface.");
        }

        String serviceName = serviceInterface.getName();
        if ((serviceName == null) || serviceName.isEmpty()) {
            throw new IllegalArgumentException("Argument serviceName must be a non-null, non-empty string.");
        }

        return create(serviceInterface, serviceName);
    }

    /**
     * Create an instance of the service associated with the input interface with the name defined
     * by the input Enum. When the default naming convention is not in play,Services must provide an
     * Enum that defines the names of the available services. The Enum should be in the same
     * location in the baseline as the interface.
     * 
     * @param <I>
     *            the expected interface return value
     * @param <E>
     *            Enum type that defines the service name
     * @param serviceInterface
     *            the interface that defines the expected return value
     * @param serviceName
     *            service name for which a new instance is desired
     * @return new instance of the service
     * @throws ServiceFactoryException
     *             on error
     */
    @SuppressWarnings("unchecked")
    public static <I, E extends Enum> I create(Class<I> serviceInterface, E serviceName) throws ServiceFactoryException {
        if ((serviceInterface == null) || !serviceInterface.isInterface()) {
            throw new IllegalArgumentException("Argument serviceInterface " + serviceInterface
                    + " must be an interface.");
        }

        if ((serviceName == null)) {
            throw new IllegalArgumentException("Argument serviceName can't be null.");
        }

        return create(serviceInterface, serviceName.toString());
    }

    /**
     * Create an instance of the service associated with the input interface with the input name.
     * This method is private to avoid hard-coded service name strings in application code.
     * 
     * @param <I>
     *            the expected interface return value
     * @param serviceInterface
     *            the interface that defines the expected return value
     * @param serviceName
     *            service name for which a new instance is desired
     * @return new instance of the service
     * @throws ServiceFactoryException
     *             on error
     */
    private static <I> I create(Class<I> serviceInterface, String serviceName) throws ServiceFactoryException {

        Locator locator = getConfig().getLocator(serviceName);

        return locator.locate(serviceInterface);
    }

    /**
     * 
     * Create default service class name based on the name of the interface.
     * 
     * @param <I>
     *            the expected interface return value
     * @param serviceInterfaceName
     *            service interface name
     * @return default name of the concrete class that provides the service
     */
    protected static <I> String createDefaultServiceClassName(String serviceInterfaceName) {
        return serviceInterfaceName.replaceFirst("Service$", "");
    }

    static ServiceFactoryConfig getConfig() {
        if (config == null) {
            synchronized (ServiceFactory.class) {
                if (config == null) {
                	InputStream configFile = null;
                	try {
                        configFile = ServiceFactory.class.getResourceAsStream(CONFIG_FILE);
                        config = JiBXUtil.unmarshalObject(ServiceFactoryConfig.class, configFile);
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to load the service factory config!", e);
                    }
                	finally {
                		IOUtils.closeQuietly(configFile);
                	}
                }
            }
        }

        return config;
    }

    static void setDefaults() {
        synchronized (ServiceFactory.class) {
            config = null;
            getConfig();
        }
    }
}
