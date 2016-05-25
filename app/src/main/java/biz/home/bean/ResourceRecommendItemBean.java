package biz.home.bean;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceRecommendItemBean {
    private String resourceId;
    private String resourceTitle;
    private String resourceReleasedPerson;
    private String resourceReleasedTime;

    public ResourceRecommendItemBean() {
    }

    public ResourceRecommendItemBean(String resourceId, String resourceTitle, String resourceReleasedPerson, String resourceReleasedTime) {
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.resourceReleasedPerson = resourceReleasedPerson;
        this.resourceReleasedTime = resourceReleasedTime;
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

    public String getResourceReleasedTime() {
        return resourceReleasedTime;
    }

    public void setResourceReleasedTime(String resourceReleasedTime) {
        this.resourceReleasedTime = resourceReleasedTime;
    }
}
