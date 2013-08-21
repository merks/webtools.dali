/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.closure;

/**
 * Convenience superclass that does nothing if the argument
 * is <code>null</code>; otherwise it calls {@link #execute_(Object)},
 * which is to be implemented by subclasses.
 * 
 * @param <A> the type of the object passed to the closure
 * 
 * @see NullClosure
 * @see InterruptibleClosureAdapter
 * @see NullCheckInterruptibleClosureWrapper
 */
public abstract class AbstractInterruptibleClosure<A>
	extends InterruptibleClosureAdapter<A>
{
	@Override
	public final void execute(A argument) throws InterruptedException {
		if (argument != null) {
			this.execute_(argument);
		}
	}

	/**
	 * Process the specified argument; its value is guaranteed to be not
	 * <code>null</code>.
	 */
	protected abstract void execute_(A argument) throws InterruptedException;
}
