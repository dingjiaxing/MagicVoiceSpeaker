package biz.home.bean;

import java.io.Serializable;

public class MagicResultText 
//extends MagicResult 
implements Serializable{
	/**
	 * 返回APP信息Text实体
	 */
	private static final long serialVersionUID = -3071627305589370145L;

	private String title;

	private String content;

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



}
