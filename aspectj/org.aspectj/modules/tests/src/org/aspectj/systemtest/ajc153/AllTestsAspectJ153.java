/*******************************************************************************
 * Copyright (c) 2006 IBM 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andy Clement - initial API and implementation
 *******************************************************************************/
package org.aspectj.systemtest.ajc153;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsAspectJ153 {

	public static Test suite() {
		TestSuite suite = new TestSuite("AspectJ 1.5.3 tests");
		//$JUnit-BEGIN$
		suite.addTest(Ajc153Tests.suite());
		suite.addTest(JDTLikeHandleProviderTests.suite());
		suite.addTest(PipeliningTests.suite());
		suite.addTest(LTWServer153Tests.suite());
        //$JUnit-END$
		return suite;
	}
}
