package com.socialmedia.response.friend;

import com.socialmedia.response.BaseResponse;

public class GetUserFriendsResp extends BaseResponse {
	private DataGetUserFriendsResp data;

	public DataGetUserFriendsResp getData() {
		return data;
	}

	public void setData(DataGetUserFriendsResp data) {
		this.data = data;
	}
	
}
