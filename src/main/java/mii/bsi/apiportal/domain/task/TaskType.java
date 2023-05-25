package mii.bsi.apiportal.domain.task;


import mii.bsi.apiportal.constant.NotificationPriority;

public enum TaskType {
    PENGAJUAN_KERJASAMA("Pengajuan Kerjasama", NotificationPriority.HIGH, "PK"),
    CREATE("Create", NotificationPriority.HIGH, "CR"),
    VIEW("View", NotificationPriority.HIGH, "VI"),
    EDIT("Edit", NotificationPriority.HIGH, "ED"),
    DELETE("Delete", NotificationPriority.HIGH, "DE");
//    RESET_PASSWORD("Reset Password", NotificationPriority.HIGH),
//    LOCK_USER("Lock User", NotificationPriority.HIGH),
//    UNLOCK_USER("Unlock User", NotificationPriority.HIGH),
//    ACTIVATE_CARD("Activate Card", NotificationPriority.HIGH),
//    LINK_CARD("Link Update", NotificationPriority.HIGH),
//    UPDATE_STATUS_CARD("Update Status Card", NotificationPriority.HIGH),
//    VA_ACTIVATION("VA Activation", NotificationPriority.HIGH),
//    VA_STATUS_UPDATE("VA Status Update", NotificationPriority.HIGH),
//    ACTIVATE_COMPANY("Activate Company", NotificationPriority.HIGH),
//    INACTIVATE_COMPANY("Inactivate Company", NotificationPriority.HIGH),
//    UPDATE_TOTAL_VA_ISSUANCE("Update VA Issuance", NotificationPriority.HIGH),
//    UPDATE_REWARD_PACKAGE_CONFIG("Update Reard Package", NotificationPriority.HIGH),
//    BLOCK_CARD("Block Card", NotificationPriority.HIGH),
//    UNBLOCK_CARD("Unblock Card", NotificationPriority.HIGH),
//    CLOSE_CARD("Close Card", NotificationPriority.HIGH),
//    CARD_STOCK_REQUEST("Card Stock Request", NotificationPriority.HIGH),
//    VA_LIMIT_UPDATE("VA Limit Update", NotificationPriority.HIGH),
//    VA_STATUS_UPDATE_BLOCK("VA Status Update block", NotificationPriority.HIGH),
//    VA_STATUS_UPDATE_CLOSE("VA Status Update close", NotificationPriority.HIGH),
//    VA_STATUS_UPDATE_UNBLOCK("VA Status Update unblock", NotificationPriority.HIGH),
//    CARD_REISSUE_PIN("Reissue PIN", NotificationPriority.HIGH),
//    UNLOCK_PIN("Unlock PIN", NotificationPriority.HIGH),
//    CARD_ACCEPTANCE_INSTANCE("Card Acceptance Instance", NotificationPriority.HIGH),
//    CARD_ACCEPTANCE_REGULAR("Card Acceptance Regular", NotificationPriority.HIGH);

    private final String text;

    private final NotificationPriority priority;
    private final String code;

    TaskType(String text, NotificationPriority priority, String code) {
        this.text = text;
        this.priority = priority;
        this.code  = code;
    }

    public String toString() {
        return this.text;
    }

    public NotificationPriority getPriority() {
        return this.priority;
    }

    public String getCode() {
        return this.code;
    }
}