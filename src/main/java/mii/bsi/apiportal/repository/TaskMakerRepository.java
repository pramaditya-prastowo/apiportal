package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.User;
import mii.bsi.apiportal.domain.task.TaskMaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMakerRepository extends JpaRepository<TaskMaker, String> {

    @Query(value = "select * from bsi_task_maker where activity_id like %:code% ORDER BY activity_id DESC LIMIT 2", nativeQuery = true)
    List<TaskMaker> findByActivityIdContaining(String code);

//    @Query(value = "select * from bsi_task_maker where maker= ?1", nativeQuery = true)
    List<TaskMaker> findByMaker(User maker);
//    TaskMaker findBy
}
