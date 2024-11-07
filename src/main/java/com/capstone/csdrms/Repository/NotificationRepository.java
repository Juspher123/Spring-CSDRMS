package com.capstone.csdrms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.csdrms.Entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository <NotificationEntity, Long>{
	
	


}
