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
 */package com.jdom.util.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jdom.junit.utils.FileFixture;
import com.jdom.junit.utils.TestUtil;

public class FileWrapperTest {

	private static final long TWO_SECONDS = 2000L;

	private FileWrapper testDirectory;

	private FileWrapper testFile1;

	private FileWrapper testFile2;

	private FileWrapper destinationDirectory;

	@Before
	public void setUp() throws IOException {
		testDirectory = new FileWrapper(TestUtil.setupTestClassDir(this
				.getClass()));
		destinationDirectory = new FileWrapper(testDirectory.getParentFile(),
				"movedDirectory");
		testFile1 = new FileWrapper(FileFixture.writeFile(testDirectory,
				"testFile.txt", "test data"));
		testFile2 = new FileWrapper(FileFixture.writeFile(testDirectory,
				"testFile2.txt", "moreContents"));

		destinationDirectory.delete();
		assertFalse(destinationDirectory.exists());
		FileFixture.assertFileExists(testFile1);
		FileFixture.assertFileExists(testFile2);
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(destinationDirectory);
		FileUtils.deleteDirectory(testDirectory);
		FileUtils.delete(testFile1);
		FileUtils.delete(testFile2);
	}

	@Test
	public void testModifiedSinceReturnsTrueWhenFileWasModifiedEarlier() {
		testFile1.setLastModified(System.currentTimeMillis()
				- (TWO_SECONDS * 2));

		assertFalse(
				"The file should have returned false for having been modified more than two seconds ago!",
				testFile1.hasBeenModifiedSince(TWO_SECONDS));
	}

	@Test
	public void testModifiedSinceReturnsFalseWhenFileWasntModifiedEarlier() {
		testFile1.setLastModified(System.currentTimeMillis());

		assertTrue(
				"The file should have returned true for being modified more than two seconds ago!",
				testFile1.hasBeenModifiedSince(TWO_SECONDS));

	}

	@Test
	public void testModifiedSinceReturnsTrueWhenDirectoryContainsNewerFile() {
		testFile1.setLastModified(System.currentTimeMillis());
		testFile2.setLastModified(System.currentTimeMillis()
				- (TWO_SECONDS * 2));

		assertTrue(
				"The directory should have returned false for having been modified more than two seconds ago!",
				testDirectory.hasBeenModifiedSince(TWO_SECONDS));
	}

	@Test
	public void testModifiedSinceReturnsFalseWhenDirectoryDoesNotContainNewerFile() {
		testFile1.setLastModified(System.currentTimeMillis()
				- (TWO_SECONDS * 2));
		testFile2.setLastModified(System.currentTimeMillis()
				- (TWO_SECONDS * 2));

		assertFalse(
				"The directory should have returned false for having been modified more than two seconds ago!",
				testDirectory.hasBeenModifiedSince(TWO_SECONDS));
	}

	@Test
	public void testCopyToCreatesFileCopy() throws IOException {
		// This will overwrite testFile2 with the contents of testFile1
		testFile1.copyTo(testFile2);

		FileFixture.assertExpectedFileMatchesActual(testFile1, testFile2);
	}

	@Test
	public void testCopyToCopiesDirectoryContents() throws IOException {
		testDirectory.copyTo(destinationDirectory);

		FileFixture.assertExpectedDirectoryContentsMatchActual(testDirectory,
				destinationDirectory);
	}

	@Test
	public void testMoveToMovesDirectoryContents() throws IOException {
		File originalDirectory = FileFixture.copyDirectory(testDirectory);

		testDirectory.moveTo(destinationDirectory);

		FileFixture.assertExpectedDirectoryContentsMatchActual(
				originalDirectory, destinationDirectory);

		assertFalse("The original directory should have been deleted!",
				testDirectory.exists());
	}

	@Test
	public void testMoveToMovesDirectoryContentsEvenWithNestedDirectory()
			throws IOException {
		File nestedDirectory = new File(testDirectory, "directory");
		nestedDirectory.mkdirs();
		FileFixture.writeFile(nestedDirectory, "blah.txt", "blah");

		File originalDirectory = FileFixture.copyDirectory(testDirectory);

		testDirectory.moveTo(destinationDirectory);

		FileFixture.assertExpectedDirectoryContentsMatchActual(
				originalDirectory, destinationDirectory);

		assertFalse("The original directory should have been deleted!",
				testDirectory.exists());
	}

	@Test
	public void testMoveToMovesFile() throws IOException {
		// Make a copy to compare to after the move
		File originalFile = new File(testDirectory, "comparisonFile.txt");
		testFile1.copyTo(originalFile);

		FileUtils.delete(testFile2);
		assertFalse(testFile2.exists());

		testFile1.moveTo(testFile2);
		FileFixture.assertExpectedFileMatchesActual(originalFile, testFile2);

		assertFalse("Test file should no longer exist!", testFile1.exists());
	}

	@Test
	public void testBasenameAndExtensionAreParsedWhenExtensionPresent() {
		File originalFile = new File(testDirectory, "comparisonFile.txt");
		FileWrapper wrapper = new FileWrapper(originalFile);

		assertEquals("comparisonFile", wrapper.getBaseName());
		assertEquals(".txt", wrapper.getExtension());
	}

	@Test
	public void testBasenameIsFullPathWhenExtensionNotPresent() {
		File originalFile = new File(testDirectory, "comparisonFile");
		FileWrapper wrapper = new FileWrapper(originalFile);

		assertEquals("comparisonFile", wrapper.getBaseName());
		assertNull(wrapper.getExtension());
	}
}
