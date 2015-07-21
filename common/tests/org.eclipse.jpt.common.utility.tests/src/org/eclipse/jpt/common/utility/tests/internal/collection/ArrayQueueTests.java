/*******************************************************************************
 * Copyright (c) 2012, 2015 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal.collection;

import java.util.ArrayList;
import org.eclipse.jpt.common.utility.collection.Queue;
import org.eclipse.jpt.common.utility.internal.collection.ArrayQueue;
import org.eclipse.jpt.common.utility.internal.collection.QueueTools;
import org.eclipse.jpt.common.utility.tests.internal.TestTools;

@SuppressWarnings("nls")
public class ArrayQueueTests
	extends QueueTests
{
	public ArrayQueueTests(String name) {
		super(name);
	}

	@Override
	Queue<String> buildQueue() {
		return new ArrayQueue<String>();
	}

	public void testCollectionConstructor() {
		ArrayList<String> c = new ArrayList<String>();
		c.add("first");
		c.add("second");
		c.add("third");
		c.add("fourth");
		c.add("fifth");
		c.add("sixth");
		c.add("seventh");
		c.add("eighth");
		c.add("ninth");
		c.add("tenth"); // force some free space
		Queue<String> queue = QueueTools.arrayQueue(c);

		assertFalse(queue.isEmpty());
		assertEquals("first", queue.peek());
		queue.enqueue("eleventh");
		queue.enqueue("twelfth");

		assertEquals("first", queue.peek());
		assertEquals("first", queue.dequeue());
		assertEquals("second", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("third", queue.peek());
		assertEquals("third", queue.dequeue());
		assertEquals("fourth", queue.dequeue());
		assertEquals("fifth", queue.dequeue());
		assertEquals("sixth", queue.dequeue());
		assertEquals("seventh", queue.dequeue());
		assertEquals("eighth", queue.dequeue());
		assertEquals("ninth", queue.dequeue());
		assertEquals("tenth", queue.dequeue());
		assertEquals("eleventh", queue.dequeue());
		assertEquals("twelfth", queue.dequeue());
		assertTrue(queue.isEmpty());
	}

	public void testWrappedElements() {
		Queue<String> queue = this.buildQueue();
		assertTrue(queue.isEmpty());
		queue.enqueue("first");
		assertFalse(queue.isEmpty());
		queue.enqueue("second");
		assertFalse(queue.isEmpty());
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		queue.enqueue("sixth");

		// make room for 11 and 12
		assertEquals("first", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("second", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("third", queue.dequeue());

		queue.enqueue("seventh");
		queue.enqueue("eighth");
		queue.enqueue("ninth");
		queue.enqueue("tenth");
		queue.enqueue("eleventh");
		queue.enqueue("twelfth");

		assertEquals("fourth", queue.dequeue());
		assertEquals("fifth", queue.dequeue());
		assertEquals("sixth", queue.dequeue());
		assertEquals("seventh", queue.dequeue());
		assertEquals("eighth", queue.dequeue());
		assertEquals("ninth", queue.dequeue());
		assertEquals("tenth", queue.dequeue());
		assertEquals("eleventh", queue.dequeue());
		assertEquals("twelfth", queue.dequeue());
		assertTrue(queue.isEmpty());
	}

	public void testArrayCapacityExceeded() {
		Queue<String> queue = this.buildQueue();
		assertTrue(queue.isEmpty());
		queue.enqueue("first");
		assertFalse(queue.isEmpty());
		queue.enqueue("second");
		assertFalse(queue.isEmpty());
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		queue.enqueue("sixth");
		queue.enqueue("seventh");
		queue.enqueue("eighth");
		queue.enqueue("ninth");
		queue.enqueue("tenth");
		queue.enqueue("eleventh");
		queue.enqueue("twelfth");

		assertEquals("first", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("second", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("third", queue.dequeue());
		assertEquals("fourth", queue.dequeue());
		assertEquals("fifth", queue.dequeue());
		assertEquals("sixth", queue.dequeue());
		assertEquals("seventh", queue.dequeue());
		assertEquals("eighth", queue.dequeue());
		assertEquals("ninth", queue.dequeue());
		assertEquals("tenth", queue.dequeue());
		assertEquals("eleventh", queue.dequeue());
		assertEquals("twelfth", queue.dequeue());
		assertTrue(queue.isEmpty());
	}

	public void testArrayCapacityExceededWithWrappedElements() {
		Queue<String> queue = this.buildQueue();
		assertTrue(queue.isEmpty());
		queue.enqueue("first");
		assertFalse(queue.isEmpty());
		queue.enqueue("second");
		assertFalse(queue.isEmpty());
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		queue.enqueue("sixth");

		assertEquals("first", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("second", queue.dequeue());
		assertFalse(queue.isEmpty());
		assertEquals("third", queue.dequeue());

		queue.enqueue("seventh");
		queue.enqueue("eighth");
		queue.enqueue("ninth");
		queue.enqueue("tenth");
		queue.enqueue("eleventh");
		queue.enqueue("twelfth");
		queue.enqueue("thirteenth");
		queue.enqueue("fourteenth");
		queue.enqueue("fifteenth");

		assertEquals("fourth", queue.dequeue());
		assertEquals("fifth", queue.dequeue());
		assertEquals("sixth", queue.dequeue());
		assertEquals("seventh", queue.dequeue());
		assertEquals("eighth", queue.dequeue());
		assertEquals("ninth", queue.dequeue());
		assertEquals("tenth", queue.dequeue());
		assertEquals("eleventh", queue.dequeue());
		assertEquals("twelfth", queue.dequeue());
		assertEquals("thirteenth", queue.dequeue());
		assertEquals("fourteenth", queue.dequeue());
		assertEquals("fifteenth", queue.dequeue());
		assertTrue(queue.isEmpty());
	}

	public void testSerialization_fullArray() throws Exception {
		Queue<String> queue = new ArrayQueue<String>(3);
		queue.enqueue("first");
		queue.enqueue("second");
		queue.enqueue("third");

		this.verifyClone(queue, TestTools.serialize(queue));
	}
}
