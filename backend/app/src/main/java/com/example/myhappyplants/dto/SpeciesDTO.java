package com.example.myhappyplants.dto;

import com.example.myhappyplants.entity.Species;

public class SpeciesDTO {
    private Long id;
    private String commonName;
    private String scientificName;
    private String familyName;
    private String familyCommonName;
    private String genus;
    private String nativeTo;
    private Boolean edible;


    //Creation of DTO from entity
    public SpeciesDTO(Species species) {
        this.id = species.getTrefleId();
        this.commonName = species.getCommonName();
        this.scientificName = species.getSciName();
        this.familyName = species.getFamily();
        this.familyCommonName = species.getFamilyCommonName();
        this.genus = species.getGenus();
        this.nativeTo = species.getNativeTo();
        this.edible = species.getEdible();
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
