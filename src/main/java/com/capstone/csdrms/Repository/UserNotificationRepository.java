package com.capstone.csdrms.Repository;

import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.UserNotification;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
	
	// Fetch notifications for a specific user based on user ID
    List<UserNotification> findByUser_UserId(Long userId);
 
    List<UserNotification> findByUser_UserIdAndViewedFalse(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserNotification un SET un.viewed = true WHERE un.user.userId = :userId AND un.viewed = false")
    int markAllAsViewedForUser(Long userId);

	List<UserNotification> findByNotification_NotificationId(Long notificationId);
	
	void deleteByUserNotificationId(Long userNotificationId);

}
