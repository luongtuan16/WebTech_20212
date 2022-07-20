package com.socialmedia.response.conversation;

import com.socialmedia.response.BaseResponse;

public class GetConversationResp extends BaseResponse {
	private DataGetConversationResp data;

	public DataGetConversationResp getData() {
		return data;
	}

	public void setData(DataGetConversationResp data) {
		this.data = data;
	}
	
}
