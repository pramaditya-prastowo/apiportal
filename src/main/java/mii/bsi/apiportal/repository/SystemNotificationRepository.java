package mii.bsi.apiportal.repository;

import mii.bsi.apiportal.domain.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {
}
