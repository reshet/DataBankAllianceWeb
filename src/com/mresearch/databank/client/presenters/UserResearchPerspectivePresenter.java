package com.mresearch.databank.client.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.xml.crypto.Data;

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
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.UserAppController;
import com.mresearch.databank.client.event.RecalculateDistributionsEvent;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEventHandler;
import com.mresearch.databank.client.event.ShowStartPageMainEvent;
import com.mresearch.databank.client.event.ShowStartPageMainEventHandler;
import com.mresearch.databank.client.event.ShowVar2DDEvent;
import com.mresearch.databank.client.event.ShowVar2DDEventHandler;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.StartPageServiceAsync;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.client.views.AnalysisDescItem;
import com.mresearch.databank.client.views.ConceptItemItem;
import com.mresearch.databank.client.views.ConceptItemEntity;
import com.mresearch.databank.client.views.IPickBinder;
import com.mresearch.databank.client.views.PickElementsTableView;
import com.mresearch.databank.client.views.RealVariableDetailedView;
import com.mresearch.databank.client.views.ResearchDescItem;
import com.mresearch.databank.client.views.ResearchMetadataItem;
import com.mresearch.databank.client.views.ResearchVarList;
import com.mresearch.databank.client.views.RootFilterItemAdvanced;
import com.mresearch.databank.client.views.SearchResultsGenericGrid;
import com.mresearch.databank.client.views.SearchResultsGrid;
import com.mresearch.databank.client.views.SimpleResearchList;
import com.mresearch.databank.client.views.TextVariableDetailedView;
import com.mresearch.databank.client.views.UserOwnAnalisysList;
import com.mresearch.databank.client.views.UserOwnResearchList;
import com.mresearch.databank.client.views.UserResearchAdvancedFilesView;
import com.mresearch.databank.client.views.UserResearchDetailedFrameView;
import com.mresearch.databank.client.views.UserResearchDetailedView;
import com.mresearch.databank.client.views.VarDescItem;
import com.mresearch.databank.client.views.VariableDetailedView;
import com.mresearch.databank.client.views.UserResearchVar2DDView;
import com.mresearch.databank.client.views.WrappedCustomLabel;
import com.mresearch.databank.shared.ArticleDTO;
import com.mresearch.databank.shared.FilterBaseDTO;
import com.mresearch.databank.shared.IShowPlaceParameters;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.NewsDTO;
import com.mresearch.databank.shared.NewsSummaryDTO;
import com.mresearch.databank.shared.RealVarDTO_Detailed;
import com.mresearch.databank.shared.ResearchBundleDTO;
import com.mresearch.databank.shared.ShowAnalysisSavedParameters;
import com.mresearch.databank.shared.ShowCatalogParameters;
import com.mresearch.databank.shared.ShowFilterParameters;
import com.mresearch.databank.shared.ShowResearchSavedParameters;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.TextVarDTO_Detailed;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO_Light;
import com.mresearch.databank.shared.IPickableElement;


public class UserResearchPerspectivePresenter implements Presenter,Action
{
	
	 public interface AnalisysDisplay
	 {
		 HasClickHandlers getWeightsUse();
		 Integer getWeightsUseState();
		 HasClickHandlers getFiltersUse();
		 Integer getFiltersUseState();
		 HasClickHandlers getFiltersDetailesBtn();
		 HasClickHandlers getFiltersAddBtn();
		 HasClickHandlers getFiltersDeleteBtn();
	 }
	 public interface Display {
		 HasMouseDownHandlers getTree();
		 Tree getTreeWhole();
		 HasOpenHandlers<TreeItem> getTreeForOpen();
		 HasSelectionHandlers<TreeItem> getTreeForSelection();
		 HasOpenHandlers<TreeItem> getUserResearchTreeForOpen();
		 HasSelectionHandlers<TreeItem> getUserResearchTreeForSelection();
//		 HasOpenHandlers<TreeItem> getUserResearchAnalTreeForOpen();
//		 HasSelectionHandlers<TreeItem> getUserResearchAnalTreeForSelection();
//		 
		 HasClickHandlers getResearchItem(int index);
		 HasClickHandlers getVarItem(int index);
		 void setResearchListData(List<SocioResearchDTO_Light> data);
		 void setUserOwnResearchListData(List<SocioResearchDTO_Light> data);
		 void setAllAnalysisListData(List<UserAnalysisSaveDTO> data);
		 void setVarListData(TreeItem item, ArrayList<VarDTO_Light> data);
		 Widget asWidget();
		 void showLoadingLabel();
		 TreeItem getSelectedItem();
		 TreeItem getUserOwnSelectedItem();
		 void showResearchDetailes(ResearchBundleDTO dto);
		 VerticalPanel getCenterPanel();
		 void findInResearchList(Long id);
		 
	 }
	 
