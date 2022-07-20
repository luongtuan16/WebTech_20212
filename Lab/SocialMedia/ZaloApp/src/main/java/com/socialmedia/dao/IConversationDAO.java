package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.ConversationModel;

public interface IConversationDAO {
	Long insertOne(ConversationModel conversationModel);

	// List<ConversationModel> findByPostId(Long postId);

	boolean deleteConversation(Long conversationId);

	List<ConversationModel> findListConversation(Long accountId);

	ConversationModel findOne(Long conversationId);

	ConversationModel findOne(Long idA, Long idB);

	// boolean update(Long id,String content);

	// boolean deleteByPostId(Long postId);
}
