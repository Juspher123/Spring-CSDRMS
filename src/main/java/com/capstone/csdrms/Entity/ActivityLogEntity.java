package com.capstone.csdrms.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tblactivitylog")
public class ActivityLogEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long activitylog_id;
	private String action;
	private String description;
	private LocalDateTime timestamp;
	private Long userId; 
	
 
	public ActivityLogEntity() {
		super();
	}

	public ActivityLogEntity(String action, String description, LocalDateTime timestamp, Long userId) {
		this.action = action;
		this.description = description;
		this.timestamp = timestamp;
		this.userId = userId;
	}
 
	public Long getActivitylog_id() {
		return activitylog_id;
	}

	public void setActivitylog_id(Long activitylog_id) {
		this.activitylog_id = activitylog_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	

	
	
}
