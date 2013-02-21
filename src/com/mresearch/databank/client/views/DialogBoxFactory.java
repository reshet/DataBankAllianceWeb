package com.mresearch.databank.client.views;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DialogBoxFactory {
	public interface closeAction
	{
		void doAction();
	}
	public static PopupPanel createDialogBox(String header,Widget w,closeAction action) {
	    final PopupPanel dialogBox = new PopupPanel();
	    DialogBoxFrame fr = new DialogBoxFrame(header, dialogBox, w,action);
	  	dialogBox.setWidget(fr);
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);
		dialogBox.center();
	    return dialogBox;
	  }	
}
