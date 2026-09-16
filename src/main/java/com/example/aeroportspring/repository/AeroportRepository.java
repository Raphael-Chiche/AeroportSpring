package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Aeroport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Integer> {

    List<Aeroport> findByPersonnels_Id(Integer personnelId);
}
