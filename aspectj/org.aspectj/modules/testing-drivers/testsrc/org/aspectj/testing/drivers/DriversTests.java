/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC)
 *               2003 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 *     Wes Isberg     added JUnit harness adapters
 * ******************************************************************/


package org.aspectj.testing.drivers;

import junit.framework.*;

public class DriversTests extends TestCase {

    public static Test suite() { 
        TestSuite suite = new TestSuite(DriversTests.class.getName());
        // AjcTestsUsingJUnit takes too long to include by default
        //$JUnit-BEGIN$
        suite.addTestSuite(HarnessSelectionTest.class); 
        suite.addTest(AjcHarnessTestsUsingJUnit.suite()); 
        //$JUnit-END$
        return suite;
    }

    public DriversTests(String name) { super(name); }

}  
