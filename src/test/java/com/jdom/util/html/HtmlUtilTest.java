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
 */package com.jdom.util.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jdom.util.html.HtmlUtil;

public class HtmlUtilTest {

	private static final String URL_STRING = "http://google.com";

	private static final String BAD_URL_STRING = "blah";

	private static final String TITLE = "randomTitle";

	private static final String DISPLAY_TEXT = "Heroes.S03E21.HDTV.XviD-LOL";

	private static final String PROPER_URL = "<a href=\"" + URL_STRING
			+ "\" class=\"result_blue_bold\" title=\"" + TITLE + "\">"
			+ DISPLAY_TEXT + "</a>";

	private static final String IMPROPER_URL = "<a href=\"" + BAD_URL_STRING
			+ "\" class=\"result_blue_bold\"></a>";

	private static URL expectedUrl;

	@BeforeClass
	public static void beforeClass() throws MalformedURLException {
		expectedUrl = new URL(URL_STRING);
	}

	@Test
	public void getHypertextLinkReturnsProperUrl() {
		URL result = HtmlUtil.getHypertextLink(PROPER_URL);

		assertEquals(expectedUrl, result);
	}

	@Test
	public void getHypertextLinkReturnsNullOnBadUrl() {
		assertNull(HtmlUtil.getHypertextLink(IMPROPER_URL));
	}

	@Test
	public void getTitleAttributeFindsExistingTitle() {
		String title = HtmlUtil.getTitleAttributeValue(PROPER_URL);

		assertEquals(TITLE, title);
	}

	@Test
	public void getTitleAttributeReturnsNullForMissingTitle() {
		String title = HtmlUtil.getTitleAttributeValue(IMPROPER_URL);

		assertNull(title);
	}

	@Test
	public void getAnchorDisplayTextFindsExistingDisplayText() {
		String title = HtmlUtil.getAnchorDisplayText(PROPER_URL);

		assertEquals(DISPLAY_TEXT, title);
	}

	@Test
	public void getAnchorDisplayTextReturnsNullForMissingTitle() {
		String title = HtmlUtil.getAnchorDisplayText(IMPROPER_URL);

		assertNull(title);
	}

}
