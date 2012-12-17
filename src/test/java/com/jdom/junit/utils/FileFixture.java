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
 */package com.jdom.junit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.jdom.util.file.FileUtils;
import com.jdom.util.file.FileWrapper;

public class FileFixture {

    private static final Logger LOG = Logger.getLogger(FileFixture.class);

    private static final int MAX_READ_BUFFER = 1048576;

    /**
     * Get an input stream to a resource, relative to a class. The resource must exist on the
     * classpath, at or below the level of the class.
     * 
     * @param clazz
     *            the class to use when looking up the resource
     * @param filename
     *            the filename to search for
     * @return an input stream to the file, or null if no resource with this name is found
     */
    public static InputStream getInputStream(Class<?> clazz, String filename) {
        InputStream retValue;

        retValue = clazz.getResourceAsStream(filename);

        return retValue;
    }

    /**
     * Get a byte array of a resource, relative to a class. The resource must exist on the
     * classpath, at or below the level of the class.
     * 
     * @param clazz
     *            the class to use when looking up the resource
     * @param filename
     *            the filename to search for
     * @return a byte array containing the data in the resource, or null if no resource with this
     *         name can be found
     */
    public static byte[] read(Class<?> clazz, String filename) {
        byte[] retValue = null;
        byte[] buffer = new byte[MAX_READ_BUFFER];
        int bytesRead = 0;

        InputStream stream = null;

        try {
            stream = getInputStream(clazz, filename);
            if (stream != null) {
                int readByte = stream.read();
                while (readByte != -1) {
                    bytesRead++;
                    buffer[bytesRead - 1] = (byte) readByte;
                    readByte = stream.read();
                }
                retValue = new byte[bytesRead];
                System.arraycopy(buffer, 0, retValue, 0, bytesRead);
            }

        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }

        return retValue;
    }

    /**
     * Write the provided data to the file specified by the parent directory and filename.
     * 
     * @param dir
     *            the director to which the file should be written
     * @param filename
     *            the filename to create
     * @param data
     *            the data to write to the file
     * @return a handle to the created file
     * @throws IOException
     *             when the file cannot be opened for writing.
     */
    public static File writeFile(File dir, String filename, String data) throws IOException {
        File outputFile = new File(dir, filename);
        PrintWriter pw = new PrintWriter(outputFile);
        pw.print(data);
        pw.close();
        return outputFile;
    }

    /**
     * Write the provided data to the file specified by the parent directory and filename.
     * 
     * @param dir
     *            the director to which the file should be written
     * @param filename
     *            the filename to create
     * @param data
     *            the data to write to the file
     * @return a handle to the created file
     * @throws IOException
     *             when the file cannot be opened for writing.
     */
    public static File writeFile(String dir, String filename, String data) throws IOException {
        return writeFile(new File(dir), filename, data);
    }

    /**
     * Delete the file specified by the parent directory and filename.
     * 
     * @param dir
     *            the director to which the file should be written
     * @param filename
     *            the filename to create
     * @throws IOException
     *             when the file cannot be deleted.
     */
    public static void deleteFile(String dir, String filename) throws IOException {
        File file = new File(dir, filename);
        file.delete();
    }

    /**
     * Copies a file from the local classpath to a location on disk. Will flatten any hierarchy in
     * which the file originally resided.
     * 
     * @param clazz
     *            the search for the file is performed relative to this class in the classpath
     *            hierarchy
     * @param classPathFilename
     *            the filename to search for, including any necessary packages.
     * @param destination
     *            the directory to which the file should be written.
     * @return a reference to the destination file
     * @throws IOException
     *             when the file cannot be found on the classpath.
     */
    public static File copyFileFromClasspath(Class<?> clazz, String classPathFilename, File destination)
            throws IOException {
        File result;

        if (!(destination.exists() && destination.isDirectory() && destination.canWrite())) {
            throw new IOException("Destination should be a directory which already exists that can be written to.");
        }

        InputStream inStream = getInputStream(clazz, classPathFilename);
        if (inStream != null) {
            File classpathFile = new File(classPathFilename);
            File destinationFile = new File(destination, classpathFile.getName());
            OutputStream outStream = new FileOutputStream(destinationFile);

            int bytesRead;
            byte[] buffer = new byte[MAX_READ_BUFFER];
            while ((bytesRead = inStream.read(buffer)) > -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.close();
            inStream.close();
            result = destinationFile;
        } else {
            throw new IOException("Could not locate file on classpath: " + classPathFilename);
        }

        return result;
    }

    /**
     * Copy the directory, deleting a pre-existing version if it exists.
     * 
     * @param srcDir
     *            the source directory
     * @param destDir
     *            the copy
     * @return the directory reference
     */
    public static File copyDirectory(File srcDir) {
        // Make a copy of the directory
        File originalDirectory = new File(srcDir.getParentFile(), "copiedDirectory");
        org.apache.commons.io.FileUtils.deleteQuietly(originalDirectory);

        new FileWrapper(srcDir).copyTo(originalDirectory);

        return originalDirectory;
    }

    /**
     * Asserts a file exists and is a file.
     * 
     * @param fileInQuestion
     *            the file
     */
    public static void assertFileExists(File fileInQuestion) {
        assertTrue(fileInQuestion.exists());
        assertTrue(fileInQuestion.isFile());
    }

    /**
     * Asserts that a directory exists and is a directory.
     * 
     * @param directoryInQuestion
     *            the directory
     */
    public static void assertDirectoryExists(File directoryInQuestion) {
        assertTrue(directoryInQuestion.exists());
        assertTrue(directoryInQuestion.isDirectory());
    }

    public static void assertExpectedFileMatchesActual(File expected, File actual) {
        FileFixture.assertFileExists(expected);
        FileFixture.assertFileExists(actual);

        assertEquals("The files did not have the same length!", expected.length(), actual.length());

        // Assert contents match
        String expectedContents = new String(FileUtils.readFileFully(expected));
        String actualContents = new String(FileUtils.readFileFully(actual));

        assertEquals("The actual file contents did not match those expected!", expectedContents, actualContents);
    }

    /**
     * Asserts that two directories match each other.
     * 
     * @param expected
     *            the expected directory
     * @param actual
     *            the actual directory
     */
    public static void assertExpectedDirectoryContentsMatchActual(File expected, File actual) {
        assertDirectoryExists(expected);
        assertDirectoryExists(actual);

        // Make sure all files in the directory match
        File[] expectedFiles = FileUtils.getFilesRecursivelyFromDirectory(expected);

        // Make sure all directory in the directory match
        File[] actualFiles = FileUtils.getFilesRecursivelyFromDirectory(actual);

        assertEquals("The same number of files should have been returned!", expectedFiles.length, actualFiles.length);

        for (File expectedFile : expectedFiles) {
            // Create the path to the actual version
            String replaceAll = expectedFile.getAbsolutePath().replaceAll("\\\\", "/");
			String absolutePath = expected.getAbsolutePath().replaceAll("\\\\", "/");
			String absolutePath2 = actual.getAbsolutePath().replaceAll("\\\\", "/");
			String actualFilePath = replaceAll.replaceAll(absolutePath,
                    absolutePath2);

            File actualFile = new File(actualFilePath);
            assertExpectedFileMatchesActual(expectedFile, actualFile);
        }

        Collection<File> expectedDirectories = FileUtils.getDirectoriesRecursivelyFromDirectory(expected);
        Collection<File> actualDirectories = FileUtils.getDirectoriesRecursivelyFromDirectory(actual);

        assertEquals("Incorrect number of sub-directories found!", expectedDirectories.size(), actualDirectories.size());
    }
}
