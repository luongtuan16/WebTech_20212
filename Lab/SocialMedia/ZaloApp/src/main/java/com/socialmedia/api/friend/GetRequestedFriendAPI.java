package com.socialmedia.api.friend;

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
import com.socialmedia.model.FriendModel;
import com.socialmedia.request.friend.DataGetRequestedFriend;
import com.socialmedia.request.friend.GetRequestedFriendRequest;
import com.socialmedia.response.friend.FriendsResponse;
import com.socialmedia.response.friend.GetRequestedFriendResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IFriendService;
import com.socialmedia.service.IRoleService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.FriendService;
import com.socialmedia.service.impl.RoleService;

@WebServlet("/api/get_requested_friend")
public class GetRequestedFriendAPI extends HttpServlet {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IFriendService friendService;
	private IAccountService accountService;
	private GenericService genericService;
	private IRoleService roleService;

	public GetRequestedFriendAPI() {
		genericService = new BaseService();
		friendService = new FriendService();
		accountService = new AccountService();
		roleService = new RoleService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		String jwt = (String) request.getAttribute("token");
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		GetRequestedFriendRequest dataRequest = gson.fromJson(request.getReader(), GetRequestedFriendRequest.class);

		List<FriendsResponse> friends = new ArrayList<FriendsResponse>();
		DataGetRequestedFriend dataGetRequestedFriend = new DataGetRequestedFriend();
		GetRequestedFriendResponse getRequestedFriendResponse = new GetRequestedFriendResponse();

		if (accountModel.getRoleId() == roleService.findId("user")) {
			// request by user
			dataRequest.setUser_id(accountModel.getId());
		} else if (accountModel.getRoleId() > roleService.findId("user")) {
			// request by admin or superadmin
			if (dataRequest.getUser_id() == null)
				dataRequest.setUser_id(accountModel.getId());
			else {
				// check user exist
				AccountModel user = accountService.findById(dataRequest.getUser_id());
				if (user == null) {
					getRequestedFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					getRequestedFriendResponse.setMessage(BaseHTTP.MESSAGE_1004);
					response.getWriter().print(gson.toJson(getRequestedFriendResponse));
					return;
				}
			}
		}

		List<FriendModel> listFriends = friendService.findListFriendRequestByIdB(dataRequest.getUser_id());
		int count = dataRequest.getCount();
		for (int i = dataRequest.getIndex(); i < listFriends.size() && count > 0; i++) {
			AccountModel accountA = accountService.findById(listFriends.get(i).getIdA());
			FriendsResponse friendsResponse = new FriendsResponse();
			friendsResponse.setId(accountA.getId());
			friendsResponse.setAvatar(accountA.getAvatar());
			friendsResponse.setUsername(accountA.getName());
			friendsResponse.setCreated(listFriends.get(i).getCreatedDate());
			friends.add(friendsResponse);
			count--;
		}
		dataGetRequestedFriend.setFriends(friends);
		getRequestedFriendResponse.setData(dataGetRequestedFriend);
		getRequestedFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
		getRequestedFriendResponse.setMessage(BaseHTTP.MESSAGE_1000);
		response.getWriter().print(gson.toJson(getRequestedFriendResponse));
	}
}
