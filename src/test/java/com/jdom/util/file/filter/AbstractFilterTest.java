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
 */package com.jdom.util.file.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.jdom.junit.utils.TestUtil;

public abstract class AbstractFilterTest<FILTER extends FileFilter> {

	protected File workingDir;

	@Before
	public void setUp() {
		workingDir = TestUtil.setupTestClassDir(this.getClass());
	}

	/**
	 * Method to setup the test files that should pass.
	 * 
	 * @return the collection of non-matching files
	 * @throws IOException
	 */
	protected abstract Collection<File> setupMatchingFiles() throws IOException;

	/**
	 * Method to setup the test files that should not pass.
	 * 
	 * @return the collection of non-matching files
	 * @throws IOException
	 */
	protected abstract Collection<File> setupNonMatchingFiles()
			throws IOException;

	/**
	 * Get the filter instance.
	 * 
	 * @return the filter
	 */
	protected abstract FILTER getFilter();

	@Test
	public void testFilterProperlyMatches() throws IOException {
		FILTER filter = getFilter();

		// Verify results that should pass, do
		for (File shouldPass : setupMatchingFiles()) {
			assertTrue(filter.accept(shouldPass));
		}

		// Verify results that shouldn't pass, don't
		for (File shouldntPass : setupNonMatchingFiles()) {
			assertFalse(filter.accept(shouldntPass));
		}
	}
}
