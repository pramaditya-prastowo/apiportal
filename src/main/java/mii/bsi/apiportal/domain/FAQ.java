package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Table(name = "bsi_faq_api_portal")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id", updatable = false, unique = true)
    private Long id;
    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Question is required")
    private String question;
    @NotEmpty(message = "Answer is required")
    private String answer;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
}
