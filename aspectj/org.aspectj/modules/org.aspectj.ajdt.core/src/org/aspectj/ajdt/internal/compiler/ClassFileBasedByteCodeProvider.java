/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aspectj.ajdt.internal.compiler;

import org.aspectj.weaver.bcel.UnwovenClassFile;
import org.aspectj.weaver.bcel.UnwovenClassFileWithThirdPartyManagedBytecode;
import org.aspectj.org.eclipse.jdt.internal.compiler.ClassFile;
import org.aspectj.org.eclipse.jdt.internal.compiler.CompilationResult;

/**
 * @author colyer
 *
 *	Adaptor for ClassFiles that lets them act as the bytecode repository
 *	for UnwovenClassFiles (asking a ClassFile for its bytes causes a 
 *	copy to be made).
 */
public class ClassFileBasedByteCodeProvider 
	   implements UnwovenClassFileWithThirdPartyManagedBytecode.IByteCodeProvider{
	
	private ClassFile cf;
		
	public ClassFileBasedByteCodeProvider(ClassFile cf) {
		this.cf = cf;
	}
		
	public byte[] getBytes() {
		return cf.getBytes();
	}
		
	public static UnwovenClassFile[] unwovenClassFilesFor(CompilationResult result, 
										            IOutputClassFileNameProvider nameProvider) {
		ClassFile[] cfs = result.getClassFiles();
		UnwovenClassFile[] ret = new UnwovenClassFile[cfs.length];
		for (int i = 0; i < ret.length; i++) {
			ClassFileBasedByteCodeProvider p = new ClassFileBasedByteCodeProvider(cfs[i]);
			String fileName = nameProvider.getOutputClassFileName(cfs[i].fileName(), result);
			ret[i] = new UnwovenClassFileWithThirdPartyManagedBytecode(fileName,p);
		}
		return ret;
	}
		
}
