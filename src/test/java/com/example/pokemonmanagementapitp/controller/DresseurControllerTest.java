package com.example.pokemonmanagementapitp.controller;

import com.example.pokemonmanagementapitp.dto.DresseurDto;
import com.example.pokemonmanagementapitp.service.DresseurNotFoundException;
import com.example.pokemonmanagementapitp.service.DresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DresseurService dresseurService;

    @Test
    void should_return_200_and_dto_when_dresseur_exists() throws Exception {
        // Arrange
        when(dresseurService.getDresseur(1L)).thenReturn(new DresseurDto("Sacha", 3, 160));

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/1"));

        // Assert
        resultat.andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(160));
    }

    @Test
    void should_return_404_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        when(dresseurService.getDresseur(999L)).thenThrow(new DresseurNotFoundException(999L));

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/999"));

        // Assert
        resultat.andExpect(status().isNotFound());
    }
}
