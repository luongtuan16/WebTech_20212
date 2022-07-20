package com.socialmedia.response.account;

public class UserInfoResponse {
	private Long id;
	private String username;
	private long created;
	private String description;// null
	private String avatar;
	private String cover_image; // null
	private String link; // null
	private String address; // null
	private String city;// null
	private String country; // null
	private int listing;
	private boolean is_friend;
	private String online;// null

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	@Override
	public String toString() {
		return "UserInfoResponse [id=" + id + ", username=" + username + ", created=" + created + ", description="
				+ description + ", avatar=" + avatar + ", cover_image=" + cover_image + ", link=" + link + ", address="
				+ address + ", city=" + city + ", country=" + country + ", listing=" + listing + ", is_friend="
				+ is_friend + ", online=" + online + "]";
	}

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public int getListing() {
		return listing;
	}

	public void setListing(int listing) {
		this.listing = listing;
	}

	public boolean isIs_friend() {
		return is_friend;
	}

	public void setIs_friend(boolean is_friend) {
		this.is_friend = is_friend;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
