/********************************************************************
 * Copyright (c) 2007 Contributors. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: IBM Corporation - initial API and implementation 
 * 				 Helen Hawkins   - initial version (bug 148190)
 *******************************************************************/
package org.aspectj.ajde.ui.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.ajde.core.ICompilerConfiguration;
import org.aspectj.ajde.core.IOutputLocationManager;
import org.aspectj.ajde.core.JavaOptions;
import org.aspectj.tools.ajc.AjcTests;
import org.aspectj.util.FileUtil;

/**
 * Test implementation of ICompilerConfiguration. Allows users to configure
 * the settings via setter methods. By default returns null for all options
 * except getClasspath(), getJavaOptionsMap() (by default returns that it's 
 * 1.3 compliant), getOutputLocationManager(), getSourcePathResources() (it
 * recursively looks for them) and getProjectSourceFiles(). If no source
 * files are specified by the user, then getProjectSourceFiles() returns
 * an empty list.
 */
public class TestCompilerConfiguration implements ICompilerConfiguration {

	private String projectPath;
	
	private Set aspectpath;
	private Set inpath;
	private String outjar;
	private Map javaOptions;
	private String nonStandardOptions;
	private List projectSourceFiles = new ArrayList();
	private Map sourcePathResources;
	
	private String srcDirName = "src";
	
	private IOutputLocationManager outputLoc;
	
	public TestCompilerConfiguration(String projectPath) {
		this.projectPath = projectPath;
	}
	
	public Set getAspectPath() {
		return aspectpath;
	}

	public String getClasspath() {
		return projectPath 
        	+ File.pathSeparator 
        	+ System.getProperty("sun.boot.class.path")
        	+ File.pathSeparator 
        	+ AjcTests.aspectjrtClasspath();  
	}

	public Set getInpath() {
		return inpath;
	}

	public Map getJavaOptionsMap() {
		if (javaOptions == null) {
			javaOptions = new Hashtable();
			javaOptions.put(JavaOptions.COMPLIANCE_LEVEL,JavaOptions.VERSION_13);
			javaOptions.put(JavaOptions.SOURCE_COMPATIBILITY_LEVEL,JavaOptions.VERSION_13);
		}
		return javaOptions;
	}

	public String getNonStandardOptions() {
		return nonStandardOptions;
	}

	public String getOutJar() {
		return outjar;
	}

	public IOutputLocationManager getOutputLocationManager() {
		if (outputLoc == null) {
			outputLoc = new TestOutputLocationManager(projectPath);
		}
		return outputLoc;
	}

	public List getProjectSourceFiles() {
		return projectSourceFiles;
	}

	public Map getSourcePathResources() {
		if (sourcePathResources == null) {
			sourcePathResources = new HashMap();

			/* Allow the user to override the testProjectPath by using sourceRoots */ 
			File[] srcBase = new File[] { new File(projectPath + File.separator + srcDirName) };
			
			for (int j = 0; j < srcBase.length; j++) {
				File[] fromResources = FileUtil.listFiles(srcBase[j], new FileFilter() {
					public boolean accept(File pathname) {
						String name = pathname.getName().toLowerCase();
						return !name.endsWith(".class") 
							&& !name.endsWith(".java") 
							&& !name.endsWith(".aj")
							&& !name.endsWith(".lst")
							&& !name.endsWith(".jar");
					}
				});
				for (int i = 0; i < fromResources.length; i++) {
					String normPath = FileUtil.normalizedPath(fromResources[i] ,srcBase[j]);
					sourcePathResources.put(normPath, fromResources[i]);

				}
			}
		}
		return sourcePathResources;
	}

	
	// -------------------- setter methods useful for testing ---------------
	public void setAspectPath(Set aspectPath) {
		this.aspectpath = aspectPath;
	}

	public void setInpath(Set inpath) {
		this.inpath = inpath;
	}

	public void setOutjar(String outjar) {
		this.outjar = outjar;
	}

	public void setJavaOptions(Map javaOptions) {
		this.javaOptions = javaOptions;
	}
	
	public void setNonStandardOptions(String options) {
		this.nonStandardOptions = options;
	}

	public void setProjectSourceFiles(List projectSourceFiles) {
		this.projectSourceFiles = projectSourceFiles;
	}

	public void setSourcePathResources(Map sourcePathResources) {
		this.sourcePathResources = sourcePathResources;
	}
	
	public void setSourceDir(String srcDirName) {
		this.srcDirName = srcDirName;
	}

}
