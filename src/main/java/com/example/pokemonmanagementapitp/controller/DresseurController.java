package com.example.pokemonmanagementapitp.controller;

import com.example.pokemonmanagementapitp.dto.DresseurDto;
import com.example.pokemonmanagementapitp.service.DresseurService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dresseurs")
public class DresseurController {

    private final DresseurService dresseurService;

    public DresseurController(DresseurService dresseurService) {
        this.dresseurService = dresseurService;
    }

    @GetMapping("/{id}")
    public DresseurDto getDresseur(@PathVariable Long id) {
        return dresseurService.getDresseur(id);
    }
}
