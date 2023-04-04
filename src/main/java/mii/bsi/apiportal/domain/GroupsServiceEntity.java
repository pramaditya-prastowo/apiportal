package mii.bsi.apiportal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Data
@Transactional
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bsi_groups_service_api_portal")
public class GroupsServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group", updatable = false, unique = true)
    private Long id;
    private String groupName;
    private String groupType;
    @OneToMany(mappedBy = "groupServiceApi", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<ServiceApiDomain> services = new ArrayList<>();
}
