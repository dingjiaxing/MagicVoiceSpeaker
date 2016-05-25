package biz.home.bean;

public enum MagicResultFunctionalMagicAppOpenEnum {

	/*
	 * 打开左边
	 */
	OPENRIGHTDRAWER("OPENRIGHTDRAWER"),
	
	/*
	 * 打开个人资料
	 */
	OPENUSERINFO("OPENUSERINFO"),
	
	/*
	 * 打开设置
	 */
	OPENSETTINGS("OPENSETTINGS");


	private String code;

	MagicResultFunctionalMagicAppOpenEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
