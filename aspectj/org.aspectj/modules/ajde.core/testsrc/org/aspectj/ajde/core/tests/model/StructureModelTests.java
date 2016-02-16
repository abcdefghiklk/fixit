/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 *     AMC 21.01.2003 fixed for new source location in eclipse.org
 *     Helen Hawkins    Converted to new interface (bug 148190)
 * ******************************************************************/
package org.aspectj.ajde.core.tests.model;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.aspectj.ajde.core.AjdeCoreTestCase;
import org.aspectj.ajde.core.TestCompilerConfiguration;
import org.aspectj.ajde.core.TestMessageHandler;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.HierarchyWalker;
import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IProgramElement;

public class StructureModelTests extends AjdeCoreTestCase {

	private AsmManager manager = null;

	private String[] files = new String[]{
			"figures" + File.separator + "Debug.java",
			"figures" + File.separator + "Figure.java",
			"figures" + File.separator + "FigureElement.java",
			"figures" + File.separator + "Main.java",
			"figures" + File.separator + "composites" + File.separator + "Line.java",
			"figures" + File.separator + "composites" + File.separator + "Square.java",
			"figures" + File.separator + "primitives" + File.separator + "planar" + File.separator + "Point.java",
			"figures" + File.separator + "primitives" + File.separator + "solid" + File.separator + "SolidPoint.java"
	};
	
	private TestMessageHandler handler;
	private TestCompilerConfiguration compilerConfig;

	protected void setUp() throws Exception {
		super.setUp();
		initialiseProject("figures-coverage");
		handler = (TestMessageHandler) getCompiler().getMessageHandler();
		compilerConfig = (TestCompilerConfiguration) getCompiler()
				.getCompilerConfiguration();
		compilerConfig.setProjectSourceFiles(getSourceFileList(files));
		doBuild();
		manager = AsmManager.getDefault();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		handler = null;
		compilerConfig = null;
		manager = null;
	}
	
	public void testRootForSourceFile() throws IOException {
		File testFile = openFile("figures" + File.separator + "Figure.java");	
		IProgramElement node = manager.getHierarchy().findElementForSourceFile(
			testFile.getAbsolutePath());
		assertTrue("find result", node != null) ;	
		IProgramElement pNode = (IProgramElement)node;
		String child = ((IProgramElement)pNode.getChildren().get(1)).getName();
        assertTrue("expected Figure got child " + child, child.equals("Figure"));
	}

	public void testPointcutName() throws IOException {
		File testFile = openFile("figures" + File.separator + "Main.java");	
		IProgramElement node = manager.getHierarchy().findElementForSourceFile(
			testFile.getAbsolutePath());
		assertTrue("find result", node != null) ;	
		IProgramElement pNode = (IProgramElement)((IProgramElement)node).getChildren().get(2);
		IProgramElement pointcut = (IProgramElement)pNode.getChildren().get(0);
		assertTrue("kind", pointcut.getKind().equals(IProgramElement.Kind.POINTCUT));
		assertTrue("found node: " + pointcut.getName(), pointcut.toLabelString().equals("testptct()"));
	}

	public void testFileNodeFind() throws IOException {
		File testFile = openFile("figures" + File.separator + "Main.java");
		
//		System.err.println(((IProgramElement)((IProgramElement)Ajde.getDefault().getStructureModelManager().getHierarchy().getRoot().getChildren().get(0)).getChildren().get(3)).getSourceLocation().getSourceFile().getAbsolutePath());
//		System.err.println(testFile.getAbsolutePath());
		
		IProgramElement node = manager.getHierarchy().findElementForSourceLine(
			testFile.getAbsolutePath(), 1);
		assertTrue("find result", node != null) ;	
		assertEquals("find result has children", 3, node.getChildren().size()) ;	
		IProgramElement pNode = (IProgramElement)node;
		assertTrue("found node: " + pNode.getName(), pNode.getKind().equals(IProgramElement.Kind.FILE_JAVA));
	}
  
  	/**
  	 * @todo	add negative test to make sure things that aren't runnable aren't annotated
  	 */ 
	public void testMainClassNodeInfo() throws IOException {
        IHierarchy model = manager.getHierarchy();
        assertTrue("model exists", model != null);
		assertTrue("root exists", model.getRoot() != null);
		File testFile = openFile("figures" + File.separator + "Main.java");
		IProgramElement node = model.findElementForSourceLine(testFile.getAbsolutePath(), 11);	
		assertTrue("find result", node != null);	
		IProgramElement pNode = (IProgramElement)((IProgramElement)node).getParent();
        if (null == pNode) {
            assertTrue("null parent of " + node, false);
        }
		assertTrue("found node: " + pNode.getName(), pNode.isRunnable());
	}  
	
	/**
	 * Integrity could be checked somewhere in the API.
	 */ 
	public void testModelIntegrity() {
		IProgramElement modelRoot = manager.getHierarchy().getRoot();
		assertTrue("root exists", modelRoot != null);	
		
		try {
			testModelIntegrityHelper(modelRoot);
		} catch (Exception e) {
			assertTrue(e.toString(), false);	
		}
	}

	private void testModelIntegrityHelper(IProgramElement node) throws Exception {
		for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement child = (IProgramElement)it.next();
			if (node == child.getParent()) {
				testModelIntegrityHelper(child);
			} else {
				throw new Exception("parent-child check failed for child: " + child.toString());
			}
		}		
	}
  
  	public void testNoChildIsNull() {
  		HierarchyWalker walker = new HierarchyWalker() {
  		    public void preProcess(IProgramElement node) {
  		    	if (node.getChildren() == null) return;
  		    	for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
  		    		if (it.next() == null) throw new NullPointerException("null child on node: " + node.getName());	
  		    	}
  		    }
  		};
  		manager.getHierarchy().getRoot().walk(walker);
  	}  
}
