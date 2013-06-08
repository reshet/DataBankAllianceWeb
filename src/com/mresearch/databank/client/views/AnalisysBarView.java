package com.mresearch.databank.client.views;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.event.RecalculateDistributionsEvent;
import com.mresearch.databank.client.event.RecalculateDistributionsEventHandler;
import com.mresearch.databank.client.event.SaveHTMLEvent;
import com.mresearch.databank.client.event.ShowPlaceEvent;
import com.mresearch.databank.client.event.ShowVarDetailsEvent;
import com.mresearch.databank.client.event.ShowVarPlotEvent;
import com.mresearch.databank.client.event.ShowVar2DDEvent;

import com.mresearch.databank.client.presenters.UserResearchPerspectivePresenter;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;
import com.mresearch.databank.shared.IPickableElement;
import com.mresearch.databank.shared.ShowResearchSavedParameters;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO.User2DD_Choice;
import com.mresearch.databank.shared.UserHistoryDTO;

import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;
import com.mresearch.databank.shared.VarDTO_Detailed;




public class AnalisysBarView extends Composite implements UserResearchPerspectivePresenter.AnalisysDisplay{

	
	private static AnalisysBarViewUiBinder uiBinder = GWT
			.create(AnalisysBarViewUiBinder.class);

	interface AnalisysBarViewUiBinder extends UiBinder<Widget, AnalisysBarView> {
	}
	private static final UserAccountServiceAsync srv = GWT.create(UserAccountService.class);
	//@UiField CheckBox weights_use,filters_use;
	@UiField Button filters_details_btn,save_btn,recalculate_btn;
	private Button filters_add_btn,filters_delete_btn;
	@UiField ToggleButton weights_use,filters_use;
	@UiField PushButton saveHtmlBtn,plotBtn,dim2Btn;
	@UiField ListBox weights_list;
	
	
	final private SimpleEventBus eventBus;
	private UserResearchPerspectivePresenter.Display display;
	private Long current_research_id = null;
	private int var_id = 0;
	private boolean w_use,f_use;
	private FormPanel form;
	private TextBox content = new TextBox();
	private TextBox save_name = new TextBox();
	
	String realPath = GWT.getModuleBaseURL();
	private UserAnalysisSaveDTO save_dto = null;
	private ArrayList<String> weights_names;
	private HTML_Saver saver;
	public User2DD_Choice user2dd_choice = User2DD_Choice.FREQ;
	
	public User2DD_Choice getUser2dd_choice() {
		return user2dd_choice;
	}

	public void setUser2dd_choice(User2DD_Choice user2dd_choice) {
		this.user2dd_choice = user2dd_choice;
	}






	private ArrayList<Long> weights_ids;
	public UserAnalysisSaveDTO getSave_dto() {
		return save_dto;
	}

	public void setSave_dto(UserAnalysisSaveDTO save_dto) {
		this.save_dto = save_dto;
	}

	public AnalisysBarView(SimpleEventBus bus,UserResearchPerspectivePresenter.Display display,UserAnalysisSaveDTO save_dto,HTML_Saver saver) {
		initWidget(uiBinder.createAndBindUi(this));
		if(DatabankApp.get().getCurrentUser().getId()!=0)save_btn.setVisible(true);
			else save_btn.setVisible(false);
		
		this.eventBus = bus;
		this.display = display;
		this.save_dto = save_dto;
		this.saver = saver;
		this.w_use = save_dto.getSeting().getWeights_use()==1?true:false;
		this.f_use = save_dto.getSeting().getFilters_use()==1?true:false;
		current_research_id = save_dto.getSeting().getResearh().getID();
		weights_names = save_dto.getSeting().getResearh().getVar_weight_names();
		weights_ids = save_dto.getSeting().getResearh().getVar_weight_ids();
		
		this.var_id = (int) save_dto.getVar_1().getId();
		filters_add_btn = new Button("+");
		filters_delete_btn = new Button("-");
		initSaveOption();
		bind();
	}
	
	
	
