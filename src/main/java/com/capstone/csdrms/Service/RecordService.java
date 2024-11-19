package com.capstone.csdrms.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.RecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service 
public class RecordService {

	@Autowired
	RecordRepository recordRepository;
	
	@Autowired
	SuspensionRepository suspensionRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    NotificationService notificationService;
	
	
	@Autowired
	ActivityLogService activityLogService;
	
	
	public RecordEntity insertRecord(RecordEntity record, Long initiator) {
		
		StudentEntity student = studentRepository.findById(record.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
		
		RecordEntity savedRecord = recordRepository.save(record);

		Optional<UserEntity> optionalUser = userRepository.findById(initiator);
        UserEntity user = optionalUser.get();
		activityLogService.logActivity("Insert Record", "A new record has been inserted by "+ user.getUsername()+" for student " + student.getSid() + " (" +student.getName()+")", initiator);
		
//		 // 1. Define the notification message
//        String notificationMessage = "New report created for student " + student.getName() + " (Grade " + student.getGrade() + ", Section " + student.getSection() + ")";
//
//        // 2. Set the user types who	 should receive the notification
//        List<Integer> userTypes = new ArrayList<>();
//        userTypes.add(1); // Assuming userType 1 should receive the notification
//        userTypes.add(3); // Assuming userType 3 is for advisers
//
//        // 3. Call notification service to create the notification for specific users
//        notificationService.createNotificationForUserType("Record",savedRecord.getRecordId() ,notificationMessage, userTypes, initiator, student.getGrade(), student.getSection(), student.getSchoolYear());
		
		return savedRecord;
	}

	public List<RecordEntity> getAllStudentRecords(){
		return recordRepository.findAll();
	}
	
	public 	List<RecordEntity> getAllStudentRecordsByAdviser(int grade, String section, String schoolYear){
		return recordRepository.findAllByStudent_GradeAndStudent_SectionAndStudent_SchoolYear(grade, section, schoolYear);
	}
	
	public 	List<RecordEntity> getAllRecordsByAdviser(int grade, String section, String schoolYear, Long encoderId){
		return recordRepository.findRecordsByGradeSectionAndSchoolYearOrEncoderId(grade, section, schoolYear, encoderId);
	}
	
	public List<RecordEntity> getAllRecordsByEncoderId(Long encoderId){
		return recordRepository.findAllByEncoderId(encoderId);
	}
	
	public List<RecordEntity> getStudentRecordsBySid(String sid) {
		return recordRepository.findAllByStudent_Sid(sid);
	}
	
	public RecordEntity updateStudentRecord(Long recordId, RecordEntity updatedRecord, Long initator) throws Exception {
        // Fetch the existing record by its ID
        Optional<RecordEntity> existingRecordOpt = recordRepository.findById(recordId);
        if (existingRecordOpt.isPresent()) {
            RecordEntity existingRecord = existingRecordOpt.get();
            
            existingRecord.setMonitored_record(updatedRecord.getMonitored_record());
            existingRecord.setSanction(updatedRecord.getSanction());
            
            // Save the updated record
            activityLogService.logActivity("Update Record", "Record " + recordId + " updated by SSO", initator);
            return recordRepository.save(existingRecord);
        } else {
            throw new Exception("Student record not found with ID: " + recordId);
        }
    }
	
//	public List<StudentRecordEntity> getStudentRecordsByAdviser(String sid, String section, String schoolYear){
//		return srepo.findAllBySidAndStudent_SectionAndStudent_SchoolYear(sid, section, schoolYear);
//	}
	
//	@SuppressWarnings("finally")
//	public StudentReportEntity updateStudentReport(int rid, StudentReportEntity newStudentReportDetails) {
//		StudentReportEntity student = new StudentReportEntity();
//	    try {
//	    	student = srepo.findById(rid).get();
//
//	    	student.setSid(newStudentReportDetails.getSid());
//	    	student.setDate(newStudentReportDetails.getDate());
//	    	student.setMonitored_record(newStudentReportDetails.getMonitored_record());;
//	    	student.setRemarks(newStudentReportDetails.getRemarks());
//	    	student.setSanction(newStudentReportDetails.getSanction());
//	       
//	    } catch (NoSuchElementException ex) {
//	        throw ex;
//	    } finally {
//	    	 return srepo.save(student);
//		}
//	}
	
	 public void deleteStudentRecord(Long recordId, Long initiator) {
		 boolean suspensionExist = false;
	       
	        	 Optional<SuspensionEntity> suspension = suspensionRepository.findByRecordId(recordId);
	 	        if (suspension.isPresent()) {
	 	            // If found, delete the suspension first
	 	        	suspensionExist = true;
	 	            suspensionRepository.delete(suspension.get());
	 	        }

	        // Now delete the student record
	        Optional<RecordEntity> studentRecord = recordRepository.findById(recordId);
	        if (studentRecord.isPresent()) {
	            recordRepository.delete(studentRecord.get());
	      
	            String logMessage = "Record ID " + recordId + " deleted by SSO "  + (suspensionExist ? " along with the associated Suspension" : "");
	            activityLogService.logActivity("Delete Record", logMessage, initiator);
	            
	        } else {
	            throw new RuntimeException("Student record not found for id: " + recordId);
	        }
	    }

}
