package com.socialmedia.response.like;

import com.socialmedia.response.BaseResponse;

public class LikesResponse extends BaseResponse{
	private DataLikesResponse dataLikesResponse;

	public DataLikesResponse getDataLikesResponse() {
		return dataLikesResponse;
	}

	public void setDataLikesResponse(DataLikesResponse dataLikesResponse) {
		this.dataLikesResponse = dataLikesResponse;
	}
	
}
