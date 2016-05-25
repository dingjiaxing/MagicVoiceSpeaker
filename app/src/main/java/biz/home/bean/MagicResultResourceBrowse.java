package biz.home.bean;

import java.io.Serializable;

public class MagicResultResourceBrowse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7982937715303211682L;
	
	private String browseId;
	private String postId;
	private String userId;
	private String browseDate;
	private String title;
	private String publisher;
	
	public String getBrowseId() {
		return browseId;
	}
	public void setBrowseId(String browseId) {
		this.browseId = browseId;
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
	public String getBrowseDate() {
		return browseDate;
	}
	public void setBrowseDate(String browseDate) {
		this.browseDate = browseDate;
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
