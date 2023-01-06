package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {

    List<Groups> findByScope(String paramString);
}
