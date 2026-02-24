package com.example.myhappyplants.service;

import com.example.myhappyplants.dto.SpeciesResponse;
import com.example.myhappyplants.repository.SpeciesRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeciesService {
    private static final int ENTRIES_PER_PAGE = 30;

    private final SpeciesRepository speciesRepository;

    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public List<SpeciesResponse> getAllSpecies(int pageNumber) {
        return speciesRepository.findAll(
                        Pageable.ofSize(ENTRIES_PER_PAGE).withPage(pageNumber)
                )
                .stream()
                .map(SpeciesResponse::fromSpecies)
                .toList();
    }

    public SpeciesResponse getSpecies(long id) {
        return speciesRepository.findById(id)
                .map(SpeciesResponse::fromSpecies)
                .orElse(null);
    }

    public List<SpeciesResponse> getSpeciesSearched(String searchQuery, int limit) {
        return speciesRepository.findByCommonNameContainingIgnoreCase(searchQuery, Pageable.ofSize(limit).toLimit())
                .stream().map(SpeciesResponse::fromSpecies)
                .toList();
    }
}
