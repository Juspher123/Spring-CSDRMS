package com.capstone.csdrms.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.CaseEntity;
import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.StudentRecordEntity;
import com.capstone.csdrms.Repository.CaseRepository;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class SuspensionService {
	
	@Autowired
	SuspensionRepository suspensionRepository;
	
	@Autowired
	StudentRecordRepository studentRecordRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	ReportRepository reportRepository;
	
	@Autowired
	ActivityLogService activityLogService; 
	
	@Autowired
	NotificationService notificationService;
	
	
	 

	@Transactional
	public SuspensionEntity insertSuspension(SuspensionEntity suspension, Long initiator) {
		String investigationDetails = suspension.getReportEntity().getInvestigationDetails();
	    // Fetch the associated CaseEntity using the cid (case ID)
	    Optional<ReportEntity> reportOptional = reportRepository.findById(suspension.getReportId());
	    
	    if (reportOptional.isPresent()) {
	    	ReportEntity reportEntity = reportOptional.get();
	        // Set the CaseEntity in the sanction
	    	suspension.setReportEntity(reportEntity);
	    	 reportEntity.setInvestigationDetails(investigationDetails);
	    	 reportEntity.setComplete(true);
	    	 
	    	 
	    	 reportRepository.save(reportEntity);
	        
	        // Save the sanction entity
	        SuspensionEntity savedSanction = suspensionRepository.save(suspension);
	        
	       
	        
	        // Automatically insert a student report after the sanction is added
	        insertStudentRecordFromSanction(savedSanction);
	        activityLogService.logActivity("Student Suspension", "Student " + savedSanction.getReportEntity().getRecord().getStudent().getSid() + " (" +savedSanction.getReportEntity().getRecord().getStudent().getName()+")" + " has been suspended by SSO", initiator);
	        
	        
	     // 1. Define the notification message
	        String notificationMessage = "Student " + savedSanction.getReportEntity().getRecord().getStudent().getName() + " (Grade " + savedSanction.getReportEntity().getRecord().getStudent().getGrade() + ", Section " + savedSanction.getReportEntity().getRecord().getStudent().getSection() + ") has been suspended.";

	        // 2. Set the user types who should receive the notification
	        List<Integer> userTypes = new ArrayList<>();
	        userTypes.add(1);
	        userTypes.add(2);
	        userTypes.add(3); 
	        userTypes.add(5);
	        userTypes.add(6);

	        // 3. Call notification service to create the notification for specific users
	        notificationService.createNotificationForUserType("Student Suspension",savedSanction.getReportId() ,notificationMessage, userTypes, initiator, savedSanction.getReportEntity().getRecord().getStudent().getGrade(), savedSanction.getReportEntity().getRecord().getStudent().getSection(), savedSanction.getReportEntity().getRecord().getStudent().getSchoolYear());
	        
	        return savedSanction;
	    } else {
	        throw new IllegalArgumentException("Report with id " + suspension.getReportId()+ " not found.");
	    }
	}
	    
	 private void insertStudentRecordFromSanction(SuspensionEntity suspension) {
		    StudentEntity student = suspension.getReportEntity().getRecord().getStudent(); // Direct access to the student entity

		    if (student != null) {
		        // Prepare and set the fields of StudentReportEntity
		        Optional<StudentRecordEntity> studentRecordOptional = studentRecordRepository.findById(suspension.getReportEntity().getRecordId());
		        
		        StudentRecordEntity studentRecord = studentRecordOptional.orElseGet(StudentRecordEntity::new);
		        
		        String sanction = "Suspended for " + suspension.getDays()+" days starting from "+ suspension.getStartDate() + " to " +suspension.getEndDate() + " and will be return at " +suspension.getReturnDate() ;      

		        studentRecord.setSanction(sanction);
		        // Save the student record
		        studentRecordRepository.save(studentRecord);
		    } else {
		        throw new IllegalArgumentException("Student not found for the given sanction.");
		    }
		}
	    
	    
	public List<SuspensionEntity> getAllSuspensions(){
		return suspensionRepository.findAll();
	}
	
	public List<SuspensionEntity> getAllSuspensionsByStudentId(Long id){
		return suspensionRepository.findByReportEntity_Record_Student_Id(id);
	}
	
	
	
	 
	 public void markSuspensionsAsViewedForPrincipal(Long suspensionId, Long initiator) {
	        Optional<SuspensionEntity> Optionalsuspension = suspensionRepository.findById(suspensionId);
	        SuspensionEntity suspension = Optionalsuspension.get();
	        suspension.setViewedByPrincipal(true);
	        
	        
	        // 1. Define the notification message
	        String notificationMessage = "Principal view the suspension of " + suspension.getReportEntity().getRecord().getStudent().getName() + " (Grade " + suspension.getReportEntity().getRecord().getStudent().getGrade() + ", Section " + suspension.getReportEntity().getRecord().getStudent().getSection() + ")";

	        // 2. Set the user types who should receive the notification
	        List<Integer> userTypes = new ArrayList<>();
	        userTypes.add(1);
	        userTypes.add(3); 
	        userTypes.add(5);
	        userTypes.add(6);

	        // 3. Call notification service to create the notification for specific users
	        notificationService.createNotificationForUserType("View Suspension",suspension.getReportId() ,notificationMessage, userTypes, initiator, suspension.getReportEntity().getRecord().getStudent().getGrade(), suspension.getReportEntity().getRecord().getStudent().getSection(), suspension.getReportEntity().getRecord().getStudent().getSchoolYear());
	        
	        suspensionRepository.save(suspension);
	 }
	 
	  
	 public Optional<SuspensionEntity> getSuspensionByReportId(Long reportId) {
	        return suspensionRepository.findByReportId(reportId);
	    }
	 
	 @Transactional
	 public SuspensionEntity updateSuspension(Long suspensionId, SuspensionEntity updatedSuspensionData, Long initiator) {
	     Optional<SuspensionEntity> suspensionOptional = suspensionRepository.findById(suspensionId);

	     if (suspensionOptional.isPresent()) {
	         SuspensionEntity suspension = suspensionOptional.get();

	         // Update suspension details
	         suspension.setDays(updatedSuspensionData.getDays());
	         suspension.setStartDate(updatedSuspensionData.getStartDate());
	         suspension.setEndDate(updatedSuspensionData.getEndDate());
	         suspension.setReturnDate(updatedSuspensionData.getReturnDate());
	         
	         // Save the updated suspension
	         SuspensionEntity savedSuspension = suspensionRepository.save(suspension);
	         
	         activityLogService.logActivity("Update Suspension", "Suspension " + suspensionId + " updated by SSO", initiator);

	         return savedSuspension;
	     } else {
	         throw new RuntimeException("Suspension not found for id: " + suspensionId);
	     }
	 }

	 
	 public void deleteSuspension(Long suspensionId, Long initiator) {
	        Optional<SuspensionEntity> suspension = suspensionRepository.findById(suspensionId);
	        if (suspension.isPresent()) {
	        	 Optional<ReportEntity> optionalReport = reportRepository.findById(suspension.get().getReportId());
	        	 if(optionalReport.isPresent()) {
	        		 ReportEntity report = optionalReport.get();
	        		 report.setComplete(false);
	        		 reportRepository.save(report);
	        	 }
	            suspensionRepository.delete(suspension.get());
	            activityLogService.logActivity("Lift Suspension", "Suspension " + suspensionId + " has been lifted by SSO", initiator);
	        } else {
	            throw new RuntimeException("Suspension not found for id: " + suspensionId);
	        }
	    }
	 
	 
	 @Transactional
	    public boolean approveSuspension(Long suspensionId, Long initiator) {
	        Optional<SuspensionEntity> optionalSuspension = suspensionRepository.findById(suspensionId);
	        if (optionalSuspension.isPresent()) {
	            SuspensionEntity suspension = optionalSuspension.get();
	            suspension.setApproved(true);
	            suspensionRepository.save(suspension);
	            
	         // 1. Define the notification message
		        String notificationMessage = "Principal approve the suspension of " + suspension.getReportEntity().getRecord().getStudent().getName() + " (Grade " + suspension.getReportEntity().getRecord().getStudent().getGrade() + ", Section " + suspension.getReportEntity().getRecord().getStudent().getSection() + ")";

		        // 2. Set the user types who should receive the notification
		        List<Integer> userTypes = new ArrayList<>();
		        userTypes.add(1);
		        userTypes.add(3); 
		        userTypes.add(5);
		        userTypes.add(6);

		        // 3. Call notification service to create the notification for specific users
		        notificationService.createNotificationForUserType("Approve Suspension",suspension.getReportId() ,notificationMessage, userTypes, initiator, suspension.getReportEntity().getRecord().getStudent().getGrade(), suspension.getReportEntity().getRecord().getStudent().getSection(), suspension.getReportEntity().getRecord().getStudent().getSchoolYear());
	            
	            
	            
	            return true;
	        }
	        return false;
	    }
	 
	 
	 
//	public List<SuspensionEntity> getAllSanctionsById(Long id){
//        return srepo.findAllByCaseEntity_Id(id);
//    }
//	
	 
//	 
//	 @Transactional
//	    public boolean declineSanction(int sanctionId) {
//	        Optional<SuspensionEntity> optionalSanction = srepo.findById(sanctionId);
//	        if (optionalSanction.isPresent()) {
//	            SuspensionEntity sanction = optionalSanction.get();
//	            sanction.setIsApproved(2);
//	            srepo.save(sanction);
//	            return true;
//	        }
//	        return false;
//	    }
//	 
//	 public List<SuspensionEntity> getApprovedAndDeclinedSanctions() {
//		 return srepo.findByIsApprovedIn(List.of(1, 2));
//	    }
//	 
//	 public List<SuspensionEntity> getSanctionsBySectionAndSchoolYear(String section, String schoolYear) {
//		    return srepo.findByCaseEntity_Student_SectionAndCaseEntity_Student_SchoolYear(section, schoolYear);
//		}
}
