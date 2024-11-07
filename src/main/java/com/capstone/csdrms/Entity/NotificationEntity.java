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
	
	private Long reportId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
	@JoinColumn(name = "reportId", referencedColumnName = "reportId", insertable = false, updatable = false)
    private ReportEntity report;

    
    
    public NotificationEntity() {}

    public NotificationEntity(Long reportId, String message) {
    	this.reportId = reportId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
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

	public ReportEntity getReport() {
		return report;
	}

	public void setReport(ReportEntity report) {
		this.report = report;
	}

	
    
}
