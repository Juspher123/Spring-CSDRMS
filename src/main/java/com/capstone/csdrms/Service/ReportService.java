package com.capstone.csdrms.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.StudentRecordEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.UserRepository;

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
	
    public ReportEntity insertReport(Long id, ReportEntity report) throws Exception {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new Exception("Student not found");
        }

        StudentEntity student = studentOptional.get();

        Optional<UserEntity> adviserOptional = userRepository.findByGradeAndSectionAndSchoolYear(student.getGrade(),student.getSection(), student.getSchoolYear());
        if (adviserOptional.isEmpty()) {
            throw new Exception("Adviser not found for the student's section and school year");
        }

        UserEntity adviser = adviserOptional.get();
        report.setAdviserId(adviser.getUserId());

        // Save the report
        ReportEntity savedReport = reportRepository.save(report);

        // Automatically create a StudentRecordEntity
        StudentRecordEntity studentRecord = new StudentRecordEntity();
        studentRecord.setSid(student.getSid());
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

        return savedReport;
    }
	
	public List<ReportEntity> getAllReports(){
		return reportRepository.findAll();
	}
	
	public ReportEntity completeReport(Long reportId) throws Exception {
        Optional<ReportEntity> reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isPresent()) {
            ReportEntity report = reportOpt.get();
            report.setComplete(true);  // Mark the report as complete
            return reportRepository.save(report);  // Save the updated entity
        } else {
            throw new Exception("Report not found with ID: " + reportId);
        }
    }
	
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
	
	public List<ReportEntity> getReportsExcludingComplainant(String complainant) {
        return reportRepository.findReportsExcludingComplainant(complainant);
    }
	
	public ReportEntity updateReport(Long reportId, Long id, String monitored_record ,ReportEntity updatedReport) throws Exception {
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
	        Optional<UserEntity> adviserOptional = userRepository.findByGradeAndSectionAndSchoolYear(student.getGrade(), student.getSection(), student.getSchoolYear());
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
	        
	        
	        existingReport.setComplaint(updatedReport.getComplaint());
	        existingReport.setComplete(false);
	        existingReport.setReceived(null);
	        existingReport.setViewedByAdviser(false);
	        existingReport.setViewedBySso(false);

	        // Save and return the updated report
	        return reportRepository.save(existingReport);
	    } else {
	        throw new Exception("Report not found with ID: " + reportId);
	    }
	}

	
	public Optional<ReportEntity> getReportById(Long reportId) {
	    return reportRepository.findById(reportId);  // Fetch report by ID from the repository
	}
	
	public List<ReportEntity> getAllUnviewedReportsForSso(){
		return reportRepository.findAllByViewedBySsoFalse();
	}
	
	public List<ReportEntity> getAllUnviewedReportsForAdviser(int grade, String section, String schoolYear){
		return reportRepository.findAllByRecord_Student_GradeAndRecord_Student_SectionAndRecord_Student_SchoolYearAndViewedByAdviserFalse(grade, section, schoolYear);
	}
	
	public void markReportsAsViewedForSso() {
		List<ReportEntity> reports = reportRepository.findAllByViewedBySsoFalse();
		reports.forEach(report -> report.setViewedBySso(true));
		reportRepository.saveAll(reports);
	}
	
	public void markReportsAsViewedForAdviser(int grade, String section, String schoolYear) {
		List<ReportEntity> reports = reportRepository.findAllByRecord_Student_GradeAndRecord_Student_SectionAndRecord_Student_SchoolYearAndViewedByAdviserFalse(grade, section, schoolYear);
		reports.forEach(report -> report.setViewedByAdviser(true));
		reportRepository.saveAll(reports);
	}
	



}
