package com.socialmedia.model;

public class SearchModel extends BaseModel{
	private String keyword;
	private Long accountId;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
}
