package com.devepos.adt.base.ui.action;

import org.eclipse.swt.SWT;

public enum ViewLayoutOrientation {
	VERTICAL(SWT.VERTICAL),
	HORIZONTAL(SWT.HORIZONTAL),
	AUTOMATIC(-1);

	private int swtOrientation;

	private ViewLayoutOrientation(final int swtOrientation) {
		this.swtOrientation = swtOrientation;
	}

	public int getSwtOrientation() {
		return this.swtOrientation;
	}
}
