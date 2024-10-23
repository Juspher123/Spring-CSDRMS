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
import com.capstone.csdrms.Repository.SuspensionRepository;
import com.capstone.csdrms.Repository.StudentRecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;



@Service
public class StudentService {

	@Autowired
	StudentRepository srepo;
	
	@Autowired
	StudentRecordRepository studentrepo;
	
	
	@Autowired
	CaseRepository caserepo;
	
	@Autowired
	SuspensionRepository sanctionrepo;
	
	@Autowired
	FollowupRepository followuprepo;
	 
	
	public boolean studentExists(String sid, String schoolYear) {
	    return srepo.existsBySidAndSchoolYear(sid, schoolYear);
	}

	public StudentEntity insertStudent(StudentEntity student) {
	    if (studentExists(student.getSid(), student.getSchoolYear())) {
	        throw new IllegalStateException("Student with this ID and school year already exists.");
	    }

	    List<StudentEntity> existingStudents = srepo.findAllBySid(student.getSid());
	    
	    if (!existingStudents.isEmpty()) {
	        for (StudentEntity existingStudent : existingStudents) {
	            existingStudent.setCurrent(0); 
	            srepo.save(existingStudent);   
	        }
	    }
	    return srepo.save(student);
	}
	
	public List<StudentEntity> getAllStudents(){
		return srepo.findAll();
	}
 
	 
	public List<StudentEntity> getCurrentStudents(){
		return srepo.findAllByCurrent(1);
	}
	
	public List<StudentEntity> getStudentsByAdviser(int grade, String section, String schoolYear) {
        return srepo.findByCurrentAndGradeAndSectionAndSchoolYear(1,grade, section, schoolYear);
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
		return srepo.findByIdAndCurrent(id, 1);
	}
	
	public Optional<StudentEntity> getStudentById(Long id){
		return srepo.findById(id);
	}
	
	public void importStudentData(MultipartFile file, String schoolYear) throws Exception {
	    List<StudentEntity> students = new ArrayList<>();

	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);  // Assuming data is in the first sheet

	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue;  // Skip header row

	            StudentEntity student = new StudentEntity();
	            
	            student.setName(row.getCell(0).getStringCellValue());
	            
	            student.setGrade((int) row.getCell(1).getNumericCellValue());
	            
	            student.setSection(row.getCell(2).getStringCellValue());
	            
	            // Handle SID (which could be numeric in Excel)
	            if (row.getCell(3).getCellType() == CellType.NUMERIC) {
	                student.setSid(String.valueOf((long) row.getCell(0).getNumericCellValue()));
	            } else {
	                student.setSid(row.getCell(3).getStringCellValue());
	            }
	            
	            student.setGender(row.getCell(4).getStringCellValue());
	            
	            // Handle School Year
	            student.setSchoolYear(schoolYear);

	            student.setCurrent(1);

	            students.add(student);
	        }
	    }

	    srepo.saveAll(students);
	}


	
	
}