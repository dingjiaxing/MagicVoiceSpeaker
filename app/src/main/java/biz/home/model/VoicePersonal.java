package biz.home.model;

/**
 * Created by adg on 2015/7/17.
 */
public class VoicePersonal extends Personal {
    private String type;

    public VoicePersonal() {
    }

    public VoicePersonal(String uid, String key, String type) {

        super(uid, key);
        this.type = type;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
