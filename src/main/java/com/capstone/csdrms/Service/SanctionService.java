package com.capstone.csdrms.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.SanctionEntity;
import com.capstone.csdrms.Repository.SanctionRepository;

import jakarta.transaction.Transactional;

@Service
public class SanctionService {
	
	@Autowired
	SanctionRepository srepo;

	public SanctionEntity insertSanction(SanctionEntity sanction) {
		return srepo.save(sanction);
	}
	 
	public List<SanctionEntity> getPendingSanctions(){
        return srepo.findAllPendingSanctions();
    }
	
	public List<SanctionEntity> getAllSanctionsBySid(String sid){
        return srepo.findBySid(sid);
    }
	
	 @Transactional
	    public boolean approveSanction(int sanctionId) {
	        Optional<SanctionEntity> optionalSanction = srepo.findById(sanctionId);
	        if (optionalSanction.isPresent()) {
	            SanctionEntity sanction = optionalSanction.get();
	            sanction.setIsApproved(1);
	            srepo.save(sanction);
	            return true;
	        }
	        return false;
	    }
	 
	 @Transactional
	    public boolean declineSanction(int sanctionId) {
	        Optional<SanctionEntity> optionalSanction = srepo.findById(sanctionId);
	        if (optionalSanction.isPresent()) {
	            SanctionEntity sanction = optionalSanction.get();
	            sanction.setIsApproved(2);
	            srepo.save(sanction);
	            return true;
	        }
	        return false;
	    }
	 
	 public List<SanctionEntity> getApprovedAndDeclinedSanctions() {
		 return srepo.findByIsApprovedIn(List.of(1, 2));
	    }
	 
	 public List<SanctionEntity> getSanctionsBySectionAndSchoolYear(String section, String schoolYear) {
		    return srepo.findBySectionAndSchoolYear(section, schoolYear);
		}
}
