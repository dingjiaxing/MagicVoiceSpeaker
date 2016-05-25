package biz.home.bean;

/**
 * Created by admin on 2016/2/3.
 */
public class ResourceReleaseContact {
    private String releaseId;
    private String userId;
    private String realName;
    private String contactTime;

    public ResourceReleaseContact() {
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }
}
