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
	
	@PostMapping("/insertSuspension/{initiator}")
	public SuspensionEntity insertSuspension(@RequestBody SuspensionEntity suspension, @PathVariable Long initiator) {
		return suspensionService.insertSuspension(suspension, initiator);
	}
	
	@GetMapping("/getSuspensionsByStudentId/{id}")
	public List<SuspensionEntity> getAllSuspensionsByStudentId(@PathVariable Long id){
		return suspensionService.getAllSuspensionsByStudentId(id);
	}
	

    @PostMapping("/markAsViewedForPrincipal/{suspensionId}/{initiator}")
    public void markSuspensionsAsViewedForPrincipal(@PathVariable Long suspensionId, @PathVariable Long initiator ) {
    	suspensionService.markSuspensionsAsViewedForPrincipal(suspensionId,initiator);
    }
    
    @GetMapping("/getSuspensionByRecord/{recordId}")
    public ResponseEntity<SuspensionEntity> getSuspensionByReportId(@PathVariable Long recordId) {
        return suspensionService.getSuspensionByRecordId(recordId)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/delete/{suspensionId}/{initiator}")
    public ResponseEntity<String> deleteSuspension(@PathVariable Long suspensionId,  @PathVariable Long initiator) {
        try {
            suspensionService.deleteSuspension(suspensionId,initiator);
            return ResponseEntity.ok("Suspension deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @PutMapping("/update/{suspensionId}/{initiator}")
    public ResponseEntity<SuspensionEntity> updateSuspension(
            @PathVariable Long suspensionId,
            @RequestBody SuspensionEntity updatedSuspensionData,
            @PathVariable Long initiator) {

        try {
            SuspensionEntity updatedSuspension = suspensionService.updateSuspension(suspensionId, updatedSuspensionData,initiator);
            return new ResponseEntity<>(updatedSuspension, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
	@PostMapping("/approveSuspension/{initiator}")
	public boolean approveSuspension(@RequestParam Long suspensionId, @PathVariable Long initiator) {
		return suspensionService.approveSuspension(suspensionId, initiator);
	} 
    
	
	

//	@GetMapping("/getSanctionsById/{id}")
//	public List<SuspensionEntity> getAllSanctionsById(@PathVariable Long id){
//		return sserv.getAllSanctionsById(id);
//	}
//	

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
