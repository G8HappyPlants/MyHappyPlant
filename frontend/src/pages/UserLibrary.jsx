import React, { useState } from "react";

export default function UserLibrary() {
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [search, setSearch] = useState("");

  const filteredPlants = [];

  return (
    <div style={{ display: "flex", padding: 0, gap: 0, height: "100%", minHeight: 0, boxSizing: 'border-box' }}>
      <div
        style={{
          flex: 2,
          background: "#fcfccb",
          borderLeft: "2px solid #b6e388",
          borderTop: "2px solid #b6e388",
          borderBottom: "2px solid #b6e388",
          minHeight: 0,
          minWidth: 0,
          display: "flex",
          flexDirection: "column",
          boxSizing: 'border-box',
          justifyContent: 'center',
          alignItems: 'center'
        }}
      >
        <div style={{
          width: '95%',
          margin: '18px auto 0 auto',
          position: 'relative',
          zIndex: 2
        }}>
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search in user library..."
            style={{
              width: '100%',
              padding: '0.7em 1.2em',
              borderRadius: 18,
              border: '1.5px solid #b6e388',
              background: 'rgba(255,255,255,0.6)',
              fontSize: '1.1em',
              marginBottom: 10,
              outline: 'none',
              boxShadow: '0 1px 4px rgba(0,0,0,0.03)',
              transition: 'border 0.2s',
              color: '#2e5d34',
              fontWeight: 400
            }}
          />
        </div>
        <div style={{
          flex: 1,
          minHeight: 0,
          minWidth: 0,
          boxSizing: 'border-box',
          width: '95%',
          height: 'calc(95% - 56px)',
          background: '#d6f5c6',
          border: '2px solid #87C013',
          borderRadius: 24,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.04)'
        }}>
          <span style={{ color: '#2e5d34', fontSize: '1.3em', opacity: 0.7 }}>
            no plants loaded yet
          </span>
        </div>
      </div>
      <div
        style={{
          flex: 1,
          background: "#fcfccb",
          borderRight: "2px solid #b6e388",
          borderTop: "2px solid #b6e388",
          borderBottom: "2px solid #b6e388",
          minHeight: 0,
          minWidth: 0,
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          boxSizing: 'border-box'
        }}
      >
        <div style={{
          flex: 1,
          minHeight: 0,
          minWidth: 0,
          boxSizing: 'border-box',
          width: '95%',
          height: '95%',
          background: '#b6e388',
          border: '2px solid #87C013',
          borderRadius: 24,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          boxShadow: '0 2px 8px rgba(0,0,0,0.04)'
        }}>
          {selectedPlant ? (
            <>
              <h2 style={{ color: "#2e5d34" }}>{selectedPlant.name}</h2>
              <img src={selectedPlant.image} alt={selectedPlant.name} style={{ width: 120, height: 120, margin: "16px 0" }} />
              <div style={{ marginTop: 16, color: "#2e5d34" }}>
                <strong>Group:</strong> {selectedPlant.taxonomy.group}<br />
                <strong>Class:</strong> {selectedPlant.taxonomy.class}<br />
                <p style={{ marginTop: 12 }}>{selectedPlant.description}</p>
              </div>
            </>
          ) : (
            <div style={{ color: "#2e5d34", marginTop: 48 }}>Select a plant to see details</div>
          )}
        </div>
      </div>
    </div>
  );
}
