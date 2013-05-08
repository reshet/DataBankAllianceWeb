package com.mresearch.databank.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

public interface UserAccountServiceAsync {

	void initDefaultUsers(AsyncCallback<Void> callback);

	void login(String email, String password, String token,
			AsyncCallback<UserAccountDTO> callback);

	void logout(AsyncCallback<Void> callback);

	void updateResearchState(UserHistoryDTO dto,
			AsyncCallback<UserHistoryDTO> callback);

	void getUserAllAnalisysList(AsyncCallback<List<UserAnalysisSaveDTO>> cb);

	void getMyResearchesList(AsyncCallback<List<SocioResearchDTO_Light>> cb);

	void saveResearchAnalisys(UserAnalysisSaveDTO dto, AsyncCallback<Void> cb);

	void addToSelectedResearches(SocioResearchDTO dt,
			AsyncCallback<Void> callback);

	void getResearchSetting(Long research_id,
			AsyncCallback<UserResearchSettingDTO> callback);

}
