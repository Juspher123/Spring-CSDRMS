package com.capstone.csdrms.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.NotificationEntity;
import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Entity.UserNotification;
import com.capstone.csdrms.Repository.NotificationRepository;
import com.capstone.csdrms.Repository.RecordRepository;
import com.capstone.csdrms.Repository.UserNotificationRepository;
import com.capstone.csdrms.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

	 @Autowired
	    private NotificationRepository notificationRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private UserNotificationRepository userNotificationRepository;
	    
	    @Autowired
	    private RecordRepository recordRepository;
	    

	    // Create a notification and associate it with specific user types
	    public NotificationEntity createNotificationForUserType(String type, Long recordId, String message, List<Integer> userTypes, Long initiator, Integer grade, String section, String schoolYear) {
	        NotificationEntity notification = new NotificationEntity(recordId, message);
	        notification = notificationRepository.save(notification);
	        
	        Optional<RecordEntity> recordOptional = recordRepository.findById(recordId);
	        if (!recordOptional.isPresent()) {
	            return notification;
	        }
	        Long recordCreatorId = recordOptional.get().getUserId();

	        // Find users by their userType and create UserNotification records
	        List<UserEntity> users = new ArrayList<>();
	        for (Integer userType : userTypes) {
	            if (userType == 1) {
	                // SSO logic
	                List<UserEntity> ssoUsers = userRepository.findAllByUserTypeInAndDeletedFalse(List.of(userType));
	                for (UserEntity user : ssoUsers) {
	                    if (!user.getUserId().equals(initiator)) {
	                        users.add(user);
	                    }
	                }
	            }
	            else if (userType == 2) {
	                // Principal logic: Notify for everything they are interested in
	                Optional<UserEntity> optionalPrincipal = userRepository.findByUserTypeAndDeleted(userType, false);
	                optionalPrincipal.ifPresent(user -> {
	                    if (!user.getUserId().equals(initiator)) {
	                        users.add(user);
	                    }
	                });
	            }
	            else if (userType == 3) {
	                // Adviser logic
	                Optional<UserEntity> optionalAdviser = userRepository.findByGradeAndSectionAndSchoolYearAndDeleted(grade, section, schoolYear, false);
	                optionalAdviser.ifPresent(user -> {
	                    if (!users.contains(user) && !user.getUserId().equals(initiator)) {
	                        users.add(user);
	                    }
	                });
	                
	                // Also notify the record creator if they are an adviser
	                Optional<UserEntity> creator = userRepository.findByUserIdAndDeleted(recordCreatorId, false);
	                creator.ifPresent(user -> {
	                    if (!users.contains(user) && user.getUserType() == 3 && !user.getUserId().equals(initiator)) {
	                        users.add(user);
	                    }
	                });
	            }
	            else if (userType == 5 || userType == 6) {
	                // Teacher and Guidance logic: Only notify if they are the record creator
	                Optional<UserEntity> creator = userRepository.findByUserIdAndDeleted(recordCreatorId, false);
	                creator.ifPresent(user -> {
	                    if (user.getUserType() == userType && !user.getUserId().equals(initiator)) {
	                        if (!users.contains(user)) {
	                            users.add(user);
	                        }
	                    }
	                });
	            }
	            else {
	                // Other user types (e.g., Admin)
	                List<UserEntity> otherUsers = userRepository.findAllByUserTypeInAndDeletedFalse(List.of(userType));
	                for (UserEntity user : otherUsers) {
	                    if (!users.contains(user) && !user.getUserId().equals(initiator)) {
	                        users.add(user);
	                    }
	                }
	            }
	        }

	        // Create UserNotification records for each found user
	        for (UserEntity user : users) {
	            UserNotification userNotification = new UserNotification(user, notification);
	            userNotificationRepository.save(userNotification);
	        }
	        
	        return notification;
	    }


	    // Retrieve notifications for a specific user
	    public List<UserNotification> getNotificationsForUser(Long userId) {
	        return userNotificationRepository.findByUser_UserId(userId);
	    }
	    
	    @Transactional
	    public void deleteUserNotification(Long userNotificationId) {
	        userNotificationRepository.deleteByUserNotificationId(userNotificationId);
	    }

	    
	    public int markAllNotificationsAsViewedForUser(Long userId) {
	        return userNotificationRepository.markAllAsViewedForUser(userId);
	    }
}
