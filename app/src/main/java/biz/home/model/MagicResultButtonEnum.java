package biz.home.model;

public enum MagicResultButtonEnum {

	/**
	 * 打开链接
	 */
	URL("URL"),
	/**
	 * 资源
	 */
	RESOURCEINFO("RESOURCEINFO"),
	/**
	 * 服务器处理
	 */
	SERVICE("SERVICE");

	
	private String code;

	MagicResultButtonEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
