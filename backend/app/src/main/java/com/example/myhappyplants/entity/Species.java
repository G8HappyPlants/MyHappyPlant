package com.example.myhappyplants.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "species")
public class Species {

    @Id
    @Column(name = "trefle_id", nullable = false)
    private Long trefleId;

    @Column(name = "sci_name")
    private String sciName;

    @Column(name = "genus")
    private String genus;

    @Column(name = "family")
    private String family;

    @Column(name = "native_to")
    private String nativeTo;

    @Column(name = "edible")
    private Boolean edible;


    public Species() {

    }

    public Species(Long trefleId,  String sciName, String gens, String family, String nativeTo, Boolean edible) {
        this.trefleId = trefleId;
        this.sciName = sciName;
        this.family = family;
        this.nativeTo = nativeTo;
        this.edible = edible;
        this.genus = genus;
    }

    public Long getTrefleId() {
        return trefleId;
    }

    public void setTrefleId(Long trefleId) {
        this.trefleId = trefleId;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
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
