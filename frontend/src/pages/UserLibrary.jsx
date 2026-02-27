import React, { useState } from "react";
import "../styles/UserLibrary.css";
import UserPlantGrid from "../components/UserPlantGrid";
import { dummyUserPlants } from "../dummyUserPlants";



export default function UserLibrary() {
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0); // backend page index, starts at 0
  const [pageInput, setPageInput] = useState(1); // for input field
  const pageSize = 15; // number of plants per page
  const totalPlants = dummyUserPlants.length;
  const maxPage = Math.max(1, Math.ceil(totalPlants / pageSize));

  // Sync pageInput with page
  React.useEffect(() => {
    setPageInput(page + 1);
  }, [page]);

  // Filter, sort, and paginate
  const filteredPlants = dummyUserPlants.filter(p =>
    p.nickname.toLowerCase().includes(search.toLowerCase())
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
  const paginatedPlants = sortedPlants.slice(page * pageSize, (page + 1) * pageSize);

  return (
    <div className="user-library-root">
      <div className="user-library-grid-section">
        <div className="species-library-search-container" style={{ display: 'flex', alignItems: 'center' }}>
          <input
            type="text"
            value={search}
            onChange={e => { setSearch(e.target.value); setPage(0); }}
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
            <span style={{ fontWeight: 'bold', fontSize: '1.3em', fontFamily: 'Arial Black, Arial, sans-serif', lineHeight: 1 }}>&#8676;</span>
          </button>
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => setPage(Math.max(0, page - 1))}
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
            onChange={e => setPageInput(e.target.value)}
            onKeyDown={e => {
              if (e.key === 'Enter') {
                const val = parseInt(pageInput, 10);
                if (!isNaN(val) && val > 0 && val <= maxPage) {
                  setPage(val - 1);
                }
              }
            }}
            style={{ margin: '0 8px', minWidth: 40, textAlign: 'center', fontWeight: 600, fontSize: '1.1em', color: '#2e5d34', width: 60 }}
            aria-label="Page number"
          />
          <button
            className="species-library-page-btn"
            style={{ marginLeft: 0 }}
            onClick={() => setPage(Math.min(maxPage - 1, page + 1))}
            aria-label="Next page"
            disabled={page + 1 >= maxPage}
          >
            <span style={{ fontWeight: 'bold', fontSize: '1.3em', fontFamily: 'Arial Black, Arial, sans-serif', lineHeight: 1 }}>&#8594;</span>
          </button>
        </div>
        <div className="user-library-plantgrid-container" style={{ display: 'block', width: '100%', height: '', overflowY: 'auto' }}>
          <UserPlantGrid
            plants={paginatedPlants}
            showAddButton={page === maxPage - 1}
          />
        </div>
      </div>
      <div className="user-library-details-section">
        {/* Main card for selected plant */}
        <div className="user-details-main-card">
          {selectedPlant ? (
            <div style={{ fontWeight: 'bold', fontSize: '1.2em', padding: 12 }}>
              Main info for: {selectedPlant.nickname || 'Unnamed Plant'}
            </div>
          ) : (
            <div style={{ color: '#aaa', padding: 12 }}>Select a plant to see details</div>
          )}
        </div>
        {/* 2x2 grid for details */}
        <div className="user-details-grid">
          <div className="user-details-square">Species info</div>
          <div className="user-details-square">Watering history</div>
          <div className="user-details-square">Pinned favorite</div>
          <div className="user-details-square">Pinned favorite</div>
        </div>
      </div>
    </div>
  );
}

