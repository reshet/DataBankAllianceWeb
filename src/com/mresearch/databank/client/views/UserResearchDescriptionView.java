package com.mresearch.databank.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;
import com.mresearch.databank.client.views.DBfillers.MultiValuedField;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SearchTaskResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;

public class UserResearchDescriptionView extends Composite {

	private static UserResearchDetailedViewUiBinder uiBinder = GWT
			.create(UserResearchDetailedViewUiBinder.class);

	interface UserResearchDetailedViewUiBinder extends
			UiBinder<Widget, UserResearchDescriptionView> {
	}
	private static UserAccountServiceAsync srv = GWT.create(UserAccountService.class);
	public UserResearchDescriptionView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	@UiField HTML html;
	@UiField Button addToSelectedBtn;
	private SocioResearchDTO dto;
	public UserResearchDescriptionView(SocioResearchDTO dto)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		html.setHTML(dto.getDesctiption()==null?"":dto.getDesctiption());
		if(DatabankApp.get().getCurrentUser().getId()==0)addToSelectedBtn.setVisible(false);
			else
			{
				addToSelectedBtn.setVisible(false);
				new RPCCall<List<SocioResearchDTO_Light>>() {

					@Override
					public void onFailure(Throwable arg0) {
						addToSelectedBtn.setVisible(true);
					}

					@Override
					public void onSuccess(List<SocioResearchDTO_Light> lst) {
						addToSelectedBtn.setVisible(true);
						for(SocioResearchDTO_Light dt:lst)
						{
							if(dt.getId() == UserResearchDescriptionView.this.dto.getId()){
								addToSelectedBtn.setVisible(false);
								break;				
							}
						}
					}

					@Override
					protected void callService(
							AsyncCallback<List<SocioResearchDTO_Light>> cb) {
						srv.getMyResearchesList(cb);
					}
				}.retry(2);
			}
		
	}
	
	@UiHandler(value="addToSelectedBtn")
	void onAdd(ClickEvent ev)
	{
		new RPCCall<Void>()
		{
			@Override
			public void onFailure(Throwable arg0) {
			}
			@Override
			public void onSuccess(Void arg0) {
				addToSelectedBtn.setVisible(false);
				PopupPanel b = DialogBoxFactory.createDialogBox("Избранные исследования",new Label("Исследование успешно добавлено!"),null,"ОК");
				b.show();
			}
			@Override
			protected void callService(AsyncCallback<Void> cb) {
				srv.addToSelectedResearches(dto, cb);
			}
		}.retry(2);
	}
	
}