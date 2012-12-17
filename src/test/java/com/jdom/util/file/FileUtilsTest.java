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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jdom.junit.utils.TestUtil;

/**
 * This class handles the testing of the FileUtil class.
 * 
 * @author djohnson
 * 
 */
public final class FileUtilsTest {

	private static final File INVALID_FILE = new File(
			"blah/thisisnotavalidfilename");

	private static final String TEST_DATA = "testOne\ntestTwo";

	private File tempDir;

	private File tempFile;

	private File fileToWrite;

	private InputStream inputStream;

	private OutputStream outputStream;

	@Before
	public void beforeTest() throws IOException {
		assertFalse("Expected the invalid file to not exist!",
				INVALID_FILE.exists());

		// Create test class directory
		tempDir = TestUtil.setupTestClassDir(FileUtilsTest.class);

		assertTrue("Temporary test class directory must exist!",
				tempDir.exists());

		// Create a temporary file
		tempFile = new File(tempDir, "test.txt");
		tempFile.createNewFile();

		assertTrue("The temporary file was not created!", tempFile.exists());

		// Write out data to the temp file
		PrintWriter tempFileOut = new PrintWriter(tempFile);
		tempFileOut.write(TEST_DATA);
		tempFileOut.close();

		// Create the file object to write
		fileToWrite = new File(tempDir, "fileToWrite.txt");

		assertNull("InputStream must be null before each test!", inputStream);
		assertNull("OutputStream must be null before each test!", outputStream);
	}

	@After
	public void afterTest() throws IOException {
		// Close stream if null
		if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}

