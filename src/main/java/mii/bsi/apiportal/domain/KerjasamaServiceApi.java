package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_kerjasama_service_api_portal")
public class KerjasamaServiceApi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    private Long pekerId;
    private Long serviceApiId;
    @Transient
    private String serviceApiName;
}

