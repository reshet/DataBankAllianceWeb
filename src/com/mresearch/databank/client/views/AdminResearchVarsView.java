package com.mresearch.databank.client.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.client.views.DBfillers.MultiValuedField;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.shared.MetaUnitEntityItemDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SearchTaskResearchDTO;
import com.mresearch.databank.shared.ShowCatalogParameters;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.VarDTO_Light;

public class AdminResearchVarsView extends Composite {

	private static UserResearchDetailedViewUiBinder uiBinder = GWT
			.create(UserResearchDetailedViewUiBinder.class);

	interface UserResearchDetailedViewUiBinder extends
			UiBinder<Widget, AdminResearchVarsView> {
	}

	public AdminResearchVarsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	//@UiField HTML htm;
	@UiField Tree tree;
	private ResearchVarList item;
	private final UserSocioResearchServiceAsync rpcService = UserSocioResearchService.Util.getInstance();
	public AdminResearchVarsView(SocioResearchDTO dto)
	{
		initWidget(uiBinder.createAndBindUi(this));

		
		//html.setHTML(dto.getDesctiption()==null?"":dto.getDesctiption());
		item = new ResearchVarList(new SocioResearchDTO_Light(dto.getId(),dto.getName()));
		item.setState(true);
		tree.setStyleName("law_section");
		tree.addItem(item);
		fetchResearchVarData(item, item.getResearch_id());
//		tree.addOpenHandler(new OpenHandler<TreeItem>() {
//			@Override
//			public void onOpen(OpenEvent<TreeItem> event) {
//				TreeItem it = event.getTarget();
//				if (it instanceof ResearchVarList)
//				{
//					ResearchVarList rv = (ResearchVarList)it;
//					fetchResearchVarData(it, rv.getResearch_id());
//					//current_research_id = rv.getResearch_id();
//				}
//			}
//		});
//		
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			//private VarDescItem prevVar;
			private WrappedCustomLabel prevRes;
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem it = tree.getSelectedItem();
				if(prevRes!=null)prevRes.getLabel().removeStyleDependentName("selected");
				//if(prevVar!=null)prevVar.getLabel().removeStyleDependentName("selected");
				
				if (it instanceof VarDescItem)
				{
					VarDescItem rv = (VarDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					//fetchResearchVarData(it, rv.getResearch_id());
					DatabankApp.get().getEventBus().fireEvent(new ShowVarDetailsEvent(rv.getVar_id()));
				//	eventBus.fireEvent(new AddResearchDisabledEvent());
				}
			}
		});
	}
	private void fetchResearchVarData(final TreeItem item,final Long id_research)
	{
		new RPCCall<ArrayList<VarDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" news: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<VarDTO_Light> result) {
				item.removeItems();
				for(VarDTO_Light dto:result)
				{
					
					VarDescItem var_node = new VarDescItem(dto,200);
					var_node.getLabel().setWidth("580px");
					//var_node.addItem(dto.getLabel());
					item.addItem(var_node);
				}
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<VarDTO_Light>> cb) {
				rpcService.getResearchVarsSummaries(id_research, cb);
			}
		}.retry(3);
	}
}



