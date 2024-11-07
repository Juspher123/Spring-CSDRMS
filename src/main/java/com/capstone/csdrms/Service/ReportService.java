package com.capstone.csdrms.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.StudentRecordEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service 
public class ReportService { 
	
	@Autowired
	ReportRepository reportRepository;
	
	@Autowired
    StudentRepository studentRepository;

	@Autowired
	UserRepository userRepository;
	
    @Autowired
    StudentRecordRepository studentRecordRepository;
    
    @Autowired
    SuspensionRepository suspensionRepository;
    
    @Autowired
    NotificationService notificationService;
    
    
    @Autowired
    ActivityLogService activityLogService;
	
    public ReportEntity insertReport(Long id, ReportEntity report, Long initiator) throws Exception {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new Exception("Student not found");
        }

        StudentEntity student = studentOptional.get();

        Optional<UserEntity> adviserOptional = userRepository.findByGradeAndSectionAndSchoolYearAndDeleted(student.getGrade(),student.getSection(), student.getSchoolYear(), false);
        if (adviserOptional.isEmpty()) {
            throw new Exception("Adviser not found for the student's section and school year");
        }

        UserEntity adviser = adviserOptional.get();
        report.setAdviserId(adviser.getUserId());

        // Save the report
        ReportEntity savedReport = reportRepository.save(report);

        // Automatically create a StudentRecordEntity
        StudentRecordEntity studentRecord = new StudentRecordEntity();
        studentRecord.setId(student.getId());
        studentRecord.setRecord_date(savedReport.getDate());  // Assuming ReportEntity has a date field
        studentRecord.setIncident_date(savedReport.getDate());
        studentRecord.setTime(savedReport.getTime());
        studentRecord.setMonitored_record("TBD");
        studentRecord.setRemarks(savedReport.getComplaint());  // You can modify the remarks as needed
        studentRecord.setSanction("");  // You can set this based on report or leave it empty

        // Save the student record
        StudentRecordEntity savedStudentRecord = studentRecordRepository.save(studentRecord);
        
        savedReport.setRecordId(savedStudentRecord.getRecordId());
        
        reportRepository.save(savedReport);
        
        Optional<UserEntity> optionalUser1 = userRepository.findById(initiator);
        UserEntity user = optionalUser1.get();
        activityLogService.logActivity("Create Report", "Report ID " + savedReport.getReportId() + " created by User "+ user.getUsername(), initiator);
        
        // 1. Define the notification message
        String notificationMessage = "New report created for student " + student.getName() + " (Grade " + student.getGrade() + ", Section " + student.getSection() + ")";

        // 2. Set the user types who should receive the notification
        List<Integer> userTypes = new ArrayList<>();
        userTypes.add(1); // Assuming userType 1 should receive the notification
        userTypes.add(3); // Assuming userType 3 is for advisers

        // 3. Call notification service to create the notification for specific users
        notificationService.createNotificationForUserType("Report",savedReport.getReportId() ,notificationMessage, userTypes, initiator, student.getGrade(), student.getSection(), student.getSchoolYear());

        return savedReport;
    }
	
	public List<ReportEntity> getAllReports(){
		return reportRepository.findAll();
	}
	
//	public ReportEntity completeReport(Long reportId, String comment, Long initiator) throws Exception {
//        Optional<ReportEntity> reportOpt = reportRepository.findById(reportId);
//        if (reportOpt.isPresent()) {
//            ReportEntity report = reportOpt.get();
//            report.setComplete(true);  // Mark the report as complete
//            report.setComment(comment);
//            activityLogService.logActivity("Complete Report", "Report ID " + reportId + " completed by SSO", initiator);
//            return reportRepository.save(report);  // Save the updated entity
//        } else {
//            throw new Exception("Report not found with ID: " + reportId);
//        }
//    }
	
	public Optional<ReportEntity> updateReceived(Long reportId, String receivedDate) {
        Optional<ReportEntity> reportOptional = reportRepository.findById(reportId);
        if (reportOptional.isPresent()) {
            ReportEntity report = reportOptional.get();
            report.setReceived(receivedDate);
            reportRepository.save(report);
            return Optional.of(report);
        }
        return Optional.empty();
    }
	
	public List<ReportEntity> getAllReportsForAdviser(int grade, String section, String schoolYear, String complainant){
		return reportRepository.findReportsByGradeSectionAndSchoolYearOrComplainant(grade, section, schoolYear, complainant);
	}
	
	public List<ReportEntity> getAllReportsByComplainant(String complainant){
		return reportRepository.findAllByComplainant(complainant);
	}
	
//	public List<ReportEntity> getReportsExcludingComplainant(String complainant) {
//        return reportRepository.findReportsExcludingComplainant(complainant);
//    }
	
	public ReportEntity updateReport(Long reportId, Long id, String monitored_record ,ReportEntity updatedReport, Long initiator) throws Exception {
	    Optional<ReportEntity> existingReportOpt = reportRepository.findById(reportId);
	    if (existingReportOpt.isPresent()) {
	        ReportEntity existingReport = existingReportOpt.get();
	        
	        // Fetch the updated student using updatedReport's studentId
	        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
	        if (studentOptional.isEmpty()) {
	            throw new Exception("Student not found");
	        }

	        StudentEntity student = studentOptional.get();

	        // Retrieve the adviser based on the student's section and school year
	        Optional<UserEntity> adviserOptional = userRepository.findByGradeAndSectionAndSchoolYearAndDeleted(student.getGrade(), student.getSection(), student.getSchoolYear(), false);
	        if (adviserOptional.isEmpty()) {
	            throw new Exception("Adviser not found for the student's section and school year");
	        }

	        UserEntity adviser = adviserOptional.get();
	        existingReport.setAdviserId(adviser.getUserId());
	        
	        Optional<StudentRecordEntity> studentRecordOpt = studentRecordRepository.findById(updatedReport.getRecordId());
	        if (studentRecordOpt.isEmpty()) {
	            throw new Exception("Student record not found with ID: " + updatedReport.getRecordId());
	        }
	        StudentRecordEntity studentRecord = studentRecordOpt.get();
	        studentRecord.setId(id);
	        studentRecord.setMonitored_record(monitored_record);
	        studentRecord.setRemarks(updatedReport.getComplaint());
	        
	        studentRecordRepository.save(studentRecord);
	        
	        existingReport.setComment(updatedReport.getComment());
	        existingReport.setComplaint(updatedReport.getComplaint());
	        existingReport.setComplete(updatedReport.isComplete());
	        existingReport.setReceived(null);
	        
	        Optional<UserEntity> optionalUser1 = userRepository.findById(initiator);
	        UserEntity user = optionalUser1.get();
	        activityLogService.logActivity("Modify Report", "Report ID " + reportId + " modifed by User "+ user.getUsername(), initiator);

	        // Save and return the updated report
	        return reportRepository.save(existingReport);
	    } else {
	        throw new Exception("Report not found with ID: " + reportId);
	    }
	}

	
	public Optional<ReportEntity> getReportById(Long reportId) {
	    return reportRepository.findById(reportId);  // Fetch report by ID from the repository
	}
	
	
	 public void deleteReport(Long reportId, Long initiator) {
		 boolean suspensionExist = false;
	        // Find any existing suspensions associated with the report
	        Optional<SuspensionEntity> suspension = suspensionRepository.findByReportId(reportId);
	        if (suspension.isPresent()) {
	            // If found, delete the suspension first
	            suspensionRepository.delete(suspension.get());
	            suspensionExist = true;
	        }

	        // Now delete the report
	        Optional<ReportEntity> report = reportRepository.findById(reportId);
	        if (report.isPresent()) {    	
	        	
	            reportRepository.delete(report.get());
	            Optional<StudentRecordEntity> studentRecord = studentRecordRepository.findById(report.get().getRecordId());
	        	 if (studentRecord.isPresent()) {
	 	            studentRecordRepository.delete(studentRecord.get());
	        	 }
	            
	            
	            Optional<UserEntity> optionalUser1 = userRepository.findById(initiator);
	            UserEntity user = optionalUser1.get();
	            String logMessage = "Report ID " + reportId + (suspensionExist ? " and its suspension" : "") + " deleted by User " + user.getUsername() + " along with the associated record";
	            activityLogService.logActivity("Delete Report", logMessage, initiator);
	        } else {
	            throw new RuntimeException("Report not found for id: " + reportId);
	        }
	    }
	 
	 @Transactional
	 public void deleteAllReportsByComplainant(String complainant) {
		 suspensionRepository.deleteAllByReportEntity_Complainant(complainant);
		 reportRepository.deleteAllByComplainant(complainant);
	 }
	



}
