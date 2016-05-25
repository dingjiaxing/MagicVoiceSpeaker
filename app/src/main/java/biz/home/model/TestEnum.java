package biz.home.model;
/**
 * 枚举例子
 */
public enum TestEnum {
	TYPE("111"),
	BANK("111001"),
	TENPAY("111002");
	
	private String code;

	TestEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
