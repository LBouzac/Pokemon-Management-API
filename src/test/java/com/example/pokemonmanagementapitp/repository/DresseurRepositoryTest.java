package com.example.pokemonmanagementapitp.repository;

import com.example.pokemonmanagementapitp.model.Dresseur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Jeu de données : src/test/resources/data.sql
@DataJpaTest
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Test
    void should_find_dresseur_when_nom_exists() {
        // Arrange
        String nom = "Sacha";

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findByNom(nom);

        // Assert
        assertTrue(resultat.isPresent());
        assertEquals("Kanto", resultat.get().getRegion());
    }

    @Test
    void should_return_empty_when_nom_does_not_exist() {
        // Arrange
        String nom = "Inconnu";

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findByNom(nom);

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void should_find_dresseur_when_id_exists() {
        // Arrange
        Long id = dresseurRepository.findByNom("Ondine").orElseThrow().getId();

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findById(id);

        // Assert
        assertTrue(resultat.isPresent());
        assertEquals("Ondine", resultat.get().getNom());
    }

    @Test
    void should_return_empty_when_id_does_not_exist() {
        // Arrange
        Long idInexistant = 999L;

        // Act
        Optional<Dresseur> resultat = dresseurRepository.findById(idInexistant);

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void should_load_pokemons_when_dresseur_has_pokemons() {
        // Arrange
        Dresseur sacha = dresseurRepository.findByNom("Sacha").orElseThrow();

        // Act
        int nombrePokemons = sacha.getPokemons().size();

        // Assert
        assertEquals(3, nombrePokemons);
    }

    @Test
    void should_return_no_pokemon_when_dresseur_has_none() {
        // Arrange
        Dresseur novice = dresseurRepository.findByNom("Novice").orElseThrow();

        // Act
        int nombrePokemons = novice.getPokemons().size();

        // Assert
        assertEquals(0, nombrePokemons);
    }

    @Test
    void should_persist_dresseur_when_saved() {
        // Arrange
        Dresseur pierre = new Dresseur("Pierre", "Kanto");

        // Act
        Dresseur sauvegarde = dresseurRepository.save(pierre);

        // Assert
        assertNotNull(sauvegarde.getId());
        assertEquals(4, dresseurRepository.count());
    }
}
