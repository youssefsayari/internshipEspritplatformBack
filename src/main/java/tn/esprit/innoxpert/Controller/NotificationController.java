package tn.esprit.innoxpert.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Notification;
import tn.esprit.innoxpert.Service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public List<Notification> getNotificationsForUser(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @PutMapping("/read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }
}
