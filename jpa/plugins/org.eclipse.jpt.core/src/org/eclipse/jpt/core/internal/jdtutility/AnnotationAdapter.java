/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jdtutility;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Adapt a Java annotation with a simple-to-use interface.
 */
public interface AnnotationAdapter {

	/**
	 * Return the value of the adapter's annotation.
	 * If the compilation unit is available, #getAnnotation(CompilationUnit)
	 * might be more performant.
	 * @see #getAnnotation(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	Annotation getAnnotation();

	/**
	 * Given the specified compilation unit, return the value of the
	 * adapter's annotation.
	 * @see #getAnnotation()
	 */
	Annotation getAnnotation(CompilationUnit astRoot);

	/**
	 * Build a new marker annotation, replacing the original annotation if present.
	 */
	void newMarkerAnnotation();

	/**
	 * Build a new single member annotation, replacing the original annotation if present.
	 */
	void newSingleMemberAnnotation();

	/**
	 * Build a new normal annotation, replacing the original annotation if present.
	 */
	void newNormalAnnotation();

	/**
	 * Remove the annotation.
	 */
	void removeAnnotation();

	/**
	 * Return the AST node corresponding to the annotation.
	 * If the annotation is missing, return the annotation's parent's node.
	 * If the compilation unit is available, #astNode(CompilationUnit)
	 * might be more performant.
	 * @see #astNode(org.eclipse.jdt.core.dom.CompilationUnit)
	 */
	ASTNode astNode();

	/**
	 * Return the AST node corresponding to the annotation.
	 * If the annotation is missing, return the annotation's parent node.
	 * @see #astNode()
	 */
	ASTNode astNode(CompilationUnit astRoot);

}
