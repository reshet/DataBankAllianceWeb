
package com.mresearch.databank.client.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.DateTimeFormatInfo;
import com.google.gwt.i18n.shared.impl.cldr.DateTimeFormatInfoImpl_ru;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.views.DBviewers.MultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedFieldViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedValuesViewer;
import com.mresearch.databank.client.views.DBviewers.NiceMultiValuedViewer;
import com.mresearch.databank.shared.MetaUnitMultivaluedEntityDTO;
import com.mresearch.databank.shared.PublicationDTO;
import com.sun.org.apache.xpath.internal.operations.And;

public class PublicationDetailedView extends Composite {

	private static PublicationDetailedViewUiBinder uiBinder = GWT
			.create(PublicationDetailedViewUiBinder.class);

	interface PublicationDetailedViewUiBinder extends
			UiBinder<Widget, PublicationDetailedView> {
	}

	//@UiField Label _name,_abstract,_number;
	//keywords,authors,date,date_accept,date_decline;
	//@UiField Hyperlink _enc_link;
	@UiField HorizontalPanel link_panel;
	@UiField Anchor show_meta;
	@UiField HTML html;
	@UiField Label name,date;
	//@UiField Label path;
	VerticalPanel elasticDBfieldNames = new VerticalPanel();
	public String arrToStr(ArrayList<String> arr)
	{
		String ans = "";
		for(String str:arr)
		{
			ans+=str+",";
		}
		if (ans.length() > 2)ans = ans.substring(0, ans.length()-2);
		return ans;
	}
	private PublicationDTO dto;
	private MetaUnitMultivaluedEntityDTO db;
	public PublicationDetailedView(PublicationDTO dto,MetaUnitMultivaluedEntityDTO dt,String path) {
		initWidget(uiBinder.createAndBindUi(this));
		//this._name.setText(dto.getHeader());
		//this._abstract.setText(dto.getContents());
		//this._number.setText(dto.getNumber());
		
		
		
		this.dto =  dto;
		this.db = dt;
		this.html.setHTML(dto.getContents()==null?"":dto.getContents());
		this.name.setText(dto.getHeader());
		String date_auth = "";
		if(dto.getFilling().containsKey("publication_authors"))date_auth = dto.getFilling().get("publication_authors");
		String date = "";
		if(dto.getFilling().containsKey("publication_date"))date = dto.getFilling().get("publication_date");
		//Locale locale = new Locale("ru","RU");
		//DateFormat full = DateFormat.getDateInstance(DateFormat.LONG, locale);
		//DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, new Locale("ru","RU"));
		String human_date = "";
		 //DateTimeFormatInfo formatRU = new DateTimeFormatInfoImpl_ru();
		
			if(date!=null && !date.equals(""))human_date = DateTimeFormat.getLongDateFormat().format(new Date(date));
		//String human_date = df.format(new Date(date));
		//String human_date = full.format();
		if(!human_date.equals("")&& !date_auth.equals(""))
			date_auth = date_auth+", "+human_date;
		else
			date_auth = date_auth+human_date;
		
		if(dto.getFilling().containsKey("publication_date"))this.date.setText(date_auth);
		
		
	//	this.path.setText(path);
//		authors.setText(arrToStr(dto.getAuthors()));
//		keywords.setText(arrToStr(dto.getKey_words()));
//		if (dto.getDate() != null)date.setText(dto.getDate().toString());
//		if (dto.getAccept_date() != null)date_accept.setText(dto.getAccept_date().toString());
//		if (dto.getDecline_date() != null)date_decline.setText(dto.getDecline_date().toString());
//		
		String realPath = GWT.getModuleBaseURL();
		
		//_enc_link.setTargetHistoryToken();
		if(dto.getEnclosure_key()!=null)link_panel.add(new HTML("<a href=\""+realPath+"serve?blob-key="+dto.getEnclosure_key()+"\">Скачать документ </a>"));
		//renderDBfillers();
		}
		
	
	
	@UiHandler(value="show_meta")
	public void onShowMeta(ClickEvent ev)
	{
		renderDBfillers();
	}
		private void renderDBfillers()
		{
			
			
			elasticDBfieldNames.clear();
			//elasticDBfieldValues.clear();
			
			
			if(dto.getFilling()== null)dto.setFilling(new HashMap<String, String>());
			
			ArrayList<String> exclude = new ArrayList<String>();
			exclude.add("contents");
			NiceMultiValuedViewer mv = new NiceMultiValuedViewer(true,db,dto.getFilling(),"",exclude);
			//NiceMultiValuedValuesViewer mv2 = new NiceMultiValuedValuesViewer(db,dto.getFilling(),"");
			
			elasticDBfieldNames.add(mv);
			//elasticDBfieldValues.add(mv2);
			
			
			HorizontalPanel h = new HorizontalPanel();
			h.setSpacing(6);
			h.add(elasticDBfieldNames);
			//h.add(elasticDBfieldValues);
			
			//MultiValuedFieldViewer mv = new MultiValuedFieldViewer(db,dto.getFilling(),"");

			
			final PopupPanel dialogBox = DialogBoxFactory.createDialogBox("Подробности публикации",h,null);
	        dialogBox.show();
		}
		
		
			
//	@UiHandler(value="_enc_link")
//	public void onEncLinkClick(ClickEvent e)
//	{
//		//_enc_link.set
//		Window.alert("Enc loaded!");
//	}

}
