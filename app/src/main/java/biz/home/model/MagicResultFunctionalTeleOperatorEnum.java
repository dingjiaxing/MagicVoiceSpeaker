package biz.home.model;

/*
 * 讯飞电话枚举
 */
public enum MagicResultFunctionalTeleOperatorEnum {
	
	/*
	 * 中国移动
	 */
	CHINAMOBILE("CHINAMOBILE"), 
	/*
	 * 中国电信
	 */
	CHINATELECOM("CHINATELECOM"),
	/*
	 * 中国联通
	 */
	CHINAUNICOM("CHINAUNICOM");

	private String code;

	MagicResultFunctionalTeleOperatorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
