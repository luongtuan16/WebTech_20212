package com.socialmedia.api.post;

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
import com.socialmedia.model.FileModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.request.post.GetPostRequest;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.response.post.AuthorGetPostResponse;
import com.socialmedia.response.post.DataGetPostReponse;
import com.socialmedia.response.post.GetPostResponse;
import com.socialmedia.response.post.ImageGetPostResponse;
import com.socialmedia.response.post.VideoGetPostResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.ICommentService;
import com.socialmedia.service.IFileService;
import com.socialmedia.service.ILikesService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;
import com.socialmedia.service.impl.CommentService;
import com.socialmedia.service.impl.FileService;
import com.socialmedia.service.impl.LikesService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/get_post")
public class GetPostAPI extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IPostService postService;
	private ILikesService likesService;
	private ICommentService commentService;
	private GenericService genericService;
	private IAccountService accountService;
	private IBlocksService blocksService;
	private IFileService fileService;

	public GetPostAPI() {
		postService = new PostService();
		likesService = new LikesService();
		commentService = new CommentService();
		genericService = new BaseService();
		accountService = new AccountService();
		blocksService = new BlocksService();
		fileService = new FileService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();
		try {
			String idStr = request.getParameter("id");
			if (idStr != null) {
				Long id = Long.valueOf(idStr);
				PostModel model = postService.findPostById(id);
				if (model != null) {
					response.getWriter().print(gson.toJson(model));
					return;
				} else {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
					baseResponse.setMessage(BaseHTTP.MESSAGE_9995);
					response.getWriter().print(gson.toJson(baseResponse));
					return;
				}
			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
				baseResponse.setMessage(BaseHTTP.MESSAGE_9994);
			}
		} catch (NumberFormatException e) {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1003);
		}
		response.getWriter().print(gson.toJson(baseResponse));

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		DataGetPostReponse dataGetPostReponse = new DataGetPostReponse();
		GetPostResponse getPostResponse = new GetPostResponse();
		GetPostRequest getPostRequest = new GetPostRequest();
		String idQuery = request.getParameter("id");
		// get token
		// String jwt = request.getHeader(BaseHTTP.Authorization);
		String jwt = request.getParameter("token");
		if (idQuery != null) {
			getPostRequest.setId(Long.valueOf(idQuery));
			Long postId = getPostRequest.getId();
			if (postId > 0) {
				dataGetPostReponse.setId(postId);
				// search post by id
				PostModel postModel = postService.findPostById(postId);

				if (postModel != null) {
					System.out.println("postModel = " + postModel);
//					PostModel postModel = postService.findPostById(postId);
					dataGetPostReponse.setDescribed(postModel.getContent());
					dataGetPostReponse.setCreated(String.valueOf(postModel.getCreatedDate()));
					dataGetPostReponse.setModified(String.valueOf(postModel.getModifiedDate()));
					// amount of like
					int like = likesService.findByPostId(postId);
					dataGetPostReponse.setLike(like);
					// amount of comment
					int comment = commentService.findByPostId(postId);
					dataGetPostReponse.setComment(comment);
					// check this user liked this post
					String phoneNumber = genericService.getPhoneNumberFromToken(jwt);
					AccountModel accountModel = accountService.findByPhoneNumber(phoneNumber);// jwt
					Long accountId = accountModel.getId();
					boolean checkThisUserLiked = likesService.checkThisUserLiked(accountId, postId);
					dataGetPostReponse.setIs_liked(checkThisUserLiked);
					// is_blocked
					BlocksModel blocksModel = blocksService.findOne(postModel.getAccountId(), accountId);
					if (blocksModel == null) {
						dataGetPostReponse.setIs_blocked("UnBlocked");
					} else {
						dataGetPostReponse.setIs_blocked("Blocked");
					}
					// can_edit
					if (accountId == postModel.getAccountId()) {
						dataGetPostReponse.setCan_edit("Can Edit");
					} else {
						dataGetPostReponse.setCan_edit("Can't Edit");
					}
					if (dataGetPostReponse.getIs_blocked().equalsIgnoreCase("Blocked")) {
						dataGetPostReponse.setCan_comment("Can't Comment");
					} else {
						dataGetPostReponse.setCan_comment("Can Comment");
					}
					// get file
					List<ImageGetPostResponse> imageGetPostResponses = new ArrayList<ImageGetPostResponse>();
					VideoGetPostResponse videoGetPostResponse = new VideoGetPostResponse();
					if (fileService.findByPostId(postId) != null) {
						List<FileModel> list = fileService.findByPostId(postId);
						System.out.println("size = " + fileService.findByPostId(postId).size());
						for (FileModel fileModel : list) {
							if (fileModel.getContent().endsWith(".jpg") || fileModel.getContent().endsWith(".svg")
									|| fileModel.getContent().endsWith(".JPEG")
									|| fileModel.getContent().endsWith(".png")) {
								ImageGetPostResponse imageGetPostResponse = new ImageGetPostResponse();
								imageGetPostResponse.setId(fileModel.getId());
								imageGetPostResponse.setUrl(fileModel.getContent());
								imageGetPostResponses.add(imageGetPostResponse);
							} else if (fileModel.getContent().endsWith(".mp4")) {
//								VideoGetPostResponse videoGetPostResponse = new VideoGetPostResponse();
//								videoGetPostResponse.setId(fileModel.getId());
//								videoGetPostResponse.setUrl(fileModel.getContent());
//								videoGetPostResponses.add(videoGetPostResponse);
								videoGetPostResponse.setThumbnail(fileModel.getContent());
								videoGetPostResponse.setUrl(fileModel.getContent());
							}
						}
					} else {
						imageGetPostResponses = null;
						videoGetPostResponse = null;
					}
					// get author
					AuthorGetPostResponse authorGetPostResponse = new AuthorGetPostResponse();
					authorGetPostResponse.setId(postModel.getAccountId());
					authorGetPostResponse.setName(accountService.findById(postModel.getAccountId()).getName());
					authorGetPostResponse.setAvatar(accountService.findById(postModel.getAccountId()).getAvatar());
					dataGetPostReponse.setAuthor(authorGetPostResponse);
					dataGetPostReponse.setImage(imageGetPostResponses);
					dataGetPostReponse.setVideo(videoGetPostResponse);
					// set created modified in long type
					dataGetPostReponse.setCreated(
							String.valueOf(genericService.convertTimestampToSeconds(postModel.getCreatedDate())));
					if (postModel.getModifiedDate() != null) {
						dataGetPostReponse.setModified(
								String.valueOf(genericService.convertTimestampToSeconds(postModel.getModifiedDate())));
					}
					getPostResponse.setDataGetPostReponse(dataGetPostReponse);
					getPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
					getPostResponse.setMessage(BaseHTTP.MESSAGE_1000);
				} else {
					getPostResponse.setCode(String.valueOf(BaseHTTP.CODE_9992));
					getPostResponse.setDataGetPostReponse(null);
					getPostResponse.setMessage(BaseHTTP.MESSAGE_9992);
					response.getWriter().print(gson.toJson(getPostResponse));
					return;
				}
			} else {
				getPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				getPostResponse.setDataGetPostReponse(null);
				getPostResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}

		} else {
			getPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			getPostResponse.setDataGetPostReponse(null);
			getPostResponse.setMessage(BaseHTTP.MESSAGE_1002);
			response.getWriter().print(gson.toJson(getPostResponse));
			return;
		}

//		}
		response.getWriter().print(gson.toJson(getPostResponse));
	}

}
