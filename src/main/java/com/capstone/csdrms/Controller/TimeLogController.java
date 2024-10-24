package com.capstone.csdrms.Controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.TimeLogEntity;
import com.capstone.csdrms.Methods.TimeLogRequest;
import com.capstone.csdrms.Service.TimeLogService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/time-log")
public class TimeLogController {

	@Autowired
    private TimeLogService timeLogService;

	//s
	 @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestBody TimeLogRequest timeLogRequest) {
	        Long userId = timeLogRequest.getUserId();
	        OffsetDateTime loginTime = OffsetDateTime.parse(timeLogRequest.getLoginTime()); // Updated to OffsetDateTime

	        TimeLogEntity savedLog = timeLogService.createTimeLog(userId, loginTime);
	        return ResponseEntity.ok("Login time logged for user: " + userId + " with TimeLog ID: " + savedLog.getTimelog_id());
	    }

	    // Log logout time using timelog_id
	    @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestBody TimeLogRequest timeLogRequest) {
	        Long timelogId = timeLogRequest.getTimelogId();  // Assume you pass timelog_id here
	        OffsetDateTime logoutTime = OffsetDateTime.parse(timeLogRequest.getLogoutTime()); // Updated to OffsetDateTime

	        timeLogService.updateLogoutTime(timelogId, logoutTime);
	        return ResponseEntity.ok("Logout time logged for TimeLog ID: " + timelogId);
	    }
  
    @GetMapping("/getAll")
    public List<TimeLogEntity> getAllTimeLogs() {
        return timeLogService.getAllTimeLogs(); 
    }     
    
    @GetMapping("/getLatestLog/{userId}")
    public ResponseEntity<TimeLogEntity> getLatestLog(@PathVariable Long userId) {
        TimeLogEntity timeLog = timeLogService.getLatestLogByUser(userId);
        return ResponseEntity.ok(timeLog); 
    }
    
    @GetMapping("/getAllTimelogsByUser/{userId}")
    public List<TimeLogEntity> getAllTimelogsByUser(@PathVariable Long userId){
    	return timeLogService.getAllTimelogsByUser(userId);
    }
    
    
    
    
}
