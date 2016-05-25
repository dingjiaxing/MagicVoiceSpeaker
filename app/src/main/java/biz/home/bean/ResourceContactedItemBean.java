package biz.home.bean;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceContactedItemBean {
    private String resourceId;
    private String resourceTitle;
    private String resourceReleasedPerson;
    private String resourceContactTime;

    public ResourceContactedItemBean(String resourceId, String resourceTitle, String resourceReleasedPerson, String resourceContactTime) {
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.resourceReleasedPerson = resourceReleasedPerson;
        this.resourceContactTime = resourceContactTime;
    }

    public ResourceContactedItemBean() {
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceReleasedPerson() {
        return resourceReleasedPerson;
    }

    public void setResourceReleasedPerson(String resourceReleasedPerson) {
        this.resourceReleasedPerson = resourceReleasedPerson;
    }

    public String getResourceContactTime() {
        return resourceContactTime;
    }

    public void setResourceContactTime(String resourceContactTime) {
        this.resourceContactTime = resourceContactTime;
    }
}
