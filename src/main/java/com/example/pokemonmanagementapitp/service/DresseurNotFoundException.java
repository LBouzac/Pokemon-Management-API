package com.example.pokemonmanagementapitp.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 404 plutôt que le "201" du sujet.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DresseurNotFoundException extends RuntimeException {

    public DresseurNotFoundException(Long id) {
        super("Dresseur introuvable : " + id);
    }
}
