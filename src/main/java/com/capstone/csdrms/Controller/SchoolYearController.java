package com.capstone.csdrms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.SchoolYearEntity;
import com.capstone.csdrms.Service.SchoolYearService;

@RestController
@RequestMapping("/schoolYear")
public class SchoolYearController {

	@Autowired
	SchoolYearService syService;

	@GetMapping("/getAllSchoolYears")
	public List<SchoolYearEntity> getAllSchoolYears() {
		return syService.getAllSchoolYears();
	}

	@PostMapping("/addSchoolYear")
	public String addSchoolYear(@RequestBody SchoolYearEntity syEntity) {
		return syService.addSchoolYear(syEntity);
	}

	@DeleteMapping("/deleteSchoolYear/{schoolYear_ID}") // Use DELETE method
	public ResponseEntity<String> deleteSchoolYear(@PathVariable Long schoolYear_ID) {
		boolean isDeleted = syService.deleteSchoolYear(schoolYear_ID);
		if (isDeleted) {
			return ResponseEntity.ok("School year deleted successfully.");
		} else {
			return ResponseEntity.status(404).body("School year not found.");
		}
	}

}
