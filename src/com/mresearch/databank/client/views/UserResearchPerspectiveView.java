package com.mresearch.databank.client.views;

import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.plaf.basic.BasicTreeUI.TreeSelectionHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.Place;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.ResearchBundleDTO;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Light;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserResearchPerspectiveView extends Composite implements UserResearchPerspectivePresenter.Display, Place{

	private static UserResearchPerspectiveViewUiBinder uiBinder = GWT
			.create(UserResearchPerspectiveViewUiBinder.class);

	interface UserResearchPerspectiveViewUiBinder extends
			UiBinder<Widget, UserResearchPerspectiveView> {
	}
	
	@Override
	public String getPlaceName() {
		return "user-research";
	}
	
	@UiField VerticalPanel centerPanel;
//	VerticalPanel centralPanel;
	@UiField Tree tree,F_S_tree,F_V_tree,My_researches_tree;
	@UiField ScrollPanel centerChild;
	@UiField SplitLayoutPanel split_panel;
	//@UiField DecoratedTabPanel decortab;
	@UiField TabLayoutPanel tabLayout;
	//HLayout panel;
//	@UiField CheckBox weights_use,filters_use;
//	@UiField Button filters_details_btn,filters_add_btn,filters_delete_btn;
	SimpleResearchList simpleResearchListItem;
	UserOwnResearchList user_own_ResearchListItem;
	UserOwnAnalisysList analisys_list;
	RootConceptsList rootResearchConcepts, rootVarConcepts;
	private Long research_to_find =null;
	private List<SocioResearchDTO_Light> researchList;
	public UserResearchPerspectiveView(SimpleEventBus bus) {
		initWidget(uiBinder.createAndBindUi(this));
		if(DatabankApp.get().getCurrentUser().getId()!=0)tabLayout.getTabWidget(2).getParent().setVisible(true); 
		else tabLayout.getTabWidget(2).getParent().setVisible(false);
		
	
		
		
//		panel = new HLayout();
//		//panel.setWidth100();
//		//panel.setHeight100();
//		VLayout cPanel = new VLayout();
//		cPanel.setWidth("70%");
//		cPanel.setAlign(Alignment.LEFT);  
//		cPanel.setOverflow(Overflow.VISIBLE);
//		
//		
//		//cPanel.setShowResizeBar(true);  
//		centralPanel = new VerticalPanel();
//		cPanel.addMember(centralPanel);
//
//
//
//
//
//		tree = new Tree();
		//tree.setStyleName("research-catalog");
		//TreeItem db = new TreeItem("_Банк данных_");
		
		
		
		
		//split_panel.get
		tree.setStyleName("research_section");
		split_panel.setWidgetMinSize(centerChild, 720);
		//split_panel.get
		simpleResearchListItem = new SimpleResearchList();
		user_own_ResearchListItem = new UserOwnResearchList();
		analisys_list = new UserOwnAnalisysList();
		tree.addItem(simpleResearchListItem);
		My_researches_tree.setStyleName("research_section");
		//My_analisys_tree.setStyleName("research_section");
		My_researches_tree.addItem(user_own_ResearchListItem);
		//My_analisys_tree.addItem(analisys_list);
		My_researches_tree.addItem(analisys_list); 
		
		rootResearchConcepts = new RootConceptsList("socioresearch","Концепты каталогизации исследований");
		//rootResearchConcepts.setState(true);
		tree.addItem(rootResearchConcepts);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		rootVarConcepts = new RootConceptsList("sociovar","Концепты каталогизации переменных");
		//rootVarConcepts.setState(true);
		tree.addItem(rootVarConcepts);
		
		RootFilterItemAdvanced f1 = new RootFilterItemAdvanced(this,bus,"socioresearch","Фильтровать исследования");
		F_S_tree.addItem(f1);
		//f1.setState(true);
		RootFilterItemAdvanced f2 = new RootFilterItemAdvanced(this,bus,"sociovar","Фильтровать переменные");
		F_V_tree.addItem(f2);
		
		
		//decortab.selectTab(0);
		//f2.setState(true);


		
		
		
		//tree.addItem(db);
//		VLayout vLayout = new VLayout();
//		vLayout.setAlign(Alignment.LEFT);  
//	    vLayout.setOverflow(Overflow.SCROLL);  
//	    vLayout.setWidth("30%");  
//	   // vLayout.setHeight100();
//	    vLayout.setShowResizeBar(true);  
//	    vLayout.setResizeBarSize(9);
//	    //vLayout.r
//	    //ScrollPanel scr = new ScrollPanel(tree);
//	    
//	    
//	    
//	    
//	    //scr.setHeight("100%");
//	    //scr.setWidth("100%");
//	    vLayout.addMember(tree);
//		//vLayout.getS
//		//vLayout.sendToBack();
//
//		panel.setWidth("100%");
//		panel.setHeight("100%");
//		panel.addMember(vLayout);
//		panel.addMember(cPanel);
//		panel.sendToBack();
//
//		
//		centerPanel.add(panel);
		
		
		
		
		
		
		
//		
//		 for (int i = 0; i < tabLayout.getWidgetCount(); i++) {
//		        final Widget widget = tabLayout.getWidget(i);
//		        DOM.setStyleAttribute(widget.getElement(), "position", "relative");
//
//		        final Element parent = DOM.getParent(widget.getElement());
//		        DOM.setStyleAttribute(parent, "overflowX", "visible");
//		        DOM.setStyleAttribute(parent, "overflowY", "visible");
//		    }
//		
		
	}
	private void displayResearchList()
	{
		simpleResearchListItem.removeItems();
		for(SocioResearchDTO_Light dto:researchList)
		{
			ResearchDescItem research_node = new ResearchDescItem(dto);
			//research_node.addItem(new ResearchMetadataItem(dto));
			//research_node.addItem(new ResearchVarList(dto));
			//for(String )
			simpleResearchListItem.addItem(research_node);
			if (research_to_find!= null && research_node.getContents_id() == research_to_find)
			{
				research_node.setSelected(true);
				tree.setSelectedItem(research_node);
				//tree.fireEvent();
				research_to_find = null;
			}
		}
		int b = 2;
	}
	
	@Override
	public HasMouseDownHandlers getTree() {
		return tree;
	}
	@Override
	public HasClickHandlers getResearchItem(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HasClickHandlers getVarItem(int index)  {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	@Override
	public void setResearchListData(List<SocioResearchDTO_Light> data) {
		researchList = data;
		displayResearchList();
	}
	@Override
	public void setVarListData(TreeItem item, ArrayList<VarDTO_Light> data) {
		item.removeItems();
		for(VarDTO_Light dto:data)
		{
			
			VarDescItem var_node = new VarDescItem(dto,VarDescItem.LABEL_SHORTAGE_NUMBER);
			//var_node.addItem(dto.getLabel());
			item.addItem(var_node);
		}
	}
	@Override
	public void showLoadingLabel() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public TreeItem getSelectedItem() {
		// TODO Auto-generated method stub
		return tree.getSelectedItem();
	}
	@Override
	public void showResearchDetailes(final ResearchBundleDTO dto) {
		
		centerPanel.clear();
		UserResearchDescriptionView desc = new UserResearchDescriptionView(dto.getDto());
		UserResearchDetailedView view = new UserResearchDetailedView(dto.getDto(),dto.getMeta());
		UserResearchAdvancedFilesView files = new UserResearchAdvancedFilesView(dto.getDto().getID(),dto.getFiles_dto());
		UserResearchVarsView vars_v = new UserResearchVarsView(dto.getDto());
		UserResearchDetailedFrameView frame = new UserResearchDetailedFrameView(desc,view, files,vars_v);
	
		centerPanel.add(frame);

//		new RPCCall<MetaUnitMultivaluedEntityDTO>() {
//
//			@Override
//			public void onFailure(Throwable arg0) {
//			}
//
//			@Override
//			public void onSuccess(MetaUnitMultivaluedEntityDTO res) {
//			}
//
//			@Override
//			protected void callService(
//					AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
//				UserSocioResearchService.Util.getInstance().getDatabankStructure("socioresearch", cb);
//			}
//		}.retry(2);
		}
	
	
	@Override
	public HasOpenHandlers<TreeItem> getTreeForOpen() {
		return tree;
	}
	@Override
	public VerticalPanel getCenterPanel() {
		// TODO Auto-generated method stub
		return centerPanel;
	}
	@Override
	public void findInResearchList(Long id) {
		simpleResearchListItem.setState(true);
		research_to_find = id;
//		displayResearchList();
//		for(int i = 0; i < simpleResearchListItem.getChildCount();i++)
//		{
//			TreeItem it = simpleResearchListItem.getChild(i);
//			if (it instanceof ResearchDescItem)
//			{
//				ConceptContentsItem item = (ConceptContentsItem)simpleResearchListItem.getChild(i);
//				if (item.getContents_id().equals(id))
//				{
//					item.setSelected(true);
//					break;
//				}
//			}
//		}
	}
	@Override
	public HasSelectionHandlers<TreeItem> getTreeForSelection() {
		return tree;
	}
	@Override
	public Tree getTreeWhole() {
		// TODO Auto-generated method stub
		return tree;
	}
	@Override
	public HasOpenHandlers<TreeItem> getUserResearchTreeForOpen() {
		return My_researches_tree;
	}
	@Override
	public HasSelectionHandlers<TreeItem> getUserResearchTreeForSelection() {
		return My_researches_tree;
	}
//	@Override
//	public HasOpenHandlers<TreeItem> getUserResearchAnalTreeForOpen() {
//		return My_analisys_tree;
//	}
//	@Override
//	public HasSelectionHandlers<TreeItem> getUserResearchAnalTreeForSelection() {
//		return My_analisys_tree;
//	}
	@Override
	public void setUserOwnResearchListData(List<SocioResearchDTO_Light> data) {
		
			user_own_ResearchListItem.removeItems();
			for(SocioResearchDTO_Light dto:data)
			{
				ResearchDescItem research_node = new ResearchDescItem(dto);
				//research_node.addItem(new ResearchMetadataItem(dto));
				//research_node.addItem(new ResearchVarList(dto));
				//for(String )
				user_own_ResearchListItem.addItem(research_node);
				if (research_to_find!= null && research_node.getContents_id() == research_to_find)
				{
					research_node.setSelected(true);
					My_researches_tree.setSelectedItem(research_node);
					//tree.fireEvent();
					research_to_find = null;
				}
			}
	}

	@Override
	public void setAllAnalysisListData(List<UserAnalysisSaveDTO> data) {
		analisys_list.removeItems();
		for(UserAnalysisSaveDTO dto:data)
		{
			AnalysisDescItem research_node = new AnalysisDescItem(dto);
			analisys_list.addItem(research_node);
			
		}
	}
	@Override
	public TreeItem getUserOwnSelectedItem() {
		return My_researches_tree.getSelectedItem();
	}
	
}
