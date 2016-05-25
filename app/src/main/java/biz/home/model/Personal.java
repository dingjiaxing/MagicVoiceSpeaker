package biz.home.model;

/**
 * Created by adg on 2015/7/17.
 */
public class Personal {
    /**
     * 基本上就是用户的识别号。一般是手机号
     */
    private String uid;
    /**
     * 请求准许码
     */
    private String key;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Personal(String uid, String key) {
        this.uid = uid;
        this.key = key;
    }

    public Personal() {
    }
}
