package com.capstone.csdrms.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblreport")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    
    private Long recordId; 

    private Long adviserId; 

    private String date;
    private String time;
    private String complaint;
    private String complainant;
    private String received;
    private String encoder;  
    private boolean complete;
    private boolean viewedByAdviser;
	private boolean viewedBySso;

    @OneToOne
    @JoinColumn(name = "recordId", referencedColumnName = "recordId", insertable = false, updatable = false)
    private StudentRecordEntity record;

    @ManyToOne
    @JoinColumn(name = "adviserId", referencedColumnName = "userId", insertable = false, updatable = false)
    private UserEntity adviser;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "complainant", referencedColumnName = "username", insertable = false, updatable = false)
    private UserEntity userComplainant;

    
    
    public ReportEntity() { 
    }



	public ReportEntity(Long reportId, Long recordId, Long adviserId, String date, String time, String complaint,
			String complainant, String received, String encoder, boolean complete, boolean viewedByAdviser,
			boolean viewedBySso, StudentRecordEntity record, UserEntity adviser, UserEntity userComplainant) {
		super();
		this.reportId = reportId;
		this.recordId = recordId;
		this.adviserId = adviserId;
		this.date = date;
		this.time = time;
		this.complaint = complaint;
		this.complainant = complainant;
		this.received = received;
		this.encoder = encoder;
		this.complete = complete;
		this.viewedByAdviser = viewedByAdviser;
		this.viewedBySso = viewedBySso;
		this.record = record;
		this.adviser = adviser;
		this.userComplainant = userComplainant;
	}



	public Long getReportId() {
		return reportId;
	}



	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}



	public Long getRecordId() {
		return recordId;
	}



	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}



	public Long getAdviserId() {
		return adviserId;
	}



	public void setAdviserId(Long adviserId) {
		this.adviserId = adviserId;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getComplaint() {
		return complaint;
	}



	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}



	public String getComplainant() {
		return complainant;
	}



	public void setComplainant(String complainant) {
		this.complainant = complainant;
	}



	public String getReceived() {
		return received;
	}



	public void setReceived(String received) {
		this.received = received;
	}



	public String getEncoder() {
		return encoder;
	} 



	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}



	public boolean isComplete() {
		return complete;
	}



	public void setComplete(boolean complete) {
		this.complete = complete;
	}



	public boolean isViewedByAdviser() {
		return viewedByAdviser;
	}



	public void setViewedByAdviser(boolean viewedByAdviser) {
		this.viewedByAdviser = viewedByAdviser;
	}



	public boolean isViewedBySso() {
		return viewedBySso;
	}



	public void setViewedBySso(boolean viewedBySso) {
		this.viewedBySso = viewedBySso;
	}



	public StudentRecordEntity getRecord() {
		return record;
	}



	public void setRecord(StudentRecordEntity record) {
		this.record = record;
	}



	public UserEntity getAdviser() {
		return adviser;
	}



	public void setAdviser(UserEntity adviser) {
		this.adviser = adviser;
	}



	public UserEntity getUserComplainant() {
		return userComplainant;
	}



	public void setUserComplainant(UserEntity userComplainant) {
		this.userComplainant = userComplainant;
	}
    
    
    
}
	