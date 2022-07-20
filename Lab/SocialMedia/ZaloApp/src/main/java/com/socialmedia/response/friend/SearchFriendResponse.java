package com.socialmedia.response.friend;

import java.util.List;

import com.socialmedia.response.BaseResponse;
import com.socialmedia.response.account.UserInfoResponse;

public class SearchFriendResponse extends BaseResponse {
	private List<UserInfoResponse> userInfoResponses;

	public List<UserInfoResponse> getUserInfoResponses() {
		return userInfoResponses;
	}

	public void setUserInfoResponses(List<UserInfoResponse> userInfoResponses) {
		this.userInfoResponses = userInfoResponses;
	}
	
}
