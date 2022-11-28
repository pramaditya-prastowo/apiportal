package mii.bsi.apiportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mii.bsi.apiportal.domain.User;

@Data
@AllArgsConstructor
public class UserDTO {
    private String responseCode;
    private String responseMessage;
    private String id;
    private String firstName;
    private String lastName;
    private String corporateName;
    private boolean accountInactive;
    private boolean accountLocked;
    private Integer retryPasswordCount;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;

    private boolean emailVerified;

    private String emailVerifiedDate;

    public UserDTO() {
    }

    public UserDTO(String id) {
        this.id = id;
    }

//    public UserDTO(String responseCode, String responseMessage, String id, String firstName, String lastName,
//            String coorporateName, boolean accountInactive, boolean accountLocked, Integer retryPasswordCount,
//            String createBy, String createDate, String updateBy, String updateDate) {
//        this.responseCode = responseCode;
//        this.responseMessage = responseMessage;
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.coorporateName = coorporateName;
//        this.accountInactive = accountInactive;
//        this.accountLocked = accountLocked;
//        this.retryPasswordCount = retryPasswordCount;
//        this.createBy = createBy;
//        this.createDate = createDate;
//        this.updateBy = updateBy;
//        this.updateDate = updateDate;
//    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.corporateName = user.getCorporateName();
        this.accountInactive = user.isAccountInactive();
        this.accountLocked = user.isAccountLocked();
        this.retryPasswordCount = user.getRetryPasswordCount();
        this.createBy = user.getCreateBy();
        this.createDate = user.getCreateDate().toString();
        this.updateBy = user.getUpdateBy();
        this.updateDate = user.getUpdateDate().toString();
        this.emailVerified = user.isEmailVerified();
        this.emailVerifiedDate = user.getEmailVerifiedDate().toString();
    }

}
