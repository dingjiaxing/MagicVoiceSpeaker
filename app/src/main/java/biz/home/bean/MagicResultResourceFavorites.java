package biz.home.bean;

import java.io.Serializable;

public class MagicResultResourceFavorites implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7982937715303211682L;
	
	private String favoritesId;
	private String postId;
	private String userId;
	private String favoritesDate;
	private String title;
	private String publisher;
	
	public String getFavoritesId() {
		return favoritesId;
	}
	public void setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFavoritesDate() {
		return favoritesDate;
	}
	public void setFavoritesDate(String favoritesDate) {
		this.favoritesDate = favoritesDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	
}
