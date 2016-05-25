package biz.home.model;

/**
 * Created by adg on 2015/7/17.
 */
public class VoiceText extends Personal {
    /**
     * 一般为提交方法
     */
    private String sub;
    /**
     * 要提交或者接收的文本
     */
    private String text;

    public VoiceText(String uid, String key, String sub, String text) {
        super(uid, key);
        this.sub = sub;
        this.text = text;
    }

    public VoiceText(String sub, String text) {
        this.sub = sub;
        this.text = text;
    }

    public VoiceText() {
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
