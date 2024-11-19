package com.capstone.csdrms.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.RecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class SuspensionService {
	
	@Autowired
	SuspensionRepository suspensionRepository;
	
	@Autowired
	RecordRepository recordRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	ActivityLogService activityLogService; 
	
	@Autowired
	NotificationService notificationService;
	
	
	 

	@Transactional
	public SuspensionEntity insertSuspension(SuspensionEntity suspension, Long initiator) {
	    // Fetch the associated CaseEntity using the cid (case ID)
	        
	        // Save the sanction entity
	        SuspensionEntity savedSanction = suspensionRepository.save(suspension);
	        
	       
	        
	        // Automatically insert a student report after the sanction is added
	        insertStudentRecordFromSanction(savedSanction);
	        activityLogService.logActivity("Student Suspension", "Student " + savedSanction.getRecord().getStudent().getSid() + " (" +savedSanction.getRecord().getStudent().getName()+")" + " has been suspended by SSO", initiator);
	        
	        
	     // 1. Define the notification message
	        String notificationMessage = "Student " + savedSanction.getRecord().getStudent().getName() + " (Grade " + savedSanction.getRecord().getStudent().getGrade() + ", Section " + savedSanction.getRecord().getStudent().getSection() + ") has been suspended.";

	        // 2. Set the user types who should receive the notification
	        List<Integer> userTypes = new ArrayList<>();
	        userTypes.add(1);
	        userTypes.add(2);
	        userTypes.add(3); 
	        userTypes.add(5);
	        userTypes.add(6);

	        // 3. Call notification service to create the notification for specific users
	        notificationService.createNotificationForUserType("Student Suspension",savedSanction.getRecordId() ,notificationMessage, userTypes, initiator, savedSanction.getRecord().getStudent().getGrade(), savedSanction.getRecord().getStudent().getSection(), savedSanction.getRecord().getStudent().getSchoolYear());
	        
	        return savedSanction;
	    } 
	    
	 private void insertStudentRecordFromSanction(SuspensionEntity suspension) {
		    StudentEntity student = suspension.getRecord().getStudent(); // Direct access to the student entity

		    if (student != null) {
		        // Prepare and set the fields of StudentReportEntity
		        Optional<RecordEntity> studentRecordOptional = recordRepository.findById(suspension.getRecordId());
		        
		        RecordEntity studentRecord = studentRecordOptional.orElseGet(RecordEntity::new);
		        
		        String sanction = "Suspended for " + suspension.getDays()+" days starting from "+ suspension.getStartDate() + " to " +suspension.getEndDate() + " and will be return at " +suspension.getReturnDate() ;      

		        studentRecord.setSanction(sanction);
		        // Save the student record
		        recordRepository.save(studentRecord);
		    } else {
		        throw new IllegalArgumentException("Student not found for the given sanction.");
		    }
		}
	    
	    
	public List<SuspensionEntity> getAllSuspensions(){
		return suspensionRepository.findAll();
	}
	
	public List<SuspensionEntity> getAllSuspensionsByStudentId(Long id){
		return suspensionRepository.findByRecord_Student_Id(id);
	}
	
	
	
	 
	 public void markSuspensionsAsViewedForPrincipal(Long suspensionId, Long initiator) {
	        Optional<SuspensionEntity> Optionalsuspension = suspensionRepository.findById(suspensionId);
	        SuspensionEntity suspension = Optionalsuspension.get();
	        suspension.setViewedByPrincipal(true);
	        
	        
	        // 1. Define the notification message
	        String notificationMessage = "Principal view the suspension of " + suspension.getRecord().getStudent().getName() + " (Grade " + suspension.getRecord().getStudent().getGrade() + ", Section " + suspension.getRecord().getStudent().getSection() + ")";

	        // 2. Set the user types who should receive the notification
	        List<Integer> userTypes = new ArrayList<>();
	        userTypes.add(1);
	        userTypes.add(3); 
	        userTypes.add(5);
	        userTypes.add(6);

	        // 3. Call notification service to create the notification for specific users
	        notificationService.createNotificationForUserType("View Suspension", suspension.getRecordId() ,notificationMessage, userTypes, initiator, suspension.getRecord().getStudent().getGrade(), suspension.getRecord().getStudent().getSection(), suspension.getRecord().getStudent().getSchoolYear());
	        
	        suspensionRepository.save(suspension);
	 }
	 
	  
	 public Optional<SuspensionEntity> getSuspensionByRecordId(Long recordId) {
	        return suspensionRepository.findByRecordId(recordId);
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
	        	 Optional<RecordEntity> optionalRecord = recordRepository.findById(suspension.get().getRecordId());
	        	 if(optionalRecord.isPresent()) {
	        		 RecordEntity record = optionalRecord.get();
	        		 record.setComplete(0);;
	        		 recordRepository.save(record);
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
		        String notificationMessage = "Principal approve the suspension of " + suspension.getRecord().getStudent().getName() + " (Grade " + suspension.getRecord().getStudent().getGrade() + ", Section " + suspension.getRecord().getStudent().getSection() + ")";

		        // 2. Set the user types who should receive the notification
		        List<Integer> userTypes = new ArrayList<>();
		        userTypes.add(1);
		        userTypes.add(3); 
		        userTypes.add(5);
		        userTypes.add(6);

		        // 3. Call notification service to create the notification for specific users
		        notificationService.createNotificationForUserType("Approve Suspension",suspension.getRecordId() ,notificationMessage, userTypes, initiator, suspension.getRecord().getStudent().getGrade(), suspension.getRecord().getStudent().getSection(), suspension.getRecord().getStudent().getSchoolYear());
	            
	            
	            
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
