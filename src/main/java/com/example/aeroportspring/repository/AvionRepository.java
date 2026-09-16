package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Avion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public interface AvionRepository extends JpaRepository<Avion, Integer> {
}

