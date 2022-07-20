package com.socialmedia.api.friend;

import java.io.IOException;
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
import com.socialmedia.request.friend.FriendIdRequest;
import com.socialmedia.response.friend.DataSetFriendResponse;
import com.socialmedia.response.friend.SetFriendResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.IFriendService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;
import com.socialmedia.service.impl.FriendService;

@WebServlet("/api/set_request_friend")
public class SetRequestFriendAPI extends HttpServlet {


	private IAccountService accountService;
	private GenericService genericService;
	private IFriendService friendService;
	private IBlocksService blocksService;

	public SetRequestFriendAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		friendService = new FriendService();
		blocksService = new BlocksService();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		SetFriendResponse setFriendResponse = new SetFriendResponse();
		DataSetFriendResponse dataResponse = new DataSetFriendResponse();
		try {
//			FriendIdRequest friendIdRequest = gson.fromJson(request.getReader(), FriendIdRequest.class);
			String userIdQuery = request.getParameter("user_id");
			FriendIdRequest friendIdRequest = new FriendIdRequest();
			friendIdRequest.setUserId(Long.valueOf(userIdQuery));

			Long userId = friendIdRequest.getUserId();
			if (userId != null) {
				if (userId.toString().length() > 0) {
					AccountModel accountModel = accountService.findById(userId);
					if (accountModel != null) {
						// get userId , id through token
						// String jwt = request.getHeader(BaseHTTP.Authorization);
						String jwt = (String) request.getAttribute("token");

						String phoneNumber = genericService.getPhoneNumberFromToken(jwt);
						AccountModel model = accountService.findByPhoneNumber(phoneNumber);
						// check accountId == userId
						if (model.getId() != userId) {
							// Check Block
							BlocksModel blocksModel = blocksService.findOne(model.getId(), userId);
							BlocksModel blocksModel2 = blocksService.findOne(userId, model.getId());
							if (blocksModel == null && blocksModel2 == null) {
								boolean checkRequestExsisted = friendService.checkRequestExisted(model.getId(), userId,
										false);
								boolean checkRequestExsisted1 = friendService.checkRequestExisted(userId, model.getId(),
										false);
								boolean checkFrExisted = friendService.checkFriendExisted(model.getId(), userId, true);
								boolean checkFrExisted1 = friendService.checkFriendExisted(userId, model.getId(), true);
								if (checkRequestExsisted == false && checkRequestExsisted1 == false
										&& checkFrExisted == false && checkFrExisted1 == false) {
									Long id = friendService.insertOne(model.getId(), userId);
									// get list requests of accountId from token
									List<FriendModel> list = friendService.findListFriendRequestByIdA(model.getId());
									dataResponse.setRequested_friends(list.size());
									setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
									setFriendResponse.setMessage(BaseHTTP.MESSAGE_1000);
									setFriendResponse.setData(dataResponse);
								} else {
									// Exception
									dataResponse.setRequested_friends(-1);
									setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
									setFriendResponse.setMessage(BaseHTTP.MESSAGE_9999);
									setFriendResponse.setData(dataResponse);
								}
							} else {
								// Exception
								dataResponse.setRequested_friends(-1);
								setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
								setFriendResponse.setMessage(BaseHTTP.MESSAGE_9999);
								setFriendResponse.setData(dataResponse);
							}

						} else {
							// exception
							dataResponse.setRequested_friends(-1);
							setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_9999));
							setFriendResponse.setMessage(BaseHTTP.MESSAGE_9999);
							setFriendResponse.setData(dataResponse);
						}

					} else {
						// User is not validate
						dataResponse.setRequested_friends(-1);
						setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
						setFriendResponse.setMessage(BaseHTTP.MESSAGE_9995);
						setFriendResponse.setData(dataResponse);
					}
				} else {
					// value invalid
					dataResponse.setRequested_friends(-1);
					setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					setFriendResponse.setMessage(BaseHTTP.MESSAGE_1004);
					setFriendResponse.setData(dataResponse);
				}
			} else {
				// not enough
				dataResponse.setRequested_friends(-1);
				setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				setFriendResponse.setMessage(BaseHTTP.MESSAGE_1002);
				setFriendResponse.setData(dataResponse);
			}
//			} else {
//				// No data
//				setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
//				setFriendResponse.setMessage(BaseHTTP.MESSAGE_9994);
//				setFriendResponse.setRequestedFriends(-1);
//			}
			response.getWriter().print(gson.toJson(setFriendResponse));
		} catch (JsonSyntaxException | NumberFormatException e) {
			dataResponse.setRequested_friends(-1);
			setFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
			setFriendResponse.setMessage(BaseHTTP.MESSAGE_1003);
			setFriendResponse.setData(dataResponse);
			response.getWriter().print(gson.toJson(setFriendResponse));
		}
	}

}
