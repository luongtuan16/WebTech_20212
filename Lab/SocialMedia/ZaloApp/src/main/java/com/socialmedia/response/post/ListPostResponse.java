package com.socialmedia.response.post;

import java.util.List;

public class ListPostResponse {
	private Long id;
	private String described;
	private String created;
	private String modified;
	private int like;// so luong
	private int comment;
	private boolean is_liked;
	private List<String> image;
	private VideoGetPostResponse video;
	private AuthorGetPostResponse author;
	private String is_blocked;
	private String can_edit;
	private String can_comment;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescribed() {
		return described;
	}
	public void setDescribed(String described) {
		this.described = described;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public VideoGetPostResponse getVideo() {
		return video;
	}
	public void setVideo(VideoGetPostResponse video) {
		this.video = video;
	}
	public AuthorGetPostResponse getAuthor() {
		return author;
	}
	public void setAuthor(AuthorGetPostResponse author) {
		this.author = author;
	}
	public String getIs_blocked() {
		return is_blocked;
	}
	public void setIs_blocked(String is_blocked) {
		this.is_blocked = is_blocked;
	}
	public String getCan_edit() {
		return can_edit;
	}
	public void setCan_edit(String can_edit) {
		this.can_edit = can_edit;
	}
	public String getCan_comment() {
		return can_comment;
	}
	public void setCan_comment(String can_comment) {
		this.can_comment = can_comment;
	}
	
}
