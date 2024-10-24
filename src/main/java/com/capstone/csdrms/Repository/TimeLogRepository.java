package com.capstone.csdrms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.TimeLogEntity;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLogEntity, Long>{

	TimeLogEntity findFirstByUserIdOrderByLoginTimeDesc(Long userId);
	
	List<TimeLogEntity> findAllByUserId(Long userId);
	
	 void deleteAllByUserId(Long userId);


}
