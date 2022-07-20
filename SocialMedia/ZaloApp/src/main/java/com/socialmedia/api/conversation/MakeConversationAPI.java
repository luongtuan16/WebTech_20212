package com.socialmedia.api.conversation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.ConversationModel;
import com.socialmedia.response.conversation.MakeConversationResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IConversationService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.ConversationService;

@WebServlet("/api/make_conversation")
public class MakeConversationAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;

	public MakeConversationAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		conversationService = new ConversationService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("aa");
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		MakeConversationResponse makeConversationResponse = new MakeConversationResponse();

		String jwt = (String) req.getAttribute("token");
		String partnerIdStr = req.getParameter("partnerId");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null && accountModel.isActive()) {
			if (partnerIdStr != null) {
				Long partnerId = Long.valueOf(partnerIdStr);
				if (partnerId != accountModel.getId()) {

					ConversationModel conversationModel = conversationService.findOne(accountModel.getId(), partnerId);
					if (conversationModel != null) {
						makeConversationResponse.setConversationId(conversationModel.getId());
					} else {
						conversationModel = new ConversationModel();
						conversationModel.setAccountA(accountModel.getId());
						conversationModel.setAccountB(partnerId);
						Long conversationId = conversationService.insertOne(conversationModel);
						makeConversationResponse.setConversationId(conversationId);
					}
					makeConversationResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
					makeConversationResponse.setMessage(BaseHTTP.MESSAGE_1000);
				} else {
					makeConversationResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					makeConversationResponse.setMessage(BaseHTTP.MESSAGE_1004);
				}
			} else {
				makeConversationResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				makeConversationResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}

		} else {
			makeConversationResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
			makeConversationResponse.setMessage(BaseHTTP.MESSAGE_1004);
		}
		resp.getWriter().print(gson.toJson(makeConversationResponse));
	}
}
