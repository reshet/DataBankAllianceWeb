package com.mresearch.databank.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mplatforma.amr.service.remote.RxStorageBeanRemote;
import com.mplatforma.amr.service.remote.UserAccountBeanRemote;
import com.mresearch.databank.shared.RxStoredDTO;

@RemoteServiceRelativePath("serve")
public class RxStorageServe extends HttpServlet {
   // private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private static RxStorageBeanRemote eao;
	static
	{
		Object obj = new String("some");
		try {
		  InitialContext ic = new InitialContext();
		  System.out.println("start lookup");
		//  final String jndiName = "java:global/DatabankEnterprise-ejb/RxStorageRemoteBean";
		  final String jndiName = "java:global/DatabankEnterprise-ejb/RxStorageRemoteBean";
			  obj = ic.lookup(jndiName);
		  System.out.println("lookup returned " + obj);
		  eao = (RxStorageBeanRemote) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	


	
	
public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException {
        //BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
        //BlobInfo info = f.loadBlobInfo(blobKey);
		String id = req.getParameter("blob-key");
		long idd = Integer.parseInt(id);
        RxStoredDTO dto = eao.getFileInfo(idd);
	
		//res.setHeader("Content-Type", dto.getDesc());
        String filename =  URLEncoder.encode(dto.getName(), "UTF8");
        res.setHeader( "Content-Disposition", "attachment; filename=\"" + filename +"\"" );
        byte[] arr = eao.getFileContents(idd);
        res.getOutputStream().write(arr);
        res.getOutputStream().close();
        //blobstoreService.serve(blobKey, res);
       
    }
}