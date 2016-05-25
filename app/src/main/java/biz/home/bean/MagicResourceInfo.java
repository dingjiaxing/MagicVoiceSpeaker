package biz.home.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MagicResourceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2147463117843400349L;
	
	/*
	 * 标题
	 */
	public String title;
	
	/*
	 * 内容
	 */
	public String content;
	
	/*
	 * 确认
	 */
	private boolean confirm;
	/*
 	* 图片
	 */
	public Map<String,String> image;

	public Map<String, String> getImage() {
		return image;
	}

	public void setImage(Map<String, String> image) {
		this.image = image;
	}

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

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
	
}
