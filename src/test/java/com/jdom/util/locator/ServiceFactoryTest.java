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
 */

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jdom.util.locator.ServiceFactory;
import com.jdom.util.locator.interfaces.TestImplPackageService;
import com.jdom.util.locator.interfaces.impl.TestImplPackage;
import com.jdom.util.locator.strategies.PojoLocator;

public class ServiceFactoryTest {

    @Test
    public void testServiceFactoryIsInitializedToUsePojoLocatorAsDefault() {
        assertTrue(ServiceFactory.getConfig().defaultLocator instanceof PojoLocator);

        // Verify we can create a pojo service
        assertTrue(ServiceFactory.create(TestImplPackageService.class) instanceof TestImplPackage);
    }

}