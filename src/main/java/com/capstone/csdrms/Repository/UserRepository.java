package com.capstone.csdrms.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.capstone.csdrms.Entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long>{

	Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByGradeAndSectionAndSchoolYear(int grade, String section, String schoolYear);
    Optional<UserEntity> findByUserType(int userType);
    
//    Optional<UserEntity> findBySectionAndSchoolYear(String section, String schooYear);
}
