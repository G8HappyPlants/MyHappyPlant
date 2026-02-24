import React, { useState, useEffect } from "react";
import PlantGrid from "../components/PlantGrid";
import speciesService from "../services/speciesService";
import "../styles/SpeciesLibrary.css";

export default function SpeciesLibrary() {
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [plantDetails, setPlantDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);
  const [search, setSearch] = useState("");
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    const token = localStorage.getItem("token");
    speciesService.getAllSpecies(token)
      .then(data => {
        setPlants(
          (data.body || []).slice(0, 30).map(item => ({
            id: item.id,
            commonName: item.commonName == null || item.commonName === '' ? null : item.commonName,
            scientificName: item.scientificName || null,
            familyCommonName: item.familyCommonName || "",
            image: "/assets/plant1.png",
            taxonomy: {
              group: item.familyCommonName || "",
              class: item.scientificName || ""
            },
            description: item.familyCommonName || item.nativeTo || ""
          }))
        );
        setLoading(false);
      })
      .catch(e => {
        setError(e.message);
        setLoading(false);
      });
  }, []);

  const filteredPlants = plants.filter((plant) => {
    const q = search.toLowerCase();
    return (
      (plant.commonName || '').toLowerCase().includes(q) ||
      (plant.familyCommonName || '').toLowerCase().includes(q) ||
      (plant.taxonomy.group || '').toLowerCase().includes(q) ||
      (plant.taxonomy.class || '').toLowerCase().includes(q)
    );
  });

  useEffect(() => {
    if (!selectedPlant) {
      setPlantDetails(null);
      setDetailsError(null);
      setDetailsLoading(false);
      return;
    }
    setDetailsLoading(true);
    setDetailsError(null);
    const token = localStorage.getItem("token");
    speciesService.getSpeciesById(selectedPlant.id, token)
      .then(data => {
        setPlantDetails(data.body || data);
        setDetailsLoading(false);
      })
      .catch(e => {
        setDetailsError(e.message);
        setDetailsLoading(false);
      });
  }, [selectedPlant]);

  return (
    <div className="species-library-root">
      {loading && <div className="species-library-loading">Loading plants...</div>}
      {error && <div className="species-library-error">Error: {error}</div>}
      <div className="species-library-grid-section">
        <div className="species-library-search-container">
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search in species..."
            className="species-library-search-input"
          />
        </div>
        <div className="species-library-plantgrid-container">
          <PlantGrid
            plants={filteredPlants}
            onSelect={plant => {
              setSelectedPlant(plant);
            }}
            selectedId={selectedPlant?.id}
            style={{ height: "100%", width: "100%" }}
          />
        </div>
      </div>
      <div className="species-library-details-section">
        {selectedPlant ? (
          detailsLoading ? (
            <div className="species-library-details-loading">Loading details...</div>
          ) : detailsError ? (
            <div className="species-library-details-error">Error: {detailsError}</div>
          ) : plantDetails ? (
            <>
              <h2 className="species-library-details-title">{plantDetails.commonName || plantDetails.scientificName || selectedPlant.name}</h2>
              <img src={selectedPlant.image} alt={plantDetails.commonName || plantDetails.scientificName || selectedPlant.name} className="species-library-details-image" />
              <div className="species-library-details-info">
                <strong>Group:</strong> {plantDetails.familyName || ""}<br />
                <strong>Class:</strong> {plantDetails.scientificName || ""}<br />
                <p className="species-library-details-description">{plantDetails.familyCommonName || plantDetails.nativeTo || selectedPlant.description}</p>
              </div>
            </>
          ) : (
            <div className="species-library-details-empty">No details found.</div>
          )
        ) : (
          <div className="species-library-details-empty">Select a plant to see details</div>
        )}
      </div>
    </div>
  );
}
