package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Notification;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface NotificationServiceInterface {
    void notifyUser(User user, String message, Quiz quiz);
    List<Notification> getNotificationsForUser(Long userId);
    void markAsRead(Long notificationId);
}
