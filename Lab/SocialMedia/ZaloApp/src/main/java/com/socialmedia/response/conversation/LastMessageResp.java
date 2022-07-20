package com.socialmedia.response.conversation;

import java.sql.Timestamp;

public class LastMessageResp {
	private String message;
	private boolean unread;
	private Timestamp created;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	
}
