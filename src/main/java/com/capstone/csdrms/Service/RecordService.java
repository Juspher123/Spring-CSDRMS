package com.capstone.csdrms.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.NotificationEntity;
import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Entity.UserNotification;
import com.capstone.csdrms.Repository.NotificationRepository;
import com.capstone.csdrms.Repository.RecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.UserNotificationRepository;
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
	UserNotificationRepository userNotificationRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
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
		
		 // 1. Define the notification message
        String notificationMessage = "New report created for student " + student.getName() + " (Grade " + student.getGrade() + ", Section " + student.getSection() + ")";

        // 2. Set the user types who	 should receive the notification
        List<Integer> userTypes = new ArrayList<>();
        userTypes.add(1); // Assuming userType 1 should receive the notification
        userTypes.add(3); // Assuming userType 3 is for advisers

        // 3. Call notification service to create the notification for specific users
        notificationService.createNotificationForUserType("Report",savedRecord.getRecordId() ,notificationMessage, userTypes, initiator, student.getGrade(), student.getSection(), student.getSchoolYear());
		
		return savedRecord;
	}
	
	public void insertMultipleRecords(List<RecordEntity> records) {
        recordRepository.saveAll(records);
    }

	public List<RecordEntity> getAllStudentRecords(){
		return recordRepository.findAll();
	}
	
	public 	List<RecordEntity> getAllStudentRecordsByAdviser(int grade, String section, String schoolYear){
		return recordRepository.findAllByStudent_GradeAndStudent_SectionAndStudent_SchoolYear(grade, section, schoolYear);
	}
	
	public 	List<RecordEntity> getAllRecordsByAdviser(int grade, String section, String schoolYear, Long userId){
		return recordRepository.findRecordsByGradeSectionAndSchoolYearOrUserId(grade, section, schoolYear, userId);
	}
	
	public List<RecordEntity> getAllRecordsByUserId(Long userId){
		return recordRepository.findAllByUserId(userId);
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
            existingRecord.setRemarks(updatedRecord.getRemarks());
            existingRecord.setSanction(updatedRecord.getSanction());
            existingRecord.setComplainant(updatedRecord.getComplainant());
            existingRecord.setComplaint(updatedRecord.getComplaint());
            existingRecord.setInvestigationDetails(updatedRecord.getInvestigationDetails());
            existingRecord.setComplete(updatedRecord.getComplete());
            
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
	
	 public void deleteRecord(Long recordId, Long initiator) {
		 boolean suspensionExist = false;
	       
	        	 Optional<SuspensionEntity> suspension = suspensionRepository.findByRecordId(recordId);
	 	        if (suspension.isPresent()) {
	 	            // If found, delete the suspension first
	 	        	suspensionExist = true;
	 	            suspensionRepository.delete(suspension.get());
	 	        }
	 	        
	 	       List<NotificationEntity> notifications = notificationRepository.findByRecordId(recordId);
	 	      if (!notifications.isEmpty()) {
	 	          // For each notification, delete associated UserNotifications first
	 	          for (NotificationEntity notification : notifications) {
	 	              List<UserNotification> userNotifications = userNotificationRepository.findByNotification_NotificationId(notification.getNotificationId());
	 	              if (!userNotifications.isEmpty()) {
	 	                  userNotificationRepository.deleteAll(userNotifications);
	 	              }
	 	          }
	 	           
	 	          // Now delete the notifications
	 	          notificationRepository.deleteAll(notifications);
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
	 
	 
	 public void importRecords(MultipartFile file, Long initiator) throws Exception {
		    List<RecordEntity> records = new ArrayList<>();
		    
		    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
		        Sheet sheet = workbook.getSheetAt(1);  // Assuming data is in the first sheet

		        for (Row row : sheet) {
		            if (row.getRowNum() == 0) continue;  // Skip header row

		            RecordEntity record = new RecordEntity();
		            
		            record.setUserId(initiator);
		            
		            record.setRecord_date(getCellValue(row.getCell(1)));
		            
		            record.setTime(getCellValue(row.getCell(2)));
		            
		            Optional<StudentEntity> optionalStudent = studentRepository.findByName(row.getCell(3).getStringCellValue().trim().replaceAll("\\s([,!?])", "$1").replaceAll("\\s+", " "));
		            if(optionalStudent.isPresent()) {
		            	StudentEntity student = optionalStudent.get();
		            	record.setId(student.getId());
		            }  
		            
		            else {
		            	System.out.println(row.getCell(3).getStringCellValue().trim().replaceAll("\\s([,!?])", "$1").replaceAll("\\s+", " ") + " is not existing");
		            	continue;
		            }
		             
		            
		            record.setMonitored_record(getCellValue(row.getCell(4)));
		            
		            String remarks = getCellValue(row.getCell(5));
		            record.setRemarks(truncateRemarks(remarks, 255)); 
		            
		            record.setSanction(getCellValue(row.getCell(6)));
		            
		            record.setEncoder(getCellValue(row.getCell(8)));
		           		            		            

		            records.add(record); 
		        }
		    }

		    recordRepository.saveAll(records);
		}
	 
	 
	 private String truncateRemarks(String remarks, int maxLength) {
		    if (remarks == null) return "";
		    return remarks.length() > maxLength ? remarks.substring(0, maxLength) : remarks;
		}
	 
	 
	 private String getCellValue(Cell cell) {
		    if (cell == null) return "";  // If cell is null, return an empty string

		    switch (cell.getCellType()) {
		        case STRING:
		            return cell.getStringCellValue();
		        case NUMERIC:
		            // If the cell contains a number, check if it's a date
		            if (DateUtil.isCellDateFormatted(cell)) {
		                return new SimpleDateFormat("MM/dd/yyyy").format(cell.getDateCellValue());
		            } else {
		                // If it's a number, return it as a string
		                return String.valueOf(cell.getNumericCellValue());
		            }
		        case BOOLEAN:
		            return String.valueOf(cell.getBooleanCellValue());
		        case FORMULA:
		            return cell.getCellFormula();
		        default:
		            return "";
		    }  
		}
	   
	 

}
