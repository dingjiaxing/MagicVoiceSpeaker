package biz.home.model;

/*
 * 返回数据类型
 */
public enum MagicResultEnum { 
	
	/*
	 * 文本类型
	 */
	TEXT("TEXT"),
	/*
	 * 链接类型
	 */
	URL("URL"),
	/*
	 * 问答类型
	 */
	QUESTION("QUESTION"),
	/*
	 * 功能类型
	 */
	FUNCTIONAL("FUNCTIONAL"),
	/*
	 *发资源类型
	 */
	RESOURCEINFO("RESOURCEINFO");
	
	private String code;

	MagicResultEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
