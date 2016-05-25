package biz.home.model;

/*
 * 返回功能性枚举
 */
public enum MagicResultFunctionalEnum {

	SCHEDULE("SCHEDULE"), 
	APP("APP"),
	MESSAGE("MESSAGE"),
	TELEPHONE("TELEPHONE"),
	MAGICAPP("MAGICAPP");

	private String code;

	MagicResultFunctionalEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
