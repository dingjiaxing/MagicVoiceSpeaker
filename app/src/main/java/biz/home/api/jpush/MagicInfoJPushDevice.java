package biz.home.api.jpush;

import java.io.Serializable;
import java.util.List;

public class MagicInfoJPushDevice implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4707484860687930869L;
	
	public String registrationID;
	public List<String> tag;
	public String alias;
	
	
	public String getRegistrationID() {
		return registrationID;
	}
	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	

	
	

}
