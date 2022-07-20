package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.MessageModel;

public interface IMessageDAO {
	Long insertOne(MessageModel messageModel);

	boolean deleteMessage(Long messageId);

	List<MessageModel> findConversation(Long conversationId);
	
	MessageModel findOne(Long messId);

	boolean deleteConversation(Long conversationId);
	
	List<MessageModel> searchMessage(String keyword);
}
