package biz.home.bean;

import java.io.Serializable;

import biz.home.model.MagicResultFunctionalTeleOperatorEnum;


public class MagicResultFunctionalMessage implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5642775031458331486L;

	/*
	 * 号码归属地
	 */
	private String location;
	
	/*
	 * 联系人姓名
	 */
	private String name;
	
	/*
	 * 电话号码
	 */
	private String code;
	
	/*
	 * 短信内容
	 */
	private String content;
	
	/*
	 * 类型
	 */
	private String category;
	
	/*
	 * 运营商
	 */
	private MagicResultFunctionalTeleOperatorEnum teleOperator;
	
	/*
	 * 号段
	 */
	private String headNum;
	
	
	/*
	 * 尾号
	 */
	private String tailNum;

    /*
     * 短信内容类型
     */
	private String contentType;
	
    /*
     * 留言类型
     */
	private String messageType;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public MagicResultFunctionalTeleOperatorEnum getTeleOperator() {
		return teleOperator;
	}

	public void setTeleOperator(MagicResultFunctionalTeleOperatorEnum teleOperator) {
		this.teleOperator = teleOperator;
	}

	public String getHeadNum() {
		return headNum;
	}

	public void setHeadNum(String headNum) {
		this.headNum = headNum;
	}

	public String getTailNum() {
		return tailNum;
	}

	public void setTailNum(String tailNum) {
		this.tailNum = tailNum;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
