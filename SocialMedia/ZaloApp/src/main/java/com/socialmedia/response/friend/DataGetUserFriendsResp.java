package com.socialmedia.response.friend;

import java.util.List;

public class DataGetUserFriendsResp {
	private List<FrsDataGUFrsResp> friends;
	private int total;
	public List<FrsDataGUFrsResp> getFriends() {
		return friends;
	}
	public void setFriends(List<FrsDataGUFrsResp> friends) {
		this.friends = friends;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
