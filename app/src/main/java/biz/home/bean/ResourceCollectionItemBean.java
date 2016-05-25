package biz.home.bean;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceCollectionItemBean {
    private String resourceId;
    private String resourceTitle;
    private String resourceReleasedPerson;
    private String collectionTime;

    public ResourceCollectionItemBean() {
    }

    public ResourceCollectionItemBean(String resourceId, String resourceTitle, String resourceReleasedPerson, String collectionTime) {
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.resourceReleasedPerson = resourceReleasedPerson;
        this.collectionTime = collectionTime;
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

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }
}
