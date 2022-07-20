package com.socialmedia.api.post;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.request.post.DeletePostRequest;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/delete_post")
public class DeletePostAPI extends HttpServlet {

	private IPostService postService;
	private GenericService genericService;
	private IAccountService accountService;

	public DeletePostAPI() {
		postService = new PostService();
		genericService = new BaseService();
		accountService = new AccountService();
	}

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		//String jwt = request.getHeader(BaseHTTP.Authorization);
		String jwt = (String) request.getAttribute("token");
		BaseResponse baseResponse = new BaseResponse();
//		List<PostModel> list = postService.findAll();
		DeletePostRequest deletePostRequest = new DeletePostRequest();
		String idQuery = request.getParameter("id");
		deletePostRequest.setId(Long.valueOf(idQuery));
		PostModel postModel = postService.findById(deletePostRequest.getId());	
		if (postModel != null) {
			Long id = postService.findAccountIdByPostId(deletePostRequest.getId());
			System.out.println("ID = " + id);
			String phoneNumber = genericService.getPhoneNumberFromToken(jwt);
			AccountModel accountModel = accountService.findByPhoneNumber(phoneNumber);
			if (id == accountModel.getId()) {
				boolean b = postService.deleteById(deletePostRequest.getId());
				if (b == true) {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
				} else {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9992));
					baseResponse.setMessage(BaseHTTP.MESSAGE_9992);
				}

			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1009);
			}

		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9992));
			baseResponse.setMessage(BaseHTTP.MESSAGE_9992);
		}

//			} else {
//				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
//				baseResponse.setMessage(BaseHTTP.MESSAGE_9994);
//			}
//		} catch (JsonSyntaxException | NumberFormatException e) {
//			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
//			baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
//		}
		response.getWriter().print(gson.toJson(baseResponse));

	}

}
