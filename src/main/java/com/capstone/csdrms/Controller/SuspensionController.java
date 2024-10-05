package com.capstone.csdrms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	SuspensionService sserv;
	
	@GetMapping("/getAllSuspensions")
	public List<SuspensionEntity> getAllSuspensions(){
		return sserv.getAllSuspensions();
	}
	
	@PostMapping("/insertSuspension")
	public SuspensionEntity insertSuspension(@RequestBody SuspensionEntity suspension) {
		return sserv.insertSuspension(suspension);
	}
	
	@GetMapping("/getSuspensionsByStudentId/{id}")
	public List<SuspensionEntity> getAllSuspensionsByStudentId(@PathVariable Long id){
		return sserv.getAllSuspensionsByStudentId(id);
	}
	
	@GetMapping("/getAllSuspensionsBySectionAndSchoolYear")
	public List<SuspensionEntity> getAllSuspensionsBySectionAndSchoolYear(@RequestParam String section,@RequestParam String schoolYear){
		return sserv.getAllSuspensionsBySectionAndSchoolYear(section, schoolYear);
	}
	
	@GetMapping("/unviewedForSso")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForSso() {
        return sserv.getAllUnviewedSuspensionsForSso();
    }

    @GetMapping("/unviewedForPrincipal")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForPrincipal() {
        return sserv.getAllUnviewedSuspensionsForPrincipal();
    }

    @GetMapping("/unviewedForAdviser")
    public List<SuspensionEntity> getAllUnviewedSuspensionsForAdviser(@RequestParam String section, @RequestParam String schoolYear) {
        return sserv.getAllUnviewedSuspensionsForAdviser(section, schoolYear);
    }

    // New methods to mark suspensions as viewed

    @PostMapping("/markAsViewedForSso")
    public void markSuspensionsAsViewedForSso() {
        sserv.markSuspensionsAsViewedForSso();
    }

    @PostMapping("/markAsViewedForPrincipal")
    public void markSuspensionsAsViewedForPrincipal() {
        sserv.markSuspensionsAsViewedForPrincipal();
    }

    @PostMapping("/markAsViewedForAdviser")
    public void markSuspensionsAsViewedForAdviser(@RequestParam String section, @RequestParam String schoolYear) {
        sserv.markSuspensionsAsViewedForAdviser(section, schoolYear);
    }
    
    @GetMapping("/getSuspensionByReport/{reportId}")
    public ResponseEntity<SuspensionEntity> getSuspensionByReportId(@PathVariable Long reportId) {
        return sserv.getSuspensionByReportId(reportId)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
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
