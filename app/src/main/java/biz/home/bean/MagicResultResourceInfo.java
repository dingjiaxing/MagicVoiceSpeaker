package biz.home.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MagicResultResourceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2147463117843400349L;
	/*
	 * 资源标示
	 */
	String postId;
	/*
	 * 资源标题
	 */
	String title;
	/*
	 * 资源内容 url
	 */
	String content;
	/*
	 * 发送资源类型
	 */
	String resourceTypeName;
	/*
	 * 发送时间
	 */
	String dateTime;
	/*
	 * 资源发布人信息
	 */
	String userId;
	String realName;
	String companyName;
	String position;
	String telephone;
	String email;
	
	/*
	 * 收藏
	 */
	int favoritesCount;
	
	/*
	 * 点击
	 */
	int browseCount;
	
	/*
	 * 联系
	 */
	int contactCount;

	/*
	 * 声音
	 */
	String voicePath;

	
	/*
	 * 图片
	 */
	public Map<String, String> image;

	/*
	 * 确认
	 */
	private boolean confirm;
	/*
	 * 是否收藏
	 */
	boolean favoritesStatus;

	public boolean isFavoritesStatus() {
		return favoritesStatus;
	}

	public void setFavoritesStatus(boolean favoritesStatus) {
		this.favoritesStatus = favoritesStatus;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
	
	public Map<String, String> getImage() {
		return image;
	}

	public void setImage(Map<String, String> image) {
		this.image = image;
	}

	public int getFavoritesCount() {
		return favoritesCount;
	}

	public void setFavoritesCount(int favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

	public int getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(int browseCount) {
		this.browseCount = browseCount;
	}

	public int getContactCount() {
		return contactCount;
	}

	public void setContactCount(int contactCount) {
		this.contactCount = contactCount;
	}

	
	
	
}
