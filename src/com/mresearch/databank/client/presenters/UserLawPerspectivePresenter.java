package com.mresearch.databank.client.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.UserAppController;
import com.mresearch.databank.client.event.CreateConceptEnabledEvent;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEventHandler;
import com.mresearch.databank.client.event.ShowStartPageMainEvent;
import com.mresearch.databank.client.event.ShowStartPageMainEventHandler;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEventHandler;
import com.mresearch.databank.client.event.ShowZaconDetailsEvent;
import com.mresearch.databank.client.event.ShowZaconDetailsEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminArticleService;
import com.mresearch.databank.client.service.AdminArticleServiceAsync;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.StartPageServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.client.views.ArticleDescItem;
import com.mresearch.databank.client.views.ArticleDetailedView;
import com.mresearch.databank.client.views.ArticlesShortView;

import com.mresearch.databank.client.views.ConceptContentsItem;
import com.mresearch.databank.client.views.ConceptItem;
import com.mresearch.databank.client.views.IPickBinder;

import com.mresearch.databank.client.views.ConceptItemItem;
import com.mresearch.databank.client.views.ConceptItemItem.AsyncAction;
import com.mresearch.databank.client.views.ConceptItemEntity;
import com.mresearch.databank.client.views.PickElementsTableView;
import com.mresearch.databank.client.views.ResearchDescItem;
import com.mresearch.databank.client.views.ResearchVarList;
import com.mresearch.databank.client.views.RootConceptsList;
import com.mresearch.databank.client.views.SearchResultsGrid;
import com.mresearch.databank.client.views.SimpleArticleList;
import com.mresearch.databank.client.views.SimpleResearchList;
import com.mresearch.databank.client.views.SimpleZaconList;
import com.mresearch.databank.client.views.VarDescItem;
import com.mresearch.databank.client.views.VariableDetailedView;
import com.mresearch.databank.client.views.WrappedCustomLabel;
import com.mresearch.databank.client.views.ZaconDescItem;
import com.mresearch.databank.client.views.ZaconDetailedView;
import com.mresearch.databank.client.views.ZaconsShortView;
import com.mresearch.databank.shared.FilterBaseDTO;
import com.mresearch.databank.shared.IShowPlaceParameters;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.NewsDTO;
import com.mresearch.databank.shared.NewsSummaryDTO;
import com.mresearch.databank.shared.ShowCatalogParameters;
import com.mresearch.databank.shared.ShowFilterParameters;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.ZaconDTO;
import com.mresearch.databank.shared.ZaconDTO_Light;

public class UserLawPerspectivePresenter implements Presenter,Action
{
	 public interface Display {
		 HasMouseDownHandlers getTree();
		 HasOpenHandlers<TreeItem> getTreeForOpen();
		 HasSelectionHandlers<TreeItem> getTreeForSelection();
		 void setZaconListData(ArrayList<ZaconDTO> data);
		 Widget asWidget();
		 void showLoadingLabel();
		 TreeItem getSelectedItem();
		 void showZaconDetailes(ZaconDTO dto,String path);
		 void showZaconIndex(ArrayList<ZaconDTO> dtos,String path);
		 
		 VerticalPanel getCenterPanel();
		 void findInZaconList(Long id);
	 }
	 private final AdminArticleServiceAsync rpcService;
	 private UserSocioResearchServiceAsync service = UserSocioResearchService.Util.getInstance();
	 private final SimpleEventBus eventBus;
	 private final Display display;
	 public UserLawPerspectivePresenter(AdminArticleServiceAsync rpcService,SimpleEventBus eventBus,
		      Display view) {
		    this.rpcService = rpcService;
		    this.eventBus = eventBus;
		    this.display = view;
		    bind();
		  }
	@Override
	public void go(HasWidgets container,ArrayList<String> p_names,ArrayList<String> p_values) {
		 container.clear();
		 container.add(display.asWidget());
		 fetchZaconListData();
		 if (p_names.contains("showZacon"))
		 {
			 int index = p_names.indexOf("showZacon");
			 String id = p_values.get(index);
			 Long idd = Long.parseLong(id);
			 display.findInZaconList(idd);
			 fetchZaconDetailes(idd,"Законодательство");
			 // eventBus.fireEvent(new ShowResearchDetailsEvent(id));
		 } if (p_names.contains("action"))
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
	}
	void doCatalogSearch(ShowCatalogParameters p)
	{
		//final String [] types_to_search = (String[])(new HashSet<String>(p.getIdents())).toArray();
		final String [] types_to_search = new String[]{"law"};
		ArrayList<Long> ids = p.getIds();
		if(ids.size()==0)return;
		final HashMap<String,String> mapping = new LinkedHashMap<String, String>();
        mapping.put("law_name", "Название");
        mapping.put("law_number", "Номер");
        mapping.put("law_date", "Дата принятия");
        mapping.put("law_catalog", "Каталогизация");
        mapping.put("law_authors", "Авторы");
        //mapping.put("", "");
         
        
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
		        SearchResultsGrid gr = new SearchResultsGrid(false,"law_name",false,650,eventBus,tot, hiters, mapping);
		        display.getCenterPanel().clear();
		        display.getCenterPanel().add(gr);
		      }

