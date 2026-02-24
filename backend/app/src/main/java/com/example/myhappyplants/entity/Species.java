package com.example.myhappyplants.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "family_common_name")
    private String familyCommonName;


    public Species() {

    }

    public Species(Long trefleId, String sciName, String genus, String family, String nativeTo, Boolean edible, String commonName, String familyCommonName) {
        this.trefleId = trefleId;
        this.sciName = sciName;
        this.family = family;
        this.nativeTo = nativeTo;
        this.edible = edible;
        this.genus = genus;
        this.commonName = commonName;
        this.familyCommonName = familyCommonName;
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

    public String getFamilyCommonName() {
        return familyCommonName;
    }

    public void setFamilyCommonName(String familyCommonName) {
        this.familyCommonName = familyCommonName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String toString() {
        return "id: " + trefleId + ", sc:" + sciName + " G " + genus + " fam:" + family + " fam-com:" + familyCommonName + " na:" + nativeTo + " ed:" + edible + " common:" + commonName;
    }
}
