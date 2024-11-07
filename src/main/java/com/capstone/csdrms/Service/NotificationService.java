package com.capstone.csdrms.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.NotificationEntity;
import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Entity.UserNotification;
import com.capstone.csdrms.Repository.NotificationRepository;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.UserNotificationRepository;
import com.capstone.csdrms.Repository.UserRepository;

@Service
public class NotificationService {

	 @Autowired
	    private NotificationRepository notificationRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private UserNotificationRepository userNotificationRepository;
	    
	    @Autowired 
	    private ReportRepository reportRepository;

	    // Create a notification and associate it with specific user types
	    public NotificationEntity createNotificationForUserType(String type,Long reportId, String message, List<Integer> userTypes, Long initiator, Integer grade, String section, String schoolYear) {
	        NotificationEntity notification = new NotificationEntity(reportId,message);
	        notification = notificationRepository.save(notification);
	        
	        Optional<ReportEntity> reportOptional = reportRepository.findById(reportId);
	        String complainant = reportOptional.get().getComplainant();
	        

	     // Find users by their userType and create UserNotification records
	        List<UserEntity> users = new ArrayList<>();
	        for (Integer userType : userTypes) {
	            if (userType == 1) {
	            	
	            	if("Report".equals(type)) {
	            		 List<UserEntity> potentialUsers = userRepository.findAllByUserTypeInAndDeletedFalse(List.of(userType));
	                     
	                     // Add only users who are not the complainant
	                     for (UserEntity user : potentialUsers) {
	                         if (!user.getUserId().equals(initiator)) {
	                             users.add(user);
	                         }
	                     }
	            	}
	            	
	            	else if("View Suspension".equals(type) || "Approve Suspension".equals(type)) {
	            		users.addAll(userRepository.findAllByUserTypeInAndDeletedFalse(List.of(userType)));
	            	}
	            		
	            	else if("Student Suspension".equals(type)) {
	            		List<UserEntity> potentialUsers = userRepository.findAllByUserTypeInAndDeletedFalse(List.of(userType));
	            		
	            		for (UserEntity user : potentialUsers) {
	                        if (!user.getUserId().equals(initiator)) {
	                            users.add(user);  // Add only if not the initiator
	                        }
	                    }
	            	}
	            	   
	            }
	            
	            else if(userType == 2){
	            	if("Student Suspension".equals(type)) {
	            		Optional<UserEntity> optionalPrincipal = userRepository.findByUserTypeAndDeleted(userType, false);
	            		
	            		optionalPrincipal.ifPresent(users::add);
	            		
	            	}
	            	
	            }
	            else if(userType == 3) {
	            	
	            	if("Report".equals(type)) {
	            		Optional<UserEntity> optionalAdviser = userRepository.findByGradeAndSectionAndSchoolYearAndDeleted(grade, section, schoolYear, false);
	            		optionalAdviser.ifPresent(user -> {
	                        if (!users.contains(user) && !user.getUserId().equals(initiator)) {  // Avoid duplicate entry
	                            users.add(user);
	                        }
	                    });
	            	}
	            	
	            	else {
	            		Optional<UserEntity> optionalAdviser = userRepository.findByGradeAndSectionAndSchoolYearAndDeleted(grade, section, schoolYear, false);
	            		optionalAdviser.ifPresent(user -> {
	                        if (!users.contains(user)) {  // Avoid duplicate entry
	                            users.add(user);
	                        }
	                    });
		            
		            	 Optional<UserEntity> complainantAdviser = userRepository.findByUsernameAndDeleted(complainant, false);
		            	 complainantAdviser.ifPresent(user -> {
		                        if (!users.contains(user) && user.getUserType() == 3) {  // Avoid duplicate entry
		                            users.add(user);
		                        }
		                    });
	            	}
	            		            	 	               
	            	
	            }
	            
	            	else {
	            		if (!"Report".equals(type)) {
	            			Optional<UserEntity> optionalUser = userRepository.findByUserTypeAndDeleted(userType, false);
		            		
	            			optionalUser.ifPresent(user -> {
		                        if (!users.contains(user) && user.getUsername().equals(complainant)) {  
		                            users.add(user);
		                        }
		                    });
	            			
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

	    // Mark a specific notification as view for a specific user
	    public Optional<UserNotification> markAsViewForUser(Long userId, Long notificationId) {
	        Optional<UserNotification> userNotification = userNotificationRepository.findByUser_UserIdAndNotification_NotificationId(userId, notificationId);
	        userNotification.ifPresent(un -> {
	            un.setViewed(true);
	            userNotificationRepository.save(un);
	        });
	        return userNotification;
	    }
	    
	    public int markAllNotificationsAsViewedForUser(Long userId) {
	        return userNotificationRepository.markAllAsViewedForUser(userId);
	    }
}
