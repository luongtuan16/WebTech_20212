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
import com.socialmedia.response.friend.DataGetSuggestedFrResp;
import com.socialmedia.response.friend.GetSuggestedListFrResp;
import com.socialmedia.response.friend.ListFriendsResp;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IFriendService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.FriendService;

@WebServlet("/api/get_suggested_list_friends")
public class GetSuggestedListFriendsAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IFriendService friendService;
	private IAccountService accountService;
	private GenericService genericService;

	public GetSuggestedListFriendsAPI() {
		genericService = new BaseService();
		friendService = new FriendService();
		accountService = new AccountService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		DataGetSuggestedFrResp data = new DataGetSuggestedFrResp();
		GetSuggestedListFrResp responseFrResp = new GetSuggestedListFrResp();
		List<ListFriendsResp> listFriendsResps = new ArrayList<ListFriendsResp>();

		String jwt = req.getParameter("token");
		String indexStr = req.getParameter("index");
		String countStr = req.getParameter("count");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (accountModel.isActive()) {
			if (indexStr != null && countStr != null) {
				int index = Integer.valueOf(indexStr);
				int count = Integer.valueOf(countStr);
				List<AccountModel> listFrs = accountService.listSuggestedAccounts(accountModel.getId());
				for (int i = index; i < listFrs.size() && i < index + count; i++) {
					AccountModel acc = listFrs.get(i);
					ListFriendsResp friend = new ListFriendsResp();
					friend.setUser_id(acc.getId());
					friend.setAvatar(acc.getAvatar());
					friend.setUsername(acc.getName());
					friend.setSame_friends(friendService.sameFriends(acc.getId(), accountModel.getId()));
					listFriendsResps.add(friend);

				}
				data.setList_users(listFriendsResps);
				responseFrResp.setData(data);
				responseFrResp.setCode(String.valueOf(BaseHTTP.CODE_1000));
				responseFrResp.setMessage(BaseHTTP.MESSAGE_1000);

			} else {
				responseFrResp.setCode(String.valueOf(BaseHTTP.CODE_1004));
				responseFrResp.setMessage(BaseHTTP.MESSAGE_1004);

			}
		} else {
			responseFrResp.setCode(String.valueOf(BaseHTTP.CODE_9995));
			responseFrResp.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(responseFrResp));
	}
}
