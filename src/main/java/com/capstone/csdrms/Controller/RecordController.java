package com.capstone.csdrms.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Service.RecordService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/record")
public class RecordController {
 
	 @Autowired
	RecordService recordService;
	
	@PostMapping("/insert/{initiator}")
	public RecordEntity insertRecord(@RequestBody RecordEntity studentRecord, @PathVariable Long initiator) {
		return recordService.insertRecord(studentRecord, initiator);
	}
	
	 @PostMapping("/insertMultipleRecords")
	    public ResponseEntity<String> insertMultipleRecords(@RequestBody List<RecordEntity> records) {
	        try {
	            recordService.insertMultipleRecords(records);
	            return ResponseEntity.ok("Records inserted successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert records");
	        }
	    }
	
	@GetMapping("/getAllRecords")
	public List<RecordEntity> getAllStudentRecords(){
		return recordService.getAllStudentRecords();
	}

	@GetMapping("/getStudentRecords/{sid}")
	public List<RecordEntity> getStudentRecordsBySid(@PathVariable String sid){
		return recordService.getStudentRecordsBySid(sid);
	}
	
	@PutMapping("/update/{recordId}/{initiator}")
    public ResponseEntity<RecordEntity> updateStudentRecord(
            @PathVariable Long recordId, 
            @RequestBody RecordEntity updatedRecord,
            @PathVariable Long initiator) {
        try {
            // Call the service to update the student record
            RecordEntity updated = recordService.updateStudentRecord(recordId, updatedRecord, initiator);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Handle not found or other exceptions
        }
    }
	
//	@GetMapping("/getStudentRecordsByAdviser")
//	public List<StudentRecordEntity> getStudentRecordsByAdviser(@RequestParam  String sid,@RequestParam  String section,@RequestParam  String schoolYear){
//		return sserv.getStudentRecordsByAdviser(sid,section,schoolYear);
//	}
	
//	@PutMapping("/updateStudentReport") 
//	public StudentReportEntity updateStudentReport(@RequestParam int rid,@RequestBody StudentReportEntity newStudentReportDetails) {
//		return sserv.updateStudentReport(rid, newStudentReportDetails);
//	}
//	
	 @DeleteMapping("/delete/{recordId}/{initiator}")
	    public ResponseEntity<String> deleteStudentRecord(@PathVariable Long recordId, @PathVariable Long initiator) {
	        try {
	        	recordService.deleteRecord(recordId, initiator);
	            return ResponseEntity.ok("Student record deleted successfully.");
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(404).body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("An error occurred while deleting the student record.");
	        }
	    }
	
	@GetMapping("/getStudentRecordsByAdviser")
	public List<RecordEntity> getAllStudentRecordsByAdviser(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear) {
	    return recordService.getAllStudentRecordsByAdviser(grade, section, schoolYear);
	}
	
	@GetMapping("/getRecordsByAdviser")
	public List<RecordEntity> getAllRecordsByAdviser(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear, @RequestParam Long userId ) {
	    return recordService.getAllRecordsByAdviser(grade, section, schoolYear, userId);
	}
	
	@GetMapping("/getAllRecordsByUserId")
	public List<RecordEntity> getAllRecordsByUserId(@RequestParam Long userId){
		return recordService.getAllRecordsByUserId(userId);
	}
	
	@PostMapping("/import/{initiator}")
	public ResponseEntity<Map<String, Object>> importRecords(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long initiator) {
        try {
            // Import records and get non-existent students
            Set<String> nonExistentStudents = recordService.importRecords(file, initiator);
            
            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("importedCount", recordService.getImportedCount()); // Assuming a method to get count
            response.put("nonExistentStudents", nonExistentStudents);
            response.put("duplicateCount", recordService.getDuplicateCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            // Return an error message in case of failure
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to import records. " + e.getMessage()
            ));
        } 
    }
	 
}
