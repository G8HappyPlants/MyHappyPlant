import React, { useState, useEffect } from "react";
import PlantGrid from "../components/PlantGrid";
import SpeciesDetailsCard from "../components/SpeciesDetailsCard";
import speciesService from "../services/speciesService";
import "../styles/SpeciesLibrary.css";
import AddToLibraryModal from "../components/AddPlantModal.jsx";

export default function SpeciesLibrary() {
  const [page, setPage] = useState(0); // backend page index, starts at 0
  const [pageInput, setPageInput] = useState(1); // for input field
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [plantDetails, setPlantDetails] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);
  const [detailsError, setDetailsError] = useState(null);
  const [search, setSearch] = useState("");
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const[showAddModal, setShowAddModal] = useState(false);
  const maxPage = 376;

  useEffect(() => {
    setLoading(true);
    const token = localStorage.getItem("token");
    speciesService.getAllSpecies(token, page, 30)
      .then(data => {
        setPlants(
          (Array.isArray(data) ? data : data.body || []).map(item => ({
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
  }, [page]);

  useEffect(() => {
    setPageInput(page + 1);
  }, [page]);

  const filteredPlants = plants.filter((plant) => {
    const q = search.toLowerCase();
    const match =
      (plant.commonName || '').toLowerCase().includes(q) ||
      (plant.familyCommonName || '').toLowerCase().includes(q) ||
      (plant.taxonomy.group || '').toLowerCase().includes(q) ||
      (plant.taxonomy.class || '').toLowerCase().includes(q);
    // ...existing code...
    return match;
  });

  // ...existing code...

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
      <div className="species-library-grid-section">
        {error && <div className="species-library-error">Error: {error}</div>}
        <div className="species-library-search-container" style={{ display: 'flex', alignItems: 'center' }}>
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search in species..."
            className="species-library-search-input"
          />
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 8, marginRight: 8 }}
            onClick={() => {
              setPage(0);
              console.log('Going to first page');
            }}
            aria-label="First page"
            disabled={page === 0}
          >
            <span style={{ fontWeight: 'bold', fontSize: '1.3em', fontFamily: 'Arial Black, Arial, sans-serif', lineHeight: 1 }}>&#8676;</span>
          </button>
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => {
              if (page > 0) {
                const newPage = page - 1;
                setPage(newPage);
                console.log('Going to page', newPage);
              }
            }}
            aria-label="Previous page"
            disabled={page === 0}
          >
            <span style={{ fontWeight: 'bold', fontSize: '1.3em', fontFamily: 'Arial Black, Arial, sans-serif', lineHeight: 1 }}>&#8592;</span>
          </button>
          <input
            type="number"
            min={1}
            max={maxPage}
            value={pageInput}
            onChange={e => {
              const val = e.target.value;
              setPageInput(val);
            }}
            onKeyDown={e => {
              if (e.key === 'Enter') {
                const val = parseInt(pageInput, 10);
                if (!isNaN(val) && val > 0 && val <= maxPage) {
                  setPage(val - 1);
                  console.log('Jumping to page', val - 1);
                }
              }
            }}
            style={{ margin: '0 8px', minWidth: 40, textAlign: 'center', fontWeight: 600, fontSize: '1.1em', color: '#2e5d34', width: 60 }}
            aria-label="Page number"
          />
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => {
              const newPage = page + 1;
              if (newPage < maxPage) {
                setPage(newPage);
                console.log('Going to page', newPage);
              }
            }}
            aria-label="Next page"
            disabled={page + 1 >= maxPage}
          >
            <span style={{ fontWeight: 'bold', fontSize: '1.3em', fontFamily: 'Arial Black, Arial, sans-serif', lineHeight: 1 }}>&#8594;</span>
          </button>
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
            <SpeciesDetailsCard plantDetails={plantDetails}
                                selectedPlant={selectedPlant}
                                onAddToLibrary={() => setShowAddModal(true)}
            />
          ) : (
            <div className="species-library-details-empty">No details found.</div>
          )
        ) : (
          <div className="species-library-details-empty">Select a plant to see details</div>

        )}
          {showAddModal && (
              <AddToLibraryModal
                  preselectedSpecies={selectedPlant}
                  onClose={() => setShowAddModal(false)}
                  onPlantAdded={() => setShowAddModal(false)}
              />)}
      </div>
    </div>

  );
}
