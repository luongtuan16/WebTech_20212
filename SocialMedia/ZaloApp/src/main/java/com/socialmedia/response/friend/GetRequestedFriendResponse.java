package com.socialmedia.response.friend;

import com.socialmedia.request.friend.DataGetRequestedFriend;
import com.socialmedia.response.BaseResponse;

public class GetRequestedFriendResponse extends BaseResponse {
	private DataGetRequestedFriend data;

	public DataGetRequestedFriend getData() {
		return data;
	}

	public void setData(DataGetRequestedFriend data) {
		this.data = data;
	}
	
	
}
