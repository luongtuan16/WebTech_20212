package com.socialmedia.response.friend;

import com.socialmedia.response.BaseResponse;

public class SetFriendResponse extends BaseResponse {
	private DataSetFriendResponse data;

	public DataSetFriendResponse getData() {
		return data;
	}

	public void setData(DataSetFriendResponse data) {
		this.data = data;
	}
}
