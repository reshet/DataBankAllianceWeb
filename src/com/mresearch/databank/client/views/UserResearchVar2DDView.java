package com.mresearch.databank.client.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.RecalculateDistributionsEvent;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.client.service.UserSocioResearchServiceAsync;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO.User2DD_Choice;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.VarDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.mresearch.databank.shared.VarDTO_Light;
//There MVP pattern is ommited)
public class UserResearchVar2DDView extends Composite implements HTML_Saver{

	private static UserResearchVar2DDViewUiBinder uiBinder = GWT
			.create(UserResearchVar2DDViewUiBinder.class);
	private UserSocioResearchServiceAsync service = GWT.create(UserSocioResearchService.class);
	interface UserResearchVar2DDViewUiBinder extends
			UiBinder<Widget, UserResearchVar2DDView> {
	}
	
	
	private long research_id;
	@UiField ListBox var1_lbox,var2_lbox;
	@UiField Button build_btn;
	@UiField FlexTable the2DD_table;
	@UiField HTML msg;
	//@UiField HTMLPanel percents_type_pnl;
	@UiField RadioButton show_percents,show_frequences,percents_colomn,percents_row,percents_table;
	private ArrayList<String> var_names;
	private ArrayList<Long> var_ids;
	
	private VarDTO var1,var2;
	private ArrayList<Double> distribution;
	//@UiField HorizontalPanel target_panel;
	@UiField HTMLPanel content_panel;
	@UiField VerticalPanel selected_vars;
	@UiField HorizontalPanel analysis_bar;
	private AnalisysBarView anal_bar_w;
	private UserAnalysisSaveDTO pre_saved;
	private long curr_res = 0;
	public UserResearchVar2DDView(final long research_id,SimpleEventBus bus,UserResearchPerspectivePresenter.Display display, UserAnalysisSaveDTO pre_saved)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.pre_saved = pre_saved;
		this.pre_saved.setDistr_type(pre_saved.DISTR_TYPE_2D);
		curr_res = research_id;
		//DatabankApp.get().getCurrentUser().setCurrent_research(research_id);
		
		//DatabankApp.get().updateUserAccountState();
		//UserAccountDTO user = DatabankApp.get().getCurrentUser();
		anal_bar_w = new AnalisysBarView(bus, display,this.pre_saved,this);
		analysis_bar.add(anal_bar_w);
		
