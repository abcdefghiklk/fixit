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

import org.aspectj.ajde.IRuntimeProperties;

/**
 * IRuntimeProperties with empty implementation
 */
public class TestRuntimeProperties implements IRuntimeProperties {

	public String getClassToExecute() {
		return null;
	}

	public String getExecutionArgs() {
		return null;
	}

}
