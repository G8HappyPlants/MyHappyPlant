import React, { useState, useEffect } from "react";
import PlantGrid from "../components/PlantGrid";


export default function DatabaseLibrary() {
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
    fetch("http://localhost:8080/api/species/all", {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to fetch species");
        return res.json();
      })
      .then(data => {
        console.log("Fetched species data:", data); // Debugging log
        // Map backend data to PlantGrid format
        setPlants(
          (data.body || []).slice(0, 15).map(item => ({
            id: item.id,
            name: item.commonName || item.scientificName || "Unknown",
            image: "/assets/plant1.png", // Placeholder, backend has no image
            taxonomy: {
              group: item.familyName || "",
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
      plant.name.toLowerCase().includes(q) ||
      plant.taxonomy.group.toLowerCase().includes(q) ||
      plant.taxonomy.class.toLowerCase().includes(q)
    );
  });

  // Fetch plant details when selectedPlant changes
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
    fetch(`http://localhost:8080/api/species/${selectedPlant.id}`, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to fetch plant details");
        return res.json();
      })
      .then(data => {
        setPlantDetails(data.body || data); // handle both wrapped and direct response
        setDetailsLoading(false);
      })
      .catch(e => {
        setDetailsError(e.message);
        setDetailsLoading(false);
      });
  }, [selectedPlant]);

  return (
    <div style={{ display: "flex", padding: 0, gap: 0, height: "100%", minHeight: 0, boxSizing: 'border-box' }}>
      {/* Optionally show loading or error */}
      {loading && <div style={{position:'absolute',left:0,right:0,top:0,bottom:0,background:'rgba(255,255,255,0.7)',zIndex:10,display:'flex',alignItems:'center',justifyContent:'center'}}>Loading plants...</div>}
      {error && <div style={{color:'red',padding:16}}>Error: {error}</div>}
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
            placeholder="Search in database..."
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
          alignItems: 'stretch',
          boxShadow: '0 2px 8px rgba(0,0,0,0.04)'
        }}>
          <PlantGrid
            plants={filteredPlants}
            onSelect={setSelectedPlant}
            selectedId={selectedPlant?.id}
            style={{ height: "100%", width: "100%" }}
          />
        </div>
      </div>
      <div
        style={{
          flex: 1,
          background: "#b6e388",
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
        {selectedPlant ? (
          detailsLoading ? (
            <div style={{ color: '#2e5d34', marginTop: 48 }}>Loading details...</div>
          ) : detailsError ? (
            <div style={{ color: 'red', marginTop: 48 }}>Error: {detailsError}</div>
          ) : plantDetails ? (
            <>
              <h2 style={{ color: "#2e5d34" }}>{plantDetails.commonName || plantDetails.scientificName || selectedPlant.name}</h2>
              <img src={selectedPlant.image} alt={plantDetails.commonName || plantDetails.scientificName || selectedPlant.name} style={{ width: 120, height: 120, margin: "16px 0" }} />
              <div style={{ marginTop: 16, color: "#2e5d34" }}>
                <strong>Group:</strong> {plantDetails.familyName || ""}<br />
                <strong>Class:</strong> {plantDetails.scientificName || ""}<br />
                <p style={{ marginTop: 12 }}>{plantDetails.familyCommonName || plantDetails.nativeTo || selectedPlant.description}</p>
              </div>
            </>
          ) : (
            <div style={{ color: "#2e5d34", marginTop: 48 }}>No details found.</div>
          )
        ) : (
          <div style={{ color: "#2e5d34", marginTop: 48 }}>Select a plant to see details</div>
        )}
      </div>
    </div>
  );
}