	 private final UserSocioResearchServiceAsync rpcService;
	 private final UserAccountServiceAsync rpc_acc_service = GWT.create(UserAccountService.class);
	// private final UserSocioResearchServiceAsync rpcService;
	 private final SimpleEventBus eventBus;
	 private final Display display;
	 private Widget previous_centerpanel_state = null;
	 private Long current_research_id = null, prev_res_id = null;
	 private ResearchBundleDTO current_research = null;
	 public UserResearchPerspectivePresenter(UserSocioResearchServiceAsync rpcService,SimpleEventBus eventBus,
		      Display view) {
		    this.rpcService = rpcService;
		    this.eventBus = eventBus;
		    this.display = view;
		    bind();
		  }
	 private void updateUserHistory(UpdateUserHistoryActor actor){
		 
	 }
	abstract class UpdateUserHistoryActor{
		public UpdateUserHistoryActor(final UserResearchSettingDTO setting){
			new RPCCall<UserHistoryDTO>() {
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error on updating account state!");
				}
		
				@Override
				public void onSuccess(UserHistoryDTO result) {
					DatabankApp.get().setCurrentUserHistory(result);
					doPostAction();
				}
		
				@Override
				protected void callService(AsyncCallback<UserHistoryDTO> cb) {
					UserHistoryDTO dt = DatabankApp.get().getCurrentUserHistory();
					dt.setCurrent_research(setting);
					DatabankApp.get().getUserService().updateResearchState(dt,cb);
					
				}
			}.retry(1);
			
		}
		public abstract void doPostAction();
	}
	@Override
	public void go(HasWidgets container,ArrayList<String> p_names,ArrayList<String> p_values) {
		 container.clear();
		 container.add(display.asWidget());
		 fetchResearchListData();
		 if (p_names.contains("showResearch"))
		 {
			 int index = p_names.indexOf("showResearch");
			 String id = p_values.get(index);
			 current_research_id = Long.parseLong(id);
			 //display.findInResearchList(Long.parseLong(id));
			 if(prev_res_id != null && prev_res_id.equals(current_research_id))
			 {
				 display.getCenterPanel().clear();
				 display.getCenterPanel().add(previous_centerpanel_state);
//				 USERRESEARCHSETTINGDTO DTO = DATABANKAPP.GET().GETCURRENTUSERHISTORY().GETCURRENT_RESEARCH();
//				 DTO.SETRESEARH(RESEARH)
			 }
			 else{
				 prev_res_id = new Long(current_research_id);
				 	display.getCenterPanel().clear();
					display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
					//current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
					fetchResearchDetailes(current_research_id);
			 }
			  // eventBus.fireEvent(new ShowResearchDetailsEvent(id));
		 }
		 if (p_names.contains("showVar"))
		 {
			 int index = p_names.indexOf("showVar");
			 String id = p_values.get(index);
			 display.getCenterPanel().clear();
			 display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
			 fetchVariableDetailes(Long.parseLong(id));
		 }
		 if (p_names.contains("action"))
		 {
			 int index = p_names.indexOf("action");
			 String act = p_values.get(index);
			 IShowPlaceParameters params = UserAppController.query_log.get(p_values.get(p_names.indexOf("params")));
			 //display.getCenterPanel().clear();
			 if(params!=null)performAction(act, params);
			 //display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
			 //fetchVariableDetailes(Long.parseLong(id));
		 }
		 
	}
	@Override
	public void performAction(String action, IShowPlaceParameters params) {
		display.getCenterPanel().clear();
		if(action.equals("catalog") && params instanceof ShowCatalogParameters)
		{
			ShowCatalogParameters p = (ShowCatalogParameters)params;
			doCatalogSearch(p);
		}
		if(action.equals("filter") && params instanceof ShowFilterParameters)
		{
			ShowFilterParameters p = (ShowFilterParameters)params;
			doFilterSearch(p);
		}
		if(action.equals("showResearchSaved") && params instanceof ShowResearchSavedParameters)
		{
			ShowResearchSavedParameters p = (ShowResearchSavedParameters)params;
			doShowSavedResearch(p);
		}
		if(action.equals("showAnalisysSaved") && params instanceof ShowAnalysisSavedParameters)
		{
			ShowAnalysisSavedParameters p = (ShowAnalysisSavedParameters)params;
			doShowSavedAnalysis(p);
		}
	}
	void doShowSavedResearch(ShowResearchSavedParameters p){
		UserResearchSettingDTO dto = p.getDto();
		current_research_id = dto.getResearh().getId();
		 //display.findInResearchList(Long.parseLong(id));
		 if(prev_res_id != null && prev_res_id.equals(current_research_id) && previous_centerpanel_state !=null)
		 {
			 display.getCenterPanel().clear();
			 display.getCenterPanel().add(previous_centerpanel_state);
		 }
		 else{
			 prev_res_id = new Long(current_research_id);
			 	display.getCenterPanel().clear();
				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
				DatabankApp.get().getCurrentUser().setCurrent_research(current_research_id);
				//current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
				fetchResearchDetailes(current_research_id);
		 }
	}
	void doShowSavedAnalysis(ShowAnalysisSavedParameters p){
		UserAnalysisSaveDTO dto = p.getDto();
		display.getCenterPanel().clear();
		 display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
		 if(dto.getDistr_type().equals(UserAnalysisSaveDTO.DISTR_TYPE_2D))
		 {
				display.getCenterPanel().clear();
				
				display.getCenterPanel().add(new UserResearchVar2DDView(dto.getVar_1().getResearch_id(),eventBus,display,dto));

		 }else{
			 fetchVariableSaved(dto);
		 }
	}
	void doCatalogSearch(ShowCatalogParameters p)
	{
		//final String [] types_to_search = (String[])(new HashSet<String>(p.getIdents())).toArray();
		final String [] types_to_search = new String[]{"research"};
		ArrayList<Long> ids = p.getIds();
		final Set<String> s = new HashSet<String>(p.getIdents());
		final HashMap<String,String> mapping = new LinkedHashMap<String, String>();
		if(ids.size()==0)return;
		
		if(s.size()==1)
		{
			if(s.contains("socioresearch"))
			{
				types_to_search[0] = "research";
				mapping.put("socioresearch_name", "Название");
		        mapping.put("socioresearch_gen_geath", "Генеральная совокупность");
		        mapping.put("socioresearch_sel_size", "Объем выборки");
		        mapping.put("socioresearch_dates", "Даты");
		        //dates added implicitly, combined in grid class
		        mapping.put("socioresearch_orgs_org_impl_organization", "Организация");
		        mapping.put("socioresearch_tag", "Теги");
		   }
		   else if(s.contains("sociovar"))
		   {
			   types_to_search[0]="sociovar";
			   mapping.put("sociovar_name", "Текст вопроса");
			   mapping.put("sociovar_alt_values", "Тексты альтернатив");
			   mapping.put("sociovar_researchname", "Исследование");
		   }	
		}
        
		 final String query;
		 
		 JSONObject obj_root = new JSONObject();
	     JSONObject obj_ids = new JSONObject();
	      JSONArray arr_values = new JSONArray();
	      for (int i = 0; i < ids.size(); i++)
	      {
	        arr_values.set(i, new JSONString(String.valueOf(ids.get(i))));
	      }
	      obj_ids.put("type", new JSONString(types_to_search[0]));
	      obj_ids.put("values", arr_values);
	      
	      obj_root.put("ids", obj_ids);
	      
	      query = obj_root.toString();
		    
		    new RPCCall<String>()
		    {
		      public void onFailure(Throwable caught)
		      {
		        display.getCenterPanel().clear();
		        display.getCenterPanel().add(new Label("Error on performing search!    " + caught.getMessage()));
		      }

		      public void onSuccess(String result)
		      {
		    	
		       
		        if(result.equals("Error"))
		        {
		        	 display.getCenterPanel().clear();
		        	 display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
		             display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
		             return;
		        }
		        ArrayList<JSONObject> hiters = new ArrayList<JSONObject>();
		        JSONObject res = (JSONObject)JSONParser.parse(result);
		        JSONObject hits = (JSONObject)res.get("hits");
		        JSONNumber total = (JSONNumber)hits.get("total");
		        Integer tot = Integer.valueOf((int)total.getValue());
		        JSONArray hits_arr = (JSONArray)hits.get("hits");
		        if(hits_arr.size()==0)
		        {
		        	 display.getCenterPanel().clear();
		        	 display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
		             return;
		        }
		        
		        for (int i = 0; i < hits_arr.size(); i++)
		        {
		          JSONObject hit = (JSONObject)hits_arr.get(i);
		          hiters.add(hit);
		        }

		        
		        //display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
		       // display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
		        if (s.size()==1){
		        	String link_code = "socioresearch_name";
		        	SearchResultsGrid gr = null;
		        	if(types_to_search[0].equals("sociovar"))
		        	{
		        		link_code = "sociovar_name";
		        		gr = new SearchResultsGrid(true,link_code,false,650,eventBus,tot, hiters, mapping);
				    }else
			        {
			        	gr = new SearchResultsGrid(true,link_code,false,650,eventBus,tot, hiters, mapping);
				    }
		        	display.getCenterPanel().clear();
				    if(gr!=null)display.getCenterPanel().add(gr);
		        }
	        	else {
	        		 SearchResultsGenericGrid gr = new SearchResultsGenericGrid(eventBus,tot, hiters);
	 		        display.getCenterPanel().clear();
	 		        display.getCenterPanel().add(gr);
	        	}
		      }

		      protected void callService(AsyncCallback<String> cb)
		      {
		        rpcService.doIndexSearch(query,types_to_search, cb);
		      }
		    }
		    .retry(2);
	      
	}
	void doFilterSearch(ShowFilterParameters p)
	{
		ArrayList<FilterBaseDTO> filters = p.getFilters();
		//final HashMap<String,String> mapping = p.getMapping();
		final HashMap<String,String> mapping = new LinkedHashMap<String, String>();
		final Set<String> s = new HashSet<String>(p.getIdents());
		String entity_substitute_name_w = null;
	    if(s.size()==1)
		{
			if(s.contains("research"))
			{
				entity_substitute_name_w = "socioresearch_name";
				mapping.put("socioresearch_name", "Название");
		        mapping.put("socioresearch_gen_geath", "Генеральная совокупность");
		        mapping.put("socioresearch_sel_size", "Объем выборки");
		        mapping.put("socioresearch_dates", "Даты");
		        //dates added implicitly, combined in grid class
		        mapping.put("socioresearch_orgs_org_impl_organization", "Организация");
		        mapping.put("socioresearch_tag", "Теги");
	   }
		   else if(s.contains("sociovar"))
		   {
			   entity_substitute_name_w = "sociovar_name";
			   mapping.put("sociovar_name", "Текст вопроса");
			   mapping.put("sociovar_alt_values", "Тексты альтернатив");
			   mapping.put("sociovar_researchname", "Исследование");
		   }	
		}
	    final String entity_substitute_name  = entity_substitute_name_w;
	    
		
		final String [] types_to_search = new String[p.getIdents().size()];
		int it = 0;
		for(String ss:p.getIdents())
		{
			types_to_search[it++]=ss;
		}
		 final String query;
		    //String query;
		    if (filters.size() > 0)
		    {
		      JSONObject obj_bool = new JSONObject();
		      JSONObject obj_should = new JSONObject();
		      JSONArray arr_should = new JSONArray();
		      for (int i = 0; i < filters.size(); i++)
		      {
		        arr_should.set(i, ((FilterBaseDTO)filters.get(i)).getJSONFilter());
		      }
		      obj_should.put("must", arr_should);
		      obj_bool.put("bool", obj_should);

		      
//		      {"bool":{"should":[
//		                         {"text":{"socioresearch_name":"sdfsdf"}},
//		                         {"range":{"socioresearch_sel_size":{"from":5, "to":10000}}},
//		                         {"bool":{"should":[{"text":{"socioresearch_generation_type_randomity":"случайная"}}]}},
//		                         {"bool":
//		                         	{"should":[{"text":{"socioresearch_orgs_org_order_organization":"КМИС"}},
//		                         	           {"text":{"socioresearch_orgs_org_order_organization":"Альянс"}}]}}]}}
		      
		      query = obj_bool.toString();
		    }
		    else {
		      JSONObject obj_b = new JSONObject();
		      JSONObject obj_text = new JSONObject();
		      obj_text.put("_all", new JSONString("*"));
		      obj_b.put("text", obj_text);

		      query = obj_b.toString();
		    }
		    int b = 2;

		    
		    
		    
		    
		    
		    
		    new RPCCall<String>()
		    {
		      public void onFailure(Throwable caught)
		      {
		        display.getCenterPanel().clear();
		        display.getCenterPanel().add(new Label("Error on performing search!    " + caught.getMessage()));
		      }

		      public void onSuccess(String result)
		      {
		    	
		       
		        if(result.equals("Error"))
		        {
		        	 display.getCenterPanel().clear();
		        	 display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
		             display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
		             return;
		        }
		        ArrayList<JSONObject> hiters = new ArrayList<JSONObject>();
		        JSONObject res = (JSONObject)JSONParser.parse(result);
		        JSONObject hits = (JSONObject)res.get("hits");
		        JSONNumber total = (JSONNumber)hits.get("total");
		        Integer tot = Integer.valueOf((int)total.getValue());
		        JSONArray hits_arr = (JSONArray)hits.get("hits");
		        if(hits_arr.size()==0)
		        {
		        	 display.getCenterPanel().clear();
		        	 display.getCenterPanel().add(new HTML("<H2>По вашему запросу ничего не найдено. Попробуйте изменить параметры поиска</H2>"));
		             return;
		        }
		        
		        for (int i = 0; i < hits_arr.size(); i++)
		        {
		          JSONObject hit = (JSONObject)hits_arr.get(i);
		          hiters.add(hit);
		        }

		        //display.getCenterPanel().add(new HTML("<H2>ПОИСКОВЫЙ ЗАПРОС:</H2><br><p>" + query + "</p>"));
		       // display.getCenterPanel().add(new HTML("<H3>ОТВЕТ ДВИЖКА:</H3><br><p>" + result + "</p>"));
		        display.getCenterPanel().clear();
		      	SearchResultsGrid gr = null;
	        	if(types_to_search[0].equals("sociovar"))
	        	{
	        		gr = new SearchResultsGrid(true,entity_substitute_name,false,650,eventBus,tot, hiters, mapping);
			    }else
		        {
		        	gr = new SearchResultsGrid(false,entity_substitute_name,false,650,eventBus,tot, hiters, mapping);
			    }
	        	if(gr!=null)display.getCenterPanel().add(gr);
		      }

		      protected void callService(AsyncCallback<String> cb)
		      {
		        rpcService.doIndexSearch(query,types_to_search, cb);
		      }
		    }
		    .retry(2);
	}
	
	static void removeParentStyles(TreeItem item)
	{
		if(item instanceof WrappedCustomLabel)
		{
			WrappedCustomLabel lbl = (WrappedCustomLabel)item;
			lbl.getLabel().removeStyleDependentName("parent");
			//lbl.getLabel().removeStyleDependentName("selected");
		}else
		{
			item.removeStyleDependentName("parent");
			//item.removeStyleDependentName("selected");
		}
		for(int i = 0; i < item.getChildCount(); i++)
		{
			removeParentStyles(item.getChild(i));
		}
		
	}
	static void removeParentStylesRoot(Tree tr)
	{
		for(int i = 0; i < tr.getItemCount(); i++)
		{
			removeParentStyles(tr.getItem(i));
		}
	}
	static void setParentStyles(TreeItem parent)
	{
		if(parent != null)
		{
			//TreeItem parent = source.getParentItem();
			if(parent instanceof WrappedCustomLabel)
			{
				WrappedCustomLabel lbl = (WrappedCustomLabel)parent;
				String style = lbl.getLabel().getStyleName();
				System.out.println(style);
				lbl.getLabel().removeStyleDependentName("selected");
				style = lbl.getLabel().getStyleName();
				System.out.println(style);
				lbl.getLabel().addStyleDependentName("parent");
				style = lbl.getLabel().getStyleName();
				System.out.println(style);
				int a2;
			}else
			{
				parent.removeStyleDependentName("selected");
				parent.addStyleDependentName("parent");
				String style = parent.getStyleName();
				System.out.println(style);
			}
			setParentStyles(parent.getParentItem());
		}
	}
	public void bind()
	{
//		display.getTree().addMouseDownHandler(new MouseDownHandler() {
//			@Override
//			public void onMouseDown(MouseDownEvent event) {
//				TreeItem it = display.getSelectedItem();
//				if (it instanceof SimpleResearchList)
//				{
//					//fetchResearchListData();
//				}
//				
//			}
//		});
		
		

		
		display.getTreeForSelection().addSelectionHandler(new SelectionHandler<TreeItem>() {
			//private VarDescItem prevVar;
			private WrappedCustomLabel prevRes;
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem it = display.getSelectedItem();
				removeParentStylesRoot(display.getTreeWhole());
				if(prevRes!=null)prevRes.getLabel().removeStyleDependentName("selected");
			
				if (it instanceof SimpleResearchList)
				{
					//fetchResearchListData();
				}else if (it instanceof ResearchDescItem)
				{
					ResearchDescItem rv = (ResearchDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					//fetchResearchVarData(it, rv.getResearch_id());
					
					current_research_id = rv.getContents_id();
					DatabankApp.get().getCurrentUser().setCurrent_research(current_research_id);
					eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getContents_id()));
				}else if (it instanceof ResearchVarList)
				{
					
					
				}
				else if (it instanceof ResearchMetadataItem)
				{
					ResearchMetadataItem rv = (ResearchMetadataItem)it;
					current_research_id = rv.getContents_id();
					//fetchResearchVarData(it, rv.getResearch_id());
					eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getContents_id()));
				}else if (it instanceof VarDescItem)
				{
					VarDescItem rv = (VarDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					//fetchResearchVarData(it, rv.getResearch_id());
					eventBus.fireEvent(new ShowVarDetailsEvent(rv.getVar_id()));
				//	eventBus.fireEvent(new AddResearchDisabledEvent());
				}
				else if (it instanceof ConceptItemItem)
				{
					final ConceptItemItem rcl = (ConceptItemItem)it;
					rcl.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rcl;
					rcl.refreshTaggedEntitiesIDs(new ConceptItemItem.AsyncAction() {
						@Override
						public void doAction() {
							eventBus.fireEvent(new ShowPlaceEvent("user-research", "catalog", new ShowCatalogParameters(rcl.getAll_tagged_ids(), rcl.getAll_tagged_idents())));
						}
					});
				}else if (it instanceof ConceptItemEntity)
				{
					final ConceptItemEntity rcl = (ConceptItemEntity)it;
					rcl.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rcl;
				}
				setParentStyles(it.getParentItem());
			}
		});
		display.getUserResearchTreeForSelection().addSelectionHandler(new SelectionHandler<TreeItem>() {
			//private VarDescItem prevVar;
			private WrappedCustomLabel prevRes;
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem it = display.getUserOwnSelectedItem();
				if(prevRes!=null)prevRes.getLabel().removeStyleDependentName("selected");
			
				if (it instanceof ResearchDescItem)
				{
					ResearchDescItem rv = (ResearchDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					//fetchResearchVarData(it, rv.getResearch_id());
					
					current_research_id = rv.getContents_id();
					//eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getContents_id()));
					new RPCCall<UserResearchSettingDTO>() {

						@Override
						public void onFailure(Throwable arg0) {
						}

						@Override
						public void onSuccess(UserResearchSettingDTO arg0) {
							UserResearchSettingDTO ddt = arg0;
							eventBus.fireEvent(new ShowPlaceEvent("user-research","showResearchSaved",new ShowResearchSavedParameters(ddt)));
					
						}

						@Override
						protected void callService(
								AsyncCallback<UserResearchSettingDTO> cb) {
							rpc_acc_service.getResearchSetting(current_research_id, cb);
						}
					}.retry(2);
		
				}else
				if (it instanceof AnalysisDescItem)
				{
					AnalysisDescItem rv = (AnalysisDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevRes = (WrappedCustomLabel)rv;
					UserAnalysisSaveDTO save_setting = rv.getDto();
					eventBus.fireEvent(new ShowPlaceEvent("user-research","showAnalisysSaved",new ShowAnalysisSavedParameters(save_setting)));
					
//					if(save_setting.getDistr_type().equals(UserAnalysisSaveDTO.DISTR_TYPE_1D))
//					{
//						eventBus.fireEvent(new ShowVarDetailsEvent(rv.getDto().getVar_1().getId(),save_setting));
//					}else
//					{
//						eventBus.fireEvent(new ShowVar2DDEvent(save_setting.getSeting().getResearh().getID(),save_setting));
//					}
				}
			}
		});
		display.getUserResearchTreeForOpen().addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem it = event.getTarget();
				if (it instanceof UserOwnResearchList)
				{
					fetchMyResearchListData();
				}else if (it instanceof UserOwnAnalisysList)
				{
					fetchMyAllAnalysisListData();
				}
			}
		});
		display.getTreeForOpen().addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem it = event.getTarget();
				removeParentStylesRoot(display.getTreeWhole());
				if (it instanceof SimpleResearchList)
				{
					fetchResearchListData();
				}else if (it instanceof ResearchVarList)
				{
					ResearchVarList rv = (ResearchVarList)it;
					fetchResearchVarData(it, rv.getResearch_id());
					current_research_id = rv.getResearch_id();
					
					//eventBus.fireEvent(new ShowResearchDetailsEvent(rv.getResearch_id()));
				}
				else if (it instanceof ConceptItemItem)
				{
					ConceptItemItem rcl = (ConceptItemItem)it;
					rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent());
				}
				else if (it instanceof ConceptItemEntity)
				{
					ConceptItemEntity rcl = (ConceptItemEntity)it;
					rcl.refreshContents();
					//eventBus.fireEvent(new CreateConceptEnabledEvent());
				}
				
				setParentStyles(it.getParentItem());
			
			}
		});
		
		
		
		
		
		
