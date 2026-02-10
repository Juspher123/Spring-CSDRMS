package com.capstone.csdrms.Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/db")
    public ResponseEntity<String> healthDb() {
        entityManager.createNativeQuery("SELECT 1").getSingleResult();
        return ResponseEntity.ok("ok");
    }
}
