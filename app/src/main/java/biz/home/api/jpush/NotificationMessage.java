package biz.home.api.jpush;

/**
 * Created by admin on 2015/12/8.
 */
public class NotificationMessage {
    /**
     * 消息id
     */
    private Long notificationId;
    /**
     * 消息是否被打开过
     */
    private Boolean isOpen;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }
}