//		eventBus.addHandler(ShowResearchDetailsEvent.TYPE, new ShowResearchDetailsEventHandler() {
//			@Override
//			public void onShowResearchDetails(ShowResearchDetailsEvent event) {
//				display.getCenterPanel().clear();
//				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
//				current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
//				fetchResearchDetailes(event.getResearch_id());
//			}
//		});
//		eventBus.addHandler(ShowVarDetailsEvent.TYPE, new ShowVarDetailsEventHandler() {
//			@Override
//			public void onShowVarDetails(ShowVarDetailsEvent event) {
//				display.getCenterPanel().clear();
//				//display.getCenterPanel().add(new VariableDetailedView(event.get))
//				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
//				fetchVariableDetailes(event.getVar_id());
//				//fetchResearchDetailes(event.getResearch_id());
//			}
//		});
		eventBus.addHandler(ShowVar2DDEvent.TYPE, new ShowVar2DDEventHandler() {
			@Override
			public void onShowVar2DD(ShowVar2DDEvent event) {
				display.getCenterPanel().clear();
				
				display.getCenterPanel().add(new UserResearchVar2DDView(event.getRes_id(),eventBus,display,event.getPre_saved()));
			}
		});
		//		eventBus.addHandler(ShowStartPageMainEvent.TYPE, new ShowStartPageMainEventHandler() {
