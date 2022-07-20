package com.socialmedia.response.conversation;

import java.util.List;

public class DataGetConversationResp {
	private List<ConversationResp> conversation;
	private boolean is_blocked;
	public List<ConversationResp> getConversation() {
		return conversation;
	}
	public void setConversation(List<ConversationResp> conversation) {
		this.conversation = conversation;
	}
	public boolean isIs_blocked() {
		return is_blocked;
	}
	public void setIs_blocked(boolean is_blocked) {
		this.is_blocked = is_blocked;
	}
	
}
