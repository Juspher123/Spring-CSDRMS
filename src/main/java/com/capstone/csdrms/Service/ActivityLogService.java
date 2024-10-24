package com.capstone.csdrms.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.ActivityLogEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.ActivityLogRepository;
import com.capstone.csdrms.Repository.UserRepository;

@Service
public class ActivityLogService {
	
	@Autowired
    ActivityLogRepository activityLogRepository;
	
	@Autowired
    UserRepository userRepository;

	public void logActivity(String action, String description, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElse(null); // Fetch user entity, handle null if user not found
        
        ActivityLogEntity logEntry = new ActivityLogEntity(action, description, LocalDateTime.now(), userId);
        activityLogRepository.save(logEntry);
    }
	
	public List<ActivityLogEntity> getAllActivityLogs(){
		return activityLogRepository.findAll();
	}

}
