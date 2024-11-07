package com.capstone.csdrms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.SuspensionEntity;

@Repository
public interface SuspensionRepository extends JpaRepository<SuspensionEntity, Long> {
	
	List<SuspensionEntity> findByReportEntity_Record_Student_Id(Long id);
	
	List<SuspensionEntity> findAllByReportEntity_Record_Student_GradeAndReportEntity_Record_Student_SectionAndReportEntity_Record_Student_SchoolYear(int grade, String section, String schoolYear);
	
	
	
	
	Optional<SuspensionEntity> findByReportId(Long reportId);
	
	List<SuspensionEntity> findAllByReportEntity_Complainant(String username);
	
	void deleteAllByReportEntity_Complainant(String complainant);
	
	void deleteAllByReportEntity_Record_Id(Long id);

	Optional<SuspensionEntity> findById(Long suspensionId);


	
//    List<SuspensionEntity> findByIsApproved(int isApproved);
//
//    Optional<SuspensionEntity> findByCaseEntity_Id(Long id);
//    List<SuspensionEntity> findByIsApprovedIn(List<Integer> isApprovedValues);
//    
////    List<SanctionEntity> findBySid(String sid);
//    
//    
//    List<SuspensionEntity> findByCaseEntity_Student_SectionAndCaseEntity_Student_SchoolYear(String section, String schoolYear);
//
//	List<SuspensionEntity> findAllByCaseEntity_Id(Long id);
}
