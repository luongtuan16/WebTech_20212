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
import com.socialmedia.model.BlocksModel;
import com.socialmedia.model.ConversationModel;
import com.socialmedia.model.MessageModel;
import com.socialmedia.request.conversation.GetConversationReq;
import com.socialmedia.response.conversation.ConversationResp;
import com.socialmedia.response.conversation.DataGetConversationResp;
import com.socialmedia.response.conversation.GetConversationResp;
import com.socialmedia.response.conversation.SenderResp;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlockUserService;
import com.socialmedia.service.IConversationService;
import com.socialmedia.service.IMessageService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlockUserService;
import com.socialmedia.service.impl.ConversationService;
import com.socialmedia.service.impl.MessageService;

@WebServlet("/api/get_conversation")
public class GetConversationAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IConversationService conversationService;
	private IMessageService messageService;
	private IBlockUserService blockUserService;

	public GetConversationAPI() {
		blockUserService = new BlockUserService();
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

		GetConversationReq request = gson.fromJson(req.getReader(), GetConversationReq.class);
		DataGetConversationResp data = new DataGetConversationResp();
		GetConversationResp response = new GetConversationResp();
		List<ConversationResp> conversations = new ArrayList<ConversationResp>();

		//String jwt = req.getParameter("token");
		String jwt = (String) req.getAttribute("token");
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (accountModel != null && accountModel.isActive()) {
			ConversationModel conversationModel = null;
			if (request.getConversation_id() != null) {
				conversationModel = conversationService.findOne(request.getConversation_id());
			} else if (request.getPartner_id() != null) {
				conversationModel = conversationService.findOne(accountModel.getId(), request.getPartner_id());
			} else {
				response.setCode(String.valueOf(BaseHTTP.CODE_1002));
				response.setMessage(BaseHTTP.MESSAGE_1002);
			}
			if (conversationModel != null) {
				AccountModel accountA = null;
				AccountModel accountB = null;
				Long id2 = 0L;
				Long idA = conversationModel.getAccountA();
				Long idB = conversationModel.getAccountB();
				if (idA == accountModel.getId()) {
					accountA = accountModel;
					accountB = accountService.findById(idB);
					id2 = idB;
				} else {
					accountB = accountModel;
					accountA = accountService.findById(idA);
					id2 = idA;
				}

				if (accountB != null && accountB.isActive()) {
					if (request.getIndex() >= 0 && request.getCount() > 0) {
						List<MessageModel> messages = messageService.findConversation(conversationModel.getId());
						int index = request.getIndex();
						int count = request.getCount();

						for (int i = index; i < messages.size() && i < index + count; i++) {
							ConversationResp conversationResp = new ConversationResp();
							SenderResp senderResp = new SenderResp();

							if (messages.get(i).isaToB()) {
								senderResp.setId(accountA.getId());
								senderResp.setAvatar(accountA.getAvatar());
								senderResp.setUsername(accountA.getName());
							} else {
								senderResp.setId(accountB.getId());
								senderResp.setAvatar(accountB.getAvatar());
								senderResp.setUsername(accountB.getName());
							}

							conversationResp.setMessage(messages.get(i).getContent());
							conversationResp.setMessage_id(messages.get(i).getId());
							conversationResp.setCreated(messages.get(i).getCreatedDate());
							conversationResp.setUnread(messages.get(i).isUnread());
							conversationResp.setSender(senderResp);

							conversations.add(conversationResp);
						}
						BlocksModel blocksModel = blockUserService.findOne(id2, accountModel.getId());

						data.setConversation(conversations);
						if (blocksModel == null)
							data.setIs_blocked(false);
						else {
							data.setIs_blocked(true);
						}
						response.setCode(String.valueOf(BaseHTTP.CODE_1000));
						response.setMessage(BaseHTTP.MESSAGE_1000);
						response.setData(data);
					} else {
						response.setCode(String.valueOf(BaseHTTP.CODE_1002));
						response.setMessage(BaseHTTP.MESSAGE_1002);
					}
				} else {
					response.setCode(String.valueOf(BaseHTTP.CODE_9995));
					response.setMessage(BaseHTTP.MESSAGE_9995);
				}

			} else {
				response.setCode(String.valueOf(BaseHTTP.CODE_1000));
				response.setMessage(BaseHTTP.MESSAGE_1000);
				response.setData(null);
			}

		} else {
			response.setCode(String.valueOf(BaseHTTP.CODE_9995));
			response.setMessage(BaseHTTP.MESSAGE_9995);
		}

		resp.getWriter().print(gson.toJson(response));
	}
}
