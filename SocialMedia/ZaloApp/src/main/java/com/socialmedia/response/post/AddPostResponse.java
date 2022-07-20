package com.socialmedia.response.post;

import com.socialmedia.response.BaseResponse;

public class AddPostResponse extends BaseResponse {
	private DataAddPostResponse dataPostResponse;

	public DataAddPostResponse getDataPostResponse() {
		return dataPostResponse;
	}

	public void setDataPostResponse(DataAddPostResponse dataPostResponse) {
		this.dataPostResponse = dataPostResponse;
	}

}
