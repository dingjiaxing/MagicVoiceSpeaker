package biz.home.model;

import java.io.Serializable;

public class MagicResultResourcePush implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4970867921309688260L;
	private String pushId;
	private String postId;
	/*
	 * 发布人userId
	 */
	private String userId;
	private String pushDate;
	private String title;
	private String publisher;


	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
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
	public String getPushDate() {
		return pushDate;
	}
	public void setPushDate(String pushDate) {
		this.pushDate = pushDate;
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
