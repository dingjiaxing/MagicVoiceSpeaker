package biz.home.bean;

import java.io.Serializable;
import java.util.List;

import biz.home.api.jpush.MagicInfoJPushDevice;
import biz.home.model.MagicInfoApiEnum;
import biz.home.model.MagicUserInfo;


/*
 * App消息交互实体类
 */
public class MagicInfo implements Serializable{
	/**
	 * APP传输信息实体
	 */
	private static final long serialVersionUID = 4454288463532608722L;

	/*
	 * 用户标识
	 */
	private String uid;

	/*
	 * 验证
	 */	
	private String token;
	
	/*
	 * 发送内容
	 */
	private String text;
	
	/*
	 * 修改
	 */
	private boolean alter;
	
    /*
     * 临时会话ID
     */
	private String sessionId;

	
	/*
	 * 手机标识
	 */
	private String deviceId;

	
	/*
	 * 调用方法名称
	 */
	private MagicInfoApiEnum api;

	/*
	 *关于手机的位置信息
	 */
	private MagicMapLocation location;
	/*
	 *消息id
	 */
	//消息存储ID
	private String messageId;

	//是否退出会话模式
	private boolean exitDialog;

	//是否返回
	private boolean backDialog;

	private MagicInfoJPushDevice pushDevice;

	//排除已经有资源Id
	private List<String> excludePostId;
	//资源访问Id
	private String postId;
	//资源标题
	private String title;
	//资源发布人
	private String publisher;




	public MagicInfoJPushDevice getPushDevice() {
		return pushDevice;
	}

	public void setPushDevice(MagicInfoJPushDevice pushDevice) {
		this.pushDevice = pushDevice;
	}

	public boolean isExitDialog() {
		return exitDialog;
	}

	public void setExitDialog(boolean exitDialog) {
		this.exitDialog = exitDialog;
	}

	public boolean isBackDialog() {
		return backDialog;
	}

	public void setBackDialog(boolean backDialog) {
		this.backDialog = backDialog;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public MagicMapLocation getLocation() {
		return location;
	}

	public void setLocation(MagicMapLocation location) {
		this.location = location;
	}




	
//	private InetAddress ipAddress;
//	
//	private int port;

	/*
	 * 资源
	 */
	private MagicResultResourceInfo resourceInfo;
	/*
	 * 用户信息
	 */
	private MagicUserInfo userInfo;

	public MagicUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(MagicUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public MagicInfo(){

	}

	public MagicInfo(String uid, String token, String text, MagicMapLocation location, String deviceId, MagicInfoApiEnum api, MagicResultResourceInfo resourceInfo) {
		this.uid = uid;
		this.token = token;
		this.text = text;
		this.location=location;
		this.deviceId = deviceId;
		this.api = api;
		this.resourceInfo = resourceInfo;
	}
	public MagicInfo(String uid,String token,MagicInfoApiEnum api,MagicUserInfo userInfo){
		this.uid=uid;
		this.token=token;
		this.api=api;
		this.userInfo=userInfo;
	}

	//	public InetAddress getIpAddress() {
//		return ipAddress;
//	}
//
//	public void setIpAddress(InetAddress ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//
//	public int getPort() {
//		return port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
      


	public MagicInfoApiEnum  getApi() {
		return api;
	}

	public void setApi(MagicInfoApiEnum api) {
		this.api = api;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public MagicResultResourceInfo getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(MagicResultResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public boolean isAlter() {
		return alter;
	}

	public void setAlter(boolean alter) {
		this.alter = alter;
	}

	public List<String> getExcludePostId() {
		return excludePostId;
	}

	public void setExcludePostId(List<String> excludePostId) {
		this.excludePostId = excludePostId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
