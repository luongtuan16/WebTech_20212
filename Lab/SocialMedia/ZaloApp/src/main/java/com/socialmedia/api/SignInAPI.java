package com.socialmedia.api;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.model.AccountModel;
import com.socialmedia.request.account.SignInRequest;
import com.socialmedia.response.account.DataSignInResponse;
import com.socialmedia.response.account.SignInResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;

@WebServlet("/api/login")
public class SignInAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;

	public SignInAPI() {
		// TODO Auto-generated constructor stub
		accountService = new AccountService();
		genericService = new BaseService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		SignInResponse signInResponse = new SignInResponse();
		SignInRequest signInRequest = gson.fromJson(request.getReader(), SignInRequest.class); //Body JSON
		if (signInRequest != null) {
			try {
				String phoneNumber = signInRequest.getPhonenumber();
				String password = signInRequest.getPassword();
				Pattern p = Pattern.compile("[^A-Za-z0-9]");
				Matcher m = p.matcher(password);
				if (!m.find() && phoneNumber.length() == 10 && phoneNumber.charAt(0) == '0' && password.length() >= 6
						&& password.length() <= 10 && !phoneNumber.equalsIgnoreCase(password)) {
					AccountModel accountModel = accountService.findByPhoneNumber(phoneNumber);

					if (accountModel != null) {
						String jwt = accountService.signIn(signInRequest);
						phoneNumber = genericService.getPhoneNumberFromToken(jwt);

						if (jwt != null) {
							// Convert Date to seconds
							accountModel.setCreatedDateLong(
									genericService.convertTimestampToSeconds(accountModel.getCreatedDate()));
							if (accountModel.getModifiedDate() != null) {
								accountModel.setModifiedDateLong(
										genericService.convertTimestampToSeconds(accountModel.getModifiedDate()));
							}
							signInResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							signInResponse.setMessage(BaseHTTP.MESSAGE_1000);
							DataSignInResponse dataSignInResponse = new DataSignInResponse();
							dataSignInResponse.setAvatar(accountModel.getAvatar());
							dataSignInResponse.setId(String.valueOf(accountModel.getId()));
							dataSignInResponse.setUsername(accountModel.getName());
							dataSignInResponse.setToken(jwt);
							signInResponse.setData(dataSignInResponse);
							response.getWriter().print(gson.toJson(signInResponse));
							return;
						} else {
							signInResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
							signInResponse.setMessage(BaseHTTP.MESSAGE_9999);
						}
					} else {
						signInResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
						signInResponse.setMessage(BaseHTTP.MESSAGE_9995);
					}
				} else {
					signInResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					signInResponse.setMessage(BaseHTTP.MESSAGE_1004);
				}
			} catch (NullPointerException e) {
				signInResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				signInResponse.setMessage(BaseHTTP.MESSAGE_1002);
			}
		}
//		else {
//			signInResponse.setCode(BaseHTTP.CODE_9994);
//			signInResponse.setMessage(BaseHTTP.MESSAGE_9994);
//			signInResponse.setAccountModel(null);
//		}
		signInResponse.setData(null);
		response.getWriter().print(gson.toJson(signInResponse));

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
