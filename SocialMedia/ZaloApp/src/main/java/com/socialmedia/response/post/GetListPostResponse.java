package com.socialmedia.response.post;

import com.socialmedia.response.BaseResponse;

public class GetListPostResponse extends BaseResponse {
	private DataGetListPost data;
	private String in_campaign;
	private String campaign_id;
	public DataGetListPost getData() {
		return data;
	}
	public void setData(DataGetListPost data) {
		this.data = data;
	}
	public String getIn_campaign() {
		return in_campaign;
	}
	public void setIn_campaign(String in_campaign) {
		this.in_campaign = in_campaign;
	}
	public String getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(String campaign_id) {
		this.campaign_id = campaign_id;
	}
	
}
