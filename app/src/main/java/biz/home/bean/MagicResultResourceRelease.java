package biz.home.bean;

import java.io.Serializable;
import java.util.List;

public class MagicResultResourceRelease implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7982937715303211682L;
	
	private String releaseId;
	private String postId;
	private String userId;
	private String releaseDate;
	private String title;
	private String telephone;
	private String browseCount;
	// 联系过的资源
	private List<MagicResultResourceContactToRelease> resourceContact;

	public String getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(String browseCount) {
		this.browseCount = browseCount;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
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
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<MagicResultResourceContactToRelease> getResourceContact() {
		return resourceContact;
	}
	public void setResourceContact(List<MagicResultResourceContactToRelease> resourceContact) {
		this.resourceContact = resourceContact;
	}
    
	

}
