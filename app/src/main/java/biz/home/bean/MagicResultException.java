package biz.home.bean;

import java.io.Serializable;

/*
 * 异常消息实体
 */
public class MagicResultException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6126580439628532201L;
	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
