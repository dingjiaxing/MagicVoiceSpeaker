package biz.home.model;

/**
 * 
 * Redis的键枚举
 */
 
public enum RedisEnum {
				
	APPSESSIONID("APPSESSIONID:");//用户标识
	
	private String code;

	RedisEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
