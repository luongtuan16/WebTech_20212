package com.socialmedia.api.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.response.account.ChangePasswordResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;

@WebServlet("/api/change_password")
public class ChangePasswordAPI extends HttpServlet {
	private GenericService genericService;
	private IAccountService accountService;

	public ChangePasswordAPI() {
		// phân biệt hàm khi có hàm trùng nhau...
		genericService = new BaseService();
		accountService = new AccountService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
		Gson gson = new Gson();
		// Query String
		String passwordQuery = request.getParameter("password");
		String newPasswordQuery = request.getParameter("new_password");
		//System.out.println(checkSimilarPassword(passwordQuery, newPasswordQuery));
		if (passwordQuery != null && newPasswordQuery != null) {
			if (passwordQuery.length() >= 6 && passwordQuery.length() <= 10 && newPasswordQuery.length() >= 6
					&& newPasswordQuery.length() <= 10 && checkSimilarPassword(passwordQuery, newPasswordQuery)==false) {
				if (!passwordQuery.equals(newPasswordQuery)) {
//					String token = request.getHeader(BaseHTTP.Authorization);
					String token = (String) request.getAttribute("token");
					boolean changePassword = accountService.changePassword(token, passwordQuery, newPasswordQuery);
					if (changePassword) {
						changePasswordResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						changePasswordResponse.setMessage(BaseHTTP.MESSAGE_1000);
						changePasswordResponse.setData(String.valueOf(changePassword));

					} else {
						// passwordQuery truyen len khong trung voi password da luu trong database truoc
						// do
						// Exeption error
						changePasswordResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
						changePasswordResponse.setMessage(BaseHTTP.MESSAGE_9999);
						changePasswordResponse.setData(String.valueOf(changePassword));
					}
				} else {
					// passwordQuery = newPasswordQuery
					// exception error
					changePasswordResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
					changePasswordResponse.setMessage(BaseHTTP.MESSAGE_9999);
					changePasswordResponse.setData(String.valueOf(false));
				}

			} else {
				// param value invalid
				changePasswordResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				changePasswordResponse.setMessage(BaseHTTP.MESSAGE_1004);
				changePasswordResponse.setData(String.valueOf(false));
			}
		} else {
			// param not enough
			changePasswordResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			changePasswordResponse.setMessage(BaseHTTP.MESSAGE_1002);
			changePasswordResponse.setData(String.valueOf(false));
		}
		response.getWriter().print(gson.toJson(changePasswordResponse));

	}
	
	public boolean checkSimilarPassword(String password, String newPassword) {
		int len = password.length();
		int maxSimilar=0;
		for (int i=0;i<len-1;i++)
			for (int j=i+1;j<len;j++) {
				String x = password.substring(i, j+1);
				if (newPassword.indexOf(x)>=0 && maxSimilar<x.length())
					maxSimilar =  x.length();
			}
		double similar = (double) maxSimilar/newPassword.length();
		return similar>=0.8;
	}
}
