/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal.iterator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.eclipse.jpt.common.utility.internal.ClassTools;
import org.eclipse.jpt.common.utility.internal.ReverseComparator;
import org.eclipse.jpt.common.utility.internal.collection.HashBag;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.internal.iterator.EmptyIterator;
import org.eclipse.jpt.common.utility.internal.iterator.IteratorTools;

@SuppressWarnings("nls")
public class IteratorToolsTests
	extends TestCase
{
	public IteratorToolsTests(String name) {
		super(name);
	}


	// ********** contains **********

	public void testContainsIteratorObject_Object() {
		Collection<Object> c = new HashBag<Object>();
		c.add("zero");
		c.add("one");
		c.add("two");
		c.add("three");
		String one = "one";
		assertTrue(IteratorTools.contains(c.iterator(), one));
		assertFalse(IteratorTools.contains(c.iterator(), null));
		c.add(null);
		assertTrue(IteratorTools.contains(c.iterator(), null));
	}

	public void testContainsIteratorObject_String() {
		Collection<String> c = this.buildStringList1();
		assertTrue(IteratorTools.contains(c.iterator(), "one"));
		assertFalse(IteratorTools.contains(c.iterator(), null));
		c.add(null);
		assertTrue(IteratorTools.contains(c.iterator(), null));
	}


	// ********** contains all **********

	public void testContainsAllIteratorCollection_StringString() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildStringList1()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildStringList2()));
	}

	public void testContainsAllIteratorIntCollection() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), 5, this.buildStringList1()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), 5, this.buildStringList2()));
	}

	public void testContainsAllIteratorIntIterable() {
		Iterable<String> iterable = this.buildStringList1();
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, iterable));
		iterable = this.buildStringList2();
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, iterable));
	}

	public void testContainsAllIteratorIntIterator() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, this.buildStringList1().iterator()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, this.buildStringList2().iterator()));
	}

	public void testContainsAllIteratorIntObjectArray() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, this.buildObjectArray1()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), 3, this.buildObjectArray2()));
	}

	public void testContainsAllIteratorIterable() {
		Iterable<String> iterable = this.buildStringList1();
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), iterable));
		iterable = this.buildStringList2();
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), iterable));
	}

	public void testContainsAllIteratorIterator_ObjectString() {
		Collection<Object> c1 = new ArrayList<Object>();
		c1.add("zero");
		c1.add("one");
		c1.add("two");
		Collection<String> c2 = new ArrayList<String>();
		c2.add("zero");
		c2.add("one");
		c2.add("two");
		assertTrue(IteratorTools.containsAll(c1.iterator(), c2.iterator()));
	}

	public void testContainsAllIteratorIterator_StringString() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildStringList1().iterator()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildStringList2().iterator()));
	}

	public void testContainsAllIteratorObjectArray() {
		assertTrue(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildObjectArray1()));
		assertFalse(IteratorTools.containsAll(this.buildStringList1().iterator(), this.buildObjectArray2()));
	}

	public void testContainsAllIteratorCollection_ObjectString() {
		Collection<Object> c1 = new ArrayList<Object>();
		c1.add("zero");
		c1.add("one");
		c1.add("two");
		Collection<String> c2 = new ArrayList<String>();
		c2.add("zero");
		c2.add("one");
		c2.add("two");
		assertTrue(IteratorTools.containsAll(c1.iterator(), c2));
	}


	// ********** elements are equal **********

	public void testElementsAreDifferentIteratorIterator() {
		List<String> list1 = new ArrayList<String>();
		list1.add("1000");
		list1.add("2000");
		list1.add("3000");
		list1.add("4000");

		List<String> list2 = new ArrayList<String>();

		assertTrue(IteratorTools.elementsAreDifferent(list1.iterator(), list2.iterator()));
		assertFalse(IterableTools.elementsAreEqual(list1, list2));
	}

	public void testElementsAreEqualIteratorIterator() {
		List<String> list1 = new ArrayList<String>();
		list1.add("1000");
		list1.add("2000");
		list1.add("3000");
		list1.add("4000");

		List<String> list2 = new ArrayList<String>();
		for (int i = 0; i < list1.size(); i++) {
			list2.add(String.valueOf((i + 1) * 1000));
		}
		assertFalse(IteratorTools.elementsAreIdentical(list1.iterator(), list2.iterator()));
		assertFalse(IteratorTools.elementsAreDifferent(list1.iterator(), list2.iterator()));
		assertTrue(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}


	// ********** elements are identical **********

	public void testElementsAreIdenticalIteratorIterator() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		assertTrue(IteratorTools.elementsAreIdentical(list1.iterator(), list2.iterator()));
		assertTrue(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}

	public void testElementsAreIdenticalIteratorIterator_Not() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		list2.remove(0);
		assertFalse(IteratorTools.elementsAreIdentical(list1.iterator(), list2.iterator()));
		assertFalse(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}

	public void testElementsAreIdenticalIteratorIterator_DifferentSizes() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		list2.remove(3);
		assertFalse(IteratorTools.elementsAreIdentical(list1.iterator(), list2.iterator()));
		assertFalse(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}

	public void testElementsAreNotIdenticalIteratorIterator() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		assertFalse(IteratorTools.elementsAreNotIdentical(list1.iterator(), list2.iterator()));
		assertTrue(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}

	public void testElementsAreNotIdenticalIteratorIterator_Not() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		list2.remove(0);
		assertTrue(IteratorTools.elementsAreNotIdentical(list1.iterator(), list2.iterator()));
		assertFalse(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}

	public void testElementsAreNotIdenticalIteratorIterator_DifferentSizes() {
		List<String> list1 = new ArrayList<String>();
		list1.add("0");
		list1.add("1");
		list1.add("2");
		list1.add("3");

		List<String> list2 = new ArrayList<String>();
		for (String s : list1) {
			list2.add(s);
		}
		list2.remove(3);
		assertTrue(IteratorTools.elementsAreNotIdentical(list1.iterator(), list2.iterator()));
		assertFalse(IteratorTools.elementsAreEqual(list1.iterator(), list2.iterator()));
	}


	// ********** get **********

	public void testGetIteratorInt1() {
		List<String> list = this.buildStringList1();
		String o = IteratorTools.get(list.iterator(), 1);
		assertEquals("one", o);
		list.add(null);
		o = IteratorTools.get(list.iterator(), list.size() - 1);
		assertNull(o);
	}

	public void testGetIteratorInt2() {
		List<String> list = this.buildStringList1();
		boolean exCaught = false;
		try {
			IteratorTools.get(list.iterator(), list.size());
			fail();
		} catch (IndexOutOfBoundsException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}


	// ********** index of **********

	public void testIndexOfIteratorObject_String() {
		List<String> list = this.buildStringList1();
		assertEquals(1, IteratorTools.indexOf(list.iterator(), "one"));
	}

	public void testIndexOfIteratorObject_String_Not() {
		List<String> list = this.buildStringList1();
		assertEquals(-1, IteratorTools.indexOf(list.iterator(), null));
		assertEquals(-1, IteratorTools.indexOf(list.iterator(), "shazam"));
	}

	public void testIndexOfIteratorObject_Null() {
		List<String> list = this.buildStringList1();
		list.add(null);
		assertEquals(list.size() - 1, IteratorTools.indexOf(list.iterator(), null));
	}

	public void testIndexOfIteratorObject_Object() {
		List<Object> list = new ArrayList<Object>();
		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");

		String one = "1";
		assertEquals(1, IteratorTools.indexOf(list.iterator(), one));
		list.add(null);
		assertEquals(list.size() - 1, IteratorTools.indexOf(list.iterator(), null));
	}


	// ********** is empty **********
	
	public void testIsEmptyIterator() {
		assertFalse(IteratorTools.isEmpty(buildObjectList1().iterator()));
		assertTrue(IteratorTools.isEmpty(EmptyIterator.instance()));
	}
	
	
	// ********** iterable/iterator **********

	public void testIteratorObjectArray() {
		String[] a = this.buildStringArray1();
		int i = 0;
		for (Iterator<String> stream = IteratorTools.iterator(a); stream.hasNext(); i++) {
			assertEquals(a[i], stream.next());
		}
	}


	// ********** last **********

	public void testLastIterator1() {
		List<String> list = this.buildStringList1();
		assertEquals("two", IteratorTools.last(list.iterator()));
		list.add(null);
		assertEquals(null, IteratorTools.last(list.iterator()));
	}

	public void testLastIterator2() {
		List<String> list = new ArrayList<String>();
		boolean exCaught = false;
		try {
			IteratorTools.last(list.iterator());
			fail();
		} catch (NoSuchElementException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}


	// ********** last index of **********

	public void testLastIndexOfIteratorObject() {
		List<String> list = this.buildStringList1();
		assertEquals(1, IteratorTools.lastIndexOf(list.iterator(), "one"));
		list.add(null);
		assertEquals(list.size() - 1, IteratorTools.lastIndexOf(list.iterator(), null));
	}

	public void testLastIndexOfIteratorObject_Empty() {
		assertEquals(-1, IteratorTools.lastIndexOf(EmptyIterator.instance(), "foo"));
	}


	// ********** list iterator **********

	public void testListIteratorObjectArray() {
		String[] a = this.buildStringArray1();
		int i = 0;
		for (ListIterator<String> stream = IteratorTools.listIterator(a); stream.hasNext(); i++) {
			assertEquals(a[i], stream.next());
		}
	}

	public void testListIteratorObjectArrayInt() {
		String[] a = this.buildStringArray1();
		int i = 1;
		for (ListIterator<String> stream = IteratorTools.listIterator(a, 1); stream.hasNext(); i++) {
			assertEquals(a[i], stream.next());
		}
	}


	// ********** singleton iterator **********

	public void testSingletonIterator_String() {
		Iterator<String> stream = IteratorTools.singletonIterator("foo");
		assertTrue(stream.hasNext());
		assertEquals("foo", stream.next());
	}

	public void testSingletonIterator_Object() {
		Iterator<Object> stream = IteratorTools.<Object>singletonIterator("foo");
		assertTrue(stream.hasNext());
		assertEquals("foo", stream.next());
	}

	public void testSingletonIterator_Cast() {
		Iterator<Object> stream = IteratorTools.singletonIterator((Object) "foo");
		assertTrue(stream.hasNext());
		assertEquals("foo", stream.next());
	}

	public void testSingletonListIterator_String() {
		ListIterator<String> stream = IteratorTools.singletonListIterator("foo");
		assertTrue(stream.hasNext());
		assertEquals("foo", stream.next());
		assertFalse(stream.hasNext());
		assertTrue(stream.hasPrevious());
		assertEquals("foo", stream.previous());
	}


	// ********** size **********

	public void testSizeIterator() {
		assertEquals(3, IteratorTools.size(this.buildObjectList1().iterator()));
	}
	
	
	// ********** sort **********

	public void testSortIterator() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("0");
		list.add("2");
		list.add("3");
		list.add("1");

		SortedSet<String> ss = new TreeSet<String>();
		ss.addAll(list);

		Iterator<String> iterator1 = list.iterator();
		Iterator<String> iterator2 = IteratorTools.<String>sort(iterator1);
		assertTrue(IteratorTools.elementsAreEqual(ss.iterator(), iterator2));
	}

	public void testSortIteratorInt() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("0");
		list.add("2");
		list.add("3");
		list.add("1");

		SortedSet<String> ss = new TreeSet<String>();
		ss.addAll(list);

		Iterator<String> iterator1 = list.iterator();
		Iterator<String> iterator2 = IteratorTools.<String>sort(iterator1, 77);
		assertTrue(IteratorTools.elementsAreEqual(ss.iterator(), iterator2));
	}

	public void testSortIteratorComparator() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("0");
		list.add("2");
		list.add("3");
		list.add("1");

		SortedSet<String> ss = new TreeSet<String>(new ReverseComparator<String>());
		ss.addAll(list);

		Iterator<String> iterator1 = list.iterator();
		Iterator<String> iterator2 = IteratorTools.<String>sort(iterator1, new ReverseComparator<String>());
		assertTrue(IteratorTools.elementsAreEqual(ss.iterator(), iterator2));
	}

	public void testSortIteratorComparatorInt() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("0");
		list.add("2");
		list.add("3");
		list.add("1");

		SortedSet<String> ss = new TreeSet<String>(new ReverseComparator<String>());
		ss.addAll(list);

		Iterator<String> iterator1 = list.iterator();
		Iterator<String> iterator2 = IteratorTools.<String>sort(iterator1, new ReverseComparator<String>(), 77);
		assertTrue(IteratorTools.elementsAreEqual(ss.iterator(), iterator2));
	}

	public void testConstructor() {
		boolean exCaught = false;
		try {
			Object at = ClassTools.newInstance(IteratorTools.class);
			fail("bogus: " + at); //$NON-NLS-1$
		} catch (RuntimeException ex) {
			if (ex.getCause() instanceof InvocationTargetException) {
				if (ex.getCause().getCause() instanceof UnsupportedOperationException) {
					exCaught = true;
				}
			}
		}
		assertTrue(exCaught);
	}


	// ********** test harness **********

	private Object[] buildObjectArray1() {
		return new Object[] { "zero", "one", "two" };
	}

	private String[] buildStringArray1() {
		return new String[] { "zero", "one", "two" };
	}

	private Object[] buildObjectArray2() {
		return new Object[] { "three", "four", "five" };
	}

	private List<String> buildStringList1() {
		List<String> l = new ArrayList<String>();
		this.addToCollection1(l);
		return l;
	}

	private List<Object> buildObjectList1() {
		List<Object> l = new ArrayList<Object>();
		this.addToCollection1(l);
		return l;
	}

	private void addToCollection1(Collection<? super String> c) {
		c.add("zero");
		c.add("one");
		c.add("two");
	}

	private List<String> buildStringList2() {
		List<String> l = new ArrayList<String>();
		this.addToCollection2(l);
		return l;
	}

	private void addToCollection2(Collection<? super String> c) {
		c.add("three");
		c.add("four");
		c.add("five");
	}
}
