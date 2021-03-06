package com.socialmedia.api.like;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.request.like.LikesRequest;
import com.socialmedia.response.like.DataLikesResponse;
import com.socialmedia.response.like.LikesResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.ILikesService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;
import com.socialmedia.service.impl.LikesService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/like")
public class LikesAPI extends HttpServlet {

	/**
	 * 
	 */
	private ILikesService likesService;
	private IAccountService accountService;
	private GenericService genericService;
	private IBlocksService blocksService;
	private IPostService postService;

	public LikesAPI() {
		likesService = new LikesService();
		genericService = new BaseService();
		accountService = new AccountService();
		blocksService = new BlocksService();
		postService = new PostService();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		// String jwt = request.getHeader(BaseHTTP.Authorization);
		String jwt = (String) request.getAttribute("token");

		LikesResponse likesResponse = new LikesResponse();
//		try {
//			LikesRequest likesRequest = gson.fromJson(request.getReader(), LikesRequest.class);
//			if (likesRequest == null) {
//				likesResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
//				likesResponse.setMessage(BaseHTTP.MESSAGE_9994);
//				likesResponse.setDataLikesResponse(null);
//				response.getWriter().print(gson.toJson(likesResponse));
//				return;
//			} else {
		String postId = request.getParameter("id");
		LikesRequest likesRequest = new LikesRequest();
		likesRequest.setPostId(Long.valueOf(postId));
		if (likesRequest.getPostId() != null) {
			AccountModel accountModel = accountService.findByPhoneNumber((genericService.getPhoneNumberFromToken(jwt)));
			likesRequest.setAccountId(accountModel.getId());
			// check accountId co bi author block ko ?
			PostModel postModel = postService.findById(likesRequest.getPostId());
			try {
				BlocksModel blocksModel = blocksService.findOne(postModel.getAccountId(), accountModel.getId());
				if (blocksModel == null) {
					// check da like chua
					boolean check = likesService.checkThisUserLiked(likesRequest.getAccountId(),
							likesRequest.getPostId());
					System.out.println("Check = " + check);
					if (check == false) {
						// chua like --> like
						Long id = likesService.insertOne(likesRequest);
						if (id == -1) {
							likesResponse.setCode(String.valueOf(9992));
							likesResponse.setMessage("Post is not existed");
							likesResponse.setDataLikesResponse(null);
						} else {

							likesResponse.setCode(String.valueOf(1000));
							likesResponse.setMessage("OK");
							int like = likesService.findByPostId(likesRequest.getPostId());
							DataLikesResponse dataLikesResponse = new DataLikesResponse();
							dataLikesResponse.setLike(like);
							likesResponse.setDataLikesResponse(dataLikesResponse);

						}
					} else {
						// da like --> bo like
//						likesResponse.setCode(String.valueOf(1010));
//						likesResponse.setMessage("Action has been done previously by this user");
//						likesResponse.setDataLikesResponse(null);
						
						boolean b=likesService.disLike(likesRequest.getPostId(), likesRequest.getAccountId());
						if (b == false) {
							likesResponse.setCode(String.valueOf(1001));
							likesResponse.setMessage("Can not connect to DB");
						} else {
							likesResponse.setCode(String.valueOf(1000));
							likesResponse.setMessage("OK");
							int like = likesService.findByPostId(likesRequest.getPostId());
							DataLikesResponse dataLikesResponse = new DataLikesResponse();
							dataLikesResponse.setLike(like);
							likesResponse.setDataLikesResponse(dataLikesResponse);
						}
					}

				} else {
					likesResponse.setCode(String.valueOf(1009));
					likesResponse.setMessage("Not access");
					likesResponse.setDataLikesResponse(null);
				}
			} catch (NullPointerException e) {
				System.out.println("Null Pointer Exception LikesAPI : " + e.getMessage());
				likesResponse.setCode(String.valueOf(9992));
				likesResponse.setMessage("Post is not existed");
				likesResponse.setDataLikesResponse(null);
			}

		} else {
			likesResponse.setCode(String.valueOf(1002));
			likesResponse.setMessage("Parameter is not enough");
			likesResponse.setDataLikesResponse(null);
		}
		response.getWriter().print(gson.toJson(likesResponse));
//			}
//		} catch (NumberFormatException | JsonSyntaxException e) {
//			likesResponse.setCode(String.valueOf(1003));
//			likesResponse.setMessage("Parameter type is invalid");
//			likesResponse.setDataLikesResponse(null);
//			response.getWriter().print(gson.toJson(likesResponse));
//		}

	}
	
//	@Override
//	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
//		response.setContentType("application/json");
//		Gson gson = new Gson();
//		BaseResponse baseResponse = new BaseResponse();
//		// String jwt = request.getHeader(BaseHTTP.Authorization);
//		String jwt = request.getParameter("token");
//
////		try {
////			LikesRequest likesRequest = gson.fromJson(request.getReader(), LikesRequest.class);
////			if (likesRequest == null) {
////				baseResponse.setCode(String.valueOf(9994));
////				baseResponse.setMessage("No data or end of list data");
////				response.getWriter().print(gson.toJson(baseResponse));
////				return;
////			} else {
//		String postId = request.getParameter("id");
//		LikesRequest likesRequest = new LikesRequest();
//		likesRequest.setPostId(Long.valueOf(postId));
//		if (likesRequest.getPostId() != null) {
//			AccountModel accountModel = accountService.findByPhoneNumber((genericService.getPhoneNumberFromToken(jwt)));
//			likesRequest.setAccountId(accountModel.getId());
//			PostModel postModel = postService.findById(likesRequest.getPostId());
//			if (postModel != null) {
//				BlocksModel blocksModel = blocksService.findOne(postModel.getAccountId(), accountModel.getId());
//				if (blocksModel == null) {
//					boolean check = likesService.checkThisUserLiked(likesRequest.getAccountId(),
//							likesRequest.getPostId());
//					System.out.println("Check = " + check);
//					if (check == true) {
//						boolean b = likesService.disLike(likesRequest.getPostId(), likesRequest.getAccountId());
//						if (b == false) {
//							baseResponse.setCode(String.valueOf(1001));
//							baseResponse.setMessage("Can not connect to DB");
//						} else {
//							baseResponse.setCode(String.valueOf(1000));
//							baseResponse.setMessage("OK");
//						}
//					} else {
//						baseResponse.setCode(String.valueOf(1010));
//						baseResponse.setMessage(BaseHTTP.MESSAGE_1010);
//					}
//				} else {
//					baseResponse.setCode(String.valueOf(1009));
//					baseResponse.setMessage("Not Access");
//				}
//
//			} else {
//				baseResponse.setCode(String.valueOf(9992));
//				baseResponse.setMessage("Post is not existed");
//			}
//
//		} else {
//			baseResponse.setCode(String.valueOf(1002));
//			baseResponse.setMessage("Parameter is not enough");
//		}
//		response.getWriter().print(gson.toJson(baseResponse));
////			}
////		} catch (NumberFormatException | JsonSyntaxException e) {
////			baseResponse.setCode(String.valueOf(1003));
////			baseResponse.setMessage("Parameter type is invalid");
////			response.getWriter().print(gson.toJson(baseResponse));
////		}
//	}

}
