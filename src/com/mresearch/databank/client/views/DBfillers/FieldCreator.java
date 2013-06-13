package com.mresearch.databank.client.views.DBfillers;

import javax.xml.crypto.Data;

import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.AdminSocioResearchService;
import com.mresearch.databank.client.service.AdminSocioResearchServiceAsync;
import com.mresearch.databank.shared.JSON_Representation;
import com.mresearch.databank.shared.MetaUnitDTO;
import com.mresearch.databank.shared.MetaUnitDateDTO;
import com.mresearch.databank.shared.MetaUnitDoubleDTO;
import com.mresearch.databank.shared.MetaUnitFileDTO;
import com.mresearch.databank.shared.MetaUnitIntegerDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.MetaUnitMultivaluedStructureDTO;
import com.mresearch.databank.shared.MetaUnitStringDTO;

import org.omg.CORBA.DATA_CONVERSION;
import org.zenika.widget.client.datePicker.DatePicker;

public class FieldCreator extends Composite {

	private static FieldCreatorUiBinder uiBinder = GWT
			.create(FieldCreatorUiBinder.class);

	interface FieldCreatorUiBinder extends
			UiBinder<Widget, FieldCreator> {
	}

	public FieldCreator() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	private AdminSocioResearchServiceAsync service = AdminSocioResearchService.Util.getInstance();
	private MultiValuedField field;
	private FieldEditor editor;
	private PopupPanel par;
	//private MetaUnitFiller new_field;
	@UiField TextBox unique_name,field_name;
	@UiField ListBox field_type;
	@UiField CheckBox multiselection_en;
	public FieldCreator(FieldEditor editor,PopupPanel parent) {
		initWidget(uiBinder.createAndBindUi(this));
		this.editor = editor;
		this.field = editor.getField();
		this.par = parent;
		initFields();
	}
	private void initFields()
	{
		field_type.addItem("Текстовое поле");
		field_type.addItem("Целое числовое поле");
		field_type.addItem("Рациональное числовое поле");
		field_type.addItem("Поле выбора даты");
		field_type.addItem("Поле файла");
		field_type.addItem("Cтруктурный тег");	
		field_type.addItem("Cущность");	
	}
	@UiHandler(value="doAdd") 
	public void doAdd(ClickEvent ev)
	{
		MetaUnitDTO dto;
		String val = field_type.getItemText(field_type.getSelectedIndex());
		if(val.equals("Текстовое поле"))
		{
			dto = new MetaUnitStringDTO(new Long(0), field_name.getText(), unique_name.getText());
			addFieldToDB(dto);
		}
		if(val.equals("Целое числовое поле"))
		{
			dto = new MetaUnitIntegerDTO(new Long(0), field_name.getText(), unique_name.getText());
			addFieldToDB(dto);
		}
		if(val.equals("Рациональное числовое поле"))
		{
			dto = new MetaUnitDoubleDTO(new Long(0), field_name.getText(), unique_name.getText());
			addFieldToDB(dto);
		}
		if(val.equals("Поле выбора даты"))
		{
			dto = new MetaUnitDateDTO(new Long(0), field_name.getText(), unique_name.getText());
			addFieldToDB(dto);
		}
		if(val.equals("Поле файла"))
		{
			dto = new MetaUnitFileDTO(new Long(0), field_name.getText(), unique_name.getText());
			addFieldToDB(dto);
		}
		if(val.equals("Cтруктурный тег"))
		{
			dto = new MetaUnitMultivaluedStructureDTO(new Long(0), field_name.getText(), unique_name.getText());;
			addFieldToDB(dto);
		}
		if(val.equals("Cущность"))
		{
			dto = new MetaUnitMultivaluedEntityDTO(new Long(0), field_name.getText(), unique_name.getText());;
			((MetaUnitMultivaluedEntityDTO)dto).setIsMultiselected(multiselection_en.getValue());
			//w = new MultiValuedEntity((MetaUnitMultivaluedEntityDTO)dto, null, null,field.getUniqueName());
			addFieldToDB(dto);
		}
		par.hide();
	}
	private void addFieldToDB(final MetaUnitDTO dto_init)
	{
		new RPCCall<MetaUnitDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Ошибка добавления поля!"+caught.getMessage());
			}
			@Override
			public void onSuccess(MetaUnitDTO dto) {
				
				Widget w = null;
				if(dto instanceof MetaUnitStringDTO)
				{
					w = new SimpleStringField((MetaUnitStringDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitIntegerDTO)
				{
					w = new SimpleIntegerField((MetaUnitIntegerDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitDoubleDTO)
				{
					w = new SimpleDoubleField((MetaUnitDoubleDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitDateDTO)
				{
					w = new SimpleDateField((MetaUnitDateDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitFileDTO)
				{
					w = new SimpleFileField((MetaUnitFileDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitMultivaluedStructureDTO)
				{
					w = new MultiValuedField((MetaUnitMultivaluedStructureDTO)dto, null, null,field.getUniqueName());
				}else
				if(dto instanceof MetaUnitMultivaluedEntityDTO)
				{
					((MetaUnitMultivaluedEntityDTO)dto).setIsMultiselected(multiselection_en.getValue());
					if(multiselection_en.getValue())
					{
						w = new MultiValuedEntityMultiselected((MetaUnitMultivaluedEntityDTO)dto, null, null,field.getUniqueName());
					}else
					{
						w = new MultiValuedEntity((MetaUnitMultivaluedEntityDTO)dto, null, null,field.getUniqueName(),null);
					}
					
					//w = new MultiValuedEntity((MetaUnitMultivaluedEntityDTO)dto, null, null,field.getUniqueName());
				}
				field.dto.getSub_meta_units().add(dto);
				field.subunits_table.setWidget(field.subunits_table.getRowCount(), 0, w);
				editor.addNewField(dto);
				
			}

			@Override
			protected void callService(AsyncCallback<MetaUnitDTO> cb) {
				service.addMetaUnit(dto_init, field.dto.getId(), cb);
			}
		}.retry(2);
	}
	@UiHandler(value="doCancel") 
	public void doCancel(ClickEvent ev)
	{
		par.hide();
	}
}
