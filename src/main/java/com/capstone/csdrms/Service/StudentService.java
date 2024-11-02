package com.capstone.csdrms.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;  // Correct Sheet import
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.capstone.csdrms.Entity.CaseEntity;
import com.capstone.csdrms.Entity.FollowupEntity;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.StudentRecordEntity;
import com.capstone.csdrms.Repository.CaseRepository;
import com.capstone.csdrms.Repository.FollowupRepository;
import com.capstone.csdrms.Repository.ReportRepository;
import com.capstone.csdrms.Repository.SuspensionRepository;

import jakarta.transaction.Transactional;

import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;



@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	StudentRecordRepository studentRecordRepository;
	
	@Autowired
	ReportRepository reportRepository;
//	
	
//	@Autowired
//	CaseRepository caserepo;
	
	@Autowired
    SuspensionRepository suspensionRepository;
	
//	@Autowired
//	FollowupRepository followuprepo;
	 
	 
	public boolean studentExists(String sid, String schoolYear) {
	    return studentRepository.existsBySidAndSchoolYear(sid, schoolYear);
	}

	public StudentEntity insertStudent(StudentEntity student) {
	    if (studentExists(student.getSid(), student.getSchoolYear())) {
	        throw new IllegalStateException("Student with this ID and school year already exists.");
	    }

	    List<StudentEntity> existingStudents = studentRepository.findAllBySid(student.getSid());
	    
	    if (!existingStudents.isEmpty()) {
	        for (StudentEntity existingStudent : existingStudents) {
	            existingStudent.setCurrent(0); 
	            studentRepository.save(existingStudent);   
	        }
	    }
	    return studentRepository.save(student);
	}
	
	public List<StudentEntity> getAllStudents(){
		return studentRepository.findAll();
	}
 
	 
	public List<StudentEntity> getCurrentStudents(){
		return studentRepository.findAllByCurrent(1);
	}
	
	public List<StudentEntity> getStudentsByAdviser(int grade, String section, String schoolYear) {
        return studentRepository.findByCurrentAndGradeAndSectionAndSchoolYear(1,grade, section, schoolYear);
    }
	
	 public StudentEntity updateStudent(Long id, StudentEntity studentDetails) {
	        // Find the existing student by ID
	        StudentEntity existingStudent = studentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Student not found for id: " + id));
	        
	        // Update fields
	        existingStudent.setSid(studentDetails.getSid());
	        existingStudent.setName(studentDetails.getName());
	        existingStudent.setGrade(studentDetails.getGrade());
	        existingStudent.setSection(studentDetails.getSection());
	        existingStudent.setGender(studentDetails.getGender());
	        existingStudent.setContactNumber(studentDetails.getContactNumber());
	        existingStudent.setSchoolYear(studentDetails.getSchoolYear());
	        existingStudent.setCurrent(studentDetails.getCurrent());
	        
	        // Save the updated student back to the database
	        return studentRepository.save(existingStudent);
	    }
	 
	 @Transactional
	 public void deleteLatestAndSetPreviousAsCurrent(Long id) {
		 Optional<StudentEntity> optionalStudent = studentRepository.findById(id);
		 if(optionalStudent.isPresent()) {
			 StudentEntity student = optionalStudent.get();
			 String sid = student.getSid();
			 
			 suspensionRepository.deleteAllByReportEntity_Record_Id(id);
			 reportRepository.deleteAllByRecord_Id(id);
			 studentRecordRepository.deleteAllById(id);
			 studentRepository.delete(student);
			 
			 
			 //Find the previous record by sorting by `schoolYear` in descending order
			  List<StudentEntity> sortedStudents = studentRepository.findStudentsBySidOrderBySchoolYearDesc(sid);
		        if (!sortedStudents.isEmpty()) {
		        	// Set the first record in the sorted list to `current = 1`
		            StudentEntity previousStudent = sortedStudents.get(0);
		            previousStudent.setCurrent(1);
		            studentRepository.save(previousStudent);
		        }
			  
		 }
		 else {
	            throw new RuntimeException("Student not found for id: " + id);
	        }
	       
	    }
	

