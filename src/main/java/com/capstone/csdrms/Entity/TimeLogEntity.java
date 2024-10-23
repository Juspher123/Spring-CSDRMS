package com.capstone.csdrms.Entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tbltimelog")
public class TimeLogEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long timelog_id;

	 private Long userId;
	 private OffsetDateTime loginTime;
	 private OffsetDateTime logoutTime;
	 private long duration;
	 
	 @ManyToOne
	 @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
	 private UserEntity user;
    
	public TimeLogEntity() {
		super(); 
	}

	public TimeLogEntity(Long timelog_id, Long userId, OffsetDateTime loginTime, OffsetDateTime logoutTime,
			long duration, UserEntity user) {
		super();
		this.timelog_id = timelog_id;
		this.userId = userId;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.duration = duration;
		this.user = user;
	}

	public Long getTimelog_id() {
		return timelog_id;
	}

	public void setTimelog_id(Long timelog_id) {
		this.timelog_id = timelog_id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public OffsetDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(OffsetDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public OffsetDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(OffsetDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	
    
}
