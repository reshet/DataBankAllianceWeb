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
  public UserAccountDTO login(String email, String password) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null)
	{
		return userAcc;
	}
	
	UserAccountDTO account;
	account = eao.getUserAccount(email, password);
	if (account == null)
	{
		UserAccountDTO acc = eao.getDefaultUser();
		this.getThreadLocalRequest().getSession().setAttribute("user", acc);
		return acc;
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
}




@Override
public UserAccountDTO updateResearchState(UserAccountDTO dto_acc) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null)
	{
		if (userAcc.getId() != 0)
		{
			UserAccountDTO account,dto;
			dto = eao.updateAccountResearchState(dto_acc);	
			this.getThreadLocalRequest().getSession().setAttribute("user", dto);
			return dto;
		}
		else
		{
			if(dto_acc.getCurrent_research() == 0 && dto_acc.getCurrant_var() != 0)
			{
				dto_acc.setCurrent_research(seao.getVar(dto_acc.getCurrant_var(), null).getResearch_id());
			}
			this.getThreadLocalRequest().getSession().setAttribute("user", dto_acc);
			return dto_acc;
		}
	}
	return dto_acc;
}


@Override
public void addToSelectedResearches(SocioResearchDTO dt) {
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null)
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
	if (userAcc != null)
	{
		list = eao.getUserMyResearches(userAcc.getId());
	}
	return list;
}


@Override
public UserResearchSettingDTO getResearchSetting(Long research_id) {
	// TODO Auto-generated method stub
	UserAccountDTO userAcc =   (UserAccountDTO) this.getThreadLocalRequest().getSession().getAttribute("user");
	if (userAcc != null && userAcc.getId() != 0)
	{
		return eao.getUserResearchSetting(research_id,userAcc.getId());
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