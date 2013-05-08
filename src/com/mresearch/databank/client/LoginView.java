package com.mresearch.databank.client;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mresearch.databank.client.helper.RPCCall;
import com.mresearch.databank.client.service.UserAccountService;
import com.mresearch.databank.client.service.UserAccountServiceAsync;

public class LoginView extends Composite {

	private static LoginViewUiBinder uiBinder = GWT
			.create(LoginViewUiBinder.class);

	private final class CallbackImplementation implements
			Callback<String, Throwable> {
		private final AuthRequest req;

		private CallbackImplementation(AuthRequest req) {
			this.req = req;
		}

		@Override
		  public void onSuccess(String token) {
//		    Window.alert("Got an OAuth token:\n" + token + "\n"
//		        + "Token expires in " + AUTH.expiresIn(req) + " ms\n");
		    	main.login(login_box.getText(), pswd_box.getText(),token);
		    
		  }

		@Override
		  public void onFailure(Throwable caught) {
		    Window.alert("Error:\n" + caught.getMessage());
		  }
	}
	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}
	@UiField TextBox login_box;
	@UiField PasswordTextBox pswd_box;
	@UiField HTMLPanel panel;
	private DatabankApp main;
	private PopupPanel parent;
	//private UsernameShower userapp;
	private static final Auth AUTH = Auth.get();
 	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";

  // This app's personal client ID assigned by the Google APIs Console
  // (http://code.google.com/apis/console).
 	private static final String GOOGLE_CLIENT_ID = "157936524178.apps.googleusercontent.com";

  // The auth scope being requested. This scope will allow the application to
  // identify who the authenticated user is.
 	private static final String PLUS_ME_SCOPE = "https://www.googleapis.com/auth/plus.me";
 	private static final String PROFILE_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
 	private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
 	
	private UserAccountServiceAsync rpcUserService = GWT.create(UserAccountService.class);
	public LoginView(DatabankApp main, PopupPanel parent) {
		initWidget(uiBinder.createAndBindUi(this));
		this.main = main;
		this.parent = parent;
		//this.userapp = userapp;
		 Auth.export();
		login_box.setFocus(true);
		pswd_box.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) doLogin(null);
			}
		});
		
	}
	
	
	@UiHandler(value="enterBtn")
	public void doLogin(ClickEvent e)
	{	
		parent.hide();
		new RPCCall<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Eror on logout");
			}

			@Override
			public void onSuccess(Void result) {
				main.login(login_box.getText(), pswd_box.getText(),null);
			}

			@Override
			protected void callService(AsyncCallback<Void> cb) {
				rpcUserService.logout(cb);
			}}.retry(2);
	}
	@UiHandler(value="authGoogleBtn")
	public void doGoogleLogin(ClickEvent e)
	{	
		parent.hide();
		new RPCCall<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Eror on logout");
			}

			@Override
			public void onSuccess(Void result) {
				
				 final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID)
		            .withScopes(PROFILE_SCOPE,EMAIL_SCOPE);

		        // Calling login() will display a popup to the user the first time it is
		        // called. Once the user has granted access to the application,
		        // subsequent calls to login() will not display the popup, and will
		        // immediately result in the callback being given the token to use.
		        AUTH.login(req, new CallbackImplementation(req));
				//main.login(login_box.getText(), pswd_box.getText());
				
			}

			@Override
			protected void callService(AsyncCallback<Void> cb) {
				rpcUserService.logout(cb);
			}}.retry(2);
	}
}
