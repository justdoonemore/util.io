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

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.jdom.util.file.FileUtils;
import com.jdom.util.io.IOUtil;
import com.jdom.util.regex.RegexMatch;
import com.jdom.util.regex.RegexUtil;

public class HtmlUtil {
	private static final Logger LOG = Logger.getLogger(HtmlUtil.class);

	public static final String BREAK_TAG = "<br/>";

	private static final String OPEN_FONT_TAG = "<FONT COLOR=\"";

	private static final String CLOSE_OPEN_TAG = "\"\\\">";

	private static final String CLOSE_FONT_TAG = "</FONT>";

	private static final String QUOTES_GROUP = "(\"|')";

	private static final String HREF_LINK_REGEX = "(href=" + QUOTES_GROUP
			+ "(.+?)" + QUOTES_GROUP + ")";

	private static final String ANCHOR_DISPLAY_TEXT_REGEX = ">(.+?)</(a|A)>";

	private static final String TITLE_ATTRIBUTE_REGEX = "title=("
			+ QUOTES_GROUP + "?((.+?))" + QUOTES_GROUP + ")";

	private static final int HREF_LINK_INDEX = 3;

	private static final int TITLE_INDEX = 3;

	private static final int ANCHOR_DISPLAY_TEXT_INDEX = 1;

	public static String getColoredString(String text, Color color) {
		return OPEN_FONT_TAG + getColorAsString(color) + CLOSE_OPEN_TAG + text
				+ CLOSE_FONT_TAG;
	}

	/**
	 * Retrieve the contents of the URL.
	 * 
	 * @param url
	 *            the url
	 * @return the contents as a string
	 */
	public static String downloadUrlContents(String url) {
		URL u;
		try {
			u = new URL(url.replaceAll(" ", ""));
		} catch (MalformedURLException e) {
			throw new com.jdom.util.html.exception.MalformedURLException(e);
		}

		return downloadUrlContents(u);
	}

	/**
	 * Retrieve the contents of the URL.
	 * 
	 * @param url
	 *            the url
	 * @return the contents as a string
	 */
	public static String downloadUrlContents(URL url) {

		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Downloading URL [%s]", url.toString()));
		}

		DataInputStream is = null;
		HttpClient httpClient = new HttpClient();
		String[] urlParts = url.toString().split("\\?");

		GetMethod method = new GetMethod(urlParts[0]);

		if (urlParts.length == 2) {
			method.setQueryString(urlParts[1]);
		}

		StringBuilder builder = new StringBuilder();

		try {

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				LOG.warn("Http retrieval returned unsuccessful: "
						+ method.getStatusLine());
			}

			is = new DataInputStream(new BufferedInputStream(
					method.getResponseBodyAsStream()));

			byte[] temp = new byte[FileUtils.BYTES_TO_READ_AT_A_TIME];

			int bytesRead = is.read(temp);

			// Keep reading bytes until there's nothing left to read
			while (bytesRead != FileUtils.NOTHING_READ) {
				// Copy into stringbuffer
				for (int i = 0; i < bytesRead; i++) {
					builder.append((char) temp[i]);
				}

				bytesRead = is.read(temp);
			}
		} catch (Exception e) {
			try {
				method.getResponseBody();
			} catch (Exception ignore) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Ignoring exception getting response body:",
							ignore);
				}
			}
			LOG.error(
					"Exception downloading URL contents, aborting the method:",
					e);
			method.abort();
		} finally {
			method.releaseConnection();
			IOUtil.close(is);
		}

		return builder.toString();
	}

	private static String getColorAsString(Color color) {
		String colorString = "black";

		if (color == Color.GREEN) {
			colorString = "green";
		} else if (color == Color.RED) {
			colorString = "red";
		} else if (color == Color.YELLOW) {
			colorString = "olive";
		}

		return colorString;
	}

	/**
	 * Parse a hyptertext link from a string.
	 * 
	 * @param text
	 *            the text line
	 * @return the URL or null
	 * @throws MalformedURLException
	 *             on a bad url
	 */
	public static URL getHypertextLink(String text)
			throws com.jdom.util.html.exception.MalformedURLException {
		URL url = null;

		Collection<RegexMatch> regexMatches = RegexUtil.findRegexMatches(text,
				HREF_LINK_REGEX);

		if (!regexMatches.isEmpty()) {
			RegexMatch regexMatch = regexMatches.iterator().next();

			String link = regexMatch.getGroup(HREF_LINK_INDEX);

			try {
				url = new URL(link);
			} catch (MalformedURLException e) {
				// It's ok in some cases because of javascript
				if (LOG.isDebugEnabled()) {
					LOG.debug("Determined link [" + link
							+ "] is not a downloadable URL.");
				}
			}
		}

		return url;
	}

	/**
	 * Gets the title attribute value in an html link.
	 * 
	 * @param text
	 *            the text
	 * @return the title attribute value, or null if it could not be found
	 */
	public static String getTitleAttributeValue(String text) {
		String retVal = null;

		Collection<RegexMatch> regexMatches = RegexUtil.findRegexMatches(text,
				TITLE_ATTRIBUTE_REGEX);

		if (!regexMatches.isEmpty()) {
			RegexMatch regexMatch = regexMatches.iterator().next();

			retVal = regexMatch.getGroup(TITLE_INDEX);
		}

		return retVal;
	}

	/**
	 * Returns an anchor display text or null if it doesn't exist.
	 * 
	 * @param text
	 *            the text to search
	 * @return the display text, or null if not found
	 */
	public static String getAnchorDisplayText(String text) {
		String retVal = null;

		Collection<RegexMatch> regexMatches = RegexUtil.findRegexMatches(text,
				ANCHOR_DISPLAY_TEXT_REGEX);

		if (!regexMatches.isEmpty()) {
			RegexMatch regexMatch = regexMatches.iterator().next();

			retVal = regexMatch.getGroup(ANCHOR_DISPLAY_TEXT_INDEX);
		}

		return retVal;
	}

	public static String getColumnText(Object object) {
		return surroundInHtmlTag("td", object.toString());
	}

	public static String getBoldedText(String string) {
		return surroundInHtmlTag("B", string);
	}

	private static String surroundInHtmlTag(String tag, String elementValue) {
		return "<" + tag + ">" + elementValue + "</" + tag + ">";
	}

}
