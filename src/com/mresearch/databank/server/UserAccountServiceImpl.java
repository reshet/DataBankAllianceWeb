package com.mresearch.databank.server;


import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.catalina.session.PersistentManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.shared.SocioResearchDTO;
import com.mresearch.databank.shared.SocioResearchDTO_Light;
import com.mresearch.databank.shared.UserAccountDTO;
import com.mresearch.databank.shared.UserAnalysisSaveDTO;
import com.mresearch.databank.shared.UserHistoryDTO;
import com.mresearch.databank.shared.UserResearchSettingDTO;

@SuppressWarnings("serial")
public class UserAccountServiceImpl extends RemoteServiceServlet implements
    UserAccountService {

	
	private static UserAccountBeanRemote eao;
	static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		  //final String jndiName = "java:global/DatabankEnterpriseAlliance-ejb/UserAccountRemoteBean";
		  final String jndiName = "java:global/DatabankEnterprise-ejb/UserAccountRemoteBean";
			 obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  eao = (UserAccountBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static UserSocioResearchBeanRemote seao;
	static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		  final String jndiName = "java:global/DatabankEnterprise-ejb/UserSocioResearchRemoteBean";
		  obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  seao = (UserSocioResearchBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
  public UserAccountDTO login(String email, String password,String token) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null)
	{
		return userAcc;
	}
	
	UserAccountDTO account;
	account = eao.getUserAccount(email, password);
	if (account == null)
	{
		if(token!=null && !token.isEmpty()){
			 account = eao.getUserAccountOrRegisterByOAuthToken(token);
		}else{
			UserAccountDTO acc = eao.getDefaultUser();
			this.getThreadLocalRequest().getSession().setAttribute("user", acc);
			return acc;
		}
	}
	this.getThreadLocalRequest().getSession().setAttribute("user", account);
	return account;

// return UserAccount.toDTO(UserAccount.getDefaultUser());
//	  return UserAccount.toDTO(UserAccount.getDefaultResearchAdmin());	  
//	  return UserAccount.toDTO(UserAccount.getDefaultLawAdmin());	  
//	  return UserAccount.toDTO(UserAccount.getDefaultJuryConsultant());	  
//	  return UserAccount.toDTO(UserAccount.getDefaultSuperAdmin());	  

  }
  
  
  public void initDefaultUsers()
  {
	  eao.initDefaults();
  }

@Override
public void logout() {
	this.getThreadLocalRequest().getSession().removeAttribute("user");
	this.getThreadLocalRequest().getSession().removeAttribute("user_history");
}




@Override
public UserHistoryDTO updateResearchState(UserHistoryDTO dto_acc) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null)
	{
		//UserHistoryDTO userHist =   (UserHistoryDTO) this.getThreadLocalRequest().getSession().getAttribute("user_history");
		
		if (userAcc.getId() != 0)
		{
			UserHistoryDTO dto;
			dto = eao.updateAccountResearchState(dto_acc,userAcc.getId());	
			this.getThreadLocalRequest().getSession().setAttribute("user_history", dto);
			System.out.println("user id == "+userAcc.getId());
			return dto;
		}
		else
		{
			if(dto_acc.getCurrent_research()!=null && dto_acc.getCurrent_research().getResearh()!=null)
			if(dto_acc.getCurrent_research().getResearh().getID() == 0 && dto_acc.getCurrant_var()!=null && dto_acc.getCurrant_var().getVar_1()!=null
				&&  dto_acc.getCurrant_var().getVar_1().getId() != 0)
			{
				dto_acc.getCurrent_research().getResearh().setId(
						seao.getVar(dto_acc.getCurrant_var().getVar_1().getId(), null,null).getResearch_id());
			}
			//UserHistoryDTO dto;
			
			//dto = eao.updateAccountResearchState(dto_acc);
			this.getThreadLocalRequest().getSession().setAttribute("user_history", dto_acc);
			System.out.println("user id == 0");
			return dto_acc;
		}
	}
	System.out.println("useracc == null");
	return dto_acc;
}


@Override
public void addToSelectedResearches(SocioResearchDTO dt) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null && userAcc.getId() != 0)
	{
		UserResearchSettingDTO dto = new UserResearchSettingDTO();
		dto.setResearh(dt);
		eao.addToSelectedResearches(dto, userAcc.getId());
	}
}



@Override
public void saveResearchAnalisys(UserAnalysisSaveDTO dto) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null && userAcc.getId() != 0)
	{
		eao.saveResearchAnalisys(dto, userAcc.getId());
	}
}


@Override
public List<SocioResearchDTO_Light> getMyResearchesList() {
	List<SocioResearchDTO_Light> list= new ArrayList<SocioResearchDTO_Light>();
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	//if (userAcc != null && userAcc.getId() != 0)
	if (userAcc != null && userAcc.getId() != 0)
	{
		list = eao.getUserMyResearches(userAcc.getId());
	}
	return list;
}


@Override
public UserResearchSettingDTO getResearchSetting(Long research_id) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null && userAcc.getId() != 0)
	{
		UserHistoryDTO hist = (UserHistoryDTO)this.getThreadLocalRequest().getSession().getAttribute("user_history");
		UserResearchSettingDTO sett  = eao.getUserResearchSetting(research_id,userAcc.getId());
		hist.setCurrent_research(sett);
		return sett; 
	}else if(userAcc != null && userAcc.getId() == 0){
		//default user, not persistant, function implicitly disabled).
		UserHistoryDTO hist = (UserHistoryDTO)this.getThreadLocalRequest().getSession().getAttribute("user_history");
		//UserResearchSettingDTO sett  = 
				//eao.getUserResearchSetting(research_id,userAcc.getId());
		return hist.getCurrent_research();
	}
	return null;
}


@Override
public List<UserAnalysisSaveDTO> getUserAllAnalisysList() {
	List<UserAnalysisSaveDTO> list= new ArrayList<UserAnalysisSaveDTO>();
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null && userAcc.getId() != 0)
	{
		list =  eao.getUserAllAnalisys(userAcc.getId());
	}
	return list;
}



} // end class