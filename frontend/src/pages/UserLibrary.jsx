import React, { useState, useEffect, useCallback, useMemo } from "react";
import "../styles/UserLibrary.css";
import UserPlantGrid from "../components/UserPlantGrid";
import {useNavigate} from "react-router-dom";
import UserPlantDetailsCard from "../components/UserPlantDetailsCard";
import { getAllOwnedPlants } from "../services/userPlantsService";

const PAGE_SIZE = 15;
const BACKEND_PAGE_SIZE = 30;

export default function UserLibrary() {
  const navigate = useNavigate();
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [pageInput, setPageInput] = useState(1);

  const [activeTagFilter, setActiveTagFilter] = useState(null);

  const fetchPlants = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem("token");
      let all = [];
      let p = 0;
      while (true) {
        const batch = await getAllOwnedPlants(token, p);
        all = [...all, ...batch];
        if (batch.length < BACKEND_PAGE_SIZE) break;
        p++;
      }
      // Map speciesId -> trefleId for UserPlantCard compatibility
      const mapped = all.map((plant) => ({ ...plant, trefleId: plant.speciesId }));
      setPlants(mapped);
      return mapped;
    } catch (e) {
      setError(e.message);
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPlants();
  }, [fetchPlants]);

  // Sync pageInput with page
  useEffect(() => {
    setPageInput(page + 1);
  }, [page]);

  const allTags = useMemo(() => {
    const tagMap = new Map();
    plants.forEach((p) => (p.tags || []).forEach((t) => tagMap.set(t.name, t)));
    return Array.from(tagMap.values());
  }, [plants]);

  const handleSearchChange = (e) => {
    setSearch(e.target.value);
    setPage(0);
  };

  const handleTagFilter = (tagName) => {
    setActiveTagFilter((prev) => (prev === tagName ? null : tagName));
    setPage(0);
  };

  // Filter by nickname search and active tag
  const filteredPlants = plants
    .filter((p) => p.nickname.toLowerCase().includes(search.toLowerCase()))
    .filter((p) =>
      activeTagFilter ? p.tags?.some((t) => t.name === activeTagFilter) : true
    );

  // Sort by days remaining until water (soonest first)
  const sortedPlants = filteredPlants.slice().sort((a, b) => {
    const now = new Date();
    const msPerDay = 24 * 60 * 60 * 1000;
    const daysSinceA = Math.floor((now - new Date(a.lastWatered)) / msPerDay);
    const daysSinceB = Math.floor((now - new Date(b.lastWatered)) / msPerDay);
    const freqA = Number(a.waterFrequency);
    const freqB = Number(b.waterFrequency);
    const daysRemainingA = freqA - daysSinceA;
    const daysRemainingB = freqB - daysSinceB;
    return daysRemainingA - daysRemainingB;
  });

  const maxPage = Math.max(1, Math.ceil(sortedPlants.length / PAGE_SIZE));
  const paginatedPlants = sortedPlants.slice(
    page * PAGE_SIZE,
    (page + 1) * PAGE_SIZE
  );

  const handlePlantWatered = useCallback(async () => {
    const refreshed = await fetchPlants();
    if (selectedPlant) {
      const updated = refreshed.find((p) => p.id === selectedPlant.id);
      if (updated) setSelectedPlant(updated);
    }
  }, [fetchPlants, selectedPlant]);

  const handlePlantDeleted = useCallback(() => {
    setSelectedPlant(null);
    fetchPlants();
  }, [fetchPlants]);

  return (
    <div className="user-library-root">
      <div className="user-library-grid-section">
        {loading && (
          <div style={{ padding: "16px", color: "#2e5d34" }}>
            Loading plants...
          </div>
        )}
        {error && (
          <div style={{ padding: "16px", color: "#e74c3c" }}>
            Error: {error}
          </div>
        )}
        <div
          className="species-library-search-container"
          style={{ display: "flex", alignItems: "center" }}
        >
          <input
            type="text"
            value={search}
            onChange={handleSearchChange}
            placeholder="Search in user library..."
            className="species-library-search-input"
          />
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 8, marginRight: 8 }}
            onClick={() => setPage(0)}
            aria-label="First page"
            disabled={page === 0}
          >
            <span
              style={{
                fontWeight: "bold",
                fontSize: "1.3em",
                fontFamily: "Arial Black, Arial, sans-serif",
                lineHeight: 1,
              }}
            >
              &#8676;
            </span>
          </button>
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => setPage(Math.max(0, page - 1))}
            aria-label="Previous page"
            disabled={page === 0}
          >
            <span
              style={{
                fontWeight: "bold",
                fontSize: "1.3em",
                fontFamily: "Arial Black, Arial, sans-serif",
                lineHeight: 1,
              }}
            >
              &#8592;
            </span>
          </button>
          <input
            type="number"
            min={1}
            max={maxPage}
            value={pageInput}
            onChange={(e) => setPageInput(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                const val = parseInt(pageInput, 10);
                if (!isNaN(val) && val > 0 && val <= maxPage) {
                  setPage(val - 1);
                }
              }
            }}
            style={{
              margin: "0 8px",
              minWidth: 40,
              textAlign: "center",
              fontWeight: 600,
              fontSize: "1.1em",
              color: "#2e5d34",
              width: 60,
            }}
            aria-label="Page number"
          />
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => setPage(Math.min(maxPage - 1, page + 1))}
            aria-label="Next page"
            disabled={page + 1 >= maxPage}
          >
            <span
              style={{
                fontWeight: "bold",
                fontSize: "1.3em",
                fontFamily: "Arial Black, Arial, sans-serif",
                lineHeight: 1,
              }}
            >
              &#8594;
            </span>
          </button>
        </div>
        {allTags.length > 0 && (
          <div className="user-library-tag-filters">
            {allTags.map((t) => (
              <button
                key={t.name}
                className={`user-library-tag-btn${activeTagFilter === t.name ? " active" : ""}`}
                onClick={() => handleTagFilter(t.name)}
              >
                {t.name}
              </button>
            ))}
          </div>
        )}

        <div
          className="user-library-plantgrid-container"
          style={{ display: "block", width: "100%", height: "", overflowY: "auto" }}
        >
          <UserPlantGrid
            plants={paginatedPlants}
            onSelect={setSelectedPlant}
            selectedId={selectedPlant?.id}
            showAddButton={page === maxPage - 1}
            onAddClick={() => navigate("/species")}
          />
        </div>
      </div>

      <div className="user-library-details-section">
        <UserPlantDetailsCard
          plant={selectedPlant}
          onWatered={handlePlantWatered}
          onDeleted={handlePlantDeleted}
        />
      </div>
    </div>
  );
}
