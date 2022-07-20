package com.socialmedia.request.conversation;

public class CreateMessageReq {
	private Long partnerId;
	private Long conversationId;
	private String content;
	public Long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	public Long getConversationId() {
		return conversationId;
	}
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public CreateMessageReq(Long partnerId, Long conversationId, String content) {
		super();
		this.partnerId = partnerId;
		this.conversationId = conversationId;
		this.content = content;
	}
	
}
