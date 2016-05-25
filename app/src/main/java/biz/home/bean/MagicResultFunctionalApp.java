package biz.home.bean;

import java.io.Serializable;

public class MagicResultFunctionalApp  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2058741110155863677L;

	/*
	 * 程序名称
	 */
	private String name;
	
	private String price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
