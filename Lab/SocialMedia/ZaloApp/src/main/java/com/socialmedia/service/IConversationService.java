package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.ConversationModel;

public interface IConversationService {
	Long insertOne(ConversationModel conversationModel);

	// List<ConversationModel> findByPostId(Long postId);

	boolean deleteConversation(Long conversationId);

	List<ConversationModel> findListConversation(Long accountId);

	ConversationModel findOne(Long conversationId);

	ConversationModel findOne(Long idA, Long idB);
}
