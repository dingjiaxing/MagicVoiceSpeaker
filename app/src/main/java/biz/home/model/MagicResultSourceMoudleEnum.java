package biz.home.model;


/*
 * 处理模块枚举
 */
public enum MagicResultSourceMoudleEnum {
	/*
	 * 讯飞
	 */
	FLYTEK("FLYTEK"), 
	
	/*
	 * 图灵
	 */
	TURING("TURING"), 
	
	/*
	 * 客服
	 */
	CUSTOMERSERVICE("CUSTOMERSERVICE"), 
	
	/*
	 * 资源
	 */
	RESOURCES("RESOURCES"),
	
	/*
	 * 对话注册
	 */
	USERREGISTER("USERREGISTER");
	
	
	private String code;

	MagicResultSourceMoudleEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
