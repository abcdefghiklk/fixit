/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.weaver;

import org.aspectj.weaver.bcel.BcelWorld;

import junit.framework.TestCase;

public class TypeVariableTestCase extends TestCase {
	
	private UnresolvedType javaLangNumber;
	private UnresolvedType javaLangDouble;
	private UnresolvedType javaUtilList;
	private UnresolvedType javaIoSerializable;
	private World world;
	
	public void testDefaultBounds() {
		TypeVariable tv = new TypeVariable("T");
		assertEquals("Object",UnresolvedType.OBJECT,tv.getUpperBound());
		assertEquals("no additional bounds",0,tv.getAdditionalInterfaceBounds().length);
		assertNull("no lower bound",tv.getLowerBound());
	}
	
	public void testName() {
		TypeVariable tv = new TypeVariable("T");
		assertEquals("T",tv.getName());
	}
	
	public void testUpperBound() {
		TypeVariable tv = new TypeVariable("N",javaLangNumber);
		assertEquals("java.lang.Number",javaLangNumber,tv.getUpperBound());
	}
	
	public void testAdditionalUpperBounds() {
		TypeVariable tv = new TypeVariable("E",UnresolvedType.OBJECT,new UnresolvedType[] {javaUtilList});
		assertEquals("1 additional bound",1,tv.getAdditionalInterfaceBounds().length);
		assertEquals("java.util.List",javaUtilList,tv.getAdditionalInterfaceBounds()[0]);
	}
	
	public void testLowerBound() {
		TypeVariable tv = new TypeVariable("X",UnresolvedType.OBJECT,new UnresolvedType[0],javaLangDouble);
		assertEquals("java.lang.Double",javaLangDouble,tv.getLowerBound());
	}
	
	public void testResolution() {
		TypeVariable tv = new TypeVariable(
					"T",
					javaLangNumber,
					new UnresolvedType[] {javaUtilList},
					javaLangDouble
				);
		tv.resolve(world);
		assertEquals("resolved number",javaLangNumber.resolve(world),tv.getUpperBound());
		assertEquals("resolved list",javaUtilList.resolve(world),
									tv.getAdditionalInterfaceBounds()[0]);
		assertEquals("resolved double",javaLangDouble.resolve(world),tv.getLowerBound());
	}
	
	public void testBindWithoutResolve() {
		TypeVariable tv = new TypeVariable("X");
		try {
			tv.canBeBoundTo(null);
			fail ("Should throw illegal state exception");
		} catch (IllegalStateException ex) {}
	}
	
	public void testCanBindToUpperMatch() {
		TypeVariable tv = new TypeVariable("X",javaLangNumber);
		tv.resolve(world);
		assertTrue(tv.canBeBoundTo(javaLangDouble.resolve(world)));
	}
	
	public void testCanBindToUpperFail() {
		TypeVariable tv = new TypeVariable("X",javaLangNumber);
		tv.resolve(world);
		assertFalse(tv.canBeBoundTo(UnresolvedType.OBJECT.resolve(world)));		
	}
	
	public void testCanBindToInterfaceMatch() {
		TypeVariable tv = new TypeVariable("T",javaLangNumber,new UnresolvedType[] {javaIoSerializable});
		tv.resolve(world);
		assertTrue(tv.canBeBoundTo(javaLangDouble.resolve(world)));
	}
	
	public void testCanBindToInterfaceFail() {
		TypeVariable tv = new TypeVariable("T",javaLangNumber,new UnresolvedType[] {javaUtilList});
		tv.resolve(world);
		assertFalse(tv.canBeBoundTo(javaLangDouble.resolve(world)));	
	}
	
	public void testCanBindToLowerMatch() {
		TypeVariable tv = new TypeVariable("T",javaLangNumber,new UnresolvedType[0],javaLangDouble);
		tv.resolve(world);
		assertTrue(tv.canBeBoundTo(javaLangNumber.resolve(world)));
	}
	
	public void testCanBindToLowerFail() {
		TypeVariable tv = new TypeVariable("T",javaLangNumber,new UnresolvedType[0],javaLangNumber);
		tv.resolve(world);
		assertFalse(tv.canBeBoundTo(javaLangDouble.resolve(world)));		
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		javaLangNumber = UnresolvedType.forSignature("Ljava/lang/Number;");
		javaLangDouble = UnresolvedType.forSignature("Ljava/lang/Double;");
		javaIoSerializable = UnresolvedType.forSignature("Ljava/io/Serializable;");
		javaUtilList = UnresolvedType.forSignature("Ljava/util/List;");
		world = new BcelWorld();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
