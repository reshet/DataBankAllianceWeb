
package com.mresearch.databank.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;

public class ShowVar2DDEvent extends GwtEvent<ShowVar2DDEventHandler> {
	public static Type<ShowVar2DDEventHandler> TYPE = new Type<ShowVar2DDEventHandler>();
	private Long Res_id;  
	private UserAnalysisSaveDTO pre_saved;
	public UserAnalysisSaveDTO getPre_saved() {
		return pre_saved;
	}
	public void setPre_saved(UserAnalysisSaveDTO pre_saved) {
		this.pre_saved = pre_saved;
	}
	public ShowVar2DDEvent(long res_id) {
		this.Res_id = res_id;
		DatabankApp.get().getCurrentUser().setCurrent_research(res_id);
	}
	public ShowVar2DDEvent(long res_id,UserAnalysisSaveDTO dto) {
		this(res_id);
		this.pre_saved = dto;
	}
	@Override
	public Type<ShowVar2DDEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowVar2DDEventHandler handler) {
		handler.onShowVar2DD(this);
	}

	public long getRes_id() {
		return Res_id;
	}

}
