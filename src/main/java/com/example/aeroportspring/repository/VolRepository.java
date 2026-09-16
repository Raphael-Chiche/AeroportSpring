package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolRepository extends JpaRepository<Vol, Integer> {

    List<Vol> findByPassagers_Id(Integer passagerId);

    List<Vol> findByPersonnels_Id(Integer personnelId);

    List<Vol> findByCompagnie_Id(Integer compagnieId);

    List<Vol> findByDestination_Id(Integer aeroportId);
}
