package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WhiteFooter extends Composite {

	private static WhiteFooterUiBinder uiBinder = GWT
			.create(WhiteFooterUiBinder.class);

	interface WhiteFooterUiBinder extends UiBinder<Widget, WhiteFooter> {
	}

	public WhiteFooter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
