package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.Groups;
import mii.bsi.apiportal.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {

    List<Groups> findByScope(String paramString);
}
