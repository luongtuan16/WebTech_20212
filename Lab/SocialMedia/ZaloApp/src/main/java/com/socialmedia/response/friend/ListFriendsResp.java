package com.socialmedia.response.friend;

public class ListFriendsResp {
	private Long user_id;
	private String username;
	private String avatar;
	private int same_friends;
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getSame_friends() {
		return same_friends;
	}
	public void setSame_friends(int same_friends) {
		this.same_friends = same_friends;
	}
	
}
