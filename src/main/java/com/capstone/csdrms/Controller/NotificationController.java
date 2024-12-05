package com.capstone.csdrms.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.NotificationEntity;
import com.capstone.csdrms.Entity.UserNotification;
import com.capstone.csdrms.Service.NotificationService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/notifications")
public class NotificationController {
	
	
	@Autowired
    private NotificationService notificationService;

    // Endpoint to create a notification for specified user types
//    @PostMapping("/create")
//    public ResponseEntity<NotificationEntity> createNotification(
//            @RequestParam String message,
//            @RequestParam List<Integer> userTypes,
//            @RequestParam(required = false) Integer grade,
//            @RequestParam(required = false) String section,
//            @RequestParam(required = false) String schoolYear) {
//        
//        NotificationEntity notification = notificationService.createNotificationForUserType(message, userTypes, grade, section, schoolYear);
//        return ResponseEntity.ok(notification);
//    }

    // Endpoint to get notifications for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNotification>> getNotificationsForUser(@PathVariable Long userId) {
        List<UserNotification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping("/user/{userId}/mark-all-as-viewed")
    public ResponseEntity<String> markAllAsViewedForUser(@PathVariable Long userId) {
        int updatedCount = notificationService.markAllNotificationsAsViewedForUser(userId);
        return ResponseEntity.ok("Marked " + updatedCount + " notifications as viewed for user ID: " + userId);
    }

}
