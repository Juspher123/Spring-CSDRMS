package com.capstone.csdrms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone.csdrms.Entity.UserEntity;

import jakarta.transaction.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long>{

	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByUsernameAndDeleted(String username, boolean deleted);
    Optional<UserEntity> findByGradeAndSectionAndSchoolYearAndDeleted(int grade, String section, String schoolYear, boolean deleted);
    Optional<UserEntity> findByUserTypeAndDeleted(int userType, boolean deleted);
    
    Optional<UserEntity> findByUsernameAndUserTypeAndDeleted(String username, int userType, boolean deleted);
    
    
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.deleted = true WHERE u.username = :username")
    int softDeleteByUsername(String username);
    
    @Query("SELECT u FROM UserEntity u WHERE u.deleted = false")
    List<UserEntity> findAllActiveUsers();
    
    
    List<UserEntity> findAllByUserTypeInAndDeletedFalse(List<Integer> userTypes);

//    Optional<UserEntity> findBySectionAndSchoolYear(String section, String schooYear);
}
