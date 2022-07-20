package com.socialmedia.api.conversation;

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
import com.socialmedia.model.ConversationModel;
import com.socialmedia.model.MessageModel;
import com.socialmedia.response.conversation.DataGetListConversationResp;
import com.socialmedia.response.conversation.GetListConversationResp;
import com.socialmedia.response.conversation.LastMessageResp;
import com.socialmedia.response.conversation.SenderResp;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IConversationService;
import com.socialmedia.service.IMessageService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.ConversationService;
import com.socialmedia.service.impl.MessageService;

@WebServlet("/api/get_list_conversation")
public class GetListConversationAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;
	private IMessageService messageService;

	public GetListConversationAPI() {
		accountService = new AccountService();
		genericService = new BaseService();
		conversationService = new ConversationService();
		messageService = new MessageService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();

		GetListConversationResp response = new GetListConversationResp();
		List<DataGetListConversationResp> datas = new ArrayList<DataGetListConversationResp>();

		String jwt = (String) req.getAttribute("token");
		String indexStr = req.getParameter("index");
		String countStr = req.getParameter("count");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel != null && accountModel.isActive()) {
			if (indexStr != null && countStr != null) {
				int index = Integer.valueOf(indexStr);
				int count = Integer.valueOf(countStr);
				if (index >= 0 && count >= 0) {
					int numNewMess = 0;
					List<ConversationModel> conversations = conversationService
							.findListConversation(accountModel.getId());
					for (int i = index; i < conversations.size() && i < index + count; i++) {
						DataGetListConversationResp data = new DataGetListConversationResp();
						SenderResp partner = new SenderResp();
						LastMessageResp lastMessage = new LastMessageResp();

						AccountModel partnerAccount = null;
						if (conversations.get(i).getAccountA() == accountModel.getId()) {
							partnerAccount = accountService.findById(conversations.get(i).getAccountB());
						} else {
							partnerAccount = accountService.findById(conversations.get(i).getAccountA());
						}
						if (partnerAccount != null) {
							partner.setId(partnerAccount.getId());
							partner.setAvatar(partnerAccount.getAvatar());
							partner.setUsername(partnerAccount.getName());

							MessageModel mess = messageService.findLastMessage(conversations.get(i).getId());
							if (mess!=null) {
								lastMessage.setMessage(mess.getContent());
								lastMessage.setCreated(mess.getCreatedDate());
								lastMessage.setUnread(mess.isUnread());
								if (mess.isUnread())
									numNewMess++;

								data.setLastMessage(lastMessage);
								data.setPartner(partner);
								data.setId(conversations.get(i).getId());

								datas.add(data);
							}
							
						}
						response.setCode(String.valueOf(BaseHTTP.CODE_1000));
						response.setMessage(BaseHTTP.MESSAGE_1000);
						response.setData(datas);
						response.setNumNewMessage(numNewMess);
					}
				} else {
					response.setCode(String.valueOf(BaseHTTP.CODE_1004));
					response.setMessage(BaseHTTP.MESSAGE_1004);
				}
			} else {
				response.setCode(String.valueOf(BaseHTTP.CODE_1002));
				response.setMessage(BaseHTTP.MESSAGE_1002);
			}
		} else {
			response.setCode(String.valueOf(BaseHTTP.CODE_9995));
			response.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(response));
	}
}
