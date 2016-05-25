
package biz.home.bean;

import java.io.Serializable;
import java.util.List;

import biz.home.model.MagicResultEnum;
import biz.home.model.MagicResultLink;
import biz.home.model.MagicResultResourcePush;
import biz.home.model.MagicResultSourceMoudleEnum;
import biz.home.model.MagicResultStatusEnum;
import biz.home.model.MagicResultUserInfoStatusEnum;
import biz.home.model.MagicUserInfo;

/**
 * 返回APP信实体类
 */
public class MagicResult implements Serializable {

	private static final long serialVersionUID = 3685474674250610072L;
	
	// 用户ID
	private String userId;
	@Deprecated
	private String uid;
	 // 信息状态
	private MagicResultStatusEnum status;
	// 验证
	private String token;
	 // 类型枚举
	private MagicResultEnum type;
	 // 问题
	private String question;
	 // 文本信息实体
	private MagicResultText resultText;
	// 链接地址实体
	private MagicResultLink resultUrl;
	// 问答实体
	private MagicResultQuestion resultQuestion;
	// 功能实体
	private MagicResultFunctional resultFunctional;
	// 操作动作枚举
	private String operation;
	// 来自那个模块
	private MagicResultSourceMoudleEnum sourceMoudle;
	// 异常处理
	private MagicResultException exception;
	// 轮询频率
	private Integer frequency;
	//用户信息
	private MagicUserInfo userInfo;
	//告诉app用户信息操作状态
	private MagicResultUserInfoStatusEnum userInfoStatus;
	//发布资源信息
	private MagicResultResourceInfo resourceInfo;
	// 是否显示退出场景按钮
	private boolean exitDialogButton;
	// 退出场景按钮名称
	private String exitDialogButtonName;
	// 是否有返回按钮
	private boolean backDialogButton;
	// 是否建议键盘输入
	private boolean popKeyboard;
    // 消息存储ID
	private String messageId;
	// 推送资源列表
	private List<MagicResultResourcePush> resourcePush;
	// 我联系过的资源
	private List<MagicResultResourceBrowse> resourceBrowse;
	// 我联系过的资源
	private List<MagicResultResourceContact> resourceContact;
	// 我收藏过的资源
	private List<MagicResultResourceFavorites> resourceFavorites;
	// 我发布过的资源列表
	private List<MagicResultResourceRelease> resourceRelease;
	// 安卓版本号
	private String androidVersion;
	// 安卓下载地址
	private String androidUrl;
	

//	// 问题固定答案选择
//	private List<MagicStandardAnswer> standardAnswer;
//
//	// 问题固定答案选择类型 false 单选 true 多选
//	private boolean chooseType;

//	public boolean isChooseType() {
//		return chooseType;
//	}
//
//	public void setChooseType(boolean chooseType) {
//		this.chooseType = chooseType;
//	}
//
//	public List<MagicStandardAnswer> getStandardAnswer() {
//		return standardAnswer;
//	}
//
//	public void setStandardAnswer(List<MagicStandardAnswer> standardAnswer) {
//		this.standardAnswer = standardAnswer;
//	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public boolean isPopKeyboard() {
		return popKeyboard;
	}

	public void setPopKeyboard(boolean popKeyboard) {
		this.popKeyboard = popKeyboard;
	}

	public String getUserId() {
		if (userId == null && uid != null)
			userId = uid;
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		this.uid = userId;
	}

	@Deprecated
	public String getUid() {
		return uid;
	}

	@Deprecated
	public void setUid(String uid) {
		this.uid = uid;
		this.userId = uid;
	}

	public MagicResultStatusEnum getStatus() {
		return status;
	}

	public void setStatus(MagicResultStatusEnum status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MagicResultEnum getType() {
		return type;
	}

	public void setType(MagicResultEnum type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public MagicResultText getResultText() {
		return resultText;
	}

	public void setResultText(MagicResultText resultText) {
		this.resultText = resultText;
	}

	public MagicResultLink getResultUrl() {
		return resultUrl;
	}

	public void setResultUrl(MagicResultLink resultUrl) {
		this.resultUrl = resultUrl;
	}

	public MagicResultQuestion getResultQuestion() {
		return resultQuestion;
	}

	public void setResultQuestion(MagicResultQuestion resultQuestion) {
		this.resultQuestion = resultQuestion;
	}

	public MagicResultFunctional getResultFunctional() {
		return resultFunctional;
	}

	public void setResultFunctional(MagicResultFunctional resultFunctional) {
		this.resultFunctional = resultFunctional;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public MagicResultSourceMoudleEnum getSourceMoudle() {
		return sourceMoudle;
	}

	public void setSourceMoudle(MagicResultSourceMoudleEnum sourceMoudle) {
		this.sourceMoudle = sourceMoudle;
	}

	public MagicResultException getException() {
		return exception;
	}

	public void setException(MagicResultException exception) {
		this.exception = exception;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public MagicUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(MagicUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public MagicResultUserInfoStatusEnum getUserInfoStatus() {
		return userInfoStatus;
	}

	public void setUserInfoStatus(MagicResultUserInfoStatusEnum userInfoStatus) {
		this.userInfoStatus = userInfoStatus;
	}

	public MagicResultResourceInfo getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(MagicResultResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public boolean isExitDialogButton() {
		return exitDialogButton;
	}

	public void setExitDialogButton(boolean exitDialogButton) {
		this.exitDialogButton = exitDialogButton;
	}

	public String getExitDialogButtonName() {
		return exitDialogButtonName;
	}

	public void setExitDialogButtonName(String exitDialogButtonName) {
		this.exitDialogButtonName = exitDialogButtonName;
	}

	public boolean isBackDialogButton() {
		return backDialogButton;
	}

	public void setBackDialogButton(boolean backDialogButton) {
		this.backDialogButton = backDialogButton;
	}

	public List<MagicResultResourcePush> getResourcePush() {
		return resourcePush;
	}

	public void setResourcePush(List<MagicResultResourcePush> resourcePush) {
		this.resourcePush = resourcePush;
	}

	public List<MagicResultResourceBrowse> getResourceBrowse() {
		return resourceBrowse;
	}

	public void setResourceBrowse(List<MagicResultResourceBrowse> resourceBrowse) {
		this.resourceBrowse = resourceBrowse;
	}

	public List<MagicResultResourceFavorites> getResourceFavorites() {
		return resourceFavorites;
	}
	
	public List<MagicResultResourceContact> getResourceContact() {
		return resourceContact;
	}

	public void setResourceContact(List<MagicResultResourceContact> resourceContact) {
		this.resourceContact = resourceContact;
	}

	public void setResourceFavorites(List<MagicResultResourceFavorites> resourceFavorites) {
		this.resourceFavorites = resourceFavorites;
	}

	public List<MagicResultResourceRelease> getResourceRelease() {
		return resourceRelease;
	}

	public void setResourceRelease(List<MagicResultResourceRelease> resourceRelease) {
		this.resourceRelease = resourceRelease;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getAndroidUrl() {
		return androidUrl;
	}

	public void setAndroidUrl(String androidUrl) {
		this.androidUrl = androidUrl;
	}
	
	

}
