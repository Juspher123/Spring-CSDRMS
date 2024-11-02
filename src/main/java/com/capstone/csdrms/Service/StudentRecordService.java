package com.capstone.csdrms.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.StudentRecordEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;

import jakarta.transaction.Transactional;

@Service 
public class StudentRecordService {

	@Autowired
	StudentRecordRepository studentRecordRepository;
	
	@Autowired
	ReportRepository reportRepository;
	
	@Autowired
	SuspensionRepository suspensionRepository;
	
	@Autowired
	StudentRepository studentRepositry;
	
	
	@Autowired
	ActivityLogService activityLogService;
	
	
	public StudentRecordEntity insertStudentRecord(StudentRecordEntity studentRecord) {
		StudentRecordEntity savedRecord = studentRecordRepository.save(studentRecord);
		Optional<StudentEntity> optionalStudent = studentRepositry.findById(savedRecord.getId());
		StudentEntity student = optionalStudent.get();
//		activityLogService.logActivity("Insert Student Record", "A new record has been inserted by SSO for student " + student.getSid() + " (" +student.getName()+")", Long.valueOf(1));
		return savedRecord;
	}

	public List<StudentRecordEntity> getAllStudentRecords(){
		return studentRecordRepository.findAll();
	}
	
	public 	List<StudentRecordEntity> getAllStudentRecordsByAdviser(int grade, String section, String schoolYear){
		return studentRecordRepository.findAllByStudent_GradeAndStudent_SectionAndStudent_SchoolYear(grade, section, schoolYear);
	}
	
	public List<StudentRecordEntity> getStudentRecordsBySid(String sid) {
		return studentRecordRepository.findAllByStudent_Sid(sid);
	}
	
	public StudentRecordEntity updateStudentRecord(Long recordId, StudentRecordEntity updatedRecord) throws Exception {
        // Fetch the existing record by its ID
        Optional<StudentRecordEntity> existingRecordOpt = studentRecordRepository.findById(recordId);
        if (existingRecordOpt.isPresent()) {
            StudentRecordEntity existingRecord = existingRecordOpt.get();
            
            existingRecord.setMonitored_record(updatedRecord.getMonitored_record());
            existingRecord.setSanction(updatedRecord.getSanction());
            
            // Save the updated record
            activityLogService.logActivity("Update Record", "Record " + recordId + " updated by SSO", Long.valueOf(1));
            return studentRecordRepository.save(existingRecord);
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
	
	 public void deleteStudentRecord(Long recordId) {
		 boolean suspensionExist = false;
		 boolean reportExist = false;
	        // Find the report associated with the student record
	        ReportEntity report = reportRepository.findByRecordId(recordId);

	        if (report != null) {
	        	reportExist = true;
	        	 Optional<SuspensionEntity> suspension = suspensionRepository.findByReportId(report.getReportId());
	 	        if (suspension.isPresent()) {
	 	            // If found, delete the suspension first
	 	        	suspensionExist = true;
	 	            suspensionRepository.delete(suspension.get());
	 	        }
	            // If a report is found, delete it
	            reportRepository.delete(report);
	        }

	        // Now delete the student record
	        Optional<StudentRecordEntity> studentRecord = studentRecordRepository.findById(recordId);
	        if (studentRecord.isPresent()) {
	            studentRecordRepository.delete(studentRecord.get());
	      
	            String logMessage = "Record ID " + recordId + " deleted by SSO " + (reportExist ? "along with the associated report" +(suspensionExist ? " and its suspension" : ""): "");
	            activityLogService.logActivity("Delete Record", logMessage, Long.valueOf(1));
	            
	        } else {
	            throw new RuntimeException("Student record not found for id: " + recordId);
	        }
	    }

}
