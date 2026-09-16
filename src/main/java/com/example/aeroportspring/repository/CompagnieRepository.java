package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Compagnie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public interface CompagnieRepository extends JpaRepository<Compagnie, Integer> {
}
