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
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IConversationService;
import com.socialmedia.service.IMessageService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.ConversationService;
import com.socialmedia.service.impl.MessageService;

@WebServlet("/api/delete_conversation")
public class DeleteConversationAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;
	private IMessageService messageService;

	public DeleteConversationAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		conversationService = new ConversationService();
		messageService = new MessageService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String conversationIdStr = req.getParameter("conversation_id");
		String partnerIdStr = req.getParameter("partner_id");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null && accountModel.isActive()) {
			if (conversationIdStr != null) {
				Long conversationId = Long.valueOf(conversationIdStr);
				ConversationModel conversationModel = conversationService.findOne(conversationId);
				delConversation(conversationModel, accountModel, baseResponse);
			} else if (partnerIdStr != null) {
				Long partnerId = Long.valueOf(partnerIdStr);
				ConversationModel conversationModel = conversationService.findOne(accountModel.getId(), partnerId);
				delConversation(conversationModel, accountModel, baseResponse);
			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
			baseResponse.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(baseResponse));

	}

	private void delConversation(ConversationModel conversationModel, AccountModel accountModel,
			BaseResponse baseResponse) {
		if (conversationModel != null && (conversationModel.getAccountA() == accountModel.getId()
				|| conversationModel.getAccountB() == accountModel.getId())) {
			
			System.out.println(conversationModel.getId());
			boolean t = messageService.deleteConversation(conversationModel.getId());
			
			if (t) {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1005));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1005);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
		}
	}
}
