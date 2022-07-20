package com.socialmedia.model;

import java.sql.Timestamp;

public class MessageModel {
	private Long id;
	private Timestamp createdDate;
	private String content;
	private Long conversationId;
	private boolean aToB;
	private boolean unread;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getConversationId() {
		return conversationId;
	}
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}
	public boolean isaToB() {
		return aToB;
	}
	public void setaToB(boolean aToB) {
		this.aToB = aToB;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	
}
