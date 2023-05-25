package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.ApprovalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApprovalGroupRepository extends JpaRepository<ApprovalGroup, Long> {

    List<ApprovalGroup> findByMatrixDetailId(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from bsi_approval_group u where u.matrix_detail_id = :id", nativeQuery = true)
    void deleteByMatrixDetailId(@Param("id") Long id);
}
