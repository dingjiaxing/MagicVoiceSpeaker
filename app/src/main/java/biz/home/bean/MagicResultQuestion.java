package biz.home.bean;

import java.io.Serializable;
import java.util.ArrayList;

import biz.home.model.MagicResultQuestionEnum;


public class MagicResultQuestion 
//extends MagicResult 
implements Serializable{
	
	/**
	 * 返回APP信息问答实体
	 */
	private static final long serialVersionUID = -7915477785503583263L;

	private String title;

	private String content;
	
	private MagicResultResourceInfo resource;
	
	private MagicResultQuestionEnum type;
	
	private ArrayList<MagicResultButton> button;

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

	public ArrayList<MagicResultButton> getButton() {
		return button;
	}

	public void setButton(ArrayList<MagicResultButton> button) {
		this.button = button;
	}

	public MagicResultResourceInfo getResource() {
		return resource;
	}

	public void setResource(MagicResultResourceInfo resource) {
		this.resource = resource;
	}

	public MagicResultQuestionEnum getType() {
		return type;
	}

	public void setType(MagicResultQuestionEnum type) {
		this.type = type;
	}

	

}
