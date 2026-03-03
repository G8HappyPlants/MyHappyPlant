package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.Species;

public record SpeciesResponse(
        Long id,
        String commonName,
        String scientificName,
        String familyName,
        String familyCommonName,
        String genus,
        String nativeTo,
        Boolean edible) {

    //Creation of DTO from entity
    public static SpeciesResponse fromSpecies(Species species) {
        return new SpeciesResponse(
                species.getTrefleId(),
                species.getCommonName(),
                species.getSciName(),
                species.getFamily(),
                species.getFamilyCommonName(),
                species.getGenus(),
                species.getNativeTo(),
                species.getEdible()
        );
    }

    //Creation of entity from DTO
    public Species toEntity() {
        return new Species(
                this.id,
                this.scientificName,
                this.genus,
                this.familyName,
                this.nativeTo,
                this.edible,
                this.commonName,
                this.familyCommonName
        );
    }
}
