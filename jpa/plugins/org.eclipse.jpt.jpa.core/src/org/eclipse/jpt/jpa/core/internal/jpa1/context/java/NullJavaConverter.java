/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.Converter;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaConverter;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaJpaContextNode;
import org.eclipse.jpt.jpa.core.resource.java.Annotation;

public class NullJavaConverter
	extends AbstractJavaJpaContextNode
	implements JavaConverter
{
	public NullJavaConverter(JavaAttributeMapping parent) {
		super(parent);
	}

	@Override
	public JavaAttributeMapping getParent() {
		return (JavaAttributeMapping) super.getParent();
	}

	public Class<? extends Converter> getType() {
		return null;
	}

	public Annotation getConverterAnnotation() {
		return null;
	}

	public void dispose() {
		// NOP
	}

	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return this.getParent().getValidationTextRange(astRoot);
	}
}
