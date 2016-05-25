package biz.home.model;


public enum MagicResultUserInfoStatusEnum {

	/**
	 * 返回空值
	 */
	NULL("null"),
	
	/**
	 * 返回插入值
	 */
	INSERT("INSERT"),
	
	/**
	 * 返回修改值
	 */
	UPDATE("UPDATE");
	
	
	
	
	private String code;

	MagicResultUserInfoStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
