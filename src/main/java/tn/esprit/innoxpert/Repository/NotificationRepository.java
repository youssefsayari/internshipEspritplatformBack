package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.Notification;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByDateEnvoiDesc(User user);
}
