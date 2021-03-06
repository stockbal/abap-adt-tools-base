package com.devepos.adt.base.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Result part of a dialog which consists of a Structured Viewer that holds the
 * result items and an optional content viewer which displays the details like
 * the current selection.
 * 
 * @author stockbal
 */
public abstract class DialogResultPart {

	/**
	 * Property of the selected elements
	 */
	public static final String SELECTED_ELEMENTS_PROPERTY = "selected_elements_prop";
	protected List<?> initialSelections;
	private IBaseLabelProvider detailsLabelProvider;
	private final List<IPropertyChangeListener> propertyChangeListeners = new ArrayList<>();
	private IBaseLabelProvider resultLabelProvider;

	/**
	 * Adds property change listener to this view part
	 * 
	 * @param l the property change listener to be added
	 */
	public void addPropertyChangeListener(final IPropertyChangeListener l) {
		this.propertyChangeListeners.add(l);
	}

	/**
	 * Creates the dialog part
	 * 
	 * @param parent the parent composite
	 */
	public final void createDialogPart(final Composite parent) {
		createContent(parent);
		final StructuredViewer resultViewer = getResultViewer();
		if (resultViewer != null) {
			resultViewer.setLabelProvider(getResultLabelProvider());
		}
		final ContentViewer detailsViewer = getDetailViewer();
		if (detailsViewer != null) {
			detailsViewer.setLabelProvider(getDetailsLabelProvider());
		}
	}

	/**
	 * Returns the Detail viewer of the part.<br>
	 * The default implementation returns null. Subclasses may override
	 * <p>
	 * <strong>Note:</strong><br>
	 * The viewer is first bound after the call of
	 * {@link #createUI(org.eclipse.swt.widgets.Composite)}
	 * </p>
	 * 
	 * @return the Detail viewer of the part
	 */
	public ContentViewer getDetailViewer() {
		return null;
	}

	/**
	 * Returns the result viewer of the part.
	 * <p>
	 * <strong>Note:</strong><br>
	 * The viewer is first bound after the call of
	 * {@link #createUI(org.eclipse.swt.widgets.Composite)}
	 * </p>
	 * 
	 * @return the results viewer of the dialog
	 */
	public abstract StructuredViewer getResultViewer();

	/**
	 * Removes property change listener from this view part
	 * 
	 * @param l the property change listener to be removed
	 */
	public void removePropertyChangeListener(final IPropertyChangeListener l) {
		this.propertyChangeListeners.remove(l);
	}

	/**
	 * Sets the label provider for the detail viewer
	 * 
	 * @param labelProvider the label provider for the detail viewer
	 */
	public void setDetailsLabelProvider(final IBaseLabelProvider labelProvider) {
		this.detailsLabelProvider = labelProvider;
	}

	/**
	 * Sets initial selections for the result or detail depending on the
	 * implementation
	 * 
	 * @param initialSelection list of initial selections
	 */
	public final void setInitialSelections(final List<?> initialSelection) {
		this.initialSelections = initialSelection;
	}

	/**
	 * Sets the label provider for the result viewer
	 * 
	 * @param labelProvider the label provider for the result viewer
	 */
	public void setResultLabelProvider(final IBaseLabelProvider labelProvider) {
		this.resultLabelProvider = labelProvider;
	}

	/**
	 * Updates the results of this view part
	 * 
	 * @param newResults list of new results
	 */
	public void updateResults(final List<Object> newResults) {
		final StructuredViewer resultViewer = getResultViewer();
		if (resultViewer.getControl() == null || resultViewer.getControl().isDisposed()) {
			return;
		}
		resultViewer.setInput(newResults);
		if (newResults != null && !newResults.isEmpty()) {
			resultViewer.setSelection(new StructuredSelection(newResults.get(0)));
		} else {
			resultViewer.setSelection(null);
		}
	}

	/**
	 * Creates the content of the result part
	 * 
	 * @param parent the parent composite
	 */
	protected abstract void createContent(Composite parent);

	/**
	 * Fires new property change event for the {@link #SELECTED_ELEMENTS_PROPERTY}
	 * property
	 */
	protected void fireSelectedElementsChanged() {

		final PropertyChangeEvent event = new PropertyChangeEvent(this, SELECTED_ELEMENTS_PROPERTY, null,
			getSelectedElementCount());
		for (final IPropertyChangeListener l : this.propertyChangeListeners) {
			l.propertyChange(event);
		}
	}

	/**
	 * Computes the current count of selected elements
	 * 
	 * @return the current count of selected elements
	 */
	protected abstract int getSelectedElementCount();

	private IBaseLabelProvider getDetailsLabelProvider() {
		if (this.detailsLabelProvider == null) {
			this.detailsLabelProvider = new LabelProvider();
		}
		return this.detailsLabelProvider;
	}

	private IBaseLabelProvider getResultLabelProvider() {
		if (this.resultLabelProvider == null) {
			this.resultLabelProvider = new LabelProvider();
		}
		return this.resultLabelProvider;
	}
}
