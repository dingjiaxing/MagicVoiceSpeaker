package biz.home.model;

/*
 * 讯飞电话枚举
 */
public enum MagicResultFunctionalCategoryEnum {
	
	/*
	 * 未接来电
	 */
	MISSED("MISSED"), 
	/*
	 * 已拨电话
	 */
	DIALED("DIALED"),
	/*
	 * 已接电话
	 */
	RECEIVED("RECEIVED");

	private String code;

	MagicResultFunctionalCategoryEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
