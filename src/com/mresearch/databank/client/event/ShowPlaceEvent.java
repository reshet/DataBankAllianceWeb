package com.mresearch.databank.client.event;

import java.util.ArrayList;

import com.google.gwt.event.shared.GwtEvent;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.shared.IShowPlaceParameters;

public class ShowPlaceEvent extends GwtEvent<ShowPlaceEventHandler> {
	public static Type<ShowPlaceEventHandler> TYPE = new Type<ShowPlaceEventHandler>();
	private String place,action;
	private IShowPlaceParameters params;
	public IShowPlaceParameters getParams() {
		return params;
	}
	public void setParams(IShowPlaceParameters params) {
		this.params = params;
	}
	public ShowPlaceEvent(String place,String action,IShowPlaceParameters params) {
		this.place = place;
		this.action = action;
		this.params = params;
	}
	@Override
	public Type<ShowPlaceEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowPlaceEventHandler handler) {
		handler.onShowPlace(this);
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
