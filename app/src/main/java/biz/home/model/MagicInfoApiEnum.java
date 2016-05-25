package biz.home.model;

public enum MagicInfoApiEnum {
	
	/**
	 * 处理发送的消息
	 */
	SEND("SEND"),
	
	/**
	 * 修改用户信息
	 */
	UPDATEUSER("UPDATEUSER"),
	
	/**
	 * 轮询客服消息
	 */
	DIALOGMESSAGE("DIALOGMESSAGE"),
	
	/**
	 * 接收语音
	 */
	VOICE("VOICE"),
	
	/**
	 * 轮训间隔
	 */
	FREQUENCY("FREQUENCY"),
	
	/**
	 * 安卓版本信息
	 */
	ANDROIDVERSION("ANDROIDVERSION"),
	
	/**
	 * 修改数据库客服消息已读
	 */
	READMESSAGE("READMESSAGE"),
	
	/*
	 * 安卓下载地址
	 */
	ANDROIDAPP("ANDROIDAPP"),
	
	/**
	 * 查询我发布过的资源
	 */
	GETRESOURCERELEASE("GETRESOURCERELEASE"),
	
	/**
	 * 查询我收藏的资源
	 */
	GETRESOURCEFAVORITES("GETRESOURCEFAVORITES"),
	
	/**
	 * 查询浏览过的资源
	 */
	GETRESOURCEBROWSE("GETRESOURCEBROWSE"),
	
	/**
	 * 查询联系过的资源
	 */
	GETRESOURCECONTACT("GETRESOURCECONTACT"),
	
	/**
	 * 添加我收藏的资源
	 */
	ADDRESOURCEFAVORITES("ADDRESOURCEFAVORITES"),
	
	/**
	 * 添加浏览过的资源
	 */
	ADDRESOURCEBROWSE("ADDRESOURCEBROWSE"),
	
	/**
	 * 查询联系过的资源
	 */
	ADDRESOURCECONTACT("ADDRESOURCECONTACT"),
	
	/**
	 * 删除我收藏的资源
	 */
	REMOVERESOURCEFAVORITES("REMOVERESOURCEFAVORITES"),
	
	/*
	 * 打开app时，推送资源。
	 */
	GETRESOURCEPUSH("GETRESOURCEPUSH"),
	
	/*
	 * 阅读推送资源。
	 */
	RESOURCEPUSHREAD("RESOURCEPUSHREAD"),
	
	/*
	 * 资源图片上传
	 */
	RESOURCEIMAGETOUPLOAD("RESOURCEIMAGETOUPLOAD"),
	
	/*
	 * 获取资源信息
	 */
	GETRESOURCEINFO("GETRESOURCEINFO"),
	/*
	 *获取可拨打电话的数量
	 */
	GETRESOURCECALLCOUNT("GETRESOURCECALLCOUNT");
	
	
	private String code;

	MagicInfoApiEnum(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
