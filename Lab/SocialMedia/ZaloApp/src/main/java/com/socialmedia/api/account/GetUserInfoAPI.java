package com.socialmedia.api.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.model.FriendModel;
import com.socialmedia.request.account.GetUserInfoRequest;
import com.socialmedia.response.account.GetUserInforResponse;
import com.socialmedia.response.account.UserInfoResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.IFriendService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;
import com.socialmedia.service.impl.FriendService;

@WebServlet("/api/get_user_info")
public class GetUserInfoAPI extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IAccountService accountService;
	private IBlocksService blocksService;
	private GenericService genericService;
	private IFriendService friendService;

	public GetUserInfoAPI() {
		// TODO Auto-generated constructor stub
		accountService = new AccountService();
		blocksService = new BlocksService();
		genericService = new BaseService();
		friendService = new FriendService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check userId != null
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
//		GetUserInfoRequest getUserInfoRequest = gson.fromJson(request.getReader(), GetUserInfoRequest.class);
		String userIdQuery = request.getParameter("user_id");
		GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
//		getUserInfoRequest.setUserId(Long.valueOf(userIdQuery));
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		GetUserInforResponse getUserInforResponse = new GetUserInforResponse();
		List<FriendModel> list = new ArrayList<FriendModel>();
		// String jwt = request.getHeader(BaseHTTP.Authorization);
		String jwt = (String) request.getAttribute("token");
		// get information account from token
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		try {
			if (userIdQuery != null) {
				getUserInfoRequest.setUserId(Long.valueOf(userIdQuery));
				Long userId = getUserInfoRequest.getUserId();
				if (userId.toString() != null) {
					if (userId.toString().length() > 0) {
						// Check userId existed
						AccountModel a = accountService.findById(userId);
						if (a != null && a.isActive() && accountModel.isActive()) {
							// Check Blocks
							BlocksModel blocksModel1 = blocksService.findOne(userId, accountModel.getId());
							BlocksModel blocksModel2 = blocksService.findOne(accountModel.getId(), userId);
							if (blocksModel1 == null && blocksModel2 == null) {
								// get information of userId
								// get size of list friend userId
								list = friendService.findListFriendById(userId);
								userInfoResponse = setResponse(a, list);
								// Check isFriend
								boolean isFriend = friendService.checkFriendExisted(userId, accountModel.getId(), true);
								userInfoResponse.setIs_friend(isFriend);
								getUserInforResponse.setData(userInfoResponse);
								getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
								getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1000);
							} else {
								// Not Access
								getUserInforResponse.setData(userInfoResponse);
								getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
								getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1009);

							}
						} else {
							// User invalidate
							getUserInforResponse.setData(null);
							getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
							getUserInforResponse.setMessage(BaseHTTP.MESSAGE_9995);
						}

					} else {
						// value invalid
						getUserInforResponse.setData(null);
						getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
						getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1004);
					}
				} else {
					// Parameter not enough
					getUserInforResponse.setData(null);
					getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
					getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1002);
				}
			} else {

				// get size of list friend userId(token)
				list = friendService.findListFriendById(accountModel.getId());
				// get information of userId(token)
				userInfoResponse = setResponse(accountModel, list);
				userInfoResponse.setIs_friend(false);
				getUserInforResponse.setData(userInfoResponse);
				getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
				getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1000);
			}
		} catch (NumberFormatException | JsonSyntaxException e) {
			getUserInforResponse.setData(null);
			getUserInforResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
			getUserInforResponse.setMessage(BaseHTTP.MESSAGE_1003);
		}

		response.getWriter().print(gson.toJson(getUserInforResponse));
	}

	// get data response
	public UserInfoResponse setResponse(AccountModel accountModel, List<FriendModel> list) {
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		userInfoResponse.setId(accountModel.getId());
		userInfoResponse.setUsername(accountModel.getName());
		userInfoResponse.setAvatar(accountModel.getAvatar());
		long created = genericService.convertTimestampToSeconds(accountModel.getCreatedDate());
		userInfoResponse.setCreated(created);
		userInfoResponse.setDescription(accountModel.getDescription());
		userInfoResponse.setAvatar(accountModel.getAvatar());
		userInfoResponse.setCover_image(accountModel.getCover_image());
		int i = accountModel.getAvatar().lastIndexOf('\\');
		if (i > 0)
			userInfoResponse.setLink(accountModel.getAvatar().substring(0, i));
		userInfoResponse.setAddress(accountModel.getAddress());
		userInfoResponse.setCity(accountModel.getCity());
		userInfoResponse.setCountry(accountModel.getCountry());
		// get size of list friend userId
		userInfoResponse.setListing(list.size());
		userInfoResponse.setOnline("");
		return userInfoResponse;
	}

}
