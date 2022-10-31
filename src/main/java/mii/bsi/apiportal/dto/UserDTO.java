package mii.bsi.apiportal.dto;

import mii.bsi.apiportal.domain.User;

public class UserDTO {
    private String responseCode;
    private String responseMessage;
    private String id;
    private String firstName;
    private String lastName;
    private String coorporateName;
    private String accountInactive;
    private String accountLocked;
    private Integer retryPasswordCount;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;

    public UserDTO() {
    }

    public UserDTO(String id) {
        this.id = id;
    }

    public UserDTO(String responseCode, String responseMessage, String id, String firstName, String lastName,
            String coorporateName, String accountInactive, String accountLocked, Integer retryPasswordCount,
            String createBy, String createDate, String updateBy, String updateDate) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.coorporateName = coorporateName;
        this.accountInactive = accountInactive;
        this.accountLocked = accountLocked;
        this.retryPasswordCount = retryPasswordCount;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.coorporateName = user.getCoorporateName();
        this.accountInactive = user.getAccountInactive();
        this.accountLocked = user.getAccountLokced();
        this.retryPasswordCount = user.getRetryPasswordCount();
        this.createBy = user.getCreateBy();
        this.createDate = user.getCreateDate();
        this.updateBy = user.getUpdateBy();
        this.updateDate = user.getUpdateDate();
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCoorporateName() {
        return coorporateName;
    }

    public void setCoorporateName(String coorporateName) {
        this.coorporateName = coorporateName;
    }

    public String getAccountInactive() {
        return accountInactive;
    }

    public void setAccountInactive(String accountInactive) {
        this.accountInactive = accountInactive;
    }

    public String getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(String accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Integer getRetryPasswordCount() {
        return retryPasswordCount;
    }

    public void setRetryPasswordCount(Integer retryPasswordCount) {
        this.retryPasswordCount = retryPasswordCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
