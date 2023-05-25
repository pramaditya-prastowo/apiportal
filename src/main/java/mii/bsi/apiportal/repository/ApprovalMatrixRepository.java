package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.ApprovalMatrix;
import mii.bsi.apiportal.domain.ApprovalMatrixDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalMatrixRepository extends JpaRepository<ApprovalMatrix, Long> {

    ApprovalMatrix findByMenuId(Long menuId);
}
