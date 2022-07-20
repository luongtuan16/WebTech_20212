package com.socialmedia.response.comment;

import java.util.List;

import com.socialmedia.response.BaseResponse;

public class GetListCommentResponse extends BaseResponse {
	private boolean isBlocked;
	private List<DataGetCommentResponse> data;
	
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public List<DataGetCommentResponse> getData() {
		return data;
	}
	public void setData(List<DataGetCommentResponse> data) {
		this.data = data;
	}
	

}
