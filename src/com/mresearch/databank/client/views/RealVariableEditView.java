
package com.mresearch.databank.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.opendatafoundation.data.spss.mod.SPSSUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.ShowVarPlotEvent;
import com.mresearch.databank.client.event.ShowVarPlotEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.AdminResearchPerspectivePresenter;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.views.DBfillers.VarMultiValuedField;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.VarMultiValuedFieldViewer;
import com.mresearch.databank.shared.ComparativeSearchParamsDTO;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.RealVarDTO_Detailed;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;

public class RealVariableEditView extends Composite implements HTML_Saver{

	private static RealVariableDetailedViewUiBinder uiBinder = GWT
			.create(RealVariableDetailedViewUiBinder.class);

	interface RealVariableDetailedViewUiBinder extends
			UiBinder<Widget, RealVariableEditView> {
	}
	
	@UiField FlexTable to_generalizeTbl;
	@UiField VerticalPanel elasticDBfields;
	@UiField Button confirmBtn,generalizeBtn;
	@UiField DoubleBox param_box;
	@UiField Label dates,gen_geath,sel_size,org_impl,tag;
	@UiField Label varText,number_of_records,moda_txt,median_txt,average_txt,dispersion_txt,dispersion_root_txt;
	@UiField FlexTable generalizedTbl;
//	@UiField HorizontalPanel target_panel;
	//@UiField HTMLPanel content_panel;
	

	private MetaUnitMultivaluedEntityDTO db;
	private VarDTO_Detailed dto;
	
	private VarMultiValuedField mv;
	private AdminResearchPerspectivePresenter.Display displ;
	private ArrayList<VarDTO_Detailed> vars_to_generalize;

	@UiField HTMLPanel main_html;
	@UiField VerticalPanel research_link;

	
	
	public RealVariableEditView(RealVarDTO_Detailed dto,MetaUnitMultivaluedEntityDTO dt,AdminResearchPerspectivePresenter.Display display) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		this.db = dt;
		UserAccountDTO user = DatabankApp.get().getCurrentUser();
		
		setResearchMeta();
		param_box.setValue(2.0);
		if(dto.getFilling() == null) dto.setFilling(new HashMap<String, String>());
		//analysis_bar.add(new AnalisysBarView(bus, display,save_Dto,this));
		
		//target_panel.add(new SaveHTMLAddon(content_panel));
		//varCode.setText(dto.getCode());
		varText.setWidth("400px");
		varText.setWordWrap(true);
		varText.setText(dto.getCode()+" "+dto.getLabel());
		number_of_records.setText(String.valueOf(dto.getNumber_of_records()));
		research_link.add(new ResearchDescItemView(new SocioResearchDTO_Light(dto.getResearch_id(),dto.getResearch_name())));
		int i = 0;
		NumberFormat formatter = NumberFormat.getFormat("0.0");
        // formatter.
         //formatter.setMaximumFractionDigits(2);
        // String myNumber = formatter.format(new Double(dto.getDistribution().get(i)/total)*100);
		//codeSchemeTbl.insertCell(beforeRow, beforeColumn)
		moda_txt.setText(formatter.format(dto.getModa()));
		median_txt.setText(formatter.format(dto.getMediana()));
		average_txt.setText(formatter.format(dto.getAverage()));
		dispersion_txt.setText(formatter.format(dto.getDispersion()));
		dispersion_root_txt.setText(formatter.format(dto.getRoot_dispersion()));
						
       
//		bus.addHandler(ShowVarPlotEvent.TYPE, new ShowVarPlotEventHandler() {
//			@Override
//			public void onShowVarPlot(ShowVarPlotEvent event) {
//				if(RealVariableDetailedView.this.dto.getId() == event.getVar_id())showPlot();
//			}
//		});
		
		
		
		
		
		renderGeneralizedVars();
		renderDBfillers();
		confirmBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				
				@SuppressWarnings("unused")
				JSON_Representation json = mv.getJSON();
				mv.populateItemsLinksTo(RealVariableEditView.this.dto.getId(), "sociovar");
				HashMap<String, String> mapp =mv.returnCollectedMap();
				RealVariableEditView.this.dto.setFilling(mapp);
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
						AdminSocioResearchService.Util.getInstance().updateVar(RealVariableEditView.this.dto, cb);
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
								RealVariableEditView.this.dto = res;
								renderGeneralizedVars();
							}

							@Override
							protected void callService(AsyncCallback<VarDTO_Detailed> cb) {
								UserSocioResearchService.Util.getInstance().getVarDetailed(RealVariableEditView.this.dto.getId(), cb);
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
	

	@Override
	public String composeSpecificContent() {
		return main_html.toString();
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
