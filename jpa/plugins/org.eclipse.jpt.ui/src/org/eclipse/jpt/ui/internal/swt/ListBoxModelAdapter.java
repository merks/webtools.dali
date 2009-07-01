/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.jpt.ui.internal.listeners.SWTCollectionChangeListenerWrapper;
import org.eclipse.jpt.ui.internal.listeners.SWTListChangeListenerWrapper;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.ListenerList;
import org.eclipse.jpt.utility.internal.StringConverter;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.model.value.PropertyCollectionValueModelAdapter;
import org.eclipse.jpt.utility.model.event.CollectionAddEvent;
import org.eclipse.jpt.utility.model.event.CollectionChangeEvent;
import org.eclipse.jpt.utility.model.event.CollectionClearEvent;
import org.eclipse.jpt.utility.model.event.CollectionRemoveEvent;
import org.eclipse.jpt.utility.model.event.ListAddEvent;
import org.eclipse.jpt.utility.model.event.ListChangeEvent;
import org.eclipse.jpt.utility.model.event.ListClearEvent;
import org.eclipse.jpt.utility.model.event.ListMoveEvent;
import org.eclipse.jpt.utility.model.event.ListRemoveEvent;
import org.eclipse.jpt.utility.model.event.ListReplaceEvent;
import org.eclipse.jpt.utility.model.listener.CollectionChangeListener;
import org.eclipse.jpt.utility.model.listener.ListChangeListener;
import org.eclipse.jpt.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.utility.model.value.ListValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;

/**
 * This adapter provides a more object-oriented interface to the items and
 * selected items in a list box.
 * 'listHolder' contains the items in the list box.
 * 'selectedItemsHolder' contains the items in 'listHolder' that are selected
 * in the list box.
 */
@SuppressWarnings("nls")
public class ListBoxModelAdapter<E> {

	// ********** model **********
	/**
	 * A value model on the underlying model list.
	 */
	protected final ListValueModel<E> listHolder;

	/**
	 * A listener that allows us to synchronize the list box's contents with
	 * the model list.
	 */
	protected final ListChangeListener listChangeListener;

	/**
	 * A value model on the underlying model selections.
	 */
	protected final CollectionValueModel<E> selectedItemsHolder;

	/**
	 * A listener that allows us to synchronize the list box's selection with
	 * the model selections.
	 */
	protected final CollectionChangeListener selectedItemsChangeListener;

	/**
	 * A converter that converts items in the model list
	 * to strings that can be put in the list box.
	 */
	protected StringConverter<E> stringConverter;

	// ********** UI **********
	/**
	 * The list box we keep synchronized with the model list.
	 */
	protected final List listBox;

	/**
	 * A listener that allows us to synchronize our selection list holder
	 * with the list box's selection.
	 */
	protected final SelectionListener listBoxSelectionListener;

	/**
	 * Clients that are interested in selection change events.
	 */
	@SuppressWarnings("unchecked")
	protected final ListenerList<SelectionChangeListener> selectionChangeListenerList;

	/**
	 * Clients that are interested in double click events.
	 */
	@SuppressWarnings("unchecked")
	protected final ListenerList<DoubleClickListener> doubleClickListenerList;

	/**
	 * A listener that allows us to stop listening to stuff when the list box
	 * is disposed.
	 */
	protected final DisposeListener listBoxDisposeListener;


	// ********** static methods **********

	/**
	 * Adapt the specified model list and selections to the specified list box.
	 * Use the default string converter to convert the model items to strings
	 * to be displayed in the list box, which calls #toString() on the
	 * items in the model list.
	 */
	public static <T> ListBoxModelAdapter<T> adapt(
			ListValueModel<T> listHolder,
			CollectionValueModel<T> selectedItemsHolder,
			List listBox)
	{
		return adapt(listHolder, selectedItemsHolder, listBox, StringConverter.Default.<T>instance());
	}

	/**
	 * Adapt the specified model list and selections to the specified list box.
	 * Use the specified string converter to convert the model items to strings
	 * to be displayed in the list box.
	 */
	public static <T> ListBoxModelAdapter<T> adapt(
			ListValueModel<T> listHolder,
			CollectionValueModel<T> selectedItemsHolder,
			List listBox,
			StringConverter<T> stringConverter)
	{
		return new ListBoxModelAdapter<T>(listHolder, selectedItemsHolder, listBox, stringConverter);
	}

	/**
	 * Adapt the specified model list and selection to the specified list box.
	 * Use the default string converter to convert the model items to strings
	 * to be displayed in the list box, which calls #toString() on the
	 * items in the model list.
	 */
	public static <T> ListBoxModelAdapter<T> adapt(
			ListValueModel<T> listHolder,
			PropertyValueModel<T> selectedItemHolder,
			List listBox)
	{
		return adapt(listHolder, selectedItemHolder, listBox, StringConverter.Default.<T>instance());
	}

