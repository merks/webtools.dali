/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.model.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jpt.common.utility.internal.ArrayTools;
import org.eclipse.jpt.common.utility.internal.StringBuilderTools;
import org.eclipse.jpt.common.utility.internal.collection.CollectionTools;
import org.eclipse.jpt.common.utility.internal.collection.ListTools;
import org.eclipse.jpt.common.utility.internal.iterable.ListListIterable;
import org.eclipse.jpt.common.utility.internal.iterable.ReadOnlyCompositeListIterable;
import org.eclipse.jpt.common.utility.internal.iterable.SingleElementIterable;
import org.eclipse.jpt.common.utility.internal.iterable.TransformationListIterable;
import org.eclipse.jpt.common.utility.internal.transformer.TransformerAdapter;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.common.utility.model.event.ListAddEvent;
import org.eclipse.jpt.common.utility.model.event.ListChangeEvent;
import org.eclipse.jpt.common.utility.model.event.ListClearEvent;
import org.eclipse.jpt.common.utility.model.event.ListEvent;
import org.eclipse.jpt.common.utility.model.event.ListMoveEvent;
import org.eclipse.jpt.common.utility.model.event.ListRemoveEvent;
import org.eclipse.jpt.common.utility.model.event.ListReplaceEvent;
import org.eclipse.jpt.common.utility.model.listener.ListChangeAdapter;
import org.eclipse.jpt.common.utility.model.listener.ListChangeListener;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * A <code>CompositeListValueModel</code> wraps another
 * {@link ListValueModel} and uses a {@link Transformer}
 * to convert each item in the wrapped list to yet another
 * {@link ListValueModel}. This composite list contains
 * the combined items from all these component lists.
 * <p>
 * Terminology:<ul>
 * <li><em>sources</em> - the items in the wrapped list value model; these
 *    are converted into component LVMs by the transformer
 * <li><em>component LVMs</em> - the component list value models that are combined
 *    by this composite list value model
 * <li><em>items</em> - the items held by the component LVMs
 * </ul>
 * 
 * @param <E1> the type of items held by the wrapped list model;
 *     each of these is transformed (by the {@link #transformer}) into a
 *     list model of <code>E2</code>s
 * @param <E2> the type of items held by the composite list model
 */
public class CompositeListValueModel<E1, E2>
	extends ListValueModelWrapper<E1>
	implements ListValueModel<E2>
{
	/**
	 * This is the (optional) user-supplied object that transforms
	 * the items in the wrapped list to list value models.
	 */
	private final Transformer<E1, ? extends ListValueModel<? extends E2>> transformer;

	/**
	 * Cache of the sources, component LVMs, lists.
	 */
	private final ArrayList<Info> infoList = new ArrayList<Info>();
	protected class Info {
		// the object passed to the transformer
		final E1 source;
		// the list value model generated by the transformer
		final ListValueModel<? extends E2> componentLVM;
		// cache of the items held by the component LVM
		final ArrayList<E2> items;
		// the component LVM's beginning index within the composite LVM
		int begin;
		protected Info(E1 source, ListValueModel<? extends E2> componentLVM, ArrayList<E2> items, int begin) {
			super();
			this.source = source;
			this.componentLVM = componentLVM;
			this.items = items;
			this.begin = begin;
		}
	}

	/** Listener that listens to all the component list value models. */
	private final ListChangeListener componentLVMListener;

	/** Cache the size of the composite list. */
	private int size;


	// ********** constructors **********

	/**
	 * Construct a list value model with the specified wrapped
	 * collection value model. The specified collection model already contains other
	 * list value models.
	 */
	public static <E1 extends ListValueModel<? extends E2>, E2> CompositeListValueModel<E1, E2> forModels(CollectionValueModel<E1> collectionModel) {
		return forModels(new CollectionListValueModelAdapter<E1>(collectionModel));
	}

	/**
	 * Construct a list value model with the specified wrapped list.
	 */
	public static <E1 extends ListValueModel<? extends E2>, E2> CompositeListValueModel<E1, E2> forModels(List<E1> list) {
		return forModels(new StaticListValueModel<E1>(list));
	}

	/**
	 * Construct a list value model with the specified wrapped list.
	 */
	public static <E1 extends ListValueModel<? extends E2>, E2> CompositeListValueModel<E1, E2> forModels(E1... list) {
		return forModels(new StaticListValueModel<E1>(list));
	}

	/**
	 * Construct a list value model with the specified wrapped
	 * list value model. The specified list
	 * model already contains other list value models.
	 */
	public static <E1 extends ListValueModel<? extends E2>, E2> CompositeListValueModel<E1, E2> forModels(ListValueModel<E1> listModel) {
		return new CompositeListValueModel<E1, E2>(listModel, Transformer.Null.<E1>instance());
	}

	/**
	 * Construct a list value model with the specified wrapped
	 * collection value model and transformer.
	 */
	public CompositeListValueModel(CollectionValueModel<? extends E1> collectionModel, Transformer<E1, ? extends ListValueModel<? extends E2>> transformer) {
		this(new CollectionListValueModelAdapter<E1>(collectionModel), transformer);
	}

	/**
	 * Construct a list value model with the specified, unchanging, wrapped
	 * list and transformer.
	 */
	public CompositeListValueModel(List<? extends E1> list, Transformer<E1, ? extends ListValueModel<? extends E2>> transformer) {
		this(new StaticListValueModel<E1>(list), transformer);
	}

	/**
	 * Construct a list value model with the specified, unchanging, wrapped
	 * list and transformer.
	 */
	public CompositeListValueModel(E1[] list, Transformer<E1, ? extends ListValueModel<? extends E2>> transformer) {
		this(new StaticListValueModel<E1>(list), transformer);
	}

	/**
	 * Construct a list value model with the specified wrapped
	 * list value model and transformer.
	 */
	public CompositeListValueModel(ListValueModel<? extends E1> listModel, Transformer<E1, ? extends ListValueModel<? extends E2>> transformer) {
		super(listModel);
		this.transformer = transformer;
		this.componentLVMListener = this.buildComponentLVMListener();
		this.size = 0;
	}


	// ********** initialization **********

	protected ListChangeListener buildComponentLVMListener() {
		return new ComponentListener();
	}

	protected class ComponentListener
		extends ListChangeAdapter
	{
		@Override
		public void itemsAdded(ListAddEvent event) {
			CompositeListValueModel.this.componentItemsAdded(event);
		}		
		@Override
		public void itemsRemoved(ListRemoveEvent event) {
			CompositeListValueModel.this.componentItemsRemoved(event);
		}
		@Override
		public void itemsReplaced(ListReplaceEvent event) {
			CompositeListValueModel.this.componentItemsReplaced(event);
		}
		@Override
		public void itemsMoved(ListMoveEvent event) {
			CompositeListValueModel.this.componentItemsMoved(event);
		}
		@Override
		public void listCleared(ListClearEvent event) {
			CompositeListValueModel.this.componentListCleared(event);
		}
		@Override
		public void listChanged(ListChangeEvent event) {
			CompositeListValueModel.this.componentListChanged(event);
		}
	}


	// ********** ListValueModel implementation **********

	public E2 get(int index) {
		if ((index < 0) || (index >= this.size)) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// move backwards through the info list
		for (int i = this.infoList.size(); i-- > 0; ) {
			Info info = this.infoList.get(i);
			if (index >= info.begin) {
				return info.items.get(index - info.begin);
			}
		}
		throw new IllegalStateException();  // something is wack
	}

	public Iterator<E2> iterator() {
		return this.listIterator();
	}

	public ListIterator<E2> listIterator() {
		return this.buildListIterable().iterator();
	}

	protected ListIterable<E2> buildListIterable() {
		return new ReadOnlyCompositeListIterable<E2>(this.buildListsIterables());
	}

	protected ListIterable<ListIterable<E2>> buildListsIterables() {
		return new TransformationListIterable<Info, ListIterable<E2>>(this.infoList, new InfoTransformer());
	}

	protected class InfoTransformer
		extends TransformerAdapter<Info, ListIterable<E2>>
	{
		@Override
		public ListIterable<E2> transform(Info info) {
			return new ListListIterable<E2>(info.items);
		}
	}

	public int size() {
		return this.size;
	}

	public Object[] toArray() {
		return ArrayTools.array(this.listIterator(), this.size);
	}


	// ********** ListValueModelWrapper overrides/implementation **********

	@Override
	protected void engageModel() {
		super.engageModel();
		// sync our cache *after* we start listening to the wrapped list,
		// since its value might change when a listener is added
		this.addComponentSources(0, this.listModel, this.listModel.size(), false);  // false = do not fire event
	}

	@Override
	protected void disengageModel() {
		super.disengageModel();
		// stop listening to the component LVMs...
		for (Info info : this.infoList) {
			info.componentLVM.removeListChangeListener(LIST_VALUES, this.componentLVMListener);
		}
		// ...and clear the cache
		this.infoList.clear();
		this.size = 0;
	}

	/**
	 * Some component sources were added; update our cache.
	 */
	@Override
	protected void itemsAdded(ListAddEvent event) {
		this.addComponentSources(event.getIndex(), this.getItems(event), event.getItemsSize(), true);  // true = fire event
	}

	/**
	 * Add infos corresponding to the specified sources to our cache.
	 * Fire the appropriate event if requested.
	 */
	protected void addComponentSources(int addedSourcesIndex, Iterable<? extends E1> addedSources, int addedSourcesSize, boolean fireEvent) {
		ArrayList<Info> newInfoList = new ArrayList<Info>(addedSourcesSize);
		// the 'items' are either tacked on to the end or
		// at the 'begin' index of the first 'info' that is being pushed back
		int newItemsIndex = (addedSourcesIndex == this.infoList.size()) ? this.size : this.infoList.get(addedSourcesIndex).begin;

		int begin = newItemsIndex;
		for (E1 source : addedSources) {
			ListValueModel<? extends E2> componentLVM = this.transformer.transform(source);
			componentLVM.addListChangeListener(LIST_VALUES, this.componentLVMListener);
			ArrayList<E2> items = new ArrayList<E2>(componentLVM.size());
			CollectionTools.addAll(items, componentLVM.listIterator());
			newInfoList.add(new Info(source, componentLVM, items, begin));
			begin += items.size();
		}
		this.infoList.addAll(addedSourcesIndex, newInfoList);
		int newItemsSize = begin - newItemsIndex;
		this.size += newItemsSize;

		// bump the 'begin' index for all the infos that were pushed back by the insert
		int movedInfosIndex = addedSourcesIndex + addedSourcesSize;
		for (int i = movedInfosIndex; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin += newItemsSize;
		}

		if (fireEvent) {
			ArrayList<E2> newItems = new ArrayList<E2>(newItemsSize);
			for (int i = addedSourcesIndex; i < movedInfosIndex; i++) {
				newItems.addAll(this.infoList.get(i).items);
			}
			this.fireItemsAdded(LIST_VALUES, newItemsIndex, newItems);
		}
	}

	/**
	 * Some component sources were removed; update our cache.
	 */
	@Override
	protected void itemsRemoved(ListRemoveEvent event) {
		this.removeComponentSources(event.getIndex(), event.getItemsSize(), true);  // true = fire event
	}

	/**
	 * Remove the infos corresponding to the specified sources from our cache.
	 */
	protected void removeComponentSources(int removedSourcesIndex, int removedSourcesSize, boolean fireEvent) {
		int removedItemsIndex = this.infoList.get(removedSourcesIndex).begin;
		int movedSourcesIndex = removedSourcesIndex + removedSourcesSize;
		int movedItemsIndex = (movedSourcesIndex == this.infoList.size()) ? this.size : this.infoList.get(movedSourcesIndex).begin;
		int removedItemsSize = movedItemsIndex - removedItemsIndex;
		this.size -= removedItemsSize;

		List<Info> subList = this.infoList.subList(removedSourcesIndex, removedSourcesIndex + removedSourcesSize);
		ArrayList<Info> removedInfoList = new ArrayList<Info>(subList);  // make a copy
		subList.clear();

		// decrement the 'begin' index for all the infos that were moved forward by the deletes
		for (int i = removedSourcesIndex; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin -= removedItemsSize;
		}

		for (Info removedInfo : removedInfoList) {
			removedInfo.componentLVM.removeListChangeListener(LIST_VALUES, this.componentLVMListener);
		}

		if (fireEvent) {
			ArrayList<E2> removedItems = new ArrayList<E2>(removedItemsSize);
			for (Info removedInfo : removedInfoList) {
				removedItems.addAll(removedInfo.items);
			}
			this.fireItemsRemoved(LIST_VALUES, removedItemsIndex, removedItems);
		}
	}

	/**
	 * Some component sources were replaced; update our cache.
	 */
	@Override
	protected void itemsReplaced(ListReplaceEvent event) {
		this.replaceComponentSources(event.getIndex(), this.getNewItems(event), event.getItemsSize(), true);  // true = fire event
	}

	/**
	 * Replaced component sources will not (typically) map to a set of replaced
	 * items, so we remove and add the corresponding lists of items, resulting in
	 * two events.
	 */
	protected void replaceComponentSources(int replacedSourcesIndex, Iterable<? extends E1> newSources, int replacedSourcesSize, boolean fireEvent) {
		this.removeComponentSources(replacedSourcesIndex, replacedSourcesSize, fireEvent);
		this.addComponentSources(replacedSourcesIndex, newSources, replacedSourcesSize, fireEvent);
	}

	/**
	 * Some component sources were moved; update our cache.
	 */
	@Override
	protected void itemsMoved(ListMoveEvent event) {
		this.moveComponentSources(event.getTargetIndex(), event.getSourceIndex(), event.getLength(), true);  // true = fire event
	}

	protected void moveComponentSources(int targetSourcesIndex, int sourceSourcesIndex, int movedSourcesLength, boolean fireEvent) {
		int sourceItemsIndex = this.infoList.get(sourceSourcesIndex).begin;

		int nextSourceSourceIndex = sourceSourcesIndex + movedSourcesLength;
		int nextSourceItemIndex = (nextSourceSourceIndex == this.infoList.size()) ? this.size : this.infoList.get(nextSourceSourceIndex).begin;
		int moveItemsLength = nextSourceItemIndex - sourceItemsIndex;

		int targetItemsIndex = -1;
		if (sourceSourcesIndex > targetSourcesIndex) {
			// move from high to low index
			targetItemsIndex = this.infoList.get(targetSourcesIndex).begin;
		} else {
			// move from low to high index (higher items move down during move)
			int nextTargetSourceIndex = targetSourcesIndex + movedSourcesLength;
			targetItemsIndex = (nextTargetSourceIndex == this.infoList.size()) ? this.size : this.infoList.get(nextTargetSourceIndex).begin;
			targetItemsIndex = targetItemsIndex - moveItemsLength;
		}

		ListTools.move(this.infoList, targetSourcesIndex, sourceSourcesIndex, movedSourcesLength);

		// update the 'begin' indexes of all the affected 'infos'
		int min = Math.min(targetSourcesIndex, sourceSourcesIndex);
		int max = Math.max(targetSourcesIndex, sourceSourcesIndex) + movedSourcesLength;
		int begin = Math.min(targetItemsIndex, sourceItemsIndex);
		for (int i = min; i < max; i++) {
			Info info = this.infoList.get(i);
			info.begin = begin;
			begin += info.componentLVM.size();
		}

		if (fireEvent) {
			this.fireItemsMoved(LIST_VALUES, targetItemsIndex, sourceItemsIndex, moveItemsLength);
		}
	}

	/**
	 * The component sources were cleared; clear our cache.
	 */
	@Override
	protected void listCleared(ListClearEvent event) {
		this.clearComponentSources();
	}

	protected void clearComponentSources() {
		this.removeComponentSources(0, this.infoList.size(), false);  // false = do not fire event
		this.fireListCleared(LIST_VALUES);
	}

	/**
	 * The component sources changed; rebuild our cache.
	 */
	@Override
	protected void listChanged(ListChangeEvent event) {
		int newSize = this.listModel.size();
		if (newSize == 0) {
			this.clearComponentSources();
			return;
		}

		int oldSize = this.infoList.size();
		if (oldSize == 0) {
			this.addComponentSources(0, this.listModel, newSize, true);  // true = fire event
			return;
		}

		int min = Math.min(newSize, oldSize);
		// handle replaced sources individually so we don't fire events for unchanged sources
		for (int i = 0; i < min; i++) {
			E1 newSource = this.listModel.get(i);
			E1 oldSource = this.infoList.get(i).source;
			if (this.valuesAreDifferent(newSource, oldSource)) {
				this.replaceComponentSources(i, new SingleElementIterable<E1>(newSource), 1, true);  // true = fire event
			}
		}

		if (newSize == oldSize) {
			return;
		}

		if (newSize < oldSize) {
			this.removeComponentSources(min, oldSize - newSize, true);  // true = fire event
			return;
		}

		// newSize > oldSize
		this.addComponentSources(min, this.buildSubListHolder(min), newSize - oldSize, true);  // true = fire event
	}

	protected Iterable<? extends E1> buildSubListHolder(int fromIndex) {
		int listModelSize = this.listModel.size();
		return ListTools.list(this.listModel, listModelSize).subList(fromIndex, listModelSize);
	}

	protected Iterable<? extends E1> buildSubListHolder(int fromIndex, int toIndex) {
		int listModelSize = this.listModel.size();
		return ((fromIndex == 0) && (toIndex == listModelSize)) ?
				this.listModel :
				ListTools.list(this.listModel, listModelSize).subList(fromIndex, toIndex);
	}

	@Override
	public void toString(StringBuilder sb) {
		StringBuilderTools.append(sb, this);
	}


	// ********** internal methods **********

	/**
	 * Return the index of the specified component LVM.
	 */
	protected int indexOf(ListValueModel<E2> componentLVM) {
		for (int i = 0; i < this.infoList.size(); i++) {
			if (this.infoList.get(i).componentLVM == componentLVM) {
				return i;
			}
		}
		throw new IllegalArgumentException("invalid component LVM: " + componentLVM); //$NON-NLS-1$
	}

	/**
	 * Return the index of the specified event's component LVM.
	 */
	protected int indexFor(ListEvent event) {
		return this.indexOf(this.getComponentLVM(event));
	}

	/**
	 * Items were added to one of the component lists;
	 * synchronize our cache.
	 */
	protected void componentItemsAdded(ListAddEvent event) {
		int componentLVMIndex = this.indexFor(event);
		this.addComponentItems(componentLVMIndex, this.infoList.get(componentLVMIndex), event.getIndex(), this.getComponentItems(event), event.getItemsSize());
	}

	protected void addComponentItems(int componentLVMIndex, Info info, int addedItemsIndex, Iterable<? extends E2> addedItems, int addedItemsSize) {
		// update the affected 'begin' indices
		for (int i = componentLVMIndex + 1; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin += addedItemsSize;
		}
		this.size += addedItemsSize;

		// synchronize the cached list
		ListTools.addAll(info.items, addedItemsIndex, addedItems, addedItemsSize);

		// translate the event
		this.fireItemsAdded(LIST_VALUES, info.begin + addedItemsIndex, info.items.subList(addedItemsIndex, addedItemsIndex + addedItemsSize));
	}

	/**
	 * Items were removed from one of the component lists;
	 * synchronize our cache.
	 */
	protected void componentItemsRemoved(ListRemoveEvent event) {
		// update the affected 'begin' indices
		int componentLVMIndex = this.indexFor(event);
		int removedItemsSize = event.getItemsSize();
		for (int i = componentLVMIndex + 1; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin -= removedItemsSize;
		}
		this.size -= removedItemsSize;

		// synchronize the cached list
		Info info = this.infoList.get(componentLVMIndex);
		int itemIndex = event.getIndex();
		info.items.subList(itemIndex, itemIndex + event.getItemsSize()).clear();

		// translate the event
		this.fireItemsRemoved(event.clone(this, LIST_VALUES, info.begin));
	}

	/**
	 * Items were replaced in one of the component lists;
	 * synchronize our cache.
	 */
	protected void componentItemsReplaced(ListReplaceEvent event) {
		// no changes to the 'begin' indices or size

		// synchronize the cached list
		int componentLVMIndex = this.indexFor(event);
		Info info = this.infoList.get(componentLVMIndex);
		int i = event.getIndex();
		for (E2 item : this.getComponentItems(event)) {
			info.items.set(i++, item);
		}

		// translate the event
		this.fireItemsReplaced(event.clone(this, LIST_VALUES, info.begin));
	}

	/**
	 * Items were moved in one of the component lists;
	 * synchronize our cache.
	 */
	protected void componentItemsMoved(ListMoveEvent event) {
		// no changes to the 'begin' indices or size

		// synchronize the cached list
		int componentLVMIndex = this.indexFor(event);
		Info info = this.infoList.get(componentLVMIndex);
		ListTools.move(info.items, event.getTargetIndex(), event.getSourceIndex(), event.getLength());

		// translate the event
		this.fireItemsMoved(event.clone(this, LIST_VALUES, info.begin));
	}

	/**
	 * One of the component lists was cleared;
	 * synchronize our cache.
	 */
	protected void componentListCleared(ListClearEvent event) {
		int componentLVMIndex = this.indexFor(event);
		this.clearComponentList(componentLVMIndex, this.infoList.get(componentLVMIndex));
	}

	protected void clearComponentList(int componentLVMIndex, Info info) {
		// update the affected 'begin' indices
		int removedItemsSize = info.items.size();
		if (removedItemsSize == 0) {
			return;
		}

		for (int i = componentLVMIndex + 1; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin -= removedItemsSize;
		}
		this.size -= removedItemsSize;

		// synchronize the cached list
		ArrayList<E2> items = new ArrayList<E2>(info.items);  // make a copy
		info.items.clear();

		// translate the event
		this.fireItemsRemoved(LIST_VALUES, info.begin, items);
	}

	/**
	 * One of the component lists changed;
	 * synchronize our cache by synchronizing the appropriate
	 * list and firing the appropriate events.
	 */
	protected void componentListChanged(ListChangeEvent event) {
		int componentLVMIndex = this.indexFor(event);
		Info info = this.infoList.get(componentLVMIndex);

		int newItemsSize = info.componentLVM.size();
		if (newItemsSize == 0) {
			this.clearComponentList(componentLVMIndex, info);
			return;
		}

		int oldItemsSize = info.items.size();
		if (oldItemsSize == 0) {
			this.addComponentItems(componentLVMIndex, info, 0, info.componentLVM, newItemsSize);
			return;
		}

		int min = Math.min(newItemsSize, oldItemsSize);
		// handle replaced items individually so we don't fire events for unchanged items
		for (int i = 0; i < min; i++) {
			E2 newItem = info.componentLVM.get(i);
			E2 oldItem = info.items.set(i, newItem);
			this.fireItemReplaced(LIST_VALUES, info.begin + i, newItem, oldItem);
		}

		int delta = newItemsSize - oldItemsSize;
		if (delta == 0) {  // newItemsSize == oldItemsSize
			return;
		}

		for (int i = componentLVMIndex + 1; i < this.infoList.size(); i++) {
			this.infoList.get(i).begin += delta;
		}
		this.size += delta;

		if (delta < 0) {  // newItemsSize < oldItemsSize
			List<E2> subList = info.items.subList(newItemsSize, oldItemsSize);
			ArrayList<E2> removedItems = new ArrayList<E2>(subList);  // make a copy
			subList.clear();
			this.fireItemsRemoved(LIST_VALUES, info.begin + newItemsSize, removedItems);
			return;
		}

		// newItemsSize > oldItemsSize
		ArrayList<E2> addedItems = new ArrayList<E2>(delta);
		for (int i = oldItemsSize; i < newItemsSize; i++) {
			addedItems.add(info.componentLVM.get(i));
		}
		info.items.addAll(addedItems);
		this.fireItemsAdded(LIST_VALUES, info.begin + oldItemsSize, addedItems);
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E2> getComponentItems(ListAddEvent event) {
		return (Iterable<E2>) event.getItems();
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected Iterable<E2> getComponentItems(ListReplaceEvent event) {
		return (Iterable<E2>) event.getNewItems();
	}

	// minimize scope of suppressed warnings
	@SuppressWarnings("unchecked")
	protected ListValueModel<E2> getComponentLVM(ListEvent event) {
		return (ListValueModel<E2>) event.getSource();
	}
}
