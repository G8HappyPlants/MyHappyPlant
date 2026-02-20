package com.example.myhappyplants.service;

import com.example.myhappyplants.entity.Species;
import com.example.myhappyplants.repository.SpeciesRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.Convert;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class DatabasePopulationService {
    private static HttpClient hc = HttpClient.newBuilder().build();
    private static String reqFormat = "https://trefle.io/api/v1/species/%d?token=%s";
    private static String tkn = System.getenv("TREFLE_TOKEN");

    public SpeciesRepository rep;

    public DatabasePopulationService(SpeciesRepository rep) {
        this.rep = rep;
        if (tkn != null) {
            this.populate();
        }
    }

    public void populate() {
        long id = rep.getTopTrefleId() + 1;
        while (true) {
            try {
                populateFromSpeciesId(id++);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    }

    public void populateFromSpeciesId(long id) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(reqFormat.formatted(id,tkn)))
                .GET()
                .build();

        String body = hc.send(req, HttpResponse.BodyHandlers.ofString()).body();
        handleRequest(body);
    }

    public void handleRequest(String obj) {
        TypeToken t = new TypeToken<Map<String, Object>>() {};
        Map<String, Object> stringObjectMap = (Map<String, Object>) new Gson().fromJson(obj,t);
        Map<String, Object> data = (Map<String, Object>) stringObjectMap.get("data");

        Species species = new Species(
                Long.parseLong("%.0f".formatted((Double)data.get("id"))),
                (String) data.get("scientific_name"),
                (String) data.get("genus"),
                (String) data.get("family"),
                null,
                (Boolean) data.get("edible")
        );


        rep.save(species);
    }
}