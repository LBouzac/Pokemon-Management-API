package com.example.pokemonmanagementapitp.service;

import com.example.pokemonmanagementapitp.dto.DresseurDto;
import com.example.pokemonmanagementapitp.model.Dresseur;
import com.example.pokemonmanagementapitp.model.Pokemon;
import com.example.pokemonmanagementapitp.repository.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    private static final Long ID = 1L;

    @Mock
    private DresseurRepository dresseurRepository;

    @InjectMocks
    private DresseurService dresseurService;

    private void givenDresseurAvec(Pokemon... pokemons) {
        Dresseur dresseur = new Dresseur("Sacha", "Kanto");
        dresseur.setPokemons(List.of(pokemons));
        when(dresseurRepository.findById(ID)).thenReturn(Optional.of(dresseur));
    }

    @Test
    void should_return_1_when_dresseur_has_no_pokemon() {
        // Arrange
        givenDresseurAvec();

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void should_return_sum_of_levels_when_no_bonus_applies() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Salamèche", "Feu", 10),
                new Pokemon("Carapuce", "Eau", 20));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Salamèche", "Feu", 10),
                new Pokemon("Carapuce", "Eau", 20),
                new Pokemon("Pikachu", "Électrik", 30));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(60 + 20, niveau);
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_only_2_types() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Salamèche", "Feu", 10),
                new Pokemon("Goupix", "Feu", 15),
                new Pokemon("Carapuce", "Eau", 20));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(45, niveau);
    }

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_exactly_50() {
        // Arrange
        givenDresseurAvec(new Pokemon("Dracaufeu", "Feu", 50));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(50 + 10, niveau);
    }

    @Test
    void should_not_apply_elite_bonus_when_pokemon_level_is_49() {
        // Arrange
        givenDresseurAvec(new Pokemon("Reptincel", "Feu", 49));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void should_apply_elite_bonus_per_pokemon_when_several_are_elite() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Dracaufeu", "Feu", 50),
                new Pokemon("Arcanin", "Feu", 70));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(120 + 2 * 10, niveau);
    }

    @Test
    void should_cumulate_all_bonuses_when_diversity_and_elite_conditions_met() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Dracaufeu", "Feu", 50),
                new Pokemon("Tortank", "Eau", 60),
                new Pokemon("Pikachu", "Électrik", 10));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(ID);

        // Assert : 120 (somme) + 20 (diversité) + 2 x 10 (élites)
        assertEquals(160, niveau);
    }

    @Test
    void should_throw_exception_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.findById(ID)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(DresseurNotFoundException.class, () -> dresseurService.calculerNiveauDresseur(ID));
    }

    @Test
    void should_build_dto_when_dresseur_exists() {
        // Arrange
        givenDresseurAvec(
                new Pokemon("Dracaufeu", "Feu", 50),
                new Pokemon("Tortank", "Eau", 60));

        // Act
        DresseurDto dto = dresseurService.getDresseur(ID);

        // Assert
        assertEquals(new DresseurDto("Sacha", 2, 110 + 2 * 10), dto);
    }
}
