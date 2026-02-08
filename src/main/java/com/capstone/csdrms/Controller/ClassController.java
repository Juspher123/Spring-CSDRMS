package com.capstone.csdrms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.ClassEntity;
import com.capstone.csdrms.Service.ClassService;

@RestController
@RequestMapping("/class")
public class ClassController {

	@Autowired
	ClassService classService;

	@GetMapping("/getAllClasses")
	public List<ClassEntity> getAllClasses() {
		return classService.getAllClasses();
	}

	@GetMapping("/sections/{grade}")
	public List<String> getAllSectionsByGrade(@PathVariable String grade) {
		return classService.getAllSectionsByGrade(grade);
	}

	@GetMapping("/allUniqueGrades")
	public List<String> getAllUniqueGrades() {
		return classService.getAllUniqueGrades();
	}

	@PostMapping("/addClass")
	public ClassEntity insertClass(@RequestBody ClassEntity classEntity) {
		return classService.insertClass(classEntity);
	}

	@PutMapping("/updateClass")
	public ClassEntity updateClass(@RequestParam Long classId, @RequestBody ClassEntity newClass) {
		return classService.updateClass(classId, newClass);
	}

	@DeleteMapping("/deleteClass/{classId}")
	public ResponseEntity<String> deleteClass(@PathVariable Long classId) {
		boolean isDeleted = classService.deleteClass(classId);
		if (isDeleted) {
			return ResponseEntity.ok("Class deleted successfully.");
		} else {
			return ResponseEntity.status(404).body("Class with id " + classId + " not found.");
		}
	}

}
