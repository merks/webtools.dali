/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jdtutility;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

/**
 * Behaviorless implementation.
 */
public class NullDeclarationAnnotationElementAdapter implements DeclarationAnnotationElementAdapter {

	// singleton
	private static final NullDeclarationAnnotationElementAdapter INSTANCE = new NullDeclarationAnnotationElementAdapter();

	/**
	 * Return the singleton.
	 */
	public static DeclarationAnnotationElementAdapter instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private NullDeclarationAnnotationElementAdapter() {
		super();
	}

	public Object getValue(ModifiedDeclaration declaration) {
		return null;
	}

	public void setValue(Object value, ModifiedDeclaration declaration) {
		// do nothing
	}

	public ASTNode astNode(ModifiedDeclaration declaration) {
		return declaration.getDeclaration();
	}

	public Expression expression(ModifiedDeclaration declaration) {
		return null;
	}

}
