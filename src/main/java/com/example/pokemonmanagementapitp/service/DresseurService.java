package com.example.pokemonmanagementapitp.service;

import com.example.pokemonmanagementapitp.dto.DresseurDto;
import com.example.pokemonmanagementapitp.model.Dresseur;
import com.example.pokemonmanagementapitp.model.Pokemon;
import com.example.pokemonmanagementapitp.repository.DresseurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DresseurService {

    static final int NIVEAU_SANS_POKEMON = 1;
    static final int SEUIL_TYPES_DIVERSITE = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int SEUIL_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;

    public DresseurService(DresseurRepository dresseurRepository) {
        this.dresseurRepository = dresseurRepository;
    }

    public int calculerNiveauDresseur(Long dresseurId) {
        return calculerNiveau(trouverDresseur(dresseurId).getPokemons());
    }

    public DresseurDto getDresseur(Long dresseurId) {
        Dresseur dresseur = trouverDresseur(dresseurId);
        List<Pokemon> pokemons = dresseur.getPokemons();
        return new DresseurDto(dresseur.getNom(), pokemons.size(), calculerNiveau(pokemons));
    }

    private Dresseur trouverDresseur(Long dresseurId) {
        return dresseurRepository.findById(dresseurId)
                .orElseThrow(() -> new DresseurNotFoundException(dresseurId));
    }

    private int calculerNiveau(List<Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            return NIVEAU_SANS_POKEMON;
        }
        int somme = pokemons.stream().mapToInt(Pokemon::getNiveau).sum();
        // Types comparés tels quels : "Feu" et "feu" comptent comme deux types.
        long typesDistincts = pokemons.stream().map(Pokemon::getType).distinct().count();
        int bonusDiversite = typesDistincts >= SEUIL_TYPES_DIVERSITE ? BONUS_DIVERSITE : 0;
        long elites = pokemons.stream().filter(p -> p.getNiveau() >= SEUIL_ELITE).count();
        return somme + bonusDiversite + (int) elites * BONUS_ELITE;
    }
}
