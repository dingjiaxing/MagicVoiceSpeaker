package biz.home.bean;

import java.io.Serializable;

    /**
     * 对话注册上下文关联(redis 缓存)
     * 
     * status：-1 发信息  0收信息  1完成  3种状态
     * @author 2015-7-22
     *
     */
public class MagicDialogueRegister implements Serializable{
	private static final long serialVersionUID = 384607584105356825L;
	
	/*
	 * 用户id
	 * 
	 * value值
	 * status状态  -1 未开始  0准备中 1已完成
	 * MagicResultDialogue对象
	 */
	private MagicResultDialogue uid;
	
	
	/*
	 * 手机号telephone
	 */
	private MagicResultDialogue telephone;

    /*
     * 验证码
     */
    private MagicResultDialogue check;
    
    /*
     * 真名
     */
    private MagicResultDialogue realName;
    
    /*
     * 用户唯一识别id
     */
    private MagicResultDialogue sessionId;

    
	public MagicResultDialogue getUid() {
		return uid;
	}

	public void setUid(MagicResultDialogue uid) {
		this.uid = uid;
	}

	public MagicResultDialogue getTelephone() {
		return telephone;
	}

	public void setTelephone(MagicResultDialogue telephone) {
		this.telephone = telephone;
	}

	public MagicResultDialogue getCheck() {
		return check;
	}

	public void setCheck(MagicResultDialogue check) {
		this.check = check;
	}

	public MagicResultDialogue getRealName() {
		return realName;
	}

	public void setRealName(MagicResultDialogue realName) {
		this.realName = realName;
	}

	public MagicResultDialogue getSessionId() {
		return sessionId;
	}

	public void setSessionId(MagicResultDialogue sessionId) {
		this.sessionId = sessionId;
	}
    
	
    

}
