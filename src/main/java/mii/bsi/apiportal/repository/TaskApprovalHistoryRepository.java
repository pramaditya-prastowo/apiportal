package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.task.TaskApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskApprovalHistoryRepository extends JpaRepository<TaskApprovalHistory,Long> {

}
