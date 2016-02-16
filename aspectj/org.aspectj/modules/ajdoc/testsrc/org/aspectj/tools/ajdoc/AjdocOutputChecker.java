/********************************************************************
 * Copyright (c) 2005 Contributors. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: IBM Corporation - initial API and implementation 
 * 				 Helen Hawkins   - iniital version
 *******************************************************************/
package org.aspectj.tools.ajdoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to check whether the ajdoc contains the expected
 * information.
 */
public class AjdocOutputChecker {

	/**
	 * Checks whether the given html file contains the required String. 
	 * 
	 * @param htmlFile
	 * @param requiredString
	 * @return true if the file contains the given string or
	 * false otherwise (or if the file is null or not an html file)
	 * @throws Exception
	 */
	public static boolean containsString(File htmlFile,
			String requiredString) throws Exception {
		if ((htmlFile == null) || !htmlFile.getAbsolutePath().endsWith("html")) {
			return false;
		}
		BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
		String line = reader.readLine();
		while (line != null) {
			if (line.indexOf(requiredString) != -1) {
				reader.close();
				return true;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}

	/**
	 * Returns those strings from the given array which aren't in the
	 * html file
	 * 
	 * @param htmlFile
	 * @param an array of requiredStrings
	 * @return a List of those strings not found
	 * @throws Exception
	 */	
	public static List /*String*/ getMissingStringsInFile(File htmlFile,
			String[] requiredStrings) throws Exception {
		List missingStrings = new ArrayList();
		for (int i = 0; i < requiredStrings.length; i++) {
			String string = requiredStrings[i];
			if (!containsString(htmlFile,string)) {
				missingStrings.add(string);
			}
		}
		return missingStrings;
	}
	
	/**
	 * Checks whether the section of the html file contains the
	 * required String
	 * 
	 * @param htmlFile
	 * @param requiredString
	 * @param sectionHeader
	 * @return true if the file contains the given string within the 
	 * required section or false otherwise (or if the file is null or 
	 * not an html file)
	 * @throws Exception
	 */
	public static boolean containsStringWithinSection(File htmlFile,
			String requiredString, String sectionHeader) throws Exception {
		if ((htmlFile == null) || !htmlFile.getAbsolutePath().endsWith("html")) {
			return false;
		}
		BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
		String line = reader.readLine();
		while (line != null) {
			if (line.indexOf(sectionHeader) != -1) {
				String nextLine = reader.readLine();
				while (nextLine != null && 
						(nextLine.indexOf("========") == -1)) {
					if (nextLine.indexOf(requiredString) != -1) {
						reader.close();
						return true;
					}
					nextLine = reader.readLine();
				}
				reader.close();
				return false;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}
	
	/**
	 * Returns those strings from the given array which aren't in the
	 * ajdoc html file	 
	 *  
	 * @param htmlFile
	 * @param an array of requiredStrings
	 * @param sectionHeader
	 * @return List of those requiredStrings not found
	 * @throws Exception
	 */
	public static List /*String*/ getMissingStringsInSection(File htmlFile,
			String[] requiredStrings, String sectionHeader) throws Exception {
		List missingStrings = new ArrayList();
		for (int i = 0; i < requiredStrings.length; i++) {
			String string = requiredStrings[i];
			if (!containsStringWithinSection(htmlFile,string,sectionHeader)) {
				missingStrings.add(string);
			}
		}
		return missingStrings;
	}

	/**
	 * Returns whether the class data section has the expected 
	 * relationship and target i.e. have the relationships been 
	 * applied to the type.
	 * 
	 * @param the ajdoc html file
	 * @param the detail sectionHeader, for example "DECLARE DETAIL SUMMARY"
	 * @param the source of the relationship, for example "Point()"
	 * @param the relationship, for example HtmlDecorator.HtmlRelationshipKind.MATCHED_BY
	 * @param the expected target, for example "HREF=\"../foo/Main.html#doIt()\""
	 * @return true if the section contains the expected source/relationship/target,
	 * false otherwise
	 */
	public static boolean classDataSectionContainsRel(File htmlFile,
			HtmlDecorator.HtmlRelationshipKind relationship, 
			String target) throws Exception {
		if (((htmlFile == null) || !htmlFile.getAbsolutePath().endsWith("html"))) {
			return false;
		}
		BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
		String line = reader.readLine();
		while (line != null) {
			if (line.indexOf("START OF CLASS DATA") != -1) {
				// found the required class data section
				String subLine = reader.readLine();
				while(subLine != null 
						&& (subLine.indexOf("========") == -1)){
					int relIndex = subLine.indexOf(relationship.toString());
					int targetIndex = subLine.indexOf(target);
					if ((relIndex != -1) && (targetIndex != -1)) {
						reader.close();
						if (relIndex < targetIndex) {
							return true;
						}
						return false;
					}
					subLine = reader.readLine();
				}
				reader.close();
				return false;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}
	
	/**
	 * Returns whether the supplied source has the expected 
	 * relationship and target within the given detail section
	 * 
	 * @param the ajdoc html file
	 * @param the detail sectionHeader, for example "DECLARE DETAIL SUMMARY"
	 * @param the source of the relationship, for example "Point()"
	 * @param the relationship, for example HtmlDecorator.HtmlRelationshipKind.MATCHED_BY
	 * @param the expected target, for example "HREF=\"../foo/Main.html#doIt()\""
	 * @return true if the section contains the expected source/relationship/target,
	 * false otherwise
	 */
	public static boolean detailSectionContainsRel(File htmlFile, 
			String sectionHeader, String source, 
			HtmlDecorator.HtmlRelationshipKind relationship, 
			String target) throws Exception {
		if (((htmlFile == null) || !htmlFile.getAbsolutePath().endsWith("html"))) {
			return false;
		}
		if (sectionHeader.indexOf("DETAIL") == -1) {
			return false;
		}
		BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
		String line = reader.readLine();
		while (line != null) {
			if (line.indexOf(sectionHeader) != -1) {
				// found the required main section
				String nextLine = reader.readLine();
				while (nextLine != null && (nextLine.indexOf("========") == -1)) {
					if (nextLine.indexOf("NAME=\""+source+"\"") != -1) {
						// found the required subsection
						String subLine = reader.readLine();
						while(subLine != null 
								&& (subLine.indexOf("========") == -1)
								&& (subLine.indexOf("NAME") == -1)) {
							int relIndex = subLine.indexOf(relationship.toString());
							int targetIndex = subLine.indexOf(target);
							if ((relIndex != -1) && (targetIndex != -1)) {
								reader.close();
								if (relIndex < targetIndex) {
									return true;
								}
								return false;
							}
							subLine = reader.readLine();
						}
						reader.close();
						return false;
					}
					nextLine = reader.readLine();
				}
				reader.close();
				return false;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}
	
	/**
	 * Returns whether the supplied source has the expected 
	 * relationship and target within the given summary section
	 * 
	 * @param the ajdoc html file
	 * @param the detail sectionHeader, for example "DECLARE SUMMARY"
	 * @param the source of the relationship, for example "Point()"
	 * @param the relationship, for example HtmlDecorator.HtmlRelationshipKind.MATCHED_BY
	 * @param the expected target, for example "HREF=\"../foo/Main.html#doIt()\""
	 * @return true if the section contains the expected source/relationship/target,
	 * false otherwise
	 */
	public static boolean summarySectionContainsRel(
			File htmlFile, 
			String sectionHeader, 
			String source, 
			HtmlDecorator.HtmlRelationshipKind relationship, 
			String target) throws Exception {
		if (((htmlFile == null) || !htmlFile.getAbsolutePath().endsWith("html"))) {
			return false;
		}
		if (sectionHeader.indexOf("SUMMARY") == -1) {
			return false;
		}
		BufferedReader reader = new BufferedReader(new FileReader(htmlFile));
		String line = reader.readLine();
		while (line != null) {
			if (line.indexOf(sectionHeader) != -1) {
				// found the required main section
				String nextLine = reader.readLine();
				while (nextLine != null && (nextLine.indexOf("========") == -1)) {
					if (nextLine.indexOf(source) != -1) {
						// found the required subsection
						String subLine = nextLine;
						while(subLine != null 
								&& (subLine.indexOf("========") == -1)
								&& (subLine.indexOf("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">") == -1)) {
							int relIndex = subLine.indexOf(relationship.toString());
							int targetIndex = subLine.indexOf(target);
							if ((relIndex != -1) && (targetIndex != -1)) {
								reader.close();
								if (relIndex < targetIndex) {
									return true;
								}
								return false;
							}
							subLine = reader.readLine();
						}
						reader.close();
						return false;
					}
					nextLine = reader.readLine();
				}
				reader.close();
				return false;
			}
			line = reader.readLine();
		}
		reader.close();
		return false;
	}
	
}
