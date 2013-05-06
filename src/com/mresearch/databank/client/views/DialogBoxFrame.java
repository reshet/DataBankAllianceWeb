
package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.views.DialogBoxFactory.closeAction;

public class DialogBoxFrame extends Composite {

	private static DialogBoxFrameUiBinder uiBinder = GWT
			.create(DialogBoxFrameUiBinder.class);

	interface DialogBoxFrameUiBinder extends UiBinder<Widget, DialogBoxFrame> {
	}

	public DialogBoxFrame() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	

	@UiField
	Button button;
	@UiField Label header;
	@UiField VerticalPanel contents;
	PopupPanel parentPopup;
	private closeAction action;
	public DialogBoxFrame(String header,PopupPanel popup,Widget w,closeAction action,String close_btn_text) {
		initWidget(uiBinder.createAndBindUi(this));
		parentPopup = popup;
		contents.add(w);
		this.action = action;
		this.header.setText(header);
		button.setText(close_btn_text);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		parentPopup.hide();
		if(action!= null)action.doAction();
	}
}
