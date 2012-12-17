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
 */package com.jdom.util.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jdom.util.io.IOUtil;

public class IOUtilTest {
	private CloseableObj obj;

	@Before
	public void setUp() {
		obj = new CloseableObj();
		assertFalse("Object should not be closed already!", obj.isClosed());
	}

	@Test
	public void closeProperlyCloses() {
		IOUtil.close(obj);
		assertTrue("Object should be closed now!", obj.isClosed());
	}

	@Test
	public void closeProperlyTrapsException() throws IOException {
		// Close the object prior to calling close
		obj.close();

		IOUtil.close(obj);
	}

	/**
	 * Test the inner class.
	 * 
	 * @throws IOException
	 */
	@Test
	public void closeableObjCanBeClosed() throws IOException {
		obj.close();
		assertTrue("Object should be closed now!", obj.isClosed());
	}

	/**
	 * Test the inner class.
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void alreadyClosedObjThrowsIOException() throws IOException {
		obj.close();

		// This should throw the exception
		obj.close();
	}

	/**
	 * Inner class just to test with.
	 * 
	 * @author djohnson
	 * 
	 */
	class CloseableObj implements Closeable {

		private boolean closed = false;

		@Override
		public void close() throws IOException {

			if (closed) {
				throw new IOException("already closed!");
			}

			closed = true;

		}

		public boolean isClosed() {
			return closed;
		}
	}
}
