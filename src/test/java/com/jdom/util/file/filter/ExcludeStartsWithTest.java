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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.jdom.util.file.filter.AcceptAllDirectories;
import com.jdom.util.file.filter.ExcludeStartsWith;

public class ExcludeStartsWithTest extends
		AbstractFilterTest<ExcludeStartsWith> {

	private static final String EXCLUSION_ONE = "UNPACK_";

	private static final String EXCLUSION_TWO = "ANOTHER_";

	private final Collection<String> exclusions = new ArrayList<String>();

	@Override
	@Before
	public void setUp() {
		super.setUp();

		exclusions.add(EXCLUSION_ONE);
		exclusions.add(EXCLUSION_TWO);
	}

	@Override
	protected ExcludeStartsWith getFilter() {
		return new ExcludeStartsWith(new AcceptAllDirectories(), exclusions);
	}

	@Override
	protected Collection<File> setupMatchingFiles() throws IOException {
		File matchingDir = new File(workingDir, "matchingDir");
		File matchingDir2 = new File(workingDir, "anotherMatchingDir");

		matchingDir.mkdirs();
		matchingDir2.mkdirs();

		Collection<File> shouldMatch = new ArrayList<File>();
		shouldMatch.add(matchingDir);
		shouldMatch.add(matchingDir2);

		return shouldMatch;
	}

	@Override
	protected Collection<File> setupNonMatchingFiles() throws IOException {
		Collection<File> shouldNotMatch = new ArrayList<File>();

		File notMatchingDir = new File(workingDir, EXCLUSION_ONE + "blah");
		File notMatchingDir2 = new File(workingDir, EXCLUSION_TWO
				+ "anotherblah");
		File notMatchingFile = new File(workingDir, "safeName");

		notMatchingDir.mkdirs();
		notMatchingDir2.mkdirs();
		notMatchingFile.createNewFile();

		shouldNotMatch.add(notMatchingDir);
		shouldNotMatch.add(notMatchingDir2);
		shouldNotMatch.add(notMatchingFile);

		return shouldNotMatch;
	}

	@Test
	public void testNoDecoratedFilterConstructor() throws IOException {
		ExcludeStartsWith filter = new ExcludeStartsWith(exclusions);

		// the file one in this list should now pass
		for (File check : setupNonMatchingFiles()) {
			if (check.isFile()) {
				assertTrue(filter.accept(check));
			} // But all directories should still fail
			else {
				assertFalse(filter.accept(check));
			}
		}

		// All of these should still pass
		for (File check : setupMatchingFiles()) {
			assertTrue(filter.accept(check));
		}
	}
}
