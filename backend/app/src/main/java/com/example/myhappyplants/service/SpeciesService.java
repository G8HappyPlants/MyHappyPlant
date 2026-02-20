package com.example.myhappyplants.service;

import com.example.myhappyplants.repository.SpeciesRepository;
import org.springframework.stereotype.Service;

@Service
public class SpeciesService {
    private final SpeciesRepository speciesRepository;

    public SpeciesService(SpeciesRepository speciesRepository) {this.speciesRepository = speciesRepository;}


}
