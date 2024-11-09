package com.capstone.csdrms.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tblstudentRecord")
public class StudentRecordEntity {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;
	 
	private Long id;
	private String record_date;
	private String incident_date;
	private String time;
	private String monitored_record;
	private String details;
	private String sanction;
	
	
	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	private StudentEntity student;
	
	public StudentRecordEntity() {
		super();
	}

	public StudentRecordEntity(Long recordId, Long id, String record_date, String incident_date, String time,
			String monitored_record, String details, String sanction, StudentEntity student) {
		super();
		this.recordId = recordId;
		this.id = id;
		this.record_date = record_date;
		this.incident_date = incident_date;
		this.time = time;
		this.monitored_record = monitored_record;
		this.details = details;
		this.sanction = sanction;
		this.student = student;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecord_date() {
		return record_date;
	}

	public void setRecord_date(String record_date) {
		this.record_date = record_date;
	}

	public String getIncident_date() {
		return incident_date;
	}

	public void setIncident_date(String incident_date) {
		this.incident_date = incident_date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMonitored_record() {
		return monitored_record;
	}

	public void setMonitored_record(String monitored_record) {
		this.monitored_record = monitored_record;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getSanction() {
		return sanction;
	}

	public void setSanction(String sanction) {
		this.sanction = sanction;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	
	
}
