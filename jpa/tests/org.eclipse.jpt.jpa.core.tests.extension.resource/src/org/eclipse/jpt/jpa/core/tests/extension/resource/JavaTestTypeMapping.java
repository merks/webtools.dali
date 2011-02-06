/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.extension.resource;

import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaTypeMapping;
import org.eclipse.jpt.jpa.core.resource.java.Annotation;

public class JavaTestTypeMapping
	extends AbstractJavaTypeMapping<Annotation>
{
	public static final String TEST_TYPE_MAPPING_KEY = "test"; //$NON-NLS-1$
	public static final String TEST_TYPE_ANNOTATION_NAME = "test.Test"; //$NON-NLS-1$


	public JavaTestTypeMapping(JavaPersistentType parent) {
		super(parent, null);
	}

	public String getKey() {
		return TEST_TYPE_MAPPING_KEY;
	}

	public JavaPersistentType getIdClass() {
		return null;
	}

	public boolean isMapped() {
		return true;
	}

	public boolean tableNameIsInvalid(String tableName) {
		return false;
	}
}
