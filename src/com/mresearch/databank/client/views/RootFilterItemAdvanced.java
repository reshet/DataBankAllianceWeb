package com.mresearch.databank.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.Place;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchService.Util;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.FilterBaseDTO;
import com.mresearch.databank.shared.FilterDateDiapasonDTO;
import com.mresearch.databank.shared.FilterDiapasonDTO;
import com.mresearch.databank.shared.FilterMatchDTO;
import com.mresearch.databank.shared.FilterMultiDTO;
import com.mresearch.databank.shared.FilterRealDiapasonDTO;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedStructureDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;
import com.mresearch.databank.shared.ShowFilterParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.zenika.widget.client.datePicker.DatePicker;


public class RootFilterItemAdvanced extends VerticalPanel
{
 // private VerticalPanel results_viewer;
  private Place place;
  private Button doFilterBtn;
  //private TreeItem doUseFilters;
  //private UserSocioResearchServiceAsync service = (UserSocioResearchServiceAsync)GWT.create(UserSocioResearchService.class);
  //private AdminSocioResearchServiceAsync service_adm = AdminSocioResearchService.Util.getInstance();
  private SimpleEventBus bus;
  private final ArrayList<IFilterProvider> allFiltersRegister = new ArrayList<IFilterProvider>();

  private HashMap<String, String> mapping = new HashMap<String, String>();

  private void buildFiltersTree(TreeItem root, String base_name, ArrayList<MetaUnitDTO> subunits, HashMap<String, String> map)
  {
    for (MetaUnitDTO dto : subunits)
    {
      map.put(base_name + dto.getUnique_name(), dto.getDesc());
      if (((dto instanceof MetaUnitDoubleDTO)) || ((dto instanceof MetaUnitIntegerDTO)))
      {
        TreeItem filt = new TreeItem();
        FilterRealDiapasonView filtSelectionSize = new FilterRealDiapasonView(dto.getDesc(), base_name, dto)
        {
          public FilterDiapasonDTO getFilterDTO() {
            FilterRealDiapasonDTO dt = new FilterRealDiapasonDTO(type_to_filter, this.base_name + this.dto.getUnique_name(), getFromValue(), getToValue());
            return dt;
          }

          public boolean isFilterUsed()
          {
            return (!this.from_value.getText().isEmpty()) || (!this.to_value.getText().isEmpty());
          }

		@Override
		public void clearFilter() {
			this.from_value.setText("");
			this.to_value.setText("");
		}
        };
        this.allFiltersRegister.add(filtSelectionSize);
        filt.setWidget(filtSelectionSize.asWidget());
        root.addItem(filt);
      }
      if ((dto instanceof MetaUnitStringDTO))
      {
        TreeItem filt = new TreeItem();
        FilterStringContainsView filtSelectionSize = new FilterStringContainsView(dto.getDesc(), base_name, dto)
        {
          public FilterMatchDTO getFilterDTO() {
            FilterMatchDTO dt = new FilterMatchDTO(type_to_filter, this.base_name + this.dto.getUnique_name(), "==", getValue());
            return dt;
          }

          public boolean isFilterUsed()
          {
            return !this.value.getText().isEmpty();
          }

		@Override
		public void clearFilter() {
			this.value.setText("");
		}
        };
        this.allFiltersRegister.add(filtSelectionSize);
        filt.setWidget(filtSelectionSize.asWidget());
        root.addItem(filt);
      }
      if ((dto instanceof MetaUnitDateDTO))
      {
        TreeItem filt = new TreeItem();
        FilterDataDiapasonView filtFieldStart = new FilterDataDiapasonView(dto.getDesc(), base_name, dto)
        {
          public FilterDiapasonDTO getFilterDTO() {
            FilterDateDiapasonDTO dt = new FilterDateDiapasonDTO(type_to_filter, this.base_name + this.dto.getUnique_name(), getFromValue(), getToValue());
            return dt;
          }

          public boolean isFilterUsed()
          {
            return (!this.from_value.getText().isEmpty()) || (!this.to_value.getText().isEmpty());
          }

		@Override
		public void clearFilter() {
			this.from_value.setText("");
			this.to_value.setText("");
			
		}
        };
        this.allFiltersRegister.add(filtFieldStart);
        filt.setWidget(filtFieldStart.asWidget());
        root.addItem(filt);
      }
      if ((dto instanceof MetaUnitMultivaluedStructureDTO))
      {
        MetaUnitMultivaluedStructureDTO ddto = (MetaUnitMultivaluedStructureDTO)dto;
        TreeItem filt = new TreeItem(ddto.getDesc());
        buildFiltersTree(filt, base_name + ddto.getUnique_name() + "_", ddto.getSub_meta_units(), map);
        root.addItem(filt);
      }
      if (!(dto instanceof MetaUnitMultivaluedEntityDTO))
        continue;
      MetaUnitMultivaluedEntityDTO ddto = (MetaUnitMultivaluedEntityDTO)dto;

      TreeItem filt_gen_selection = new TreeItem();
      FilterMultiMatchView filtGS = new FilterMultiMatchView(ddto.getDesc(), base_name, dto, ddto)
      {
        public FilterBaseDTO getFilterDTO() {
          ArrayList<FilterBaseDTO> filters = new ArrayList();
          ArrayList<String> variants = getVariants();
          int i = 0;
          for (String variant : variants)
          {
            CheckBox cb = (CheckBox)this.root.getChild(i).getWidget();
            if (cb.getValue().booleanValue())
            {
              FilterMatchDTO dt = new FilterMatchDTO(type_to_filter, this.base_name + this.dto.getUnique_name(), "==", variant);
              filters.add(dt);
            }
            i++;
          }
          FilterMultiDTO mult_dto = new FilterMultiDTO(filters);
          return mult_dto;
        }

        
        public void loadVariants(FilterMultiMatchView.PostProcess postprocess)
        {
          this.variants = this.ddto.getItem_names();
          postprocess.process();
        }

        public boolean isFilterUsed()
        {
          //return ((CheckBox)getRoot().getWidget()).getValue().booleanValue();
          int c = getRoot().getChildCount();
          for(int i = 0; i < c;i++)
          {
        	  TreeItem tr = getRoot().getChild(i);
        	  if (((CheckBox)tr.getWidget()).getValue().booleanValue()) return true;
          }
          return false;
        }


		@Override
		public void clearFilter() {
			int c = getRoot().getChildCount();
	          for(int i = 0; i < c;i++)
	          {
	        	  TreeItem tr = getRoot().getChild(i);
	        	  ((CheckBox)tr.getWidget()).setValue(false);
	          }
		}
        
      };
      filt_gen_selection.setWidget(filtGS.asWidget());
      this.allFiltersRegister.add(filtGS);

      root.addItem(filt_gen_selection);
    }
  }

