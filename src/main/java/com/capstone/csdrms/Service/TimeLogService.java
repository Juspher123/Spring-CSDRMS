package com.capstone.csdrms.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.TimeLogEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.TimeLogRepository;
import com.capstone.csdrms.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TimeLogService {

	
	 @Autowired
	    private TimeLogRepository timeLogRepository;
	 
	 @Autowired
	 	private UserRepository userRepository;
	 
	 @Autowired
	 	private ActivityLogService activityLogService;
	 

	    public TimeLogEntity createTimeLog(Long userId, OffsetDateTime loginTime) {
	        TimeLogEntity timeLog = new TimeLogEntity();
	        timeLog.setUserId(userId);
	        timeLog.setLoginTime(loginTime);
	        
	        ZoneId desiredZoneId = ZoneId.of("Asia/Manila");
	        ZonedDateTime zonedDateTime = loginTime.atZoneSameInstant(desiredZoneId);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm:ss a");
	        String formattedLoginTime = zonedDateTime.format(formatter);
	                
	        Optional<UserEntity> optionalUser = userRepository.findById(userId);
	        UserEntity user = optionalUser.get();
	        activityLogService.logActivity("User Login", "User " + user.getUsername() + " logged in", userId);
	        return timeLogRepository.save(timeLog);
	    }

	    public TimeLogEntity updateLogoutTime(Long timelogId, Long userId, OffsetDateTime logoutTime) {
	        TimeLogEntity timeLog = timeLogRepository.findById(timelogId)
	            .orElseThrow(() -> new RuntimeException("TimeLog not found"));
	        
	        if (!timeLog.getUserId().equals(userId)) {
	        	throw new RuntimeException("TimeLog does not belong to the user");
	        }
	        
	        if (timeLog.getLogoutTime() != null) {
	        	throw new RuntimeException("TimeLog already closed");
	        }
	        
	        if (logoutTime.isBefore(timeLog.getLoginTime())) {
	        	throw new RuntimeException("Invalid logout time");
	        }
	        
	        timeLog.setLogoutTime(logoutTime);
	        long duration = Duration.between(timeLog.getLoginTime(), logoutTime).toMinutes();
	        timeLog.setDuration(duration);
	        
	        ZoneId desiredZoneId = ZoneId.of("Asia/Manila");
	        ZonedDateTime zonedDateTime = logoutTime.atZoneSameInstant(desiredZoneId);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm:ss a");
	        String formattedLoginTime = zonedDateTime.format(formatter);
	                
	        Optional<UserEntity> optionalUser = userRepository.findById(timeLog.getUserId());
	        UserEntity user = optionalUser.get();
	        activityLogService.logActivity("User Logout", "User " + user.getUsername() + " logged out", timeLog.getUserId());
	        
	        return timeLogRepository.save(timeLog);
	    }

    public List<TimeLogEntity> getAllTimeLogs() {
        return timeLogRepository.findAll(); // Implement filtering by userId if necessary
    }
    
    public TimeLogEntity getLatestLogByUser(Long userId) {
        // Implement a method to fetch the latest time log for the given user
        return timeLogRepository.findFirstByUserIdOrderByLoginTimeDesc(userId);
    }
    
    public List<TimeLogEntity> getAllTimelogsByUser(Long userId){
    	return timeLogRepository.findAllByUserId(userId);
    }
    
    @Transactional 
    public void deleteAllTimeLogsByUser(Long userId) {
    	 timeLogRepository.deleteAllByUserId(userId);
    }
}
