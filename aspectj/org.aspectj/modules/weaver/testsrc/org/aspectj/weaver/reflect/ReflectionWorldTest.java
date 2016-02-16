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
package org.aspectj.weaver.reflect;

import junit.framework.TestCase;

import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

public class ReflectionWorldTest extends TestCase {

	public void testDelegateCreation() {
		World world = new ReflectionWorld(getClass().getClassLoader());
		ResolvedType rt = world.resolve("java.lang.Object");
		assertNotNull(rt);
		assertEquals("Ljava/lang/Object;",rt.getSignature());
	}
	
	public void testArrayTypes() {
		IReflectionWorld world = new ReflectionWorld(getClass().getClassLoader());
		String[] strArray = new String[1];
		ResolvedType rt = world.resolve(strArray.getClass());
		assertTrue(rt.isArray());
	}
	
	public void testPrimitiveTypes() {
		IReflectionWorld world = new ReflectionWorld(getClass().getClassLoader());
		assertEquals("int",ResolvedType.INT,world.resolve(int.class));
		assertEquals("void",ResolvedType.VOID,world.resolve(void.class));
	}
	
}
