package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.GroupsServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupServiceRepository extends JpaRepository<GroupsServiceEntity, Long> {

}
