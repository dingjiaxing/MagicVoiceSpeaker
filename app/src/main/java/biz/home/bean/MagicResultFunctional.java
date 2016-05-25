package biz.home.bean;

import java.io.Serializable;

import biz.home.model.MagicResultFunctionalEnum;


/**
 * 功能性实体
 */
public class MagicResultFunctional implements Serializable {

	private static final long serialVersionUID = 384607584105356825L;

	private MagicResultFunctionalEnum type;
	
	private MagicResultFunctionalApp app;
	
	private MagicResultFunctionalMessage message;
	
	private MagicResultFunctionalSchedule schedule;
	
	private MagicResultFunctionalTelephone telephone;
	
	private MagicResultFunctionalMagicApp magicApp;

	public MagicResultFunctionalEnum getType() {
		return type;
	}

	public void setType(MagicResultFunctionalEnum type) {
		this.type = type;
	}

	public MagicResultFunctionalApp getApp() {
		return app;
	}

	public void setApp(MagicResultFunctionalApp app) {
		this.app = app;
	}

	public MagicResultFunctionalMessage getMessage() {
		return message;
	}

	public void setMessage(MagicResultFunctionalMessage message) {
		this.message = message;
	}

	public MagicResultFunctionalSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(MagicResultFunctionalSchedule schedule) {
		this.schedule = schedule;
	}

	public MagicResultFunctionalTelephone getTelephone() {
		return telephone;
	}

	public void setTelephone(MagicResultFunctionalTelephone telephone) {
		this.telephone = telephone;
	}

	public MagicResultFunctionalMagicApp getMagicApp() {
		return magicApp;
	}

	public void setMagicApp(MagicResultFunctionalMagicApp magicApp) {
		this.magicApp = magicApp;
	}
	
	
//	/**
//	 * 联系人等
//	 */
//	private String title;
//	
//	/**
//	 * 电话号码等
//	 */
//	private String code;
//	
//	/**
//	 * 短信内容。日程安排等。
//	 */
//	private String content;
//	
//	private String date;
//
//	public MagicResultFunctionalEnum getType() {
//		return type;
//	}
//
//	public void setType(MagicResultFunctionalEnum type) {
//		this.type = type;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getContent() {
//		return content;
//	}
//
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}
//
//	public String getDate() {
//		return date;
//	}
//
//	public void setDate(String date) {
//		this.date = date;
//	}
	
	
}
