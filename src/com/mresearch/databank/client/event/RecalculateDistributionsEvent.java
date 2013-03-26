package com.mresearch.databank.client.event;

import java.util.ArrayList;

import com.google.gwt.event.shared.GwtEvent;
import com.mresearch.databank.shared.CatalogConceptDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

public class RecalculateDistributionsEvent extends GwtEvent<RecalculateDistributionsEventHandler> {
	public static Type<RecalculateDistributionsEventHandler> TYPE = new Type<RecalculateDistributionsEventHandler>();
	private UserResearchSettingDTO setting;
	private UserAnalysisSaveDTO var_anal;
	public UserResearchSettingDTO getSetting() {
		return setting;
	}
	public void setSetting(UserResearchSettingDTO setting) {
		this.setting = setting;
	}
	public RecalculateDistributionsEvent(UserResearchSettingDTO dto,UserAnalysisSaveDTO var_anal)
	{
		this.setting = dto;
		this.var_anal = var_anal;
	}
	@Override
	public Type<RecalculateDistributionsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RecalculateDistributionsEventHandler handler) {
		handler.onRecalculateDistributions(this);
	}
	public UserAnalysisSaveDTO getVar_anal() {
		return var_anal;
	}
	public void setVar_anal(UserAnalysisSaveDTO var_anal) {
		this.var_anal = var_anal;
	}

	
}
