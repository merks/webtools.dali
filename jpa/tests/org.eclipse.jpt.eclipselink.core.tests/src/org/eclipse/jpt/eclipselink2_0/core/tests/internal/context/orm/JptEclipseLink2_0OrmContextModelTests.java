/*******************************************************************************
* Copyright (c) 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink2_0.core.tests.internal.context.orm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JptEclipseLink2_0OrmContextModelTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite(JptEclipseLink2_0OrmContextModelTests.class.getName());
		suite.addTestSuite(EclipseLink2_0OrmEntityTests.class);
		suite.addTestSuite(Eclipselink2_0OrmManyToOneMappingTests.class);
		suite.addTestSuite(EclipseLink2_0OrmMappedSuperclassTests.class);
		suite.addTestSuite(EclipseLink2_0OrmOneToOneMappingTests.class);
		suite.addTestSuite(EclipseLink2_0OrmOneToManyMappingTests.class);
		return suite;
	}

	private JptEclipseLink2_0OrmContextModelTests() {
		super();
		throw new UnsupportedOperationException();
	}
}
