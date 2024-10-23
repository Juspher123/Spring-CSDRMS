package com.capstone.csdrms.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.TimeLogEntity;
import com.capstone.csdrms.Repository.TimeLogRepository;

@Service
public class TimeLogService {

	
	 @Autowired
	    private TimeLogRepository timeLogRepository;

	    public TimeLogEntity createTimeLog(Long userId, OffsetDateTime loginTime) {
	        TimeLogEntity timeLog = new TimeLogEntity();
	        timeLog.setUserId(userId);
	        timeLog.setLoginTime(loginTime);
	        return timeLogRepository.save(timeLog);
	    }

	    public TimeLogEntity updateLogoutTime(Long timelogId, OffsetDateTime logoutTime) {
	        TimeLogEntity timeLog = timeLogRepository.findById(timelogId)
	            .orElseThrow(() -> new RuntimeException("TimeLog not found"));
	        timeLog.setLogoutTime(logoutTime);
	        long duration = Duration.between(timeLog.getLoginTime(), logoutTime).toMinutes();
	        timeLog.setDuration(duration);
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
}