	private void initSaveOption()
	{
		content.setVisible(false);
		content.setName("content");
		save_name.setVisible(false);
		save_name.setName("name");
		
		form = new FormPanel();
		form.setAction(realPath+"htmlSave");
		form.setMethod(FormPanel.METHOD_POST);
		VerticalPanel panel = new VerticalPanel();
		panel.add(content);
		panel.add(save_name);
	
		form.add(panel);
	}
	private int getCurrentWeightId()
	{
		if(weights_ids==null) return 0;
		if(weights_ids.size()==0)return 0;
		//if(weights_list.getSelectedIndex()==0)return 0;
		return weights_ids.get(weights_list.getSelectedIndex()).intValue();
	}
	private String composeHTMLanalysis(){
		//String s;
		//s = new String(display.getCenterPanel().toString().getBytes());
		//return s;
		return saver.composeSpecificContent();
	}
	private void bind()
	{
		
		
			
		
		plotBtn.setStyleName("plotBtn");
		saveHtmlBtn.setStyleName("htmlBtn");
		weights_use.setStyleName("weightsBtn");
		filters_use.setStyleName("filtersBtn");
		dim2Btn.setStyleName("filters2Btn");
		
		plotBtn.setTitle("Показать график");
		saveHtmlBtn.setTitle("Сохранить распределение как html");
		weights_use.setTitle("Взвешивание");
		filters_use.setTitle("Использование фильтров");
		dim2Btn.setTitle("Построить двумерное распределение");
		
		weights_use.setDown(w_use);
		filters_use.setDown(f_use);
		weights_list.setVisible(w_use);
		dim2Btn.setVisible(true);
		
		plotBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				eventBus.fireEvent(new ShowVarPlotEvent(var_id));
			}
		});
		if(weights_names!=null)
		for(String str: weights_names)
		{
			weights_list.addItem(str);
		}
		if(weights_ids != null){
			int ind = weights_ids.indexOf(new Long(save_dto.getSeting().getWeights_var_id()));
			if(ind >= 0)weights_list.setSelectedIndex(ind);
		}
		
		recalculate_btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				//eventBus.
				save_dto.setUser2dd_choice(user2dd_choice);
				eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting(),save_dto));
			}
		});
		dim2Btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				
