package com.socialmedia.response.conversation;

import java.util.List;

import com.socialmedia.response.BaseResponse;

public class GetListConversationResp extends BaseResponse {
	private List<DataGetListConversationResp> data;
	private int numNewMessage;
	public List<DataGetListConversationResp> getData() {
		return data;
	}
	public void setData(List<DataGetListConversationResp> data) {
		this.data = data;
	}
	public int getNumNewMessage() {
		return numNewMessage;
	}
	public void setNumNewMessage(int numNewMessage) {
		this.numNewMessage = numNewMessage;
	}
}
