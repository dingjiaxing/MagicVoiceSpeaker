package biz.home.model;

/*
 * 讯飞操作枚举
 */
public enum MagicResultOperationEnum {

	/*
	 * 打电话服务
	 */
	CALL("CALL"), 
	/*
	 * 发送信息
	 */
	SEND("SEND"),
	/*
	 * 创建提醒
	 */
	CREATE("CREATE"),
	
	/*
	 * 打开开始
	 */
	LAUNCH("LAUNCH"),
	
	/*
	 * 下载
	 */
	DOWNLOAD("DOWNLOAD"),
	
	
	/*
	 * 预定
	 */
	BOOK("BOOK"),
	
	/*
	 * 查找询问
	 */
	QUERY("QUERY");

	private String code;

	MagicResultOperationEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
