package biz.home.bean;

import java.io.Serializable;

import biz.home.model.MagicResultFunctionalCategoryEnum;
import biz.home.model.MagicResultFunctionalTeleOperatorEnum;


public class MagicResultFunctionalTelephone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193755871405488215L;

	/*
	 * 名称
	 */
	private String name;

	/*
	 * 电话号码
	 */
	private String code;

	/*
	 * 运营业商
	 */
	private MagicResultFunctionalTeleOperatorEnum teleOperator;

	/*
	 * 类型
	 */
	private MagicResultFunctionalCategoryEnum category;

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

	public MagicResultFunctionalTeleOperatorEnum getTeleOperator() {
		return teleOperator;
	}

	public void setTeleOperator(MagicResultFunctionalTeleOperatorEnum teleOperator) {
		this.teleOperator = teleOperator;
	}

	public MagicResultFunctionalCategoryEnum getCategory() {
		return category;
	}

	public void setCategory(MagicResultFunctionalCategoryEnum category) {
		this.category = category;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
