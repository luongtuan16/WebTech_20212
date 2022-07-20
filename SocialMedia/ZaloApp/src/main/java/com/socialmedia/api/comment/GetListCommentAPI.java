package com.socialmedia.api.comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.model.CommentModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.request.comment.GetListCommentRequest;
import com.socialmedia.response.comment.DataGetCommentResponse;
import com.socialmedia.response.comment.GetListCommentResponse;
import com.socialmedia.response.comment.PosterResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.ICommentService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;
import com.socialmedia.service.impl.CommentService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/get_comment")
public class GetListCommentAPI extends HttpServlet {

	private IBlocksService blocksService;
	private IAccountService accountService;
	private IPostService postService;
	private GenericService genericService;
	private ICommentService commentService;

	public GetListCommentAPI() {
		blocksService = new BlocksService();
		accountService = new AccountService();
		postService = new PostService();
		genericService = new BaseService();
		commentService = new CommentService();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
//		GetListCommentRequest getListCommentRequest = gson.fromJson(request.getReader(), GetListCommentRequest.class);
		GetListCommentResponse listCommentResponse = new GetListCommentResponse();
		// String jwt = request.getHeader(BaseHTTP.Authorization);
		String jwt = (String) request.getAttribute("token");
//		if (getListCommentRequest != null) {
		GetListCommentRequest getListCommentRequest = new GetListCommentRequest();
		String postIdQuery = request.getParameter("id");
		String indexQuery = request.getParameter("index");
		String countQuery = request.getParameter("count");
		getListCommentRequest.setCount(Integer.parseInt(countQuery));
		getListCommentRequest.setIndex(Integer.parseInt(indexQuery));
		getListCommentRequest.setPostId(Long.valueOf(postIdQuery));
		Long postId = getListCommentRequest.getPostId();
		int index = getListCommentRequest.getIndex();
		int count = getListCommentRequest.getCount();
//			System.out.println("String index = " + String.valueOf(index));
//			System.out.println("String count = " + String.valueOf(count));
		if (postId == null || String.valueOf(index) == null || String.valueOf(count) == null) {
			// parameter not enough
			listCommentResponse.setData(null);
			listCommentResponse.setBlocked(false);
			listCommentResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			listCommentResponse.setMessage(BaseHTTP.MESSAGE_1002);

		} else {
			if (postId.toString().length() > 0 && String.valueOf(index).length() > 0
					&& String.valueOf(count).length() > 0 && count > 0 && index >= 0) {
				// check block -> not access
				PostModel postModel = postService.findById(postId);
				if (postModel != null) {
					Long authorPostId = postModel.getAccountId();
					AccountModel accountModel = accountService
							.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
					Long accountId = accountModel.getId();
					BlocksModel blocksModel = blocksService.findOne(authorPostId, accountId);
					if (blocksModel == null) {
						List<CommentModel> list = commentService.getListCommentByPostId(postId);
						List<DataGetCommentResponse> commentResponses = new ArrayList<DataGetCommentResponse>();
						for (int i = index; i < list.size(); i++) {
							if (i >= index + count)
								break;
							CommentModel commentModel = list.get(i);
							// lay thong tin ve author comment
							AccountModel model = accountService.findById(commentModel.getAccountId());
							PosterResponse posterResponse = new PosterResponse();
							posterResponse.setAvatar(model.getAvatar());
							posterResponse.setId(model.getId());
							posterResponse.setName(model.getName());
							DataGetCommentResponse commentResponse = new DataGetCommentResponse();
							// lay thong tin ve comment
							commentResponse.setContent(commentModel.getContent());
							commentResponse.setCreated(
									genericService.convertTimestampToSeconds(commentModel.getCreatedDate()));
							commentResponse.setId(commentModel.getId());
							commentResponse.setPoster(posterResponse);
							commentResponses.add(commentResponse);

							listCommentResponse.setData(commentResponses);
							listCommentResponse.setBlocked(false);
							listCommentResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							listCommentResponse.setMessage(BaseHTTP.MESSAGE_1000);
						}
					} else {
						// not access
						listCommentResponse.setData(null);
						listCommentResponse.setBlocked(true);
						listCommentResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
						listCommentResponse.setMessage(BaseHTTP.MESSAGE_1009);
					}
				} else {
					// check bai da xoa -> post is not existed
					listCommentResponse.setData(null);
					listCommentResponse.setCode(String.valueOf(BaseHTTP.CODE_9992));
					listCommentResponse.setMessage(BaseHTTP.MESSAGE_9992);
				}

			} else {
				// parameter value is invalid
				listCommentResponse.setData(null);
				listCommentResponse.setBlocked(false);
				listCommentResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				listCommentResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		}

		response.getWriter().print(gson.toJson(listCommentResponse));
	}

}
