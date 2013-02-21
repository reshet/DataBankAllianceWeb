package com.mresearch.databank.server;


import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.catalina.session.PersistentManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mplatforma.amr.service.remote.UserSocioResearchBeanRemote;
import com.mresearch.databank.client.service.StartPageService;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.shared.ArticleDTO;
import com.mresearch.databank.shared.NewsDTO;
import com.mresearch.databank.shared.NewsSummaryDTO;
import com.mresearch.databank.shared.StartupBundleDTO;
import com.mresearch.databank.shared.UserAccountDTO;

@SuppressWarnings("serial")
public class StartPageServiceImpl extends RemoteServiceServlet implements
    StartPageService {

	
	private static UserAccountBeanRemote eao;
	static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		  final String jndiName = "java:global/DatabankEnterprise-ejb/UserAccountRemoteBean";
		  obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  eao = (UserAccountBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
		
	
//	private static UserSocioResearchBeanRemote seao;
//	static
//	{
//		Object obj = new String("some");
//		try {
//		  InitialContext ic = new InitialContext();
//		  System.out.println("start lookup");
//		  final String jndiName = "java:global/DatabankEnterprise-ejb/UserSocioResearchRemoteBean";
//		  obj = ic.lookup(jndiName);
//		  System.out.println("lookup returned " + obj);
//		  seao = (UserSocioResearchBeanRemote) obj;
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
	@Override
	public ArrayList<NewsSummaryDTO> getNewsSummaries(int latest) {
		
		return null;
	}
	@Override
	public ArticleDTO getNewsDetailed(String news_id) {
		
		return null;
	}
	@Override
	public ArticleDTO getArticle(String article_id) {
		
		return null;
	}
	@Override
	public ArticleDTO getMainPageArticle() {
		
		return null;
	}
	@Override
	public ArrayList<NewsDTO> getNews(int latest) {
		return null;
	}
	@Override
	public StartupBundleDTO getStartupContent() {
		
		return  eao.getStartupContent();
	}
 

} // end class
