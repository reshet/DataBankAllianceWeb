package com.mresearch.databank.client.views;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.opendatafoundation.data.spss.mod.SPSSUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.AdminResearchPerspectivePresenter;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.views.DBfillers.VarMultiValuedField;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.VarMultiValuedFieldViewer;
import com.mresearch.databank.shared.ComparativeSearchParamsDTO;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO_Light;
import com.rednels.ofcgwt.client.ChartWidget;

public class VariableEditView extends Composite {

	private static VariableDetailedViewUiBinder uiBinder = GWT
			.create(VariableDetailedViewUiBinder.class);

	interface VariableDetailedViewUiBinder extends
			UiBinder<Widget, VariableEditView> {
	}

	@UiField Label varText;
	@UiField FlexTable codeSchemeTbl,generalizedTbl,to_generalizeTbl;
	@UiField HTMLPanel main_html;
//	@UiField Button save_html_btn;
	//@UiField HorizontalPanel save_pnl;
	//@UiField VerticalPanel graph_pnl;
	@UiField VerticalPanel elasticDBfields;
	@UiField Button confirmBtn,generalizeBtn;
	@UiField DoubleBox param_box;
	
	//@UiField Label concept_name,concept_value;
	@UiField Label dates,gen_geath,sel_size,org_impl,tag;
//@UiField HorizontalPanel analysis_bar;
	@UiField VerticalPanel research_link;
//	Hyperlink link;
//	private FormPanel form;
//	private TextBox hidden_box;
//	private TextBox hidden_box2;
//	private VarDTO_Detailed dto;
//	@UiHandler(value="save_html_btn")
//	public void save_html(ClickEvent e)
//	{
//		String s = main_html.toString();
//		hidden_box.setText(s);
//		hidden_box2.setText("DB_saved_var_distr_"+dto.getCode());
//		form.submit();
//		//s = URL.encode(s);
//		//save_pnl.clear();
//		//save_pnl.add(new HTML("<a href=\"/databank/htmlSave?tosave="+s+"\" target=\"_blank\">Скачать файл!</a>"));
//	}
	
	
	
	
	
	private VarMultiValuedField mv;
	private MetaUnitMultivaluedEntityDTO db;
	private VarDTO_Detailed dto;
	private AdminResearchPerspectivePresenter.Display displ;
	private ArrayList<VarDTO_Detailed> vars_to_generalize;
	public VariableEditView(AdminResearchPerspectivePresenter.Display  displ,final VarDTO_Detailed dto,MetaUnitMultivaluedEntityDTO dt) {
		initWidget(uiBinder.createAndBindUi(this));
		this.displ= displ;
		this.db = dt;
		this.dto = dto;
		setResearchMeta();
		param_box.setValue(2.0);
		if(dto.getFilling() == null) dto.setFilling(new HashMap<String, String>());
		//form.a
		//save_pnl.add(new SaveHTMLAddon(main_html));
		
		//varCode.setText(dto.getCode());
		varText.setText(dto.getCode()+" "+ dto.getLabel());
		ArrayList<Double> codes = dto.getV_label_codes();
		ArrayList<String> values = dto.getV_label_values();
		
		int i = 0;
		codeSchemeTbl.setSize("600px", "350px");
		codeSchemeTbl.setWidget(0, 0, new Label("Код"));
		codeSchemeTbl.setWidget(0, 1, new Label("Текст альтернативы"));
		codeSchemeTbl.setWidget(0, 2, new Label("Частота"));
		//codeSchemeTbl.insertCell(beforeRow, beforeColumn)
		Double total = new Double(0);
		
		for(Double cnt:dto.getDistribution())
		{
			total+=cnt;
		}
        for(Double key:codes)
        {
 		   String label = values.get(i);
           codeSchemeTbl.setWidget(i+1, 0, new Label(key.toString()));
           codeSchemeTbl.setWidget(i+1, 1, new Label(label.toString()));
           
           //NumberFormat formatter = NumberFormat.getInstance();
           NumberFormat formatter = NumberFormat.getFormat("0.00");
          // formatter.
           //formatter.setMaximumFractionDigits(2);
           String myNumber = formatter.format(new Double(dto.getDistribution().get(i)/total)*100);
           
          // new NumberFormat();
           codeSchemeTbl.setWidget(i+1, 2, new Label(myNumber+"%"));
           //com.google.gwt.user.client.ui.MultiWordSuggestOracle c = new MultiWordSuggestOracle();
           //c.requestSuggestions(request, callback)
           //com.google.gwt.user.client.ui.
           i++;
        }
        
      
		
		
		
		renderGeneralizedVars();
		renderDBfillers();
		confirmBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				
				@SuppressWarnings("unused")
				JSON_Representation json = mv.getJSON();
				mv.populateItemsLinksTo(VariableEditView.this.dto.getId(), "sociovar");
				HashMap<String, String> mapp =mv.returnCollectedMap();
				VariableEditView.this.dto.setFilling(mapp);
				new RPCCall<Void>() {

					@Override
					public void onFailure(Throwable arg0) {
					}

					@Override
					public void onSuccess(Void arg0) {
						Window.alert("Переменная успешно обновлена!");
					}

					@Override
					protected void callService(AsyncCallback<Void> cb) {
						AdminSocioResearchService.Util.getInstance().updateVar(VariableEditView.this.dto, cb);
					}
				}.retry(2);
			}
		});
		
		
		generalizeBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				
				new RPCCall<Void>() {
					@Override
					public void onFailure(Throwable arg0) {
						Window.alert("Ошибка генерализации!");
					}
					@Override
					public void onSuccess(Void res) {
						
						Window.alert("Переменные генерализированы успешно!");
						to_generalizeTbl.clear();
						generalizeBtn.setVisible(false);
						new RPCCall<VarDTO_Detailed>() {

							@Override
							public void onFailure(Throwable arg0) {
							
							}

							@Override
							public void onSuccess(VarDTO_Detailed res) {
								VariableEditView.this.dto = res;
								renderGeneralizedVars();
							}

							@Override
							protected void callService(AsyncCallback<VarDTO_Detailed> cb) {
								UserSocioResearchService.Util.getInstance().getVarDetailed(VariableEditView.this.dto.getId(), cb);
							}
						}.retry(1);
					}
					@Override
					protected void callService(AsyncCallback<Void> cb) {
						AdminSocioResearchService.Util.getInstance().generalizeVars(getVarsToGeneralizeSelection(), cb);
						//(dto.getId(),getVarsToGeneralizeSelection() , cb);
					}
				}.retry(2);
			}
		});
	}

	private void setResearchMeta()
	{
		research_link.add(new ResearchDescItemView(new SocioResearchDTO_Light(dto.getResearch_id(),dto.getResearch_name())));
		HashMap<String, String> mapa = dto.getResearch_meta_filling();
		String dates1 = "",dates2="",dates="";
		if(mapa.containsKey("socioresearch_sel_size"))sel_size.setText(mapa.get("socioresearch_sel_size"));
		if(mapa.containsKey("socioresearch_gen_geath"))gen_geath.setText(mapa.get("socioresearch_gen_geath"));
		
		if(mapa.containsKey("socioresearch_dates_start_date"))dates1=mapa.get("socioresearch_dates_start_date");
		if(mapa.containsKey("socioresearch_dates_end_date"))dates2=mapa.get("socioresearch_dates_end_date");
		if(!dates1.equals("")&&!dates2.equals(""))dates=dates1+" - "+dates2; 
		else dates=dates1+dates2;
		this.dates.setText(dates);
		
		if(mapa.containsKey("socioresearch_orgs_org_impl_organization"))org_impl.setText(mapa.get("socioresearch_orgs_org_impl_organization"));
		if(mapa.containsKey("socioresearch_tag"))tag.setText(mapa.get("socioresearch_tag"));
		
	}
	private void renderGeneralizedVars()
	{
		  generalizedTbl.setSize("600px", "350px");
			for(int j = 0; j < dto.getGen_var_names().size();j++)
			{
				generalizedTbl.setWidget(j, 0, new Label("как"));
				generalizedTbl.setWidget(j, 1, new VarLink(dto.getGen_vars_ids().get(j),dto.getGen_var_names().get(j)));
				generalizedTbl.setWidget(j, 2, new Label("в исследовании"));
				generalizedTbl.setWidget(j, 3, new ResearchLink(dto.getGen_research_ids().get(j),dto.getGen_research_names().get(j)));
			}
		
	}
	private void renderDBfillers()
	{
		elasticDBfields.clear();
		db.setDesc("Метаданные переменной");
		 mv = new VarMultiValuedField(db,null,dto.getFilling(),"");
		elasticDBfields.add(mv);
	}
	
	@UiHandler(value="back_btn")
	public void back_action(ClickEvent e)
	{
		//History.back();
		if(displ.getPrevCenterState()!=null){
			displ.getCenterPanel().clear();
			displ.getCenterPanel().add(displ.getPrevCenterState().asWidget());
		}
	}
	@UiHandler(value="searchBtn")
	public void onCompSearchDo(ClickEvent ev)
	{
		new RPCCall<ArrayList<VarDTO_Detailed>>() {
			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("Ошибка поиска похожих переменных!");
			}
			@Override
			public void onSuccess(ArrayList<VarDTO_Detailed> res) {
				vars_to_generalize = res;
				fillToGeneralizeTbl();
			}
			@Override
			protected void callService(AsyncCallback<ArrayList<VarDTO_Detailed>> cb) {
				ComparativeSearchParamsDTO dt = new ComparativeSearchParamsDTO();
				dt.setBarrier_variance(param_box.getValue());
				AdminSocioResearchService.Util.getInstance().findVarsLikeThis(dto.getId(),dt, cb);
			}
		}.retry(2);
	}
	
	private void fillToGeneralizeTbl()
	{
		to_generalizeTbl.clear();
		if(vars_to_generalize==null)return;
		to_generalizeTbl.setSize("600px", "350px");
		if(vars_to_generalize.size()>0)generalizeBtn.setVisible(true);
		else {
			generalizeBtn.setVisible(false);
			to_generalizeTbl.setWidget(0, 0, new HTML("<h3 class=\"green\">По запросу в банке не найдено схожих переменных.</h3>"));
		}
		for(int j = 0; j < vars_to_generalize.size();j++)
		{
			to_generalizeTbl.setWidget(j, 0, new CheckBox("идентична"));
			ResearchDescItemView rw = new ResearchDescItemView(new SocioResearchDTO_Light(vars_to_generalize.get(j).getResearch_id(),
					vars_to_generalize.get(j).getResearch_name()));
			to_generalizeTbl.setWidget(j, 2, rw);
			to_generalizeTbl.setWidget(j, 1, new VarLink(vars_to_generalize.get(j).getId(),vars_to_generalize.get(j).getLabel()));
			//to_generalizeTbl.setWidget(j, 2, new Label(", из исследования"));
			//to_generalizeTbl.setWidget(j, 3, new ResearchLink(res.get(j).,dto.getGen_research_names().get(j)));
		}
	}
	private ArrayList<Long> getVarsToGeneralizeSelection()
	{
		ArrayList<Long> arr = new  ArrayList<Long>();
		for(int j = 0; j < vars_to_generalize.size();j++)
		{
			CheckBox cb = (CheckBox)to_generalizeTbl.getWidget(j,0);
			if(cb.getValue())arr.add(vars_to_generalize.get(j).getId());
		}
		return arr;
	}
}
