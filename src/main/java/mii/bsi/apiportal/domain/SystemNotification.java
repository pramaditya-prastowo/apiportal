package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_system_notification")
public class SystemNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    private Date notifDate;
    private String mitraId;
    private String mitraName;
    private String mediaType;
    private String mediaAddress;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String notifMessage;
    private String notifSubject;
    private String notifType;
    private boolean success;
    private String errorCode;
    private int retry;

}
