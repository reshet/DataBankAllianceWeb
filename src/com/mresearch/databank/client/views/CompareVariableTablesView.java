package com.mresearch.databank.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Legend.Align;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.PieDataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.ShowVarPlotEvent;
import com.mresearch.databank.client.event.ShowVarPlotEventHandler;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.presenters.UpdateUserHistoryActor;
import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.UserSocioResearchService;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;
import com.smartgwt.client.types.ChartType;

public class CompareVariableTablesView extends Composite {

	private static VariableDetailedViewUiBinder uiBinder = GWT
			.create(VariableDetailedViewUiBinder.class);

	interface VariableDetailedViewUiBinder extends
			UiBinder<Widget, CompareVariableTablesView> {
	}

	@UiField FlexTable compareTbl;
	@UiField HTMLPanel main_html;
	private MetaUnitMultivaluedEntityDTO db;
	private List<Long> dtos_detailed_ids;
	private AnalisysBarView anal_bar_w;
	private final UserResearchPerspectivePresenter.Display display;
	int i = 0;
	
	public CompareVariableTablesView(List<Long> dtos_detailed_ids,UserResearchPerspectivePresenter.Display display)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.dtos_detailed_ids = dtos_detailed_ids;
		this.display = display;
		final UserResearchSettingDTO setting = DatabankApp.get().getCurrentUserHistory().getCurrent_research();
		final UserResearchSettingDTO pre_setting = setting;
		
		for(final Long dto_id:dtos_detailed_ids)
        {
		   new RPCCall<VarDTO_Detailed>() {
			@Override
			public void onFailure(Throwable arg0) {
				new UpdateUserHistoryActor(pre_setting){
					@Override
					public void doPostAction() {
					}
				};
			}
			@Override
			public void onSuccess(VarDTO_Detailed dto) {
				   compareTbl.setWidget(i+1, 0, new CompareVariableTableItemView(dto, db, CompareVariableTablesView.this.display, new UserAnalysisSaveDTO()));
				   i++;
				   new UpdateUserHistoryActor(pre_setting){
						@Override
						public void doPostAction() {
						}
					};
			}
			@Override
			protected void callService(final AsyncCallback<VarDTO_Detailed> cb) {
				//switch off filters and weights for a while
				if(setting!=null){
					setting.setFilters_use(0);
					setting.setWeights_use(0);
					new UpdateUserHistoryActor(setting){
						@Override
						public void doPostAction() {
							UserSocioResearchService.Util.getInstance().getVarDetailed(dto_id, cb);
						}
					};
				}
			}
		   }.retry(1);
        }
	}
	
	
	
//	boolean plot_viewed = false;
//	private void showPlot()
//	{
//		if(!plot_viewed)
//		{
//			plot_viewed = true;
//			
////			ChartWidget widg = new ChartWidget();
////			JSONObject obj_json = new JSON_Construct(dto).getGraph();
////			String json = obj_json.toString();
////			widg.setJsonData(json);
////			widg.setPixelSize(600, 600);
////			
//			
//			//StringUtils.wordWrap(dto.getLabel(),50,Locale.getDefault());
//			double dum = 0;
//			for(Double d:dto.getDistribution())dum+=d;
//			Chart chart = new Chart()
//			   .setType(Series.Type.PIE)
////			   .setChartTitleText(wrap(dto.getLabel(),70))
//	
//			   .setChartTitle(new ChartTitle().setText(wrap(dto.getLabel(),70)).setAlign(org.moxieapps.gwt.highcharts.client.ChartTitle.Align.LEFT))
//			   .setSizeToMatchContainer()
//			   .setMarginRight(10)
////			   .setWidth100()
////			   .setHeight100()
//////			    .setLegend(new Legend()
////				      .setAlign(Legend.Align.RIGHT)
////				      .setVerticalAlign(Legend.Align.)
////				      .setBackgroundColor("#CCCCCC")
////				      .setShadow(true)
////				   )
////				
//				   .setPlotBorderWidth(null)  
//            .setPlotShadow(true)  
//            .setPiePlotOptions(new PiePlotOptions()  
//                .setAllowPointSelect(true)  
//                .setCursor(PlotOptions.Cursor.POINTER)  
//                .setPieDataLabels(new PieDataLabels()  
//                    .setEnabled(false)  
//                )  
//                .setShowInLegend(true)  
//            )  
//            .setToolTip(new ToolTip()  
//                .setFormatter(new ToolTipFormatter() {  
//                    public String format(ToolTipData toolTipData) {  
//                        return "<b>" + toolTipData.getPointName() + "</b>: " + toolTipData.getYAsDouble() + " %";  
//                    }  
//                })  
//            )   
//				   ;
//			
//			Point [] points = new Point[dto.getV_label_codes().size()];
//			int i = 0;
//			for(String name:dto.getV_label_values())
//			{
//				  NumberFormat formatter = NumberFormat.getFormat("0.0");
//		          // formatter.
//		           //formatter.setMaximumFractionDigits(2);
//				  Double d = new Double(0);
//			      if(dto.getDistribution()!=null && dto.getDistribution().get(i)!=null)
//				  {
//				      d = dto.getDistribution().get(i);
//				  }
//				  double ddd = (d/dum)*100;
//				  String st = formatter.format(ddd);
//				  if(st.contains(","))st = st.replaceAll(",", ".");
//		           double myNumber = Double.parseDouble(st);
//		         
//		           
//				points[i] = (new Point(name,myNumber));
//				i++;
//			}
//			
//			points[0].setSelected(true).setSliced(true);
////			new Point[]{  
////	                new Point("Firefox", 45.0),  
////	                new Point("IE", 26.8),  
////	                new Point("Chrome", 12.8)  
////	                    .setSliced(true)  
////	                    .setSelected(true),  
////	                new Point("Safari", 8.5),  
////	                new Point("Opera", 6.2),  
////	                new Point("Others", 0.7)  
////	            } 
////		   )
//			Series series = chart.createSeries()
//					   .setName("Варианты ответа")
//					   .setPoints(points);
//					chart.addSeries(series);
//			
//			final PopupPanel dialogBox = createDialogBox(chart);
//		    dialogBox.setGlassEnabled(true);
//		    dialogBox.setAnimationEnabled(true);
//		    
//			dialogBox.center();
//	        dialogBox.show();
//		}
//
//	}
//	
//	private PopupPanel createDialogBox(Widget w) {
//	    // Create a dialog box and set the caption text
//	    final PopupPanel dialogBox = new PopupPanel();
//	    //dialogBox.setText("График распределения");
//	    dialogBox.setHeight("450px");
//	    dialogBox.setWidth("700px");
////	    dialogBox.setHeight("100%");
////	    dialogBox.setWidth("100%");
//	    // Create a table to layout the content
//	    VerticalPanel dialogContents = new VerticalPanel();
//	    dialogContents.setWidth("100%");
//	    dialogContents.setHeight("100%");
//	    
//	    dialogContents.setSpacing(4);
//	    
//	 
//	    // Add some text to the top of the dialog
//	   
//
//	    // Add a close button at the bottom of the dialog
//	    Button closeButton = new Button("Закрыть", new ClickHandler() {
//	          public void onClick(ClickEvent event) {
//	            dialogBox.hide();
//	            plot_viewed = false;
//	          }
//	        });
//	    dialogContents.add(w);
//	    dialogContents.add(closeButton);
//	    dialogBox.setWidget(dialogContents);
//	    return dialogBox;
//	    
//	    
//	  }
}
