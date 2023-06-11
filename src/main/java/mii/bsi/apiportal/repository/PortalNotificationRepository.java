package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.PortalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortalNotificationRepository extends JpaRepository<PortalNotification, Long> {

    @Query(value = "select * from bsi_portal_notification where id_user = ?1 and is_read = ?2 " +
            "order by created_date desc limit ?3", nativeQuery = true)
    List<PortalNotification> findByIdUserAndIsReadByLimit(String idUser, boolean isRead, int limit );


    @Query(value = "select * from bsi_portal_notification where id_user = ?1 " +
            "order by created_date desc limit ?2", nativeQuery = true)
    List<PortalNotification> findByIdUserByLimit(String idUser, int limit );

    @Query(value = "select * from bsi_portal_notification where id_user = ?1 " +
            "order by created_date desc ", nativeQuery = true)
    List<PortalNotification> findByIdUserAll(String idUser);
}
