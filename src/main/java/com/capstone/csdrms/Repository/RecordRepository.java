package com.capstone.csdrms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.capstone.csdrms.Entity.RecordEntity;



@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
	      
	List<RecordEntity> findAllByStudent_Sid(String sid);
	List<RecordEntity> findAllByStudent_GradeAndStudent_SectionAndStudent_SchoolYear(int grade, String section, String schoolYear);
	
	@Query("SELECT r FROM RecordEntity r WHERE (r.student.grade = :grade AND r.student.section = :section AND r.student.schoolYear = :schoolYear) OR r.userId = :userId")
	List<RecordEntity> findRecordsByGradeSectionAndSchoolYearOrUserId(@Param("grade") int grade, @Param("section") String section, @Param("schoolYear") String schoolYear, @Param("userId") Long userId);
	
	List<RecordEntity> findAllByUserId(Long userId);
	
	List<RecordEntity> findAllByStudent_SidAndStudent_SectionAndStudent_SchoolYear(String sid, String section, String schoolYear);
	
	void deleteAllById(Long id);
	
	@Query("SELECT COUNT(r) > 0 FROM RecordEntity r WHERE r.record_date = :recordDate AND r.time = :time AND r.student.id = :studentId AND r.monitored_record = :monitoredRecord AND r.remarks = :remarks AND r.sanction = :sanction AND r.encoder = :encoder")
    boolean existsByUniqueFields(String recordDate, String time, Long studentId, String monitoredRecord, String remarks, String sanction, String encoder);

	
}
 