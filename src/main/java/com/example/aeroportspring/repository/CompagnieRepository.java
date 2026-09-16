package com.example.aeroportspring.repository;

import com.example.aeroportspring.model.Compagnie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface vide : Spring Data JPA genere lui-meme findAll, findById, save, deleteById...
//                                                     entite     type de l'id
@Repository
public interface CompagnieRepository extends JpaRepository<Compagnie, Integer> {
}
