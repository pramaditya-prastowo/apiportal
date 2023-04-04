package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.GroupsServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupServiceRepository extends JpaRepository<GroupsServiceEntity, Long> {

    List<GroupsServiceEntity> findByGroupType(String groupType);
}
