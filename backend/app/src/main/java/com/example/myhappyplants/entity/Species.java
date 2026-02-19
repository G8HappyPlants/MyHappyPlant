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


}
