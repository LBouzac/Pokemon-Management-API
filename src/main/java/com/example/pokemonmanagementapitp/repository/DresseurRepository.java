package com.example.pokemonmanagementapitp.repository;

import com.example.pokemonmanagementapitp.model.Dresseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DresseurRepository extends JpaRepository<Dresseur, Long> {

    Optional<Dresseur> findByNom(String nom);
}