		// Delete the file to write
		fileToWrite.delete();
	}

	@Test
	public void testCopyDirectoryWorks() {
		File copySource = new File(tempDir, "dir1");
		copySource.mkdirs();
		FileUtils.writeFileToDisk(new File(copySource, "file.txt"),
				"blah".getBytes());

		File copyTarget = new File(tempDir, "dir2");
		FileUtils.copyDirectory(copySource, copyTarget);

		assertTrue("The file in the source directory should have been copied!",
				new File(copyTarget, "file.txt").isFile());
	}

	@Test
	public void testCanDeleteFile() {
		assertTrue(tempFile.exists());
		FileUtils.delete(tempFile);
		assertFalse(tempFile.exists());
	}

	@Test
	public void testCanDeleteDirectory() {
		assertTrue(tempDir.exists());
		FileUtils.deleteDirectory(tempDir);
		assertFalse(tempDir.exists());
	}

	@Test
	public void testWriteByteArrayToFile() {
		String testString = "thisisatest";
		FileUtils.writeByteArrayToFile(tempFile, testString.getBytes());
		String fromFile = new String(FileUtils.readFileFully(tempFile));

		assertEquals("The byte array was not written out to the file!",
				testString, fromFile);
	}

	@Test
	public void testWriteStringToFile() {
		String testString = "thisisatest";
		FileUtils.writeStringToFile(tempFile, testString);
		String fromFile = new String(FileUtils.readFileFully(tempFile));

		assertEquals("The string was not written out to the file!", testString,
				fromFile);
	}

	@Test
	public void testGettingFilesFromDirectory() throws IOException {
		File dirForTest = new File(tempDir, "testDir");
		assertTrue(dirForTest.mkdirs());

		File shouldBeReturned = new File(dirForTest, "one.txt");
		File shouldBeReturned2 = new File(dirForTest, "two.txt");

		File anotherDir = new File(dirForTest, "anotherDir");
		assertTrue(anotherDir.mkdirs());

		File shouldBeReturnedOnRecurse = new File(anotherDir, "three.txt");

		assertTrue(shouldBeReturned.createNewFile());
		assertTrue(shouldBeReturned2.createNewFile());
		assertTrue(shouldBeReturnedOnRecurse.createNewFile());

		Collection<File> files = FileUtils.getFilesFromDirectory(dirForTest,
				false);

		assertEquals(2, files.size());
		assertTrue(files.contains(shouldBeReturned));
		assertTrue(files.contains(shouldBeReturned2));

		files = FileUtils.getFilesFromDirectory(dirForTest, true);
		assertEquals(3, files.size());
		assertTrue(files.contains(shouldBeReturned));
		assertTrue(files.contains(shouldBeReturned2));
		assertTrue(files.contains(shouldBeReturnedOnRecurse));
	}

	@Test
	public void testGettingDirectoriesFromDirectory() {
		File dirOne = new File(tempDir, "dirOne");
		File dirTwo = new File(tempDir, "dirTwo");

		assertTrue(dirOne.mkdirs());
		assertTrue(dirTwo.mkdirs());

		File dirThree = new File(dirOne, "dirThree");
		assertTrue(dirThree.mkdirs());

		Collection<File> foundDirs = FileUtils.getDirectoriesFromDirectory(
				tempDir, false);
		assertEquals(2, foundDirs.size());
		assertTrue(foundDirs.contains(dirOne));
		assertTrue(foundDirs.contains(dirTwo));

		foundDirs = FileUtils.getDirectoriesFromDirectory(tempDir, true);
		assertEquals(3, foundDirs.size());
		assertTrue(foundDirs.contains(dirOne));
		assertTrue(foundDirs.contains(dirTwo));
		assertTrue(foundDirs.contains(dirThree));
	}

	@Test
	public void testGettingDirectoriesRecursivelyFromDirectory() {
		File dirOne = new File(tempDir, "dirOne");
		File dirTwo = new File(tempDir, "dirTwo");

		assertTrue(dirOne.mkdirs());
		assertTrue(dirTwo.mkdirs());

		File dirThree = new File(dirOne, "dirThree");
		assertTrue(dirThree.mkdirs());

		Collection<File> foundDirs = FileUtils
				.getDirectoriesRecursivelyFromDirectory(tempDir);
		assertEquals(3, foundDirs.size());
		assertTrue(foundDirs.contains(dirOne));
		assertTrue(foundDirs.contains(dirTwo));
		assertTrue(foundDirs.contains(dirThree));
	}

	@Test
	public void testGettingDirectoriesFromDirectoryUsingFileFilter() {
		final File dirOne = new File(tempDir, "dirOne");
		File dirTwo = new File(tempDir, "dirTwo");

		assertTrue(dirOne.mkdirs());
		assertTrue(dirTwo.mkdirs());

		final File dirThree = new File(dirOne, "dirThree");
		assertTrue(dirThree.mkdirs());

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.equals(dirOne) || arg0.equals(dirThree);
			}
		};

		Collection<File> foundDirs = FileUtils.getDirectoriesFromDirectory(
				tempDir, true, fileFilter);
		assertEquals(2, foundDirs.size());
		assertTrue(foundDirs.contains(dirOne));
		assertTrue(foundDirs.contains(dirThree));

		foundDirs = FileUtils.getDirectoriesFromDirectory(tempDir, false,
				fileFilter);
		assertEquals(1, foundDirs.size());
		assertTrue(foundDirs.contains(dirOne));
	}

	@Test
	public void getInputStreamReturnsStreamForValidFile() throws IOException {
		inputStream = FileUtils.getInputStream(tempFile);

		assertNotNull("The input stream should not have been null!",
				inputStream);
	}

	@Test
	public void getInputStreamReturnsStreamForValidFileString()
			throws IOException {
		inputStream = FileUtils.getInputStream(tempFile.getAbsolutePath());

		assertNotNull("The input stream should not have been null!",
				inputStream);
	}

	@Test
	public void getInputStreamReturnsStreamForValidFileStrings()
			throws IOException {
		inputStream = FileUtils.getInputStream(tempFile.getParentFile()
				.getAbsolutePath(), tempFile.getName());

		assertNotNull("The input stream should not have been null!",
				inputStream);
	}

	@Test
	public void getInputStreamReturnsNullWhenInvalidFile() {
		inputStream = FileUtils.getInputStream(INVALID_FILE);

		assertNull("The input stream should have been null!", inputStream);
	}

	@Test
	public void getInputStreamReturnsNullWhenInvalidFileString() {
		inputStream = FileUtils.getInputStream(INVALID_FILE.getAbsolutePath());

		assertNull("The input stream should have been null!", inputStream);
	}

	@Test
	public void getInputStreamReturnsNullWhenInvalidStrings() {
		inputStream = FileUtils.getInputStream(INVALID_FILE.getParentFile()
				.getAbsolutePath(), INVALID_FILE.getName());

		assertNull("The input stream should have been null!", inputStream);
	}

	@Test
	public void getOutputStreamReturnsOutputStreamWhenValidFile()
			throws IOException {
		fileToWrite.createNewFile();
		outputStream = FileUtils.getOutputStream(fileToWrite);

		assertNotNull("The output stream should not have been null!",
				outputStream);
	}

	@Test
	public void getOutputStreamReturnsNullWhenInvalidFile() {
		outputStream = FileUtils.getOutputStream(INVALID_FILE);

		assertNull("The output stream should have been null!", outputStream);
	}

	@Test
	public void readFileFullyReturnsAllContents() throws IOException {
		byte[] bytes = FileUtils.readFileFully(tempFile);

		assertNotNull("The file contents should have been read!", bytes);

		String contents = new String(bytes);

		assertEquals("The contents of the file were not what was expected!",
				TEST_DATA, contents);
	}

	@Test
	public void writeFileToDiskWritesBytesOk() throws IOException {
		assertFalse("The file to write cannot exist already!",
				fileToWrite.exists());

		boolean written = FileUtils.writeFileToDisk(fileToWrite,
				TEST_DATA.getBytes());

		processWrittenFileResults(written, fileToWrite);
	}

	@Test
	public void writeFileToDiskWithHiddenFileWritesBytesOk() throws IOException {
		assertFalse("The file to write cannot exist already!",
				fileToWrite.exists());

		boolean written = FileUtils.writeFileToDisk(fileToWrite,
				TEST_DATA.getBytes(), true);

		processWrittenFileResults(written, fileToWrite);
	}

	@Test
	public void writeFileToDiskWritesStringOk() throws IOException {
		assertFalse("The file to write cannot exist already!",
				fileToWrite.exists());

		boolean written = FileUtils.writeFileToDisk(fileToWrite, TEST_DATA);

		processWrittenFileResults(written, fileToWrite);
	}

	@Test
	public void writeFileToDiskWritesStringOkToStringFile() throws IOException {
		assertFalse("The file to write cannot exist already!",
				fileToWrite.exists());

		boolean written = FileUtils.writeFileToDisk(
				fileToWrite.getAbsolutePath(), TEST_DATA);

		processWrittenFileResults(written, fileToWrite);
	}

	@Test
	public void writeFileToDiskWritesBytesOkToStringFile() throws IOException {
		assertFalse("The file to write cannot exist already!",
				fileToWrite.exists());

		boolean written = FileUtils.writeFileToDisk(
				fileToWrite.getAbsolutePath(), TEST_DATA);

		processWrittenFileResults(written, fileToWrite);
	}

	@Test
	public void getFilesRecursivelyReturnsCorrectFiles() throws IOException {
		final String fileExtension = ".testIt";
		final String badExtension = ".notit";
		final int NUM_FILES = 10;

		File currentDir = tempDir;

		for (int i = 0; i < NUM_FILES; i++) {
			// Create a new directory
			File dir = new File(currentDir, "dir_" + i);
			dir.mkdirs();

			// Create files that should be matched
			File newFile = new File(dir, i + fileExtension);
			newFile.createNewFile();

			// Create files that shouldn't be matched
			File badFile = new File(dir, i + badExtension);
			badFile.createNewFile();

			assertTrue("The current file should have been created",
					newFile.exists());
			assertTrue("The current file should have been created",
					badFile.exists());

			currentDir = dir;
		}

		File[] results = FileUtils.getFilesRecursivelyFromDirectory(tempDir,
				fileExtension);

		assertEquals("There should have been " + NUM_FILES + " results!",
				NUM_FILES, results.length);

		// Verify all files end in the correct extension
		for (File result : results) {
			assertTrue("The filename should have ended in " + fileExtension
					+ "!", result.getName().endsWith(fileExtension));
		}

		// Delete the temp file already there
		assertTrue(tempFile.delete());

		results = FileUtils.getFilesRecursivelyFromDirectory(tempDir);

		int numResults = NUM_FILES * 2;

		assertEquals("There should have been " + numResults + " results!",
				numResults, results.length);
	}

	@Test
	public void getFilesRecursivelyFiltersFiles() throws IOException {
		final String fileExtension = ".testIt";
		final String badExtension = ".notit";
		final int NUM_FILES = 10;

		File currentDir = tempDir;

		for (int i = 0; i < NUM_FILES; i++) {
			// Create a new directory
			File dir = new File(currentDir, "dir_" + i);
			dir.mkdirs();

			// Create files that should be matched
			File newFile = new File(dir, i + fileExtension);
			newFile.createNewFile();

			// Create files that shouldn't be matched
			File badFile = new File(dir, i + badExtension);
			badFile.createNewFile();

			assertTrue("The current file should have been created",
					newFile.exists());
			assertTrue("The current file should have been created",
					badFile.exists());

			currentDir = dir;
		}

		Collection<File> results = FileUtils.getFilesAndDirectories(tempDir,
				true, false, true, new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						// TODO Auto-generated method stub
						return pathname.getName().endsWith(fileExtension);
					}
				});

		assertEquals("There should have been " + NUM_FILES + " results!",
				NUM_FILES, results.size());

		// Verify all files end in the correct extension
		for (File result : results) {
			assertTrue("The filename should have ended in " + fileExtension
					+ "!", result.getName().endsWith(fileExtension));
		}
	}

	/**
	 * This method just processes the written file results.
	 * 
	 * @param result
	 *            The boolean result
	 * @param fileWritten
	 *            The file object pointing to the file that should have been
	 *            written
	 * @throws IOException
	 */
	private void processWrittenFileResults(boolean result, File fileWritten)
			throws IOException {
		assertTrue(
				"The return flag should have been true for writing the file!",
				result);

		assertTrue("The file should exist now!", fileWritten.exists());

		// Read the data and make sure it matches
		String contents = new String(FileUtils.readFileFully(fileWritten));

		assertEquals("The contents did not match the written data!", contents,
				TEST_DATA);

	}

}
