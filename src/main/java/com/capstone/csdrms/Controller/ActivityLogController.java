package com.capstone.csdrms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.ActivityLogEntity;
import com.capstone.csdrms.Service.ActivityLogService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/activity-log")
public class ActivityLogController {
	
	@Autowired
	private ActivityLogService activityLogService;

	
	@GetMapping("/getAllActivityLogs")
	public List<ActivityLogEntity> getAllActivityLogs(){
		return activityLogService.getAllActivityLogs();
	}
	
	 @GetMapping("/getAllActivityLogsByUser/{userId}")
	    public List<ActivityLogEntity> getAllActivityLogsByUser(@PathVariable Long userId) {
	        return activityLogService.getAllActivityLogsByUser(userId);
	    }
}
