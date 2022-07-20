package com.socialmedia.request.friend;

import java.util.List;

import com.socialmedia.response.friend.FriendsResponse;

public class DataGetRequestedFriend {
	private List<FriendsResponse> friends;

	public List<FriendsResponse> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendsResponse> friends) {
		this.friends = friends;
	}

}
