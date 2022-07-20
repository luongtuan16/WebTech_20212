package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IConversationDAO;
import com.socialmedia.dao.IMessageDAO;
import com.socialmedia.dao.impl.ConversationDAO;
import com.socialmedia.dao.impl.MessageDAO;
import com.socialmedia.model.ConversationModel;
import com.socialmedia.model.MessageModel;
import com.socialmedia.service.IMessageService;

public class MessageService implements IMessageService {
	private IMessageDAO messageDAO;
	private IConversationDAO conversationDAO;

	public MessageService() {
		messageDAO = new MessageDAO();
		conversationDAO = new ConversationDAO();
	}

	@Override
	public Long insertOne(MessageModel messageModel) {
		messageModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		return messageDAO.insertOne(messageModel);
	}

	@Override
	public boolean deleteMessage(Long messageId) {
		return messageDAO.deleteMessage(messageId);
	}

	@Override
	public List<MessageModel> findConversation(Long conversationId) {
		return messageDAO.findConversation(conversationId);
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		return messageDAO.deleteConversation(conversationId);
	}

	@Override
	public MessageModel findOne(Long messId) {
		return messageDAO.findOne(messId);
	}

	@Override
	public MessageModel findLastMessage(Long conversationId) {
		List<MessageModel> messages = messageDAO.findConversation(conversationId);
		if (messages.size() > 0)
			return messages.get(0);
		return null;
	}

	@Override
	public List<MessageModel> searchMessage(String keyword, Long accountId) {
		List<MessageModel> messages = messageDAO.searchMessage(keyword);
		List<MessageModel> result = new ArrayList<MessageModel>();
		List<ConversationModel> conversations = conversationDAO.findListConversation(accountId);

		for (int i = 0; i < messages.size(); i++) {
			for (int j = 0; j < conversations.size(); j++)
				if (messages.get(i).getConversationId() == conversations.get(j).getId()) {
					result.add(messages.get(i));
					break;
				}
		}
		return result;
	}

}