	/**
	 * Adapt the specified model list and selection to the specified list box.
	 * Use the specified string converter to convert the model items to strings
	 * to be displayed in the list box.
	 */
	public static <T> ListBoxModelAdapter<T> adapt(
			ListValueModel<T> listHolder,
			PropertyValueModel<T> selectedItemHolder,
			List listBox,
			StringConverter<T> stringConverter)
	{
		return new ListBoxModelAdapter<T>(listHolder, new PropertyCollectionValueModelAdapter<T>(selectedItemHolder), listBox, stringConverter);
	}


	// ********** constructors **********

	/**
	 * Constructor - the list holder, selections holder, list box, and
	 * string converter are required.
	 */
	protected ListBoxModelAdapter(
			ListValueModel<E> listHolder,
			CollectionValueModel<E> selectedItemsHolder,
			List listBox,
			StringConverter<E> stringConverter)
	{
		super();
		if ((listHolder == null) || (selectedItemsHolder == null) || (listBox == null) || (stringConverter == null)) {
			throw new NullPointerException();
		}
		this.listHolder = listHolder;
		this.selectedItemsHolder = selectedItemsHolder;
		this.listBox = listBox;
		this.stringConverter = stringConverter;

		this.listChangeListener = this.buildListChangeListener();
		this.listHolder.addListChangeListener(ListValueModel.LIST_VALUES, this.listChangeListener);

		this.selectedItemsChangeListener = this.buildSelectedItemsChangeListener();
		this.selectedItemsHolder.addCollectionChangeListener(CollectionValueModel.VALUES, this.selectedItemsChangeListener);

		this.listBoxSelectionListener = this.buildListBoxSelectionListener();
		this.listBox.addSelectionListener(this.listBoxSelectionListener);

		this.selectionChangeListenerList = this.buildSelectionChangeListenerList();
		this.doubleClickListenerList = this.buildDoubleClickListenerList();

		this.listBoxDisposeListener = this.buildListBoxDisposeListener();
		this.listBox.addDisposeListener(this.listBoxDisposeListener);

		this.synchronizeListBox();
	}


	// ********** initialization **********

	protected ListChangeListener buildListChangeListener() {
		return new SWTListChangeListenerWrapper(this.buildListChangeListener_());
	}

	protected ListChangeListener buildListChangeListener_() {
		return new ListChangeListener() {
			public void itemsAdded(ListAddEvent event) {
				ListBoxModelAdapter.this.listItemsAdded(event);
			}
			public void itemsRemoved(ListRemoveEvent event) {
				ListBoxModelAdapter.this.listItemsRemoved(event);
			}
			public void itemsMoved(ListMoveEvent event) {
				ListBoxModelAdapter.this.listItemsMoved(event);
			}
			public void itemsReplaced(ListReplaceEvent event) {
				ListBoxModelAdapter.this.listItemsReplaced(event);
			}
			public void listCleared(ListClearEvent event) {
				ListBoxModelAdapter.this.listCleared(event);
			}
			public void listChanged(ListChangeEvent event) {
				ListBoxModelAdapter.this.listChanged(event);
			}
			@Override
			public String toString() {
				return "list listener";
			}
		};
	}

	protected CollectionChangeListener buildSelectedItemsChangeListener() {
		return new SWTCollectionChangeListenerWrapper(this.buildSelectedItemsChangeListener_());
	}

	protected CollectionChangeListener buildSelectedItemsChangeListener_() {
		return new CollectionChangeListener() {
			public void itemsAdded(CollectionAddEvent event) {
				ListBoxModelAdapter.this.selectedItemsAdded(event);
			}
			public void itemsRemoved(CollectionRemoveEvent event) {
				ListBoxModelAdapter.this.selectedItemsRemoved(event);
			}
			public void collectionCleared(CollectionClearEvent event) {
				ListBoxModelAdapter.this.selectedItemsCleared(event);
			}
			public void collectionChanged(CollectionChangeEvent event) {
				ListBoxModelAdapter.this.selectedItemsChanged(event);
			}
			@Override
			public String toString() {
				return "selected items listener";
			}
		};
	}

