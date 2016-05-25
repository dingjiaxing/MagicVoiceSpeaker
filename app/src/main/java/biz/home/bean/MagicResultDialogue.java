package biz.home.bean;

import java.io.Serializable;


public class MagicResultDialogue  implements Serializable{
	private static final long serialVersionUID = 384607584105356825L;
	
	/*
	 * 值
	 */
	private String value;
	
	/*
	 * 状态
	 */
	private int status;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {

       return status;	

	}

	public void setStatus(int status) {
		this.status = status;
	}


}
