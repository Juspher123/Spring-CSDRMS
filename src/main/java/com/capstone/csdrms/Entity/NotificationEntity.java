package com.capstone.csdrms.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tblnotifications")
public class NotificationEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
	
	private Long recordId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
	@JoinColumn(name = "recordId", referencedColumnName = "recordId", insertable = false, updatable = false)
    private RecordEntity record;

    
    
    public NotificationEntity() {}

    public NotificationEntity(Long recordId, String message) {
    	this.recordId = recordId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public RecordEntity getRecord() {
		return record;
	}

	public void setReport(RecordEntity record) {
		this.record = record;
	}

	
    
}
