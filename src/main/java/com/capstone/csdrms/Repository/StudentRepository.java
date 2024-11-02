package com.capstone.csdrms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.StudentEntity;


@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

//	Optional<StudentEntity> findBySid(String sid);
	Optional<StudentEntity> findByIdAndCurrent(Long id, int current);
	List<StudentEntity> findAllBySectionAndSchoolYear(String section, String schoolYear);
	List<StudentEntity> findAllBySid(String sid);
	
	Optional<StudentEntity> findBySidAndCurrent(String sid, int current);
	
	boolean existsBySidAndSchoolYear(String sid, String schoolYear); 
	
	List<StudentEntity> findAllByCurrent(int current);
	
	List<StudentEntity> findByCurrentAndGradeAndSectionAndSchoolYear(int current,int grade, String section, String schoolYear);
	
	@Query("SELECT s FROM StudentEntity s WHERE s.sid = :sid ORDER BY FUNCTION('SUBSTRING', s.schoolYear, 1, 4) DESC")
	List<StudentEntity> findStudentsBySidOrderBySchoolYearDesc(@Param("sid") String sid);

	
}  