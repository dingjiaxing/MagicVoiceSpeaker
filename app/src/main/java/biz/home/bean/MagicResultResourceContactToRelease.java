package biz.home.bean;

import java.io.Serializable;

public class MagicResultResourceContactToRelease implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7982937715303211682L;
	
	private String userId;
	private String realName;
	private String contactDate;
	private String telephone;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContactDate() {
		return contactDate;
	}
	public void setContactDate(String contactDate) {
		this.contactDate = contactDate;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	
	
	

}