//			@Override
//			public void onShowStartPageMain(ShowStartPageMainEvent event) {
//				showMainPageArticle();
//			}
//		});
	}
	
	private void fetchVariableDetailes(final Long id_var)
	{
		new RPCCall<VarDTO_Detailed>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting var detailes :"+caught.getMessage());
			}

			@Override
			public void onSuccess(final VarDTO_Detailed result) {
			//	AdminResearchDetailedView ad_view = new AdminResearchDetailedView(new UserResearchDetailedView(result));
			//	AdminResearchEditView ed_view = new AdminResearchEditView(result);
			//	AdminResearchDetailedPresenter presenter = new AdminResearchDetailedPresenter(rpcUserService,rpcAdminService, eventBus, ad_view, ed_view);
			//	presenter.go(display.getCenterPanel());
				
				//UserAccountDTO accdto = DatabankApp.get().getCurrentUser();
				
				//histdto.
				//if
				
				//fetchResearchDetailesFirst(result.getResearch_id());
				if(current_research == null || !result.getResearch_id().equals(current_research.getDto().getId()))
				{
					new RPCCall<ResearchBundleDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error on getting research detailes :"+caught.getMessage());
						}

						@Override
						public void onSuccess(ResearchBundleDTO result_dto) {
							current_research = result_dto;
							//UserHistoryDTO histdto = DatabankApp.get().getCurrentUserHistory();
							final UserResearchSettingDTO sett = new UserResearchSettingDTO();
							sett.setResearh(current_research.getDto());
							//UserAccountDTO user = DatabankApp.get().getCurrentUser();
							//DatabankApp.get().getCurrentUserHistory().setCurrent_research(sett);
							updateUserHistory(new UpdateUserHistoryActor(sett){
								@Override
								public void doPostAction() {
									final UserAnalysisSaveDTO sv_dt = new UserAnalysisSaveDTO();
									sv_dt.setVar_1(result);
									sv_dt.setDistribution(result.getDistribution());
									UserHistoryDTO histdto2 = DatabankApp.get().getCurrentUserHistory();
									final UserResearchSettingDTO sett2 = histdto2.getCurrent_research();
									//sett2.setResearh(current_research.getDto());
									
									sv_dt.setSeting(sett2);
		
									showVariableView(result, sv_dt);
								}
							});
							//showVariableView(result, sv_dt);
							
							
						
						}

						@Override
						protected void callService(AsyncCallback<ResearchBundleDTO> cb) {
							rpcService.getResearchBundle(result.getResearch_id(), cb);
						}
					}.retry(2);
				}else
				{
					final UserAnalysisSaveDTO sv_dt = new UserAnalysisSaveDTO();
					
					sv_dt.setVar_1(result);
					sv_dt.setDistribution(result.getDistribution());
					UserHistoryDTO histdto = DatabankApp.get().getCurrentUserHistory();
					final UserResearchSettingDTO sett = histdto.getCurrent_research();
					
					sett.setResearh(current_research.getDto());
					//UserAccountDTO user = DatabankApp.get().getCurrentUser();
					sv_dt.setSeting(sett);
					showVariableView(result, sv_dt);
				}
			}

			@Override
			protected void callService(final AsyncCallback<VarDTO_Detailed> cb) {
				UserHistoryDTO histdto = DatabankApp.get().getCurrentUserHistory();
				final UserResearchSettingDTO sett = histdto.getCurrent_research();
				updateUserHistory(new UpdateUserHistoryActor(sett){
					@Override
					public void doPostAction() {
						rpcService.getVarDetailed(id_var, cb);
					}
				});
			}
		}.retry(1);
	}
	
	private void showVariableView(final VarDTO_Detailed result,final UserAnalysisSaveDTO sv_dt)
	{
		new RPCCall<MetaUnitMultivaluedEntityDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
			}

			@Override
			public void onSuccess(MetaUnitMultivaluedEntityDTO res) {
				display.getCenterPanel().clear();
				if (result instanceof RealVarDTO_Detailed)
					display.getCenterPanel().add(new RealVariableDetailedView((RealVarDTO_Detailed)result,res,eventBus,display,sv_dt));
				else if (result instanceof TextVarDTO_Detailed)
					display.getCenterPanel().add(new TextVariableDetailedView((TextVarDTO_Detailed)result,res,eventBus,display,sv_dt));
				else
					display.getCenterPanel().add(new VariableDetailedView(result,res,eventBus,display,sv_dt));
			}

			@Override
			protected void callService(
					AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
				UserSocioResearchService.Util.getInstance().getDatabankStructure("sociovar", cb);
			}
		}.retry(2);
	}
	private void fetchVariableSaved(final UserAnalysisSaveDTO saved_dto)
	{
		final VarDTO_Detailed result = saved_dto.getVar_1();
		result.setDistribution(saved_dto.getDistribution());
				
				
					new RPCCall<MetaUnitMultivaluedEntityDTO>() {

						@Override
						public void onFailure(Throwable arg0) {
						}

						@Override
						public void onSuccess(MetaUnitMultivaluedEntityDTO res) {
							display.getCenterPanel().clear();
							if (result instanceof RealVarDTO_Detailed)
								display.getCenterPanel().add(new RealVariableDetailedView((RealVarDTO_Detailed)result,res,eventBus,display,saved_dto));
							else if (result instanceof TextVarDTO_Detailed)
								display.getCenterPanel().add(new TextVariableDetailedView((TextVarDTO_Detailed)result,res,eventBus,display,saved_dto));
							else
								display.getCenterPanel().add(new VariableDetailedView(result,res,eventBus,display,saved_dto));
						}

						@Override
						protected void callService(
								AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
							UserSocioResearchService.Util.getInstance().getDatabankStructure("sociovar", cb);
						}
					}.retry(2);
	}
	private void fetchResearchListData()
	{
		//final ArrayList<NewsDTO> newsData = new ArrayList<NewsDTO>();
		
		new RPCCall<ArrayList<SocioResearchDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" news: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<SocioResearchDTO_Light> result) {
				display.setResearchListData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<SocioResearchDTO_Light>> cb) {
				rpcService.getResearchSummaries(cb);
			}
		}.retry(3);
	}
	private void fetchMyResearchListData()
	{
		//final ArrayList<NewsDTO> newsData = new ArrayList<NewsDTO>();
		
		new RPCCall<List<SocioResearchDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" users researches: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(List<SocioResearchDTO_Light> result) {
				display.setUserOwnResearchListData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<List<SocioResearchDTO_Light>> cb) {
				rpc_acc_service.getMyResearchesList(cb);
			}
		}.retry(3);
	}
	
	private void fetchMyAllAnalysisListData()
	{
		//final ArrayList<NewsDTO> newsData = new ArrayList<NewsDTO>();
		
		new RPCCall<List<UserAnalysisSaveDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" users researches: "
			            + caught.getMessage());
			}

			@Override
			public void onSuccess(List<UserAnalysisSaveDTO> result) {
				display.setAllAnalysisListData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<List<UserAnalysisSaveDTO>> cb) {
				rpc_acc_service.getUserAllAnalisysList(cb);
			}
		}.retry(3);
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
				display.setVarListData(item,result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<VarDTO_Light>> cb) {
				rpcService.getResearchVarsSummaries(id_research, cb);
			}
		}.retry(3);
	}
	private void fetchResearchDetailes(final Long id_research)
	{
		new RPCCall<ResearchBundleDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting research detailes :"+caught.getMessage());
			}

			@Override
			public void onSuccess(final ResearchBundleDTO result) {
				current_research = result;
				UserResearchSettingDTO setting = new UserResearchSettingDTO();
				if(DatabankApp.get().getCurrentUserHistory()!=null &&
						DatabankApp.get().getCurrentUserHistory().getCurrent_research()!=null &&
						DatabankApp.get().getCurrentUserHistory().getCurrent_research().getResearh() !=null &&
						DatabankApp.get().getCurrentUserHistory().getCurrent_research().getResearh().getId() == result.getDto().getId()){
					setting = DatabankApp.get().getCurrentUserHistory().getCurrent_research();
				}
				setting.setResearh(result.getDto());
				//DatabankApp.get().getCurrentUserHistory().getCurrent_research().setResearh(result.getDto());
				updateUserHistory(new UpdateUserHistoryActor(setting){
					@Override
					public void doPostAction() {
						display.showResearchDetailes(result);
						previous_centerpanel_state = display.getCenterPanel().getWidget(0);
					}
				});
				
			}

			@Override
			protected void callService(AsyncCallback<ResearchBundleDTO> cb) {
				rpcService.getResearchBundle(id_research, cb);
			}
		}.retry(3);
	}
	
}
