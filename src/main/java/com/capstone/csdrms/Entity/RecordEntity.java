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
@Table(name="tblrecord")
public class RecordEntity {
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;
	
	//The auto-generated id of student
	private Long id;
	
	//equivalent to userId
	private Long userId;
	
	private String record_date;
	private String incident_date;
	private String time;
	private String monitored_record;
	private int period;
	private String remarks;
	private String sanction;
	private String encoder;
	private String complainant;
	private String complaint;
	private String investigationDetails;
	private int source;
	private int complete;
		
	@ManyToOne
	@JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
	private StudentEntity student;
	
	@ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private UserEntity user;   
	
	public RecordEntity() {
		super();
	}

	public RecordEntity(Long recordId, Long id, Long userId, String record_date, String incident_date, String time,
			String monitored_record, int period, String remarks, String sanction, String encoder, String complainant,
			String complaint, String investigationDetails, int source, int complete, StudentEntity student,
			UserEntity user) {
		super();
		this.recordId = recordId;
		this.id = id;
		this.userId = userId;
		this.record_date = record_date;
		this.incident_date = incident_date;
		this.time = time;
		this.monitored_record = monitored_record;
		this.period = period;
		this.remarks = remarks;
		this.sanction = sanction;
		this.encoder = encoder;
		this.complainant = complainant;
		this.complaint = complaint;
		this.investigationDetails = investigationDetails;
		this.source = source;
		this.complete = complete;
		this.student = student;
		this.user = user;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSanction() {
		return sanction;
	}

	public void setSanction(String sanction) {
		this.sanction = sanction;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public String getComplainant() {
		return complainant;
	}

	public void setComplainant(String complainant) {
		this.complainant = complainant;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getInvestigationDetails() {
		return investigationDetails;
	}

	public void setInvestigationDetails(String investigationDetails) {
		this.investigationDetails = investigationDetails;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	 
}
