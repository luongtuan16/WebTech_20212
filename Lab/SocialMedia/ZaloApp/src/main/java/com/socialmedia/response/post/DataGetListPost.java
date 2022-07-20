package com.socialmedia.response.post;

import java.util.List;

public class DataGetListPost {
	private List<ListPostResponse> posts;
	private int new_items;
	private Long last_id;
	public List<ListPostResponse> getPosts() {
		return posts;
	}
	public void setPosts(List<ListPostResponse> posts) {
		this.posts = posts;
	}
	public int getNew_items() {
		return new_items;
	}
	public void setNew_items(int new_items) {
		this.new_items = new_items;
	}
	public Long getLast_id() {
		return last_id;
	}
	public void setLast_id(Long last_id) {
		this.last_id = last_id;
	}
}
