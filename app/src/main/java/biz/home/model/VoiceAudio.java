package biz.home.model;

/**
 * Created by adg on 2015/7/17.
 */
public class VoiceAudio extends Personal {
    /**
     * 一般为提交方法
     */
    private String sub;
    /**
     * 要提交或者接收的文本
     */
    private String audio;

    public VoiceAudio(String uid, String key, String sub, String audio) {
        super(uid, key);
        this.sub = sub;
        this.audio = audio;
    }

    public VoiceAudio() {

    }

    public String getSub() {

        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
