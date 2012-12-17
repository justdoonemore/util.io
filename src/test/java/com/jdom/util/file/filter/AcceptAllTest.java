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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.jdom.util.file.filter.AcceptAll;

public class AcceptAllTest extends AbstractFilterTest<AcceptAll> {

	@Override
	protected AcceptAll getFilter() {
		return new AcceptAll();
	}

	@Override
	protected Collection<File> setupMatchingFiles() throws IOException {
		File directory = new File(workingDir, "dir");
		directory.mkdirs();

		File file = new File(workingDir, "file.txt");
		file.createNewFile();

		Collection<File> shouldPass = new ArrayList<File>();
		shouldPass.add(directory);
		shouldPass.add(file);

		return shouldPass;
	}

	@Override
	protected Collection<File> setupNonMatchingFiles() {
		return Collections.emptyList();
	}
}
