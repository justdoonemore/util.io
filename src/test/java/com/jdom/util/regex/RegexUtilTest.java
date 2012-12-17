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
 */package com.jdom.util.regex;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.jdom.util.regex.RegexUtil;

public class RegexUtilTest {

	@Test
	public void getCompiledPatternReturWnsAPattern() {
		Pattern pattern = RegexUtil.getCompiledPattern("aba");
		assertNotNull("The pattern shouldn't be null!", pattern);

		Matcher m = pattern.matcher("abaaba");

		assertTrue("The matcher should have found matches!", m.find());
	}
}
