package com.socialmedia.request.post;

public class GetListPostRequest {
	private long last_id;//id cua bai viet moi nhat
	private long index; // start select
	private int count; //default = 20
	private int userId = -1;
	public long getLast_id() {
		return last_id;
	}
	public void setLast_id(long last_id) {
		this.last_id = last_id;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
