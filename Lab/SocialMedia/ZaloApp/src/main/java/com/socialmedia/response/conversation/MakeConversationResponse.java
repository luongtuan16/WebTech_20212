package com.socialmedia.response.conversation;

import com.socialmedia.response.BaseResponse;

public class MakeConversationResponse extends BaseResponse {
	Long conversationId;

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	
}
