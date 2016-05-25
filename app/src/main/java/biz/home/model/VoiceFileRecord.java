package biz.home.model;

/**
 * Created by admin on 2015/12/24.
 */
public class VoiceFileRecord {
    private String messageId;
    private String path;
    private Boolean isUpload;

    public VoiceFileRecord() {
    }

    public VoiceFileRecord(String messageId, String path, Boolean isUpload) {
        this.messageId = messageId;
        this.path = path;
        this.isUpload = isUpload;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getUpload() {
        return isUpload;
    }

    public void setUpload(Boolean upload) {
        isUpload = upload;
    }
}
