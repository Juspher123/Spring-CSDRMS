package com.capstone.csdrms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.ActivityLogEntity;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {

	List<ActivityLogEntity> findByUserId(Long userId);
}
