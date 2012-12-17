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

import java.net.URL;

public class HtmlLink {
	private final String displayName;

	private final String title;

	private final URL url;

	public HtmlLink(String link) {
		displayName = HtmlUtil.getAnchorDisplayText(link);

		String titleAttributeValue = HtmlUtil.getTitleAttributeValue(link);

		// If title attribute was null, use the display text
		title = (titleAttributeValue != null) ? titleAttributeValue : HtmlUtil
				.getAnchorDisplayText(link);

		url = HtmlUtil.getHypertextLink(link);
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getTitle() {
		return title;
	}

	public URL getUrl() {
		return url;
	}
}
