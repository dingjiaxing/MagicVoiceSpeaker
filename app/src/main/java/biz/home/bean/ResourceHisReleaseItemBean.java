package biz.home.bean;

/**
 * Created by admin on 2016/2/4.
 */
public class ResourceHisReleaseItemBean {
    private String resourceId;
    private String resourceTitle;
    private String releaseTime;

    public ResourceHisReleaseItemBean() {
    }

    public ResourceHisReleaseItemBean(String resourceId, String resourceTitle, String releaseTime) {
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.releaseTime = releaseTime;
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

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
