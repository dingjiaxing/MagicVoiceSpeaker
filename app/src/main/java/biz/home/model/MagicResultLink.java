package biz.home.model;

import java.io.Serializable;

public class MagicResultLink implements Serializable {

	/**
	 * 返回APP信息Url实体
	 */
	private static final long serialVersionUID = 42984885737235220L;

	private String url;

	private String content;

	public MagicResultLink() {
	
	}
	public MagicResultLink(String content, String url) {
		this.content = content;
		this.url = url;
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
