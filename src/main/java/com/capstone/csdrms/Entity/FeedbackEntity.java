//package com.capstone.csdrms.Entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name="tblfeedback")
//public class FeedbackEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "feedbackID")
//    private int fid;
//
//    @Column(name = "caseID")
//    private int cid;
//
//    @ManyToOne
//    @JoinColumn(name = "caseID", insertable = false, updatable = false)
//    private CaseEntity caseEntity;
//    
//    private String result;
//
//    public FeedbackEntity() {
//        super();
//    }
//
//	public FeedbackEntity(int fid, Long uid, int cid, AdviserEntity adviser, CaseEntity caseEntity,
//			String result) {
//		super();
//		this.fid = fid;
//		this.cid = cid;
//		this.caseEntity = caseEntity;
//		this.result = result;
//	}
//
//	public int getFid() {
//		return fid;
//	}
//
//	public void setFid(int fid) {
//		this.fid = fid;
//	}
//
//	public int getCid() {
//		return cid;
//	}
//
//	public void setCid(int cid) {
//		this.cid = cid;
//	}
//
//	public CaseEntity getCaseEntity() {
//		return caseEntity;
//	}
//
//	public void setCaseEntity(CaseEntity caseEntity) {
//		this.caseEntity = caseEntity;
//	}
//
//	public String getResult() {
//		return result;
//	}
//
//	public void setResult(String result) {
//		this.result = result;
//	}
//
//	
//}