//	@SuppressWarnings("finally")
//	public StudentEntity updateStudent(String sid, StudentEntity newStudentDetails) {
//		StudentEntity student = new StudentEntity();
//	    try {
//	    	student = srepo.findBySid(sid);
//
//	    	student.setFirstname(newStudentDetails.getFirstname());
//	    	student.setMiddlename(newStudentDetails.getMiddlename());
//	    	student.setLastname(newStudentDetails.getLastname());
//	    	student.setGrade(newStudentDetails.getGrade());
//	    	student.setSection(newStudentDetails.getSection());
//	    	student.setCon_num(newStudentDetails.getCon_num());
//	    	
//	       
//	    } catch (NoSuchElementException ex) {
//	        throw ex;
//	    } finally {
//	    	 return srepo.save(student);
//		}
//	}
//	
	
//	public String deleteStudent(String sid) {
//		Optional<StudentEntity> existingStudent = srepo.findBySid(sid);
//		
//		List<FeedbackEntity> existingFeedbacksByStudent = feedbackrepo.findALLByCaseEntity_Sid(sid);
//		List<StudentReportEntity> existingReportsByStudent = studentrepo.findAllBySid(sid);
//		List<CaseEntity> existingCasesByStudent = caserepo.findAllBySid(sid);
//		
//		List<FollowupEntity> existingFollowupsByStudent = followuprepo.findAllByCaseEntity_Sid(sid);
//		List<SanctionEntity> existingSanctionsByStudent = sanctionrepo.findBySid(sid);
//		
//		studentrepo.deleteAll(existingReportsByStudent);
//		sanctionrepo.deleteAll(existingSanctionsByStudent);
//		feedbackrepo.deleteAll(existingFeedbacksByStudent);
//		followuprepo.deleteAll(existingFollowupsByStudent);
//		caserepo.deleteAll(existingCasesByStudent);
//		 srepo.delete(existingStudent);
//		 
//		return "Student " + sid + " is successfully deleted!";
////	    if (existingStudent != null) {
////	        srepo.delete(existingStudent);
////	        return "Student " + sid + " is successfully deleted!";
////	    } else {
////	        return "Student " + sid + " does not exist";
////	    }
//	}
	
	public Optional<StudentEntity> getCurrentStudentById(Long id) {
		return studentRepository.findByIdAndCurrent(id, 1);
	}
	
	public Optional<StudentEntity> getStudentById(Long id){
		return studentRepository.findById(id);
	}
	
	public void importStudentData(MultipartFile file, String schoolYear) throws Exception {
	    List<StudentEntity> students = new ArrayList<>();
	    
	    // Pre-check for existing students before processing
	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);  // Assuming data is in the first sheet

	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue;  // Skip header row

	            String sid;
	            if (row.getCell(3).getCellType() == CellType.NUMERIC) {
	                sid = String.valueOf((long) row.getCell(3).getNumericCellValue());
	            } else {
	                sid = row.getCell(3).getStringCellValue();
	            }

	            // Check if the student with this SID and school year already exists
	            if (studentExists(sid, schoolYear)) {
	                throw new Exception("Import aborted: Student with SID " + sid + " and school year " + schoolYear + " already exists.");
	            }
	        }
	    }

	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);  // Assuming data is in the first sheet

	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue;  // Skip header row

	            StudentEntity student = new StudentEntity();
	            
	            student.setName(row.getCell(0).getStringCellValue());
	            
	            student.setGrade((int) row.getCell(1).getNumericCellValue());
	            
	            student.setSection(row.getCell(2).getStringCellValue());
	            
	            String sid;
	            if (row.getCell(3).getCellType() == CellType.NUMERIC) {
	                sid = String.valueOf((long) row.getCell(3).getNumericCellValue());
	            } else {
	                sid = row.getCell(3).getStringCellValue();
	            }
	            student.setSid(sid);
	            student.setGender(row.getCell(4).getStringCellValue());
	            
	            student.setContactNumber(row.getCell(5).getStringCellValue());
	            
	            // Handle School Year
	            student.setSchoolYear(schoolYear);

	            student.setCurrent(1);
	            
	            Optional<StudentEntity> existingStudent = studentRepository.findBySidAndCurrent(sid, 1);
	            if (existingStudent.isPresent() && !existingStudent.get().getSchoolYear().equals(schoolYear)) {
	                // Set current status of the existing student to 0
	                StudentEntity oldStudent = existingStudent.get();
	                oldStudent.setCurrent(0);
	                studentRepository.save(oldStudent);
	            }


	            students.add(student); 
	        }
	    }

	    studentRepository.saveAll(students);
	}


	
	
}