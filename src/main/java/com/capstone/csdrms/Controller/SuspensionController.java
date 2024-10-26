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

import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Service.SuspensionService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/suspension")
public class SuspensionController {

	@Autowired
	SuspensionService suspensionService;
	
	@GetMapping("/getAllSuspensions")
	public List<SuspensionEntity> getAllSuspensions(){
		return suspensionService.getAllSuspensions();
	}
	
	@PostMapping("/insertSuspension")
	public SuspensionEntity insertSuspension(@RequestBody SuspensionEntity suspension) {
		return suspensionService.insertSuspension(suspension);
	}
	
	@GetMapping("/getSuspensionsByStudentId/{id}")
	public List<SuspensionEntity> getAllSuspensionsByStudentId(@PathVariable Long id){
		return suspensionService.getAllSuspensionsByStudentId(id);
	}
	
	@GetMapping("/getAllSuspensionsByGradeSectionAndSchoolYear")
	public List<SuspensionEntity> getAllSuspensionsByGradeSectionAndSchoolYear(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear){
		return suspensionService.getAllSuspensionsByGradeSectionAndSchoolYear(grade, section, schoolYear);
	}
	
	@GetMapping("/getAllSuspensionsByComplainant")
	public List<SuspensionEntity> getAllSuspensionByComplainant(@RequestParam String username){
		return suspensionService.getAllSuspensionByComplainant(username);
	}
	
	@GetMapping("/unviewedForSso")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForSso() {
        return suspensionService.getAllUnviewedSuspensionsForSso();
    }

    @GetMapping("/unviewedForPrincipal")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForPrincipal() {
        return suspensionService.getAllUnviewedSuspensionsForPrincipal();
    }

    @GetMapping("/unviewedForAdviser")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForAdviser(@RequestParam int grade, @RequestParam String section, @RequestParam String schoolYear) {
        return suspensionService.getAllUnviewedSuspensionsForAdviser(grade, section, schoolYear);
    }
    
    @GetMapping("/unviewedForComplainant")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForComplainant(@RequestParam String username){
    	return suspensionService.getAllUnviewedSuspensionsForComplainant(username);
    }
    
    // New methods to mark suspensions as viewed

    @PostMapping("/markAsViewedForSso")
    public void markSuspensionsAsViewedForSso() {
    	suspensionService.markSuspensionsAsViewedForSso();
    }

    @PostMapping("/markAsViewedForPrincipal")
    public void markSuspensionsAsViewedForPrincipal() {
    	suspensionService.markSuspensionsAsViewedForPrincipal();
    }

    @PostMapping("/markAsViewedForAdviser")
    public void markSuspensionsAsViewedForAdviser(@RequestParam int grade, @RequestParam String section, @RequestParam String schoolYear) {
    	suspensionService.markSuspensionsAsViewedForAdviser(grade, section, schoolYear);
    }
    
    @PostMapping("/markAsViewedForComplainant")
    public void markSuspensionsAsViewedForComplainant(@RequestParam String username) {
    	suspensionService.markSuspensionsAsViewedForComplainant(username);
    }
    
    @GetMapping("/getSuspensionByReport/{reportId}")
    public ResponseEntity<SuspensionEntity> getSuspensionByReportId(@PathVariable Long reportId) {
        return suspensionService.getSuspensionByReportId(reportId)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/delete/{suspensionId}")
    public ResponseEntity<String> deleteSuspension(@PathVariable Long suspensionId) {
        try {
            suspensionService.deleteSuspension(suspensionId);
            return ResponseEntity.ok("Suspension deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @PutMapping("/update/{suspensionId}")
    public ResponseEntity<SuspensionEntity> updateSuspension(
            @PathVariable Long suspensionId,
            @RequestBody SuspensionEntity updatedSuspensionData) {

        try {
            SuspensionEntity updatedSuspension = suspensionService.updateSuspension(suspensionId, updatedSuspensionData);
            return new ResponseEntity<>(updatedSuspension, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
	
	

//	@GetMapping("/getSanctionsById/{id}")
//	public List<SuspensionEntity> getAllSanctionsById(@PathVariable Long id){
//		return sserv.getAllSanctionsById(id);
//	}
//	
//	@PostMapping("/approveSanction")
//	public boolean approveSanction(@RequestParam int sanctionId) {
//		return sserv.approveSanction(sanctionId);
//	} 
//	
//	@PostMapping("/declineSanction")
//	public boolean declineSanction(@RequestParam int sanctionId) {
//		return sserv.declineSanction(sanctionId);
//	}
//	
//	@GetMapping("/getApprovedAndDeclinedSanctions")
//	public List<SuspensionEntity> getApprovedAndDeclinedSanctions() {
//	    return sserv.getApprovedAndDeclinedSanctions();
//	}
//	
//	@GetMapping("/getSanctionsBySectionAndSchoolYear")
//	public List<SuspensionEntity> getSanctionsBySectionAndSchoolYear(@RequestParam String section,@RequestParam String schoolYear) {
//		return sserv.getSanctionsBySectionAndSchoolYear(section, schoolYear);
//	}
}
