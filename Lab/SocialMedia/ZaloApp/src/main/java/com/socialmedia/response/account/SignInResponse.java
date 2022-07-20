package com.socialmedia.response.account;

import com.socialmedia.response.BaseResponse;

public class SignInResponse extends BaseResponse{
	private DataSignInResponse data;

	public DataSignInResponse getData() {
		return data;
	}

	public void setData(DataSignInResponse data) {
		this.data = data;
	}
	
	
}
