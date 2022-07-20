package com.socialmedia.response.comment;

import com.socialmedia.response.BaseResponse;

public class GetCommentResponse extends BaseResponse {
	private DataGetCommentResponse data;
	private boolean is_blocked;
	public DataGetCommentResponse getData() {
		return data;
	}
	public void setData(DataGetCommentResponse data) {
		this.data = data;
	}
	public boolean isIs_blocked() {
		return is_blocked;
	}
	public void setIs_blocked(boolean is_blocked) {
		this.is_blocked = is_blocked;
	}
		
}
