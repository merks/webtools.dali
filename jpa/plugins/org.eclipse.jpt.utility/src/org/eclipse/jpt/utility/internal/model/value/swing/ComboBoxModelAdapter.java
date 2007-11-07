/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value.swing;

import javax.swing.ComboBoxModel;

import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.PropertyChangeListener;
import org.eclipse.jpt.utility.internal.model.value.CollectionValueModel;
import org.eclipse.jpt.utility.internal.model.value.ListValueModel;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.internal.model.value.ValueModel;

/**
 * This javax.swing.ComboBoxModel can be used to keep a ListDataListener
 * (e.g. a JComboBox) in synch with a ListValueModel (or a CollectionValueModel).
 * For combo boxes, the model object that holds the current selection is
 * typically a different model object than the one that holds the collection
 * of choices.
 * 
 * For example, a MWReference (the selectionOwner) has an attribute
 * "sourceTable" (the collectionOwner)
 * which holds on to a collection of MWDatabaseFields. When the selection
 * is changed this model will keep the listeners aware of the changes.
 * The inherited list model will keep its listeners aware of changes to the
 * collection model
 * 
 * In addition to the collection holder required by the superclass,
 * an instance of this ComboBoxModel must be supplied with a
 * selection holder, which is a PropertyValueModel that provides access
 * to the selection (typically a PropertyAspectAdapter).
 */
public class ComboBoxModelAdapter
	extends ListModelAdapter
	implements ComboBoxModel
{
	protected final PropertyValueModel selectionHolder;
	protected final PropertyChangeListener selectionListener;


	// ********** constructors **********

	/**
	 * Constructor - the list holder and selection holder are required;
	 */
	public ComboBoxModelAdapter(ListValueModel listHolder, PropertyValueModel selectionHolder) {
		super(listHolder);
		if (selectionHolder == null) {
			throw new NullPointerException();
		}
		this.selectionHolder = selectionHolder;
		this.selectionListener = this.buildSelectionListener();
	}

	/**
	 * Constructor - the collection holder and selection holder are required;
	 */
	public ComboBoxModelAdapter(CollectionValueModel collectionHolder, PropertyValueModel selectionHolder) {
		super(collectionHolder);
		if (selectionHolder == null) {
			throw new NullPointerException();
		}
		this.selectionHolder = selectionHolder;
		this.selectionListener = this.buildSelectionListener();
	}


	// ********** initialization **********

	protected PropertyChangeListener buildSelectionListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				// notify listeners that the selection has changed
				ComboBoxModelAdapter.this.fireSelectionChanged();
			}
			@Override
			public String toString() {
				return "selection listener";
			}
		};
	}


	// ********** ComboBoxModel implementation **********

	public Object getSelectedItem() {
		return this.selectionHolder.value();
	}

	public void setSelectedItem(Object selectedItem) {
		this.selectionHolder.setValue(selectedItem);
	}


	// ********** behavior **********

	/**
	 * Extend to engage the selection holder.
	 */
	@Override
	protected void engageModel() {
		super.engageModel();
		this.selectionHolder.addPropertyChangeListener(ValueModel.VALUE, this.selectionListener);
	}

	/**
	 * Extend to disengage the selection holder.
	 */
	@Override
	protected void disengageModel() {
		this.selectionHolder.removePropertyChangeListener(ValueModel.VALUE, this.selectionListener);
		super.disengageModel();
	}

	/**
	 * Notify the listeners that the selection has changed.
	 */
	protected void fireSelectionChanged() {
		// I guess this will work...
		this.fireContentsChanged(this, -1, -1);
	}

	@Override
	public String toString() {
		return StringTools.buildToStringFor(this, this.selectionHolder + ":" + this.listHolder);
	}

}
