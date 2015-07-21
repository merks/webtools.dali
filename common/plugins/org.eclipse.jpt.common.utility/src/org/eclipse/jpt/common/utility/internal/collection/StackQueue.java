/*******************************************************************************
 * Copyright (c) 2013, 2015 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.collection;

import java.io.Serializable;
import org.eclipse.jpt.common.utility.collection.Queue;
import org.eclipse.jpt.common.utility.collection.Stack;

/**
 * Adapt a {@link Stack} to create a LIFO implementation of the {@link Queue}
 * interface.
 * @param <E> the type of elements maintained by the queue
 * @see QueueTools
 */
public class StackQueue<E>
	implements Queue<E>, Serializable
{
	private final Stack<E> stack;

	private static final long serialVersionUID = 1L;


	public StackQueue(Stack<E> stack) {
		super();
		if (stack == null) {
			throw new NullPointerException();
		}
		this.stack = stack;
	}

	public void enqueue(E element) {
		this.stack.push(element);
	}

	public E dequeue() {
		return this.stack.pop();
	}

	public E peek() {
		return this.stack.peek();
	}

	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public String toString() {
		return this.stack.toString();
	}
}
