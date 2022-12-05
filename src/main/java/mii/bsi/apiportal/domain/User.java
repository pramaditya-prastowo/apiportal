package mii.bsi.apiportal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Table(name = "bsi_user_api_portal")
@Entity
@AllArgsConstructor
@Data
public class User implements Serializable {
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @NotEmpty(message = "firstName is required")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "lastName is required")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password is required")
    @Column(name = "password")
//    @Getter(AccessLevel.NONE)
    private String password;

    @Column(name = "corporate_name")
    private String corporateName;

    @Column(name = "account_inactive")
    private boolean accountInactive;

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "retry_password_count")
    private Integer retryPasswordCount;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    private boolean emailVerified;

//    @Column(name = "email_verified_date")
    private Date emailVerifiedDate;

    private boolean isLogin;

    @Column(columnDefinition = "ENUM('MITRA', 'SUPER_ADMIN')", name = "auth_principal")
    @Enumerated(EnumType.STRING)
    private Roles authPrincipal = Roles.MITRA;

    public User() {
    }

    public void generateCreated(String sequence){
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat timestamp = new SimpleDateFormat(pattern);
        String date = timestamp.format(new Date());
        String idUser = date.concat(StringUtils.leftPad(sequence, 4, "0"));
        setId(idUser);
        setAccountInactive(false);
        setAccountLocked(false);
        setRetryPasswordCount(0);
        setEmailVerified(false);
        setCreateDate(new Date());
    }

//    public User(String id, String firstName, String lastName, String email, String password, String coorporateName,
//            String accountInactive, String accountLokced, Integer retryPasswordCount, String createBy,
//            String createDate, String updateBy, String updateDate) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//        this.coorporateName = coorporateName;
//        this.accountInactive = accountInactive;
//        this.accountLokced = accountLokced;
//        this.retryPasswordCount = retryPasswordCount;
//        this.createBy = createBy;
//        this.createDate = createDate;
//        this.updateBy = updateBy;
//        this.updateDate = updateDate;
//    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getCoorporateName() {
//        return coorporateName;
//    }
//
//    public void setCoorporateName(String coorporateName) {
//        this.coorporateName = coorporateName;
//    }
//
//    public boolean getAccountInactive() {
//        return accountInactive;
//    }
//
//    public void setAccountInactive(boolean accountInactive) {
//        this.accountInactive = accountInactive;
//    }
//
//    public boolean getAccountLokced() {
//        return accountLokced;
//    }
//
//    public void setAccountLokced(boolean accountLokced) {
//        this.accountLokced = accountLokced;
//    }
//
//    public Integer getRetryPasswordCount() {
//        return retryPasswordCount;
//    }
//
//    public void setRetryPasswordCount(Integer retryPasswordCount) {
//        this.retryPasswordCount = retryPasswordCount;
//    }
//
//    public String getCreateBy() {
//        return createBy;
//    }
//
//    public void setCreateBy(String createBy) {
//        this.createBy = createBy;
//    }
//
//    public String getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(String createDate) {
//        this.createDate = createDate;
//    }
//
//    public String getUpdateBy() {
//        return updateBy;
//    }
//
//    public void setUpdateBy(String updateBy) {
//        this.updateBy = updateBy;
//    }
//
//    public String getUpdateDate() {
//        return updateDate;
//    }
//
//    public void setUpdateDate(String updateDate) {
//        this.updateDate = updateDate;
//    }

}
