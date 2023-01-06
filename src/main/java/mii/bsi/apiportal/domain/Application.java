package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    private String applicationName;
    private String image;
    private String description;
    private String phone;
    private String email;
    private String companyId;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
}
