package com.mresearch.databank.client.presenters;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mresearch.databank.client.DatabankApp;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

public abstract class UpdateUserHistoryActor{
	public UpdateUserHistoryActor(final UserResearchSettingDTO setting){
		new RPCCall<UserHistoryDTO>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error on updating account state!");
			}
	
			@Override
			public void onSuccess(UserHistoryDTO result) {
				DatabankApp.get().setCurrentUserHistory(result);
				doPostAction();
			}
	
			@Override
			protected void callService(AsyncCallback<UserHistoryDTO> cb) {
				UserHistoryDTO dt = DatabankApp.get().getCurrentUserHistory();
				dt.setCurrent_research(setting);
				DatabankApp.get().getUserService().updateResearchState(dt,cb);
				
			}
		}.retry(1);
		
	}
	public abstract void doPostAction();
}