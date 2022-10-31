package mii.bsi.apiportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bsi_user_api_portal")
@Entity
public class User {
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "coorporate_name")
    private String coorporateName;

    @Column(name = "account_inactive")
    private String accountInactive;

    @Column(name = "account_locked")
    private String accountLokced;

    @Column(name = "retry_password_count")
    private Integer retryPasswordCount;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private String updateDate;

    public User(String id, String firstName, String lastName, String email, String password, String coorporateName,
            String accountInactive, String accountLokced, Integer retryPasswordCount, String createBy,
            String createDate, String updateBy, String updateDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.coorporateName = coorporateName;
        this.accountInactive = accountInactive;
        this.accountLokced = accountLokced;
        this.retryPasswordCount = retryPasswordCount;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAccountLokced() {
        return accountLokced;
    }

    public void setAccountLokced(String accountLokced) {
        this.accountLokced = accountLokced;
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
