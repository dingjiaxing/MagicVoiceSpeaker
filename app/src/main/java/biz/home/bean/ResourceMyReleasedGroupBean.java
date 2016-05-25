package biz.home.bean;

/**
 * Created by admin on 2016/1/29.
 */
public class ResourceMyReleasedGroupBean {
    private String groupResourceId;
    private String groupTitle;
    private String groupBrowseCount;
    private int groupContactCount;

    public ResourceMyReleasedGroupBean() {
    }

    public ResourceMyReleasedGroupBean(String groupResourceId, String groupTitle) {
        this.groupResourceId = groupResourceId;
        this.groupTitle = groupTitle;
    }

    public ResourceMyReleasedGroupBean(String groupResourceId, String groupTitle, String groupBrowseCount) {
        this.groupResourceId = groupResourceId;
        this.groupTitle = groupTitle;
        this.groupBrowseCount = groupBrowseCount;
    }

    public int getGroupContactCount() {
        return groupContactCount;
    }

    public void setGroupContactCount(int groupContactCount) {
        this.groupContactCount = groupContactCount;
    }

    public String getGroupBrowseCount() {
        return groupBrowseCount;
    }

    public void setGroupBrowseCount(String groupBrowseCount) {
        this.groupBrowseCount = groupBrowseCount;
    }

    public String getGroupResourceId() {
        return groupResourceId;
    }

    public void setGroupResourceId(String groupResourceId) {
        this.groupResourceId = groupResourceId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
