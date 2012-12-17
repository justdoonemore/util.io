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
 */package com.jdom.util.email;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.dumbster.smtp.SimpleSmtpServer;

/**
 * This has to be a manual test because the SimpleSmtpServer is not reliable
 * enough to run in an automated fashion.
 * 
 * @author djohnson
 * 
 */
public class EmailerTest {

	private SimpleSmtpServer server;

	@Before
	public void setUp() throws InterruptedException {
		server = SimpleSmtpServer.start(7777);
	}

	@Test
	public void emailIsSent() throws InterruptedException {

		ArrayList<String> addresses = new ArrayList<String>();
		addresses.add("test@test.com");

		// Always sleep for a second to make sure the server is listening
		Emailer.email("localhost", 7777, "testUser", "testerPass", addresses,
				"testSubject", "this is a test");

		server.stop();

		int receivedEmails = server.getReceivedEmailSize();

		assertEquals("There should have been one and only one email received!",
				1, receivedEmails);
	}
}
