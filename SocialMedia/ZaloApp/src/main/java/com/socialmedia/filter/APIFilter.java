package com.socialmedia.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.impl.BaseService;

@WebFilter(urlPatterns = { "/api/logout", "/api/add_post", "/api/set_comment","/api/edit_comment","/api/del_comment",
		
		"/api/get_post", "/api/set_block_diary", "/api/set_block_user", "/api/like","/api/edit_post",
		
		"/api/delete_post", "/api/get_comment", "/api/get_list_posts", "/api/report_post", "/api/set_accept_friend",

		"/api/set_request_friend", "/api/get_user_infor","/api/change_password","/api/get_suggested_list_friends",

		"/api/set_request_friend", "/api/get_user_info", "/api/set_user_info", "/api/get_basic_user_info", "/api/make_conversation",
		
		"/api/get_saved_search", "/api/del_saved_search", "/api/set_role", "/api/set_user_state", "/api/get_requested_friend",
		
		"/api/get_user_friends", "/api/get_user_list", "/api/get_conversation","/api/delete_message", "/api/create_message",
		
		"/api/delete_conversation", "/api/get_list_conversation", "/api/delete_user", "/api/search"})

public class APIFilter implements Filter {

	private GenericService genericService;

	//private IAccountService accountService;

	public APIFilter() {
		genericService = new BaseService();
		//accountService = new AccountService();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpRequest.setCharacterEncoding("utf-8");	
		httpResponse.setContentType("application/json");
		BaseResponse baseResponse = new BaseResponse();
		Gson gson = new Gson();
		//String authToken = httpRequest.getHeader(BaseHTTP.Authorization);
		//String authToken = request.getParameter("token");
		String authToken = httpRequest.getHeader("token");
		if (authToken.length() == 0)
			authToken = request.getParameter("token");
		request.setAttribute("token", authToken);
		//System.out.println(httpRequest.getHeader("token"));
		System.out.println("authToken = " + authToken);
		String url = httpRequest.getRequestURI();
		System.out.println("url = " + url);
		try {
			if (genericService.validateToken(authToken) && genericService.getPhoneNumberFromToken(authToken) != null) {		
				chain.doFilter(request, response);
			}
			else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9998));
				baseResponse.setMessage(BaseHTTP.MESSAGE_9998);
				httpResponse.getWriter().print(gson.toJson(baseResponse));
			}
		} catch (IllegalArgumentException e) {
			if(authToken == "") {
				System.out.println("Exception = "+e.getMessage());
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9998));

				baseResponse.setMessage(BaseHTTP.MESSAGE_9998);
				httpResponse.getWriter().print(gson.toJson(baseResponse));
			}else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
				httpResponse.getWriter().print(gson.toJson(baseResponse));
			}
		}

//		} catch (IllegalArgumentException e) {
//			// token == null
////			baseResponse.setCode(9994);
//			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
//			baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
//			httpResponse.getWriter().print(gson.toJson(baseResponse));
//		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