		      protected void callService(AsyncCallback<String> cb)
		      {
		        service.doIndexSearch(query,types_to_search, cb);
		      }
		    }
		    .retry(2);
	      
		    
	}
	void doFilterSearch(ShowFilterParameters p)
	{
		ArrayList<FilterBaseDTO> filters = p.getFilters();
		//final HashMap<String,String> mapping = p.getMapping();
		
		final HashMap<String,String> mapping = new LinkedHashMap<String, String>();
        mapping.put("law_name", "Название");
        mapping.put("law_number", "Номер");
        mapping.put("law_date", "Дата принятия");
        mapping.put("law_catalog", "Каталогизация");
        mapping.put("law_authors", "Авторы");

		final String [] types_to_search = new String[p.getIdents().size()];
		int it = 0;
		for(String s:p.getIdents())
		{
			types_to_search[it++]=s;
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
		        SearchResultsGrid gr = new SearchResultsGrid(false,"law_name",false,650,eventBus,tot, hiters, mapping);
		        display.getCenterPanel().clear();
		        display.getCenterPanel().add(gr);
		      }

		      protected void callService(AsyncCallback<String> cb)
		      {
		        service.doIndexSearch(query,types_to_search, cb);
		      }
		    }
		    .retry(2);
	}
	public void bind()
	{
		display.getTree().addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				TreeItem it = display.getSelectedItem();
				if (it instanceof SimpleZaconList)
				{
				    fetchZaconListData();
				}
//				else if (it instanceof ZaconDescItem)
//				{
//					final ZaconDescItem rv = (ZaconDescItem)it;
//					new RPCCall<ZaconDTO>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							Window.alert("Error on fetching Zacon det");
//						}
//						@Override
//						public void onSuccess(final ZaconDTO result) {
//							display.getCenterPanel().clear();
//							new RPCCall<MetaUnitMultivaluedEntityDTO>() {
//
//								@Override
//								public void onFailure(Throwable arg0) {
//								}
//
//								@Override
//								public void onSuccess(MetaUnitMultivaluedEntityDTO res) {
//									display.getCenterPanel().add(new ZaconDetailedView(result,res).asWidget());
//								}
//
//								@Override
//								protected void callService(
//										AsyncCallback<MetaUnitMultivaluedEntityDTO> cb) {
//									AdminSocioResearchService.Util.getInstance().getDatabankStructure("law",cb);
//								}
//							}.retry(2);
//						}
//
//						@Override
//						protected void callService(AsyncCallback<ZaconDTO> cb) {
//							rpcService.getZacon(rv.getContents_id(), cb);
//						}
//					}.retry(2);
//				}
			}
		});
		
		
		display.getTreeForSelection().addSelectionHandler(new SelectionHandler<TreeItem>() {
			private WrappedCustomLabel prevItem;
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem it = display.getSelectedItem();
				if(prevItem!=null)prevItem.getLabel().removeStyleDependentName("selected");
				if (it instanceof SimpleZaconList)
				{
					//fetchResearchListData();
				}else if (it instanceof ZaconDescItem)
				{
					ZaconDescItem rv = (ZaconDescItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevItem = (WrappedCustomLabel)rv;
					//fetchResearchVarData(it, rv.getResearch_id());
					eventBus.fireEvent(new ShowZaconDetailsEvent(rv.getContents_id()));
				}
				else if (it instanceof ConceptItemItem)
				{
					final ConceptItemItem rv = (ConceptItemItem)it;
					rv.getLabel().addStyleDependentName("selected");
					prevItem = (WrappedCustomLabel)rv;
					rv.refreshTaggedEntitiesIDs(new ConceptItemItem.AsyncAction() {
						@Override
						public void doAction() {
							eventBus.fireEvent(new ShowPlaceEvent("user-law", "catalog", new ShowCatalogParameters(rv.getAll_tagged_ids(), rv.getAll_tagged_idents())));
						}
					});
					//if(rv.getLaw_ids().size()>0)fetchZaconIndex(rv.getLaw_ids(),rv.getCatalog_path());
				}else if (it instanceof ConceptItemEntity)
				{
					final ConceptItemEntity rcl = (ConceptItemEntity)it;
					rcl.getLabel().addStyleDependentName("selected");
					prevItem = (WrappedCustomLabel)rcl;
				}
			}
		});
		
		
		display.getTreeForOpen().addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem it = event.getTarget();
				if (it instanceof SimpleZaconList)
				{
					((SimpleZaconList)it).refreshContents();
//					fetchResearchListData();
				}else if (it instanceof RootConceptsList)
				{
					RootConceptsList rcl = (RootConceptsList)it;
					rcl.refreshContents();
				}else if (it instanceof ConceptItemItem)
				{
					ConceptItemItem rv = (ConceptItemItem)it;
					rv.refreshContents();
					//if(rv.getLaw_ids().size()>0)fetchZaconIndex(rv.getLaw_ids());
					//fetchResearchVarData(it, rv.getResearch_id());
					//eventBus.fireEvent(new ShowZaconDetailsEvent(rv.getContents_id()));
				}
			}
		});
		
