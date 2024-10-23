//package com.capstone.csdrms.Service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.capstone.csdrms.Entity.FeedbackEntity;
//import com.capstone.csdrms.Repository.FeedbackRepository;
//
//
//@Service
//public class FeedbackService {
//
//	@Autowired
//	FeedbackRepository frepo;
//	
//	public FeedbackEntity insertFeedback(FeedbackEntity feedback) {
//		return frepo.save(feedback);
//	}
//	
//	 public List<FeedbackEntity> getAllFeedbacksForAdviser(String section, String schoolYear) {
//	    return frepo.findAllByCaseEntity_Student_SectionAndCaseEntity_Student_SchoolYear(section,schoolYear);
//	    }
//	 
//	 public List<FeedbackEntity> getAllFeedbackBacks(){
//		 return frepo.findAll();
//	 }
//	 
//
//}
