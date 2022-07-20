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
import com.socialmedia.model.MessageModel;
import com.socialmedia.request.conversation.CreateMessageReq;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IConversationService;
import com.socialmedia.service.IMessageService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.ConversationService;
import com.socialmedia.service.impl.MessageService;

@WebServlet("/api/create_message")
public class CreateMessageAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;
	private IMessageService messageService;

	public CreateMessageAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		conversationService = new ConversationService();
		messageService = new MessageService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = (String) req.getAttribute("token");
		CreateMessageReq request = gson.fromJson(req.getReader(), CreateMessageReq.class);

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null && accountModel.isActive()) {
			if (request.getContent() != null && request.getConversationId() != null && request.getPartnerId() != null) {
				ConversationModel conversationModel = conversationService.findOne(request.getConversationId());
				if (conversationModel != null) {
					MessageModel messageModel = new MessageModel();
					messageModel.setContent(request.getContent());
					messageModel.setConversationId(request.getConversationId());
					messageModel.setUnread(false);
					if (conversationModel.getAccountA() == accountModel.getId() && conversationModel.getAccountB() == request.getPartnerId()) {
						messageModel.setaToB(true);
						messageService.insertOne(messageModel);
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
					}
					else if(conversationModel.getAccountA() == request.getPartnerId() && conversationModel.getAccountB() == accountModel.getId())  {
						messageService.insertOne(messageModel);
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
						messageModel.setaToB(false);
					}
					else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
					}
					
				} else {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
				}

			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
		}

		resp.getWriter().print(gson.toJson(baseResponse));

	}
}
