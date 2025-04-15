package tn.esprit.innoxpert.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Notification;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.NotificationRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void notifyUser(User user, String message, Quiz quiz) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setDateEnvoi(new Date());
        notification.setVue(false);
        notification.setUser(user);
        notification.setQuiz(quiz);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? notificationRepository.findByUserOrderByDateEnvoiDesc(user) : null;
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setVue(true);
            notificationRepository.save(notification);
        }
    }
}
