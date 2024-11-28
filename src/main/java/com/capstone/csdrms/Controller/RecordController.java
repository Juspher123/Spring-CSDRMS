package com.capstone.csdrms.Controller;

import java.util.List;

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
	public List<RecordEntity> getAllRecordsByAdviser(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear, @RequestParam Long encoderId ) {
	    return recordService.getAllRecordsByAdviser(grade, section, schoolYear, encoderId);
	}
	
	@GetMapping("/getAllRecordsByEncoderId")
	public List<RecordEntity> getAllRecordsByEncoderId(@RequestParam Long encoderId){
		return recordService.getAllRecordsByEncoderId(encoderId);
	}
	
}