		User2DD_Choice ch = pre_saved.getUser2dd_choice();
		if(ch == User2DD_Choice.FREQ) showFreqChoice();
		else{
			//show_percents.setValue(true);
			showPercentsChoice();
			if(ch == User2DD_Choice.PERC_COL) percentsColomnChoice();
			if(ch == User2DD_Choice.PERC_ROW) percentsRowChoice();
			if(ch == User2DD_Choice.PERC_ALL) percentsTableChoice();
		}
		
		
		new RPCCall<UserHistoryDTO>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on updating account state!");
			}

			@Override
			public void onSuccess(UserHistoryDTO result) {
				DatabankApp.get().setCurrentUserHistory(result);
				//target_panel.add(new SaveHTMLAddon(content_panel));
				var1_lbox.addItem("Загрузка переменных...");
				var2_lbox.addItem("Загрузка переменных...");
				UserResearchVar2DDView.this.research_id = research_id;
				fetchVarsList();
			}

			@Override
			protected void callService(AsyncCallback<UserHistoryDTO> cb) {
				DatabankApp.get().getUserService().updateResearchState(DatabankApp.get().getCurrentUserHistory(),cb);
			}
		}.retry(2);
		
		
	}
	@UiHandler(value="build_btn")
	public void onBuildBtnClick(ClickEvent e)
	{
		the2DD_table.clear();
		the2DD_table.removeAllRows();
		//the2DD_table.setWidget(0,0, );
		msg.setHTML("<h2 class=\"green\">Идет построение распределения. Подождите пожалуйста...</h2>");
		fetchSelectedVarDTOs();
	}
	private void showPercentsChoice()
	{
		show_frequences.setValue(false);
		show_percents.setValue(true);
		percents_colomn.setVisible(true);
		percents_row.setVisible(true);
		percents_table.setVisible(true);
		User2DD_Choice ch = User2DD_Choice.PERC_COL;
		if(percents_colomn.getValue())ch =  User2DD_Choice.PERC_COL;
		else if(percents_row.getValue())ch =  User2DD_Choice.PERC_ROW;
		else if(percents_table.getValue())ch =  User2DD_Choice.PERC_ALL;
		anal_bar_w.setUser2dd_choice(ch);		
	}
	@UiHandler(value="show_percents")
	public void onShowPercentsChoice(ClickEvent e)
	{
		showPercentsChoice();
		fill2DDtable(var1, var2, distribution);
		//percents_type_pnl.setVisible(true);
	}
	private void showFreqChoice()
	{
		percents_colomn.setVisible(false);
		percents_row.setVisible(false);
		percents_table.setVisible(false);
		show_percents.setValue(false);
		show_frequences.setValue(true);
		anal_bar_w.setUser2dd_choice(User2DD_Choice.FREQ);
	}
	@UiHandler(value="show_frequences")
	public void onShowFrequencesChoice(ClickEvent e)
	{
		showFreqChoice();
		fill2DDtable(var1, var2, distribution);
	//	percents_type_pnl.setVisible(false);
	}
	private void percentsColomnChoice()
	{
		percents_colomn.setValue(true);
		percents_row.setValue(false);
		percents_table.setValue(false);
		anal_bar_w.setUser2dd_choice(User2DD_Choice.PERC_COL);
	}
	@UiHandler(value="percents_colomn")
	public void onPercentsColomnChoice(ClickEvent e)
	{
		percentsColomnChoice();
		fill2DDtable(var1, var2, distribution);
	}
	private void percentsRowChoice()
	{
		percents_row.setValue(true);
		percents_colomn.setValue(false);
		percents_table.setValue(false);
		anal_bar_w.setUser2dd_choice(User2DD_Choice.PERC_ROW);
	}

	@UiHandler(value="percents_row")
	public void onPercentsRowChoice(ClickEvent e)
	{
		percentsRowChoice();
		fill2DDtable(var1, var2, distribution);
	}
	private void percentsTableChoice()
	{
		percents_table.setValue(true);
		percents_colomn.setValue(false);
		percents_row.setValue(false);
		anal_bar_w.setUser2dd_choice(User2DD_Choice.PERC_ALL);
	}
	@UiHandler(value="percents_table")
	public void onPercentsTableChoice(ClickEvent e)
	{
		percentsTableChoice();
		fill2DDtable(var1, var2, distribution);
	}
	private void fetchVarsList()
	{
		new RPCCall<ArrayList<VarDTO_Light>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on getting vars dtos:"+caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<VarDTO_Light> result) {
				fillListsWithVarDTOs(result);
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<VarDTO_Light>> cb) {
				
				service.getResearchVarsSummaries(UserResearchVar2DDView.this.research_id, cb);
			}
		}.retry(2);
	}
	private void fillListsWithVarDTOs(ArrayList<VarDTO_Light> arr)
	{
		var1_lbox.clear();
		var2_lbox.clear();
		if(var_ids == null)var_ids = new ArrayList<Long>();
		if(var_names == null)var_names = new ArrayList<String>();
		var_ids.clear();
		var_names.clear();
		
		for(VarDTO_Light dto:arr)
		{
			var_ids.add(dto.getId());
			var_names.add(dto.getLabel());
			var1_lbox.addItem(dto.getCode()+"::"+dto.getLabel());
			var2_lbox.addItem(dto.getCode()+"::"+dto.getLabel());
		}
		
		if(pre_saved.getVar_1()!= null || pre_saved.getVar_2()!=null)
		{
			int index1=0,index2=0;
			int i = 0;
			for(VarDTO_Light dto:arr)
			{
				if (pre_saved.getVar_1()!= null && dto.getLabel().equals(pre_saved.getVar_1().getLabel())) index1 = i;
				if (pre_saved.getVar_2()!=null && dto.getLabel().equals(pre_saved.getVar_2().getLabel())) index2 = i;
				i++;
			}
			var1_lbox.setItemSelected(index1, true);
			var2_lbox.setItemSelected(index2, true);
		}
		if(pre_saved.getDistribution()!=null){
			fill2DDtable(pre_saved.getVar_1(), pre_saved.getVar_2(), pre_saved.getDistribution());
		}else
		{
			if(var1_lbox.getSelectedIndex()!=0&&var2_lbox.getSelectedIndex()!=0)fetchSelectedVarDTOs();
		}
	}
	private void fill2DDtable(VarDTO var1,VarDTO var2,ArrayList<Double> distr)
	{
		
		
		selected_vars.clear();
		msg.setHTML("");
		selected_vars.add(new HTML("<h3 class=\"green\">Распределение "+var1.getCode()+"</h3>"+"<p class=\"gray25\">"+ var1.getLabel()+"</p>"));
		selected_vars.add(new HTML("<h3 class=\"green\">Относительно "+var2.getCode()+"</h3>"+"<p class=\"gray25\">"+ var2.getLabel()+"</p>"));
		
		Double total = new Double(0);
		ArrayList<Double> total1 = new ArrayList<Double>();
		ArrayList<Double> total2 = new ArrayList<Double>();
		double[][] table_distr = new double[var1.getV_label_codes().size()][var2.getV_label_codes().size()];
		
		int frame_size = var1.getV_label_codes().size();
		int frames_col = var2.getV_label_codes().size();
		int col = 0;
		for(int i = 0; i < distr.size();i+=frame_size)
		{
			int kol = 0;
			double total1elem = 0;
			//table_distr[col] = new double[frames_col];
			for(int j = i;j<i+frame_size;j++)
			{
				table_distr[kol++][col] = distr.get(j);
				total1elem+=distr.get(j);
			}
			col++;
			total+=total1elem;
			total1.add(total1elem);
		}
		for(int i = 0; i < frame_size;i++)
		{
			double total2elem = 0;
			for(int j = 0; j < frames_col;j++)
			{
				total2elem+=table_distr[i][j];
			}		
			total2.add(total2elem);
		}
				
		
		the2DD_table.clear();
		//the2DD_table.setBorderWidth(2);
		the2DD_table.setWidget(0, 0, new Label(var1.getCode()+"/"+var2.getCode()));
		for(int i = 0; i < var1.getV_label_values().size();i++)
		{
			the2DD_table.setWidget(i+1, 0, new Label(var1.getV_label_values().get(i)));
		}
		for(int i = 0; i < var2.getV_label_values().size();i++)
		{
			the2DD_table.setWidget(0, i+1, new Label(var2.getV_label_values().get(i)));
		}
		
		NumberFormat formatter = NumberFormat.getFormat("0.0");
		//NumberFormat formatter_round = NumberFormat.getFormat("0");
        
		if(show_frequences.getValue() == true)
		{
			for(int i = 1; i < var1.getV_label_values().size()+1;i++)
			{
				//Double freq2_i = var2.getDistribution().get(i-1)/total2;
				for(int j = 1; j < var2.getV_label_values().size()+1;j++)
				{ 
					//Double freq_ij = (var1.getDistribution().get(j-1)/total1)*freq2_i;
					String myNumber = String.valueOf(Math.round(table_distr[i-1][j-1]));       		
					the2DD_table.setWidget(i, j, new Label(myNumber));
				}	
			}
		}else
		if(show_percents.getValue() == true)
		{
			if(percents_row.getValue() == true)
			{
				for(int i = 1; i < var1.getV_label_values().size()+1;i++)
				{
					//Double freq2_i = var2.getDistribution().get(i-1)/total2;
					for(int j = 1; j < var2.getV_label_values().size()+1;j++)
					{ 
						//Double freq_ij = (var1.getDistribution().get(j-1)/total1)*freq2_i;
						double value = 0;
						if(total2.get(i-1) > 0) value = table_distr[i-1][j-1]/total2.get(i-1)*100;
						
						String myNumber = formatter.format(value);       		
						the2DD_table.setWidget(i, j, new Label(myNumber+"%"));
					}	
				}
			}else
			if(percents_colomn.getValue() == true)
			{
				for(int i = 1; i < var1.getV_label_values().size()+1;i++)
				{
					//Double freq2_i = var2.getDistribution().get(i-1)/total2;
					for(int j = 1; j < var2.getV_label_values().size()+1;j++)
					{ 
						//Double freq_ij = (var1.getDistribution().get(j-1)/total1)*freq2_i;
						double value = 0;
						if(total1.get(j-1) > 0) value = table_distr[i-1][j-1]/total1.get(j-1)*100;
						String myNumber = formatter.format(value);       		
						the2DD_table.setWidget(i, j, new Label(myNumber+"%"));
					}	
				}
			}else
			if(percents_table.getValue() == true)
			{
				for(int i = 1; i < var1.getV_label_values().size()+1;i++)
				{
					//Double freq2_i = var2.getDistribution().get(i-1)/total2;
					for(int j = 1; j < var2.getV_label_values().size()+1;j++)
					{ 
						//Double freq_ij = (var1.getDistribution().get(j-1)/total1)*freq2_i;
						double value = 0;
						if(total > 0) value = table_distr[i-1][j-1]/total*100;
						String myNumber = formatter.format(value);       		
						the2DD_table.setWidget(i, j, new Label(myNumber+"%"));
					}	
				}
			}		
		}
		
	}
	private void process2DDbuilding(final VarDTO var1,final VarDTO var2)
	{
		this.var1 = var1;
		this.var2 = var2;
		
		//apply weights and filters
		 // formatter.
          //formatter.setMaximumFractionDigits(2);
		new RPCCall<ArrayList<Double>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error building 2dd distr:"+caught.getMessage());
				
			}
			@Override
			public void onSuccess(ArrayList<Double> result) {
				UserResearchVar2DDView.this.distribution = result;
				anal_bar_w.getSave_dto().setDistribution(result);
				VarDTO_Detailed v1det = new VarDTO_Detailed();
				VarDTO_Detailed v2det = new VarDTO_Detailed();
				v1det.setDistribution(var1.getDistribution());
				v1det.setCode(var1.getCode());
				v1det.setLabel(var1.getLabel());
				v1det.setId(var1.getId());
				v1det.setResearch_id(var1.getResearch_id());
				v1det.setV_label_codes(var1.getV_label_codes());
				v1det.setV_label_values(var1.getV_label_values());
				

				v2det.setDistribution(var2.getDistribution());
				v2det.setCode(var2.getCode());
				v2det.setLabel(var2.getLabel());
				v2det.setId(var2.getId());
				v2det.setResearch_id(var2.getResearch_id());
				v2det.setV_label_codes(var2.getV_label_codes());
				v2det.setV_label_values(var2.getV_label_values());
				
				
				
				
				anal_bar_w.getSave_dto().setVar_1(v1det);
				anal_bar_w.getSave_dto().setVar_2(v2det);
				anal_bar_w.getSave_dto().setDistr_type(UserAnalysisSaveDTO.DISTR_TYPE_2D);
				
				fill2DDtable(UserResearchVar2DDView.this.var1, UserResearchVar2DDView.this.var2, distribution);
			}

			@Override
			protected void callService(AsyncCallback<ArrayList<Double>> cb) {
			       service.get2DDistribution(var1.getId(), var2.getId(),curr_res, cb);
			}
		}.retry(2);
		
		//default faked&wrong table filling
