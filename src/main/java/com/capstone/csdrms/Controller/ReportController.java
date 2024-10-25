package com.capstone.csdrms.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.capstone.csdrms.Entity.ReportEntity;
import com.capstone.csdrms.Service.ReportService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	@PostMapping("/insertReport/{id}/{initiator}")
	public ResponseEntity<ReportEntity> insertReport( @PathVariable("id") Long id,  @RequestBody ReportEntity report, @PathVariable Long initiator ) throws Exception {
		  ReportEntity savedReport = reportService.insertReport(id, report, initiator);
		   return ResponseEntity.ok(savedReport);
	}
	
	@GetMapping("/getAllReports")
	public List<ReportEntity> getAllReports(){
		return reportService.getAllReports();
	}
	
	@PutMapping("/complete/{id}")
    public ResponseEntity<ReportEntity> completeReport(@PathVariable("id") Long reportId, @RequestParam String comment) {
        try {
            ReportEntity completedReport = reportService.completeReport(reportId, comment);
            return ResponseEntity.ok(completedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
	 
	@PutMapping("/updateReceived/{reportId}")
    public ResponseEntity<ReportEntity> updateReceived(@PathVariable Long reportId, @RequestBody Map<String, String> data) {
        Optional<ReportEntity> updatedReport = reportService.updateReceived(reportId, data.get("received"));
        return updatedReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
	
	@GetMapping("/getAllReportsForAdviser")
	public List<ReportEntity> getAllReportsForAdviser(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear, @RequestParam String complainant){
		return reportService.getAllReportsForAdviser(grade, section, schoolYear, complainant);
	}
	
	@GetMapping("/getAllReportsByComplainant")
	public List<ReportEntity> getAllReportsByComplainant(@RequestParam String complainant){
		return reportService.getAllReportsByComplainant(complainant);
	}
	
	@GetMapping("/notifications")
    public List<ReportEntity> getReportsExcludingComplainant(@RequestParam String complainant) {
        return reportService.getReportsExcludingComplainant(complainant);
    }
	
	@PutMapping("/updateReport/{reportId}/{id}/{monitored_record}/{initiator}")
	public ResponseEntity<ReportEntity> updateReport(
	    @PathVariable Long reportId, 
	    @PathVariable String monitored_record,
	    @PathVariable Long id,
	    @RequestBody ReportEntity updatedReport,
	    @PathVariable Long initiator) {
	    try {
	        ReportEntity report = reportService.updateReport(reportId, id, monitored_record, updatedReport, initiator);
	        return ResponseEntity.ok(report);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(null);
	    }
	}
	
	@GetMapping("/getReport/{reportId}")
	public ResponseEntity<ReportEntity> getReportById(@PathVariable Long reportId) {
	    Optional<ReportEntity> report = reportService.getReportById(reportId);  // Call service method
	    return report.map(ResponseEntity::ok) // If found, return 200 OK
	                 .orElseGet(() -> ResponseEntity.notFound().build()); // If not found, return 404
	}

	@GetMapping("/unviewedForSso")
	public List<ReportEntity> getAllUnviewedReportsForSso(){
		return reportService.getAllUnviewedReportsForSso();
	}
	
	@GetMapping("/unviewedForAdviser")
	public List<ReportEntity> getAllUnviewedReportsForAdviser(@RequestParam int grade, @RequestParam String section,@RequestParam String schoolYear){
		return reportService.getAllUnviewedReportsForAdviser(grade, section, schoolYear);
	}
	
	@PostMapping("/markAsViewedForSso")
    public void markReportsAsViewedForSso() {
		reportService.markReportsAsViewedForSso();
    }

	@PostMapping("/markAsViewedForAdviser")
    public void markReportsAsViewedForAdviser(@RequestParam int grade, @RequestParam String section, @RequestParam String schoolYear) {
		reportService.markReportsAsViewedForAdviser(grade, section, schoolYear);
    }
	
	@DeleteMapping("/delete/{reportId}/{initiator}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId,@PathVariable Long initiator) {
        try {
            reportService.deleteReport(reportId,initiator);
            return ResponseEntity.ok("Report and associated suspensions deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
	
	@DeleteMapping("/complainant/{complainant}")
    public ResponseEntity<String> deleteReportsByComplainant(@PathVariable String complainant) {
        try {
            reportService.deleteAllReportsByComplainant(complainant);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reports deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting reports: " + e.getMessage());
        }
    }


	
}