	protected SelectionListener buildListBoxSelectionListener() {
		return new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				ListBoxModelAdapter.this.listBoxSelectionChanged(event);
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				ListBoxModelAdapter.this.listBoxDoubleClicked(event);
			}
			@Override
			public String toString() {
				return "list box selection listener";
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected ListenerList<DoubleClickListener> buildDoubleClickListenerList() {
		return new ListenerList<DoubleClickListener>(DoubleClickListener.class);
	}

	@SuppressWarnings("unchecked")
	protected ListenerList<SelectionChangeListener> buildSelectionChangeListenerList() {
		return new ListenerList<SelectionChangeListener>(SelectionChangeListener.class);
	}

	protected DisposeListener buildListBoxDisposeListener() {
		return new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				ListBoxModelAdapter.this.listBoxDisposed(event);
			}
			@Override
			public String toString() {
				return "list box dispose listener";
			}
		};
	}

	protected void synchronizeListBox() {
		this.synchronizeListBoxItems();
		this.synchronizeListBoxSelection();
	}


	// ********** string converter **********

	public void setStringConverter(StringConverter<E> stringConverter) {
		if (stringConverter == null) {
			throw new NullPointerException();
		}
		this.stringConverter = stringConverter;
		this.synchronizeListBox();
	}


	// ********** list **********

	/**
	 * Use the string converter to convert the specified item to a
	 * string that can be added to the list box.
	 */
	protected String convert(E item) {
		return this.stringConverter.convertToString(item);
	}

	/**
	 * Brute force synchronization of list box with the model list.
	 */
	protected void synchronizeListBoxItems() {
		if (this.listBox.isDisposed()) {
			return;
		}
		int len = this.listHolder.size();
		String[] items = new String[len];
		for (int i = 0; i < len; i++) {
			items[i] = this.convert(this.listHolder.get(i));
		}
		this.listBox.setItems(items);
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listItemsAdded(ListAddEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		int i = event.getIndex();
		for (E item : this.getItems(event)) {
			this.listBox.add(this.convert(item), i++);
		}
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listItemsRemoved(ListRemoveEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		this.listBox.remove(event.getIndex(), event.getIndex() + event.getItemsSize() - 1);
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listItemsMoved(ListMoveEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		int target = event.getTargetIndex();
		int source = event.getSourceIndex();
		int len = event.getLength();
		int loStart = Math.min(target, source);
		int hiStart = Math.max(target, source);
		// make a copy of the affected items...
		String[] subArray = CollectionTools.subArray(this.listBox.getItems(), loStart, hiStart + len - loStart);
		// ...move them around...
		subArray = CollectionTools.move(subArray, target - loStart, source - loStart, len);
		// ...and then put them back
		for (int i = 0; i < subArray.length; i++) {
			this.listBox.setItem(loStart + i, subArray[i]);
		}
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listItemsReplaced(ListReplaceEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		int i = event.getIndex();
		for (E item : this.getNewItems(event)) {
			this.listBox.setItem(i++, this.convert(item));
		}
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listCleared(@SuppressWarnings("unused") ListClearEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		this.listBox.removeAll();
	}

	/**
	 * The model has changed - synchronize the list box.
	 */
	protected void listChanged(@SuppressWarnings("unused") ListChangeEvent event) {
		this.synchronizeListBoxItems();
	}

	// minimized scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E> getItems(ListAddEvent event) {
		return (Iterable<E>) event.getItems();
	}

	// minimized scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E> getNewItems(ListReplaceEvent event) {
		return (Iterable<E>) event.getNewItems();
	}


	// ********** selected items **********

	protected int indexOf(E item) {
		int len = this.listHolder.size();
		for (int i = 0; i < len; i++) {
			if (this.listHolder.get(i) == item) {
				return i;
			}
		}
		return -1;
	}

	protected void synchronizeListBoxSelection() {
		if (this.listBox.isDisposed()) {
			return;
		}
		int[] indices = new int[this.selectedItemsHolder.size()];
		int i = 0;
		for (Iterator<E> stream = this.selectedItemsHolder.iterator(); stream.hasNext(); ) {
			indices[i++] = this.indexOf(stream.next());
		}
		this.listBox.deselectAll();
		this.listBox.select(indices);
	}

	protected void selectedItemsAdded(CollectionAddEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		int[] indices = new int[event.getItemsSize()];
		int i = 0;
		for (E item : this.getItems(event)) {
			indices[i++] = this.indexOf(item);
		}
		this.listBox.select(indices);
	}

	protected void selectedItemsRemoved(CollectionRemoveEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		int[] indices = new int[event.getItemsSize()];
		int i = 0;
		for (E item : this.getItems(event)) {
			indices[i++] = this.indexOf(item);
		}
		this.listBox.deselect(indices);
	}

	protected void selectedItemsCleared(@SuppressWarnings("unused") CollectionClearEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		this.listBox.deselectAll();
	}

	protected void selectedItemsChanged(@SuppressWarnings("unused") CollectionChangeEvent event) {
		this.synchronizeListBoxSelection();
	}

	// minimized scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E> getItems(CollectionAddEvent event) {
		return (Iterable<E>) event.getItems();
	}

	// minimized scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E> getItems(CollectionRemoveEvent event) {
		return (Iterable<E>) event.getItems();
	}


	// ********** list box events **********

	protected void listBoxSelectionChanged(@SuppressWarnings("unused") SelectionEvent event) {
		if (this.selectionChangeListenerList.size() > 0) {
			@SuppressWarnings("unchecked")
			SelectionChangeEvent<E> scEvent = new SelectionChangeEvent(this, this.selectedItems());
			for (SelectionChangeListener<E> selectionChangeListener : this.selectionChangeListenerList.getListeners()) {
				selectionChangeListener.selectionChanged(scEvent);
			}
		}
	}

	protected Collection<E> selectedItems() {
		if (this.listBox.isDisposed()) {
			return Collections.emptySet();
		}
		@SuppressWarnings("unchecked")
		ArrayList<E> selectedItems = new ArrayList(this.listBox.getSelectionCount());
		for (int selectionIndex : this.listBox.getSelectionIndices()) {
			selectedItems.add(this.listHolder.get(selectionIndex));
		}
		return selectedItems;
	}

	protected void listBoxDoubleClicked(@SuppressWarnings("unused") SelectionEvent event) {
		if (this.listBox.isDisposed()) {
			return;
		}
		if (this.doubleClickListenerList.size() > 0) {
			// there should be only a single item selected during a double-click(?)
			E selection = this.listHolder.get(this.listBox.getSelectionIndex());
			@SuppressWarnings("unchecked")
			DoubleClickEvent<E> dcEvent = new DoubleClickEvent(this, selection);
			for (DoubleClickListener<E> doubleClickListener : this.doubleClickListenerList.getListeners()) {
				doubleClickListener.doubleClick(dcEvent);
			}
		}
	}


	// ********** dispose **********

	protected void listBoxDisposed(@SuppressWarnings("unused") DisposeEvent event) {
		// the list box is not yet "disposed" when we receive this event
		// so we can still remove our listeners
		this.listBox.removeDisposeListener(this.listBoxDisposeListener);
		this.listBox.removeSelectionListener(this.listBoxSelectionListener);
		this.selectedItemsHolder.removeCollectionChangeListener(CollectionValueModel.VALUES, this.selectedItemsChangeListener);
		this.listHolder.removeListChangeListener(ListValueModel.LIST_VALUES, this.listChangeListener);
	}


	// ********** standard methods **********

	@Override
	public String toString() {
		return StringTools.buildToStringFor(this, this.listHolder);
	}


	// ********** double click support **********

	public void addDoubleClickListener(DoubleClickListener<E> listener) {
		this.doubleClickListenerList.add(listener);
	}

	public void removeDoubleClickListener(DoubleClickListener<E> listener) {
		this.doubleClickListenerList.remove(listener);
	}

	public interface DoubleClickListener<E> extends EventListener {
		void doubleClick(DoubleClickEvent<E> event);
	}

	public static class DoubleClickEvent<E> extends EventObject {
		private final E selection;
		private static final long serialVersionUID = 1L;

		protected DoubleClickEvent(ListBoxModelAdapter<E> source, E selection) {
			super(source);
			if (selection == null) {
				throw new NullPointerException();
			}
			this.selection = selection;
		}

		@Override
		@SuppressWarnings("unchecked")
		public ListBoxModelAdapter<E> getSource() {
			return (ListBoxModelAdapter<E>) super.getSource();
		}

		public E selection() {
			return this.selection;
		}

	}


	// ********** selection support **********

	public void addSelectionChangeListener(SelectionChangeListener<E> listener) {
		this.selectionChangeListenerList.add(listener);
	}

	public void removeSelectionChangeListener(SelectionChangeListener<E> listener) {
		this.selectionChangeListenerList.remove(listener);
	}

	public interface SelectionChangeListener<E> extends EventListener {
		void selectionChanged(SelectionChangeEvent<E> event);
	}

	public static class SelectionChangeEvent<E> extends EventObject {
		private final Collection<E> selection;
		private static final long serialVersionUID = 1L;

		protected SelectionChangeEvent(ListBoxModelAdapter<E> source, Collection<E> selection) {
			super(source);
			if (selection == null) {
				throw new NullPointerException();
			}
			this.selection = selection;
		}

		@Override
		@SuppressWarnings("unchecked")
		public ListBoxModelAdapter<E> getSource() {
			return (ListBoxModelAdapter<E>) super.getSource();
		}

		public Iterator<E> selection() {
			return this.selection.iterator();
		}

	}

}
