package com.mplatforma.amr.service.remote;

import java.util.List;

import javax.ejb.Remote;

import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.StartupBundleDTO;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

@Remote
public interface UserAccountBeanRemote {

    UserAccountDTO getUserAccount(String email, String password);
    UserHistoryDTO getUserResearchHistory(long user_id,long research_id);
    UserAccountDTO getDefaultUser();   
    UserAccountDTO updateAccountResearchState(UserAccountDTO dto);
    void initDefaults();
    StartupBundleDTO getStartupContent();
    void addToSelectedResearches(UserResearchSettingDTO dto,long user_id);
    void saveResearchAnalisys(UserAnalysisSaveDTO dto,long user_id);

    public List<SocioResearchDTO_Light> getUserMyResearches(long user_id);
    public UserResearchSettingDTO getUserResearchSetting(Long id,long user_id);
    public List<UserAnalysisSaveDTO> getUserAllAnalisys(long user_id);
    public List<UserAnalysisSaveDTO> getUserResearchAnalisys(Long research_id,long user_id);
    
}
