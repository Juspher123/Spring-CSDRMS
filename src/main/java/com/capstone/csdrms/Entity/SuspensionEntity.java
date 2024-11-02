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
@Table(name="tblsuspension")
public class SuspensionEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long suspensionId;
	
	private Long reportId;
	private String dateSubmitted;
	private int days;
	private String startDate;
	private String endDate;  
	private String returnDate;
	private String offense;
	private boolean approved = false;
	private boolean viewedByPrincipal;
	private boolean viewedByAdviser;
	private boolean viewedBySso;
	private boolean viewedByComplainant;
	
	 
	@ManyToOne
    @JoinColumn(name = "reportId", insertable = false, updatable = false)
    private ReportEntity reportEntity;
	
	public SuspensionEntity() {
		super();
	}

	public SuspensionEntity(Long suspensionId, Long reportId, String dateSubmitted, int days, String startDate,
			String endDate, String returnDate, String offense, boolean approved, boolean viewedByPrincipal,
			boolean viewedByAdviser, boolean viewedBySso, boolean viewedByComplainant, ReportEntity reportEntity) {
		super();
		this.suspensionId = suspensionId;
		this.reportId = reportId;
		this.dateSubmitted = dateSubmitted;
		this.days = days;
		this.startDate = startDate;
		this.endDate = endDate;
		this.returnDate = returnDate;
		this.offense = offense;
		this.approved = approved;
		this.viewedByPrincipal = viewedByPrincipal;
		this.viewedByAdviser = viewedByAdviser;
		this.viewedBySso = viewedBySso;
		this.viewedByComplainant = viewedByComplainant;
		this.reportEntity = reportEntity;
	}

	public Long getSuspensionId() {
		return suspensionId;
	}

	public void setSuspensionId(Long suspensionId) {
		this.suspensionId = suspensionId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(String dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getOffense() {
		return offense;
	}

	public void setOffense(String offense) {
		this.offense = offense;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isViewedByPrincipal() {
		return viewedByPrincipal;
	}

	public void setViewedByPrincipal(boolean viewedByPrincipal) {
		this.viewedByPrincipal = viewedByPrincipal;
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

	public boolean isViewedByComplainant() {
		return viewedByComplainant;
	}

	public void setViewedByComplainant(boolean viewedByComplainant) {
		this.viewedByComplainant = viewedByComplainant;
	}

	public ReportEntity getReportEntity() {
		return reportEntity;
	}

	public void setReportEntity(ReportEntity reportEntity) {
		this.reportEntity = reportEntity;
	}

	
}