//				new RPCCall<UserResearchSettingDTO>() {
//
//					@Override
//					public void onFailure(Throwable arg0) {
//					}
//
//					@Override
//					public void onSuccess(UserResearchSettingDTO arg0) {
//						UserResearchSettingDTO ddt = arg0;
//						if(ddt  == null)ddt = new UserResearchSettingDTO();
//						//ddt
//						UserAnalysisSaveDTO save_dto = new UserAnalysisSaveDTO();
//						save_dto.setSeting(ddt);
//						ShowVar2DDEvent event = new ShowVar2DDEvent(DatabankApp.get().getCurrentUser().getCurrent_research());
//						event.setPre_saved(save_dto);
//						eventBus.fireEvent(event);
//				
//					}
//
//					@Override
//					protected void callService(
//							AsyncCallback<UserResearchSettingDTO> cb) {
//						srv.getResearchSetting(current_research_id, cb);
//					}
//				}.retry(2);
				save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
				save_dto.getSeting().setWeights_use(getWeightsUseState());
				//save_dto.setVar_1(save_dto.getVar2);
				//save_dto.setVar_2();
				ShowVar2DDEvent event = new ShowVar2DDEvent(save_dto.getSeting().getResearh().getID());
				event.setPre_saved(save_dto);
				eventBus.fireEvent(event);
			}
		});
		saveHtmlBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				//eventBus.fireEvent(new SaveHTMLEvent());
				
				final TextBox tb = new TextBox();
	        	Label l = new Label("Введите имя файла:");
	        	VerticalPanel vp = new VerticalPanel();
	        	vp.add(l);
	        	vp.add(tb);
	        	PopupPanel b = DialogBoxFactory.createDialogBox("Сохранение результатов анализа в HTML",vp,new DialogBoxFactory.closeAction(){
	        		@Override
	        		public void doAction(){
	        			String nam = tb.getText();
	        			if (nam.equals("")) nam = "cохраненное распределение.html";
	        			final String name = nam;
	        			save_name.setText(name);
	        	
	        			content.setText(composeHTMLanalysis());
	    				
	        			form.submit();
	    			
	        			//save_dto.setName(name);
	        			//save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
	    				//save_dto.getSeting().setWeights_use(getWeightsUseState());
	        			//User;
	        		}
	        	},"Сохранить");
	        	b.show();
	       }
		});
	
		save_btn.addClickHandler(new ClickHandler()
		 {
	        public void onClick(ClickEvent ev)
	        {
	        	final TextBox tb = new TextBox();
	        	Label l = new Label("Введите имя");
	        	VerticalPanel vp = new VerticalPanel();
	        	vp.add(l);
	        	vp.add(tb);
	        	PopupPanel b = DialogBoxFactory.createDialogBox("Сохранение результатов анализа",vp,new DialogBoxFactory.closeAction(){
	        		@Override
	        		public void doAction(){
	        			String nam = tb.getText();
	        			if (nam.equals("")) nam = "cохраненное распределение";
	        			final String name = nam;
	        			save_dto.setUser2dd_choice(user2dd_choice);
	        			save_dto.setName(name);
	        			save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
	    				save_dto.getSeting().setWeights_use(getWeightsUseState());
	        			//User;
	        			new RPCCall<Void>()
	        			{
	        				@Override
	        				public void onFailure(Throwable arg0) {
	        				}
	        				@Override
	        				public void onSuccess(Void arg0) {
	        					PopupPanel b = DialogBoxFactory.createDialogBox("Сохранение результатов анализа",new Label("Результаты анализа успешно сохранены!"),null,"ОК");
	        					b.show();
	        				}
	        				@Override
	        				protected void callService(AsyncCallback<Void> cb) {
	        					srv.saveResearchAnalisys(save_dto,cb);
	        				}
	        			}.retry(2);
	        			
	        		}
	        	},"Сохранить");
	        	b.show();
	        }
		 });
				

		this.getWeightsUse().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//UserAccountDTO dto = DatabankApp.get().getCurrentUser();
				//dto.setWeights_use(getWeightsUseState());
				weights_list.setVisible(weights_use.getValue());
				save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
				save_dto.getSeting().setWeights_use(getWeightsUseState());
				
				//weights_list.setEnabled(weights_use.getValue());
				
				//eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting()));
				
				//weights_list.setEnabled(weights_use.getValue());
				//DatabankApp.get().updateUserAccountState();
				
				//rise event!
				//dto.setFilters_use(display.getFiltersUseState());
			}
		});
		weights_list.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent ev) {
				
				save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
				save_dto.getSeting().setWeights_use(getWeightsUseState());
				//save_dto.getSeting().setWeights_var_id(getCurrentWeightId());
				//eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting()));
			}
		});
		getFiltersUse().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//UserAccountDTO dto = DatabankApp.get().getCurrentUser();
				//dto.setFilters_use(getFiltersUseState());
				save_dto.getSeting().setFilters_use(getFiltersUseState());
				save_dto.getSeting().setWeights_var_id(new Integer(getCurrentWeightId()));
				save_dto.getSeting().setWeights_use(getWeightsUseState());
				eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting(),save_dto));
//				new RPCCall<UserAccountDTO>() {
//					
//					@Override
//					public void onFailure(Throwable caught) {
//						Window.alert("Error on updating account state!");
//					}
//		
//					@Override
//					public void onSuccess(UserAccountDTO result) {
//						DatabankApp.get().setCurrentUser(result);
//						current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
//						
//					}
//		
//					@Override
//					protected void callService(AsyncCallback<UserAccountDTO> cb) {
//						DatabankApp.get().getUserService().updateResearchState(DatabankApp.get().getCurrentUser(),cb);
//					}
//				}.retry(2);
//			
			
				//rise event!
							//dto.setFilters_use(display.getFiltersUseState());
			}
		});
		getFiltersDetailesBtn().addClickHandler(new ClickHandler() {
			final PopupPanel ppanel = new PopupPanel(true);
			
			@Override
			public void onClick(ClickEvent event) {
				ppanel.clear();
				VerticalPanel panel = new VerticalPanel();
				
				HorizontalPanel hpan = new HorizontalPanel();	
				
				ppanel.setPopupPosition(event.getClientX(), event.getClientY());
				ArrayList<IPickableElement> elems = new ArrayList<IPickableElement>();
				
//				final ArrayList<String> filters = DatabankApp.get().getCurrentUser().getFilters(current_research_id);
//				for(final String filter:filters)
//				{
//					elems.add(new IPickableElement() {
//						@Override
//						public String getTextRepresent() {
//							return filter;
//						}
//						
//						@Override
//						public String getID() {
//							return String.valueOf(filters.indexOf(filter));
//						}
//					});
//				}
//				//ArrayList<String> used_elems = new ArrayList<String>();
				//current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
				//final ArrayList<String> filters = DatabankApp.get().getCurrentUser().getFilters();
				//final ArrayList<Long> filter_categs = DatabankApp.get().getCurrentUser().getFilters_categories();
				final ArrayList<String> filters = save_dto.getSeting().getFilters();
				int i = 0;
				for(final String filter:filters)
				{
						final int inter = i;
						elems.add(new IPickableElement() {
							@Override
							public String getTextRepresent() {
								return filter;
							}
							
							@Override
							public long getID() {
								return inter;
							}
						});
					i++;
				}
				
				panel.add(new PickElementsTableView(elems, save_dto.getSeting().getFiltersToProcessIndecies(), new IPickBinder() {
					@Override
					public void processPickChoice(ArrayList<Long> selected_keys) {
						ArrayList<Integer> usage = new ArrayList<Integer>();
						ArrayList<String> filters = save_dto.getSeting().getFilters();
						for(String touse:filters)
						{
							usage.add(new Integer(0));
						}
						for(Long sel_index:selected_keys)
						{
							Integer index = sel_index.intValue();
							usage.set(index, new Integer(1));
						}
						save_dto.getSeting().setFilters_usage(usage);
						//DatabankApp.get().getCurrentUser().setFilters_usage(usage,current_research_id);
						
						eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting(),save_dto));
						ppanel.hide();
						
//						new RPCCall<UserAccountDTO>() {
//							
//							@Override
//							public void onFailure(Throwable caught) {
//								Window.alert("Error on updating account state!");
//							}
//				
//							@Override
//							public void onSuccess(UserAccountDTO result) {
//								DatabankApp.get().setCurrentUser(result);
//								current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
//							
//							}
//				
//							@Override
//							protected void callService(AsyncCallback<UserAccountDTO> cb) {
//								DatabankApp.get().getUserService().updateResearchState(DatabankApp.get().getCurrentUser(),cb);
//							}
//						}.retry(2);
						
					}
					
					@Override
					public String getCommandName() {
						return "Применить!";
					}

					@Override
					public String getTitle() {
						return "Доступные фильтры:";
					}
				}));
				
				hpan.add(filters_add_btn);
				hpan.add(filters_delete_btn);
				panel.add(hpan);
				ppanel.add(panel);
				ppanel.show();
			}
		});
		getFiltersAddBtn().addClickHandler(new ClickHandler() {
			final PopupPanel panel = new PopupPanel(true);
			@Override
			public void onClick(ClickEvent event) {
				HorizontalPanel hor = new HorizontalPanel();
				panel.clear();
				panel.center();
				Label l = new Label("Для создания фильтра используйте код переменной из массива, " +
						"знаки сравнения ==,<=,>=,<> со значениями альтернатив. Код переменной отделяйте пробелом." +
						"Сложные фильтры можно составлять используя скобочные выражения (), логические операторы \"И\" &&, \"ИЛИ\" ||.");
				l.setWordWrap(true);
				panel.add(l);
				final TextBox t = new TextBox();
				t.setSize("500px", "40px");
				Button doFilter = new Button("Добавить фильтр!");
				doFilter.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						panel.hide();
						//current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
						//final ArrayList<String> filters = DatabankApp.get().getCurrentUser().getFilters();
						//final ArrayList<Long> filter_categs = DatabankApp.get().getCurrentUser().getFilters_categories();
						//final ArrayList<Integer> filter_usage = DatabankApp.get().getCurrentUser().getFilters_usage();
						current_research_id = save_dto.getSeting().getResearh().getId();
						final ArrayList<String> filters = save_dto.getSeting().getFilters();
						final ArrayList<Integer> filter_usage = save_dto.getSeting().getFilters_usage();
						
						filters.add(t.getText());
						filter_usage.add(0);
						eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting(),save_dto));
						
						//filters_details_btn.fireEvent();
//						new RPCCall<UserAccountDTO>() {
//							
//							@Override
//							public void onFailure(Throwable caught) {
//								Window.alert("Error on updating account state!");
//							}
//				
//							
//							@Override
//							public void onSuccess(UserAccountDTO result) {
//								DatabankApp.get().setCurrentUser(result);
//								current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
//								Window.alert("Filter created:"+t.getText());
//							}
//				
//							@Override
//							protected void callService(AsyncCallback<UserAccountDTO> cb) {
//								DatabankApp.get().getUserService().updateResearchState(DatabankApp.get().getCurrentUser(),cb);
//							}
//						}.retry(2);
						
					}
				});
				
				hor.add(t);
				hor.add(doFilter);
				VerticalPanel pp = new VerticalPanel();
				pp.add(l);
				pp.add(hor);
				panel.add(pp);
				
				panel.show();
			}
		});
		getFiltersDeleteBtn().addClickHandler(new ClickHandler() {
			final PopupPanel panel = new PopupPanel(true);
			@Override
			public void onClick(ClickEvent event) {
				panel.clear();
				panel.setPopupPosition(event.getClientX(), event.getClientY());
				ArrayList<IPickableElement> elems = new ArrayList<IPickableElement>();
				//current_research_id = DatabankApp.get().getCurrentUser().getCurrent_research();
				//final ArrayList<String> filters = DatabankApp.get().getCurrentUser().getFilters();
				current_research_id = save_dto.getSeting().getResearh().getID();
				final ArrayList<String> filters = save_dto.getSeting().getFilters();
				int i = 0;
				for(final String filter:filters)
				{
						final int inter = i;
						elems.add(new IPickableElement() {
							@Override
							public String getTextRepresent() {
								return filter;
							}
							
							@Override
							public long getID() {
								return inter;
							}
						});
					i++;
				}
				//DatabankApp.get().getCurrentUser().getFiltersToProcess(current_research_id)
				panel.add(new PickElementsTableView(elems,new ArrayList<Long>() , new IPickBinder() {
					@Override
					public void processPickChoice(ArrayList<Long> selected_keys) {
						//ArrayList<String> filters = DatabankApp.get().getCurrentUser().getFilters();
						//ArrayList<Integer> filters_usage = DatabankApp.get().getCurrentUser().getFilters_usage();
						ArrayList<String> filters = save_dto.getSeting().getFilters();
						ArrayList<Integer> filters_usage = save_dto.getSeting().getFilters_usage();
						
						for(Long index:selected_keys)
						{
								filters.remove(index.intValue());
								filters_usage.remove(index.intValue());
						}
						eventBus.fireEvent(new RecalculateDistributionsEvent(save_dto.getSeting(),save_dto));
						panel.hide();
					}
					
					@Override
					public String getCommandName() {
						return "Удалить!";
					}

					@Override
					public String getTitle() {
						return "Доступные фильтры:";
					}
				}));
				panel.show();			
			}
		});

	}
	
	@UiHandler(value="weights_use")
	public void onWeightsUseClick(ClickEvent c)
	{
		if(weights_use.getValue()) weights_use.setTitle("Взвешивание включено");
			else weights_use.setTitle("Взвешивание отключено");
	}
	
	@UiHandler(value="filters_use")
	public void onFiltersUseClick(ClickEvent c)
	{
		if(filters_use.getValue()) filters_use.setTitle("Фильтры подключены");
			else filters_use.setTitle("Фильтры отключены");
	}
	
	@Override
	public HasClickHandlers getWeightsUse() {
		return weights_use;
	}
	@Override
	public HasClickHandlers getFiltersUse() {
		return filters_use;
	}
	@Override
	public HasClickHandlers getFiltersDetailesBtn() {
		return filters_details_btn;
	}
	@Override
	public Integer getWeightsUseState() {
		return weights_use.getValue()?1:0;
	}
	@Override
	public Integer getFiltersUseState() {
		return filters_use.getValue()?1:0;
	}
	@Override
	public HasClickHandlers getFiltersAddBtn() {
		// TODO Auto-generated method stub
		return filters_add_btn;
	}

	
	
	
	
	
	@Override
	public HasClickHandlers getFiltersDeleteBtn() {
		// TODO Auto-generated method stub
		return filters_delete_btn;
	}

}
