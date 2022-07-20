package com.socialmedia.request.conversation;

public class GetConversationReq {
	private Long partner_id;
	private Long conversation_id;
	private int index;
	private int count;
	public Long getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(Long partner_id) {
		this.partner_id = partner_id;
	}
	public Long getConversation_id() {
		return conversation_id;
	}
	public void setConversation_id(Long conversation_id) {
		this.conversation_id = conversation_id;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}	
