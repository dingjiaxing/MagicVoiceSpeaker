package biz.home.bean;

import java.io.Serializable;

import biz.home.model.MagicResultButtonEnum;


public class MagicResultButton implements Serializable{

	/**
	 * 返回APP信息问答按钮实体
	 */
	private static final long serialVersionUID = -2597448323021893136L;

	private String name;
	
	private MagicResultButtonEnum type;
	
	private String url;

	private String content;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MagicResultButtonEnum getType() {
		return type;
	}

	public void setType(MagicResultButtonEnum type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
