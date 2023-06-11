package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.ApprovalMatrixDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApprovalMatrixDetailRepository extends JpaRepository<ApprovalMatrixDetail, Long> {

    @Query(value="select * from bsi_approval_matrix_details where matrix_id = ? order by sequence asc", nativeQuery = true)
    List<ApprovalMatrixDetail> findByMatrixId(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from bsi_approval_matrix_details u  where u.matrix_id = :id ", nativeQuery = true)
    void deleteByMatrixId(@Param("id") Long id);
}
