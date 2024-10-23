//package com.capstone.csdrms.Controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.capstone.csdrms.Entity.FeedbackEntity;
//import com.capstone.csdrms.Service.FeedbackService;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/feedback")
//public class FeedbackController {
//	
//	@Autowired
//	FeedbackService fserv;
//	
//	@PostMapping("/insertFeedback")
//    public FeedbackEntity insertFeedback(@RequestBody FeedbackEntity feedback) {
//        return fserv.insertFeedback(feedback);
//    }  
//	
//	@GetMapping("/getFeedbacksForAdviser/{section}/{schoolYear}")
//    public List<FeedbackEntity> getAllFeedbacksForAdviser(@PathVariable String section,@PathVariable  String schoolYear) {
//        return fserv.getAllFeedbacksForAdviser(section,schoolYear);
//    }
//	
//	@GetMapping("/getFeedbacks")
//	 public List<FeedbackEntity> getAllFeedbackBacks(){
//		 return fserv.getAllFeedbackBacks();
//	 }
//	
//	
//	
//}
