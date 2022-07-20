package com.socialmedia.service.impl;

import java.util.List;

import com.socialmedia.dao.IConversationDAO;
import com.socialmedia.dao.impl.ConversationDAO;
import com.socialmedia.model.ConversationModel;
import com.socialmedia.service.IConversationService;

public class ConversationService implements IConversationService {
	private IConversationDAO conversationDAO;

	public ConversationService() {
		conversationDAO = new ConversationDAO();
	}

	@Override
	public Long insertOne(ConversationModel conversationModel) {
		return conversationDAO.insertOne(conversationModel);
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		return conversationDAO.deleteConversation(conversationId);
	}

	@Override
	public ConversationModel findOne(Long conversationId) {
		return conversationDAO.findOne(conversationId);
	}

	@Override
	public ConversationModel findOne(Long idA, Long idB) {
		ConversationModel conversationModel = conversationDAO.findOne(idA, idB);
		if (conversationModel == null)
			return conversationDAO.findOne(idB, idA);
		return conversationModel;
	}

	@Override
	public List<ConversationModel> findListConversation(Long accountId) {
		return conversationDAO.findListConversation(accountId);
	}
}
