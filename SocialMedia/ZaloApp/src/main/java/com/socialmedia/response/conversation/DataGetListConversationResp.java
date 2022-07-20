package com.socialmedia.response.conversation;

public class DataGetListConversationResp {
	private Long id;
	private SenderResp partner;
	private LastMessageResp lastMessage;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SenderResp getPartner() {
		return partner;
	}
	public void setPartner(SenderResp partner) {
		this.partner = partner;
	}
	public LastMessageResp getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(LastMessageResp lastMessage) {
		this.lastMessage = lastMessage;
	}
	
}
