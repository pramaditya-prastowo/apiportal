package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.ApprovalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalGroupRepository extends JpaRepository<ApprovalGroup, Long> {

    List<ApprovalGroup> findByMatrixDetailId(Long id);
}
