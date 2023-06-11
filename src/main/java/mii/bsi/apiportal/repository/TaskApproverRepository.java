package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.task.TaskApprover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskApproverRepository extends JpaRepository<TaskApprover, Long> {
//    @Query(value = "select * from bsi_task_approver where approver= ?1", nativeQuery = true)
    List<TaskApprover> findByApprover(User approver);

    List<TaskApprover> findByEntityIdAndEntityName(String entityId, String entityName);
    @Query(value = "select * from bsi_task_approver where activity_id = ?1 order by level_approver", nativeQuery = true)
    List<TaskApprover> findByActivityId(String activityId);
}
