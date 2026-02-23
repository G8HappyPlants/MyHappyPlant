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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyCommonName() {
        return familyCommonName;
    }

    public void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getNativeTo() {
        return nativeTo;
    }

    public void setNativeTo(String nativeTo) {
        this.nativeTo = nativeTo;
    }

    public Boolean getEdible() {
        return edible;
    }

    public void setEdible(Boolean edible) {
        this.edible = edible;
    }
}
