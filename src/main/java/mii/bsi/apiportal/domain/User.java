package mii.bsi.apiportal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import mii.bsi.apiportal.domain.model.Roles;
import mii.bsi.apiportal.domain.model.TokenVerificationType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

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

    @NotEmpty(message = "Corporate Name is required")
    @Column(name = "corporate_name")
    private String corporateName;

    @Column(name = "mobile_phone")
    private String mobilePhone;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "group_id")
//    private Groups groups;

//    @Column(columnDefinition = "ENUM('MITRA', 'SUPER_ADMIN', 'ADMIN')", name = "auth_principal")

    @Enumerated(EnumType.STRING)
    private Roles authPrincipal = Roles.MITRA;

    private Long groupId;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photoProfile;


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

    public Long getGroupId(){
        return groupId;
    }
    @Transient
    public String getFullName(){
        return this.firstName + " "+ this. lastName;
    }

}
