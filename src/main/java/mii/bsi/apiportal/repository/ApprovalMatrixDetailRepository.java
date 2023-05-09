package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.ApprovalMatrixDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApprovalMatrixDetailRepository extends JpaRepository<ApprovalMatrixDetail, Long> {

//    @Query(value="select * from bsi_approval_matrix_details where matrix_id = ?", nativeQuery = true)
    List<ApprovalMatrixDetail> findByMatrixId(Long id);
}
