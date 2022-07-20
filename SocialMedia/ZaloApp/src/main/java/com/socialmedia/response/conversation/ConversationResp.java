package com.socialmedia.response.conversation;

import java.sql.Timestamp;

public class ConversationResp {
	private String message;
	private Long message_id;
	private boolean unread;
	private Timestamp created;
	private SenderResp sender;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getMessage_id() {
		return message_id;
	}
	public void setMessage_id(Long message_id) {
		this.message_id = message_id;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public SenderResp getSender() {
		return sender;
	}
	public void setSender(SenderResp sender) {
		this.sender = sender;
	}
	
}
