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
	    // Save the suspension entity
	    SuspensionEntity savedSuspension = suspensionRepository.save(suspension);

	   Optional<RecordEntity> optionalRecord = recordRepository.findById(savedSuspension.getRecordId());
	   RecordEntity record = optionalRecord.get();
	    
	    activityLogService.logActivity("Student Suspension", "Student " + record.getStudent().getSid() + " (" + record.getStudent().getName() + ") has been suspended by SSO", initiator);

	    // Define the notification message
	    String notificationMessage = "Student " + record.getStudent().getName() + " (Grade " + record.getStudent().getGrade() + ", Section " + record.getStudent().getSection() + ") has been suspended.";

	    // Set the user types who should receive the notification
	    List<Integer> userTypes = new ArrayList<>();
	    userTypes.add(1);
	    userTypes.add(2);
	    userTypes.add(3);
	    userTypes.add(5);
	    userTypes.add(6);

	    // Create the notification
	    notificationService.createNotificationForUserType("Student Suspension", record.getRecordId(), notificationMessage, userTypes, initiator, record.getStudent().getGrade(), record.getStudent().getSection(), record.getStudent().getSchoolYear());

	    return savedSuspension;
	}
	    
	    
	public List<SuspensionEntity> getAllSuspensions(){
		return suspensionRepository.findAll();
	}
	
	public List<SuspensionEntity> getAllSuspensionsByStudentId(Long id){
		return suspensionRepository.findByRecord_Student_Id(id);
	}
	
	public Optional<SuspensionEntity> getSuspension(Long suspensionId) {
	    return suspensionRepository.findById(suspensionId);
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
		        
		        Optional<RecordEntity> optionalRecord = recordRepository.findById(suspension.getRecordId());
		        if (optionalRecord.isPresent()) {
		            RecordEntity record = optionalRecord.get();
		            if(record.getType() == 2) {
		            	 record.setComplete(1); // Mark the record as complete
				         recordRepository.save(record);	
		            }
		        }

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
