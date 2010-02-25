/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.java;

import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.GenericJavaOneToOneRelationshipReference;
import org.eclipse.jpt.core.jpa2.context.java.JavaOneToOneRelationshipReference2_0;

public class JavaEclipseLinkOneToOneMapping
	extends AbstractJavaEclipseLinkOneToOneMapping
{

	public JavaEclipseLinkOneToOneMapping(JavaPersistentAttribute parent) {
		super(parent);
	}

	@Override
	protected JavaOneToOneRelationshipReference2_0 buildRelationshipReference() {
		return new GenericJavaOneToOneRelationshipReference(this);
	}
}