//		eventBus.addHandler(ShowZaconDetailsEvent.TYPE, new ShowZaconDetailsEventHandler() {
//			@Override
//			public void onShowZaconDetails(ShowZaconDetailsEvent event) {
//				display.getCenterPanel().clear();
//				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
//				fetchZaconDetailes(event.getZacon_id(),"Законодательство");
//			}
//		});
		
//		eventBus.addHandler(ShowVarDetailsEvent.TYPE, new ShowVarDetailsEventHandler() {
//			@Override
//			public void onShowVarDetails(ShowVarDetailsEvent event) {
//				display.getCenterPanel().clear();
//				//display.getCenterPanel().add(new VariableDetailedView(event.get))
//				display.getCenterPanel().add(new HTML("<h2>Загрузка данных...</h2>"));
//				//fetchVariableDetailes(event.getVar_id());
//				//fetchResearchDetailes(event.getResearch_id());
//			}
//		});
//		eventBus.addHandler(ShowStartPageMainEvent.TYPE, new ShowStartPageMainEventHandler() {
//			@Override
//			public void onShowStartPageMain(ShowStartPageMainEvent event) {
//				showMainPageArticle();
//			}
//		});
	//	eventBus.addHandler(AddA, handler)
	}
	
	private void fetchZaconListData()
	{
		new RPCCall<ArrayList<ZaconDTO_Light>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fetching " +
						" news: "
			            + caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ZaconDTO_Light> result) {
//				display.setResearchListData(result);
			}

			@Override
			protected void callService(
					AsyncCallback<ArrayList<ZaconDTO_Light>> cb) {
				rpcService.getZaconsAll(cb);
			}
		}.retry(3);
	}
	
	private void fetchZaconDetailes(final Long id_Zacon,final String path)
	{
		new RPCCall<ZaconDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting zacon detailes :"+caught.getMessage());
			}

			@Override
			public void onSuccess(ZaconDTO result) {
				display.showZaconDetailes(result,path);
			}

			@Override
			protected void callService(AsyncCallback<ZaconDTO> cb) {
				rpcService.getZacon(id_Zacon, cb);
			}
		}.retry(3);
	}
	private void fetchZaconIndex(final ArrayList<Long> law_ids,final String path)
	{
		new RPCCall<ArrayList<ZaconDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting zacon detailes index:"+caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<ZaconDTO> result) {
				display.showZaconIndex(result,path);
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<ZaconDTO>> cb) {
				AdminArticleService.Util.getInstance().getZaconDTOs_Normal(law_ids, cb);
			}
		}.retry(3);
	}
}