  private String type_to_filter;
  private Tree tree = new Tree();
  private TreeItem root = new TreeItem();
  private Button cancel_btn;
  public RootFilterItemAdvanced(Place place,SimpleEventBus ev_bus,String type_to_filter,String filter_caption)
  {
    this.doFilterBtn = new Button("Фильтровать");
    this.cancel_btn = new Button("Сбросить");
    
    
    //this.doUseFilters = new TreeItem(filter_caption);
    this.place = place;
    this.bus = ev_bus;
    this.type_to_filter = type_to_filter;
    tree.setStyleName("research_section");
    tree.addItem(root);
    //root.setWidget(doUseFilters);
    root.setText(filter_caption);
    //add(this.doUseFilters);
    add(tree);
    
    this.setWidth("100%");
    this.setHeight("100%");
    
    
   // root.addItem()
    

    new RPCCall<MetaUnitMultivaluedEntityDTO>()
    {
      public void onFailure(Throwable caught)
      {
        Window.alert("Error fetching databank structure " + caught.getMessage());
      }

      protected void callService(AsyncCallback<MetaUnitMultivaluedEntityDTO> cb)
      {
        UserSocioResearchService.Util.getInstance().getDatabankStructure(RootFilterItemAdvanced.this.type_to_filter, cb);
      }

      
      public void onSuccess(MetaUnitMultivaluedEntityDTO result)
      {
        RootFilterItemAdvanced.this.buildFiltersTree(root,result.getUnique_name() + "_", result.getSub_meta_units(), RootFilterItemAdvanced.this.mapping);
        HorizontalPanel hr = new HorizontalPanel();
        hr.add(RootFilterItemAdvanced.this.doFilterBtn);
        hr.add(RootFilterItemAdvanced.this.cancel_btn);
        RootFilterItemAdvanced.this.add(hr);
        root.setState(true);
        RootFilterItemAdvanced.this.bindReactions();
      }
    }
    .retry(2);
  }

  private void bindReactions()
  {
    this.doFilterBtn.addClickHandler(new ClickHandler()
    {
      public void onClick(ClickEvent event) {
        RootFilterItemAdvanced.this.doFiltersProcess();
      }
    });
    this.cancel_btn.addClickHandler(new ClickHandler()
    {
      public void onClick(ClickEvent event) {
        RootFilterItemAdvanced.this.doFiltersClear();
      }
    });
    
  }

  private void doFiltersProcess()
  {
    ArrayList<FilterBaseDTO> filters = new ArrayList<FilterBaseDTO>();
    for (IFilterProvider provider : this.allFiltersRegister)
    {
      if (provider != null)	
      if (provider.isFilterUsed())
        filters.add(provider.getFilterDTO());
    }
    String filt_type = new String(type_to_filter);
    if(type_to_filter.equals("socioresearch")) filt_type="research";
    final String [] types_to_search = {filt_type};
    Set<String> s = new HashSet<String>(Arrays.asList(types_to_search));
    bus.fireEvent(new ShowPlaceEvent(place.getPlaceName(), "filter", new ShowFilterParameters(filters, s,mapping)));
  }
  private void doFiltersClear()
  {
    for (IFilterProvider provider : this.allFiltersRegister)
    {
      if (provider != null)	provider.clearFilter();
    }
   }
}