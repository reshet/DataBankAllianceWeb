package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.UserAppController;
import com.mresearch.databank.client.event.ShowConsultationDetailsEvent;
import com.mresearch.databank.client.event.ShowConsultationIndexEvent;
import com.mresearch.databank.client.event.ShowPublicationDetailsEvent;
import com.mresearch.databank.client.event.ShowResearchDetailsEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.event.ShowZaconDetailsEvent;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.VarDTO_Research;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.tree.TreeGrid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SearchResultsGrid extends VerticalPanel
{
  private Integer total;
  private ArrayList<JSONObject> hits;
  private HashMap<String, String> map;
  private SimpleEventBus bus;
  private ArrayList<VarDTO_Research> research_names;
  //private ArrayList<Integer> keys;
  
  static  UserSocioResearchServiceAsync userService = GWT.create(UserSocioResearchService.class);
  public SearchResultsGrid(boolean attach_research_names,final String link_field_name,final boolean multityped,final long width,final SimpleEventBus ev_bus,final Integer total, final ArrayList<JSONObject> hits,final  HashMap<String, String> map)
  {
	  if(attach_research_names){
		  final ArrayList<Long> ids = new ArrayList<Long>();
		  	for (JSONObject hit_c : hits)
		    {
		      String id =  ((JSONString)hit_c.get("_id")).stringValue();
		      ids.add(Long.parseLong(id));
		    }
		    new RPCCall<ArrayList<VarDTO_Research>>() {

			@Override
			public void onFailure(Throwable arg0) {
				
			}

			@Override
			public void onSuccess(ArrayList<VarDTO_Research> ans) {
				research_names = ans;
				doSearchResultsGrid(link_field_name, multityped,width,ev_bus, total,hits,map);
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<VarDTO_Research>> cb){
				userService.getVarsResearchNames(ids, cb);
			}
			
		}.retry(2);
		 
	  }else doSearchResultsGrid(link_field_name, multityped,width,ev_bus, total,hits,map);
	  
  }
  private void doSearchResultsGrid(String link_field_name,boolean multityped,long width,SimpleEventBus ev_bus,Integer total, ArrayList<JSONObject> hits, HashMap<String, String> map)
  {
    this.map = map;
    this.hits = hits;
    this.total = total;
    this.bus = ev_bus;
    //TreeGrid g = new TreeGrid();
    //g.setShowFilterEditor(true);
    //g.setFilt
    
    
    ListGrid countryGrid = new ListGrid();
    countryGrid.setZIndex(0);
    countryGrid.setWidth(String.valueOf(width));
    countryGrid.setHeight("100%");
    countryGrid.setShowAllRecords(Boolean.valueOf(true));
    countryGrid.setWrapCells(Boolean.valueOf(true));
    countryGrid.setCellHeight(38);
    
    

    HashMap<String,String> used_map = new LinkedHashMap<String,String>();
    final ListGridRecord[] records = new ListGridRecord[hits.size()];
    int i = 0;
    for (JSONObject hit_c : hits)
    {
      JSONObject hit = (JSONObject)hit_c.get("_source");

      ListGridRecord rec = new ListGridRecord();
      String id =  ((JSONString)hit_c.get("_id")).stringValue();
      //String link = ""
      rec.setAttribute("_id",id);
      rec.setAttribute("_type", ((JSONString)hit_c.get("_type")).stringValue());
      rec.setAttribute("_type_vis", ((JSONString)hit_c.get("_type")).stringValue());
      
      if(multityped)
      {
    	   
		  if(rec.getAttribute("_type_vis").equals("research"))rec.setAttribute("_type_vis", "<a href=\"#user-research@showResearch="+id+"\">Исследование</a>");
		  if(rec.getAttribute("_type_vis").equals("sociovar"))rec.setAttribute("_type_vis", "<a href=\"#user-research@showVar="+id+"\">Переменная</a>");
		  if(rec.getAttribute("_type_vis").equals("law"))rec.setAttribute("_type_vis", "<a href=\"#user-law@showZacon="+id+"\">Закон</a>");
		  if(rec.getAttribute("_type_vis").equals("publication"))rec.setAttribute("_type_vis", "<a href=\"#user-pub@showPub="+id+"\">Публикация</a>");
		  if(rec.getAttribute("_type_vis").equals("consultation"))rec.setAttribute("_type_vis", "<a href=\"#user-jury@showConsult="+id+"\">Консультация</a>");
		  
      }
      
      //COMBINE DATES
      {
    	  if(hit.containsKey("socioresearch_dates_start_date") && hit.containsKey("socioresearch_dates_end_date")){
    		  String date1 = ((JSONString)hit.get("socioresearch_dates_start_date")).stringValue();
    		  date1 = date1.substring(0, date1.indexOf("T"));
    		  String date2 = ((JSONString)hit.get("socioresearch_dates_end_date")).stringValue();
    		  date2 = date2.substring(0, date2.indexOf("T"));
    		  
        	  hit.put("socioresearch_dates", new JSONString(date1+" - "+date2));
        		  
    	  }
    	  
      }
      //ADD RESEARCH NAMES IF NEED
      
      
      
      
      
      
      {
    	  if(map.containsKey("sociovar_researchname")){
    		  Integer key = (int) research_names.get(i).getRes_id();
    		  String name = research_names.get(i).getRes_name();
    		  
    		  hit.put("sociovar_researchname", new JSONString("<a href=\"#user-research@showResearch="+key+"\">"+name+"</a>"));
          }
    	  
    	  
      }
      for (String key : map.keySet())
      {
        if (!hit.containsKey(key))
          continue;
        if(hit.get(key) instanceof JSONString){
        	if(!multityped && key.equals(link_field_name))
        	{
        		String val =((JSONString)hit.get(key)).stringValue();
        		// if(key.equals("sociovar_researhname"))rec.setAttribute(key, "<a href=\"#user-research@showResearch="+id+"\">"+val+"</a>");  
        		if(key.equals("socioresearch_name"))rec.setAttribute(key, "<a href=\"#user-research@showResearch="+id+"\">"+val+"</a>");
        	     // if(key.equals("sociovar_name"))rec.setAttribute(key, "<a href=\"#user-research@showVar="+id+"\">"+val+"</a>");
        	      if(key.equals("sociovar_name"))rec.setAttribute(key, "<a href=\"#user-research@showVar="+id+"\">"+val+"</a>");
          	      if(key.equals("law_name"))rec.setAttribute(key, "<a href=\"#user-law@showZacon="+id+"\">"+val+"</a>");
        	      if(key.equals("publication_name"))rec.setAttribute(key, "<a href=\"#user-pub@showPub="+id+"\">"+val+"</a>");
        	      if(key.equals("consultation_name"))rec.setAttribute(key, "<a href=\"#user-jury@showConsult="+id+"\">"+val+"</a>");
        	}else
        	{
             	rec.setAttribute(key, ((JSONString)hit.get(key	)).stringValue());
        	}
        	
//	        mapping.put("socioresearch_dates_start_date", "Дата начала");
//	        mapping.put("socioresearch_dates_end_date", "Дата конца");
        }
        if(hit.get(key) instanceof JSONNumber)
        	rec.setAttribute(key, ((JSONNumber)hit.get(key)).doubleValue());
        if(hit.get(key) instanceof JSONArray)
        	rec.setAttribute(key, ((JSONArray)hit.get(key)).toString());
        
        used_map.put(key, (String)map.get(key));
      }

      records[(i++)] = rec;
    }

    ListGridField[] fields = null;
    int j = 0;
    if(multityped)
    {
    	fields = new ListGridField[1 + used_map.size()];
        ListGridField type_f = new ListGridField("_type_vis", "Тип сущности");
        type_f.setType(ListGridFieldType.LINK);
        //type_f.setL
        //id_f.setWidth(25);
        //fields[0] = id_f;
        fields[0] = type_f;
        type_f.setWidth(120);
        j++;
    }else
    {
    	fields = new ListGridField[used_map.size()];
    }
    
    	
    
    
    for (String key : used_map.keySet())
    {
      ListGridField field = new ListGridField(key, (String)used_map.get(key));
      if(key.equals(link_field_name)){
    	  field.setType(ListGridFieldType.LINK);
    	  field.setWidth(220);
      }
      if(key.equals("sociovar_researchname")){
    	  field.setType(ListGridFieldType.LINK);
    	  field.setWidth(220);
      }
     // field.setA
      fields[(j++)] = field;
    }

   // countryGrid.setAutoFitFieldWidths(true);
    
    
    
    
    
    
    
    countryGrid.setFields(fields);

    countryGrid.setData(records);
//    countryGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
//		@Override
//		public void onCellDoubleClick(CellDoubleClickEvent event) {
//			int row = event.getRowNum();
//			int id = Integer.parseInt(records[row].getAttributeAsString("_id"));
//			String type = records[row].getAttributeAsString("_type");
//			if(type.equals("research"))
//			{
//				bus.fireEvent(new ShowResearchDetailsEvent(id));
//			}
//			if(type.equals("sociovar"))
//			{
//				bus.fireEvent(new ShowVarDetailsEvent(id));
//			}
//			if(type.equals("law"))
//			{
//				bus.fireEvent(new ShowZaconDetailsEvent(id));
//			}
//			if(type.equals("publication"))
//			{
//				bus.fireEvent(new ShowPublicationDetailsEvent(id));
//			}
//			if(type.equals("consultation"))
//			{
//				bus.fireEvent(new ShowConsultationDetailsEvent(id));
//			}
//		}
//	});
//    countryGrid.setZIndex(-99900);
    countryGrid.setSelectionType(SelectionStyle.SINGLE);
    add(countryGrid);
  }
}