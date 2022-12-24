package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "bsi_need_help_api_portal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedHelp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Phone Number is required")
    private String mobilePhone;
    @NotEmpty(message = "Company Name is required")
    private String companyName;
    @NotEmpty(message = "Company Type is required")
    private String companyType;
    @NotEmpty(message = "Message is required")
    private String message;
    private String status;
    private Date createDate;
    private Date updateDate;

}