//		for(int i = 1; i < var2.getV_label_values().size()+1;i++)
//		{
//			Double freq2_i = var2.getDistribution().get(i-1)/total2;
//			for(int j = 1; j < var1.getV_label_values().size()+1;j++)
//			{ 
//				Double freq_ij = (var1.getDistribution().get(j-1)/total1)*freq2_i;
//				String myNumber = formatter.format(freq_ij*100);       		
//				the2DD_table.setWidget(j, i, new Label(myNumber+"%"));
//			}	
//		}
	
 		
	}
	private void fetchSelectedVarDTOs()
	{	
		final long var1_id = var_ids.get(var1_lbox.getSelectedIndex());
		final long var2_id = var_ids.get(var2_lbox.getSelectedIndex());
		new RPCCall<VarDTO>() {
			private VarDTO var1 = null;
			private VarDTO var2 = null;
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error fethcing first var:"+caught.getMessage());
			}

			@Override
			public void onSuccess(VarDTO result) {
				var1 = result;
				new RPCCall<VarDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error fethcing first var:"+caught.getMessage());
					}

					@Override
					public void onSuccess(VarDTO result) {
						var2 = result;
						process2DDbuilding(var1, var2);
					}
					@Override
					protected void callService(AsyncCallback<VarDTO> cb) {
						service.getVar(var2_id, cb);
					}
				}.retry(2);

			}

			@Override
			protected void callService(AsyncCallback<VarDTO> cb) {
				service.getVar(var1_id, cb);
			}
		}.retry(2);
	}
	@Override
	public String composeSpecificContent() {
		StringBuilder ans = new StringBuilder();
			ans.append("<h3>Распределение "+var1_lbox.getItemText(var1_lbox.getSelectedIndex())+"</h3>");
			ans.append("<h3>Относительно "+var2_lbox.getItemText(var2_lbox.getSelectedIndex())+"</h3>");
		ans.append("</br>");
		if(show_frequences.getValue()) ans.append("<p>Частоты</p></br>");
		else{
			if(percents_colomn.getValue()) ans.append("<p>Проценты по столбцам</p></br>");
			if(percents_row.getValue()) ans.append("<p>Проценты по строкам</p></br>");
			if(percents_table.getValue()) ans.append("<p>Проценты по всей таблице</p></br>");
		}
		ans.append("</br>");
		ans.append(the2DD_table.toString());
		return ans.toString();
	}
}
