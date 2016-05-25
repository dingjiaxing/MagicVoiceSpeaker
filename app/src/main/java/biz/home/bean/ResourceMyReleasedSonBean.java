package biz.home.bean;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceMyReleasedSonBean {
    private String userid;
    private String name;
    private String contactTime;

    public ResourceMyReleasedSonBean() {
    }

    public ResourceMyReleasedSonBean(String userid, String name, String contactTime) {
        this.userid = userid;
        this.name = name;
        this.contactTime = contactTime;
    }

    public ResourceMyReleasedSonBean(String userid, String name, String contactTime, String tel) {
        this.userid = userid;
        this.name = name;
        this.contactTime = contactTime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }
}
