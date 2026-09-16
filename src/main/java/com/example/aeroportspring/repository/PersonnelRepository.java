package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Integer> {
}
