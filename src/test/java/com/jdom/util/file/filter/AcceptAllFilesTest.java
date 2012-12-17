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

import com.jdom.util.file.filter.AcceptAllFiles;

public class AcceptAllFilesTest extends AbstractFilterTest<AcceptAllFiles> {

	@Override
	protected AcceptAllFiles getFilter() {
		return new AcceptAllFiles();
	}

	@Override
	protected Collection<File> setupNonMatchingFiles() throws IOException {
		File dir1 = new File(workingDir, "dir1");
		File dir2 = new File(workingDir, "dir2");

		dir1.mkdirs();
		dir2.mkdirs();

		Collection<File> directories = new ArrayList<File>();
		directories.add(dir1);
		directories.add(dir2);

		return directories;
	}

	@Override
	protected Collection<File> setupMatchingFiles() throws IOException {
		Collection<File> files = new ArrayList<File>();

		File file1 = new File(workingDir, "file1.txt");
		File file2 = new File(workingDir, "file2.txt");
		file1.createNewFile();
		file2.createNewFile();

		files.add(file1);
		files.add(file2);

		return files;
	}

}
