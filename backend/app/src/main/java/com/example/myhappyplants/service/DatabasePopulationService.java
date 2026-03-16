package com.example.myhappyplants.service;

import com.example.myhappyplants.entity.Species;
import com.example.myhappyplants.repository.SpeciesRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class DatabasePopulationService {
    private static final HttpClient hc = HttpClient.newBuilder().build();
    private static final String reqFormat = "https://trefle.io/api/v1/plants?page=%d&token=%s";
    private static final String tkn = System.getenv("TREFLE_TOKEN");

    public SpeciesRepository rep;

    public DatabasePopulationService(SpeciesRepository rep) {
        this.rep = rep;
//        if (tkn != null) { //Activate if database should be populated once again, adding 20 plant-species every second
//            this.populate();
//        }
    }

    public void populate() {
        long page = 500; //500 is the latest page taken, if database is truncated - start over from 1
        while (true) {
            try {
                System.out.println("page: " + page); //printing the page that is currently being fetched, to know how much is being collected
                populateFromPlantsPage(page++);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                // optionally backoff here
            }
        }
    }

    public void populateFromPlantsPage(long page) throws IOException, InterruptedException {
        String url = reqFormat.formatted(page, tkn);

        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .build();

        String body = hc.send(req, HttpResponse.BodyHandlers.ofString()).body();
        handlePlantsListResponse(body);
    }

    public void handlePlantsListResponse(String json) {
        TypeToken<Map<String, Object>> t = new TypeToken<>() {
        };
        Map<String, Object> root = new Gson().fromJson(json, t.getType());

        Object dataObj = root.get("data");
        if (!(dataObj instanceof List<?> dataList)) {
            return;
        }

        if (dataList.isEmpty()) return;

        for (Object item : dataList) {
            if (!(item instanceof Map<?, ?>)) continue;
            @SuppressWarnings("unchecked")
            Map<String, Object> plant = (Map<String, Object>) item;

            Long id = toLong(plant.get("id"));
            String scientificName = toStr(plant.get("scientific_name"));
            String genus = toStr(plant.get("genus"));
            String family = toStr(plant.get("family"));
            String commonName = toStr(plant.get("common_name"));
            String familyCommonName = toStr(plant.get("family_common_name"));
            Boolean edible = toBool(plant.get("edible"));

            if ((genus == null || family == null) && plant.get("main_species") instanceof Map<?, ?> ms) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mainSpecies = (Map<String, Object>) ms;
                if (genus == null) genus = toStr(mainSpecies.get("genus"));
                if (family == null) family = toStr(mainSpecies.get("family"));
                if (familyCommonName == null) familyCommonName = toStr(mainSpecies.get("family_common_name"));
                if (edible == null) edible = toBool(mainSpecies.get("edible"));
            }

            if (id == null) continue;

            Species species = new Species(
                    id,
                    scientificName,
                    genus,
                    family,
                    null,
                    edible,
                    commonName,
                    familyCommonName
            );

            System.out.println(species); //Logging the addition in the run-terminal
            rep.save(species);
        }

    }


    private static String toStr(Object o) {
        return (o instanceof String s && !s.isBlank()) ? s : null;
    }

    private static Boolean toBool(Object o) {
        return (o instanceof Boolean b) ? b : null;
    }

    private static Long toLong(Object o) {
        if (o instanceof Number n) return n.longValue();
        if (o instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}