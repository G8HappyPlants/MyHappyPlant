
import React from "react";
import "../styles/PlantGridSpecies.css";

// Show all plants, grid scrolls if overflow

const PlantGrid = ({ plants, onSelect, selectedId, style, cardStyle }) => {
  // Show all plants, no empty slots
  const gridItems = plants;

  return (
    <div style={{position: 'relative', width: '100%', height: '100%'}}>
      <div
        className="plant-grid"
        style={style}
      >
        {gridItems.map((plant, idx) => (
          <div
            key={plant ? plant.id : `empty-${idx}`}
            className={`plant-card${plant && selectedId === plant.id ? " selected" : ""}`}
            onClick={plant ? () => onSelect && onSelect(plant) : undefined}
          >
            {plant ? (
              <>
                <div className="plant-card-row">
                  <div className="plant-image"></div>
                  <div className="plant-card-info">
                    <div className="plant-name">
                      {plant.commonName != null && plant.commonName !== ''
                        ? (plant.commonName.length > 22 ? plant.commonName.slice(0, 20) + '...' : plant.commonName)
                        : (plant.scientificName != null && plant.scientificName !== ''
                            ? (plant.scientificName.length > 22 ? plant.scientificName.slice(0, 20) + '...' : plant.scientificName)
                            : 'Unknown')}
                    </div>
                    <div className="plant-family-common">
                      {(plant.familyCommonName || '').length > 22
                        ? (plant.familyCommonName || '').slice(0, 20) + '...'
                        : (plant.familyCommonName || '')}
                    </div>
                  </div>
                </div>
              </>
            ) : (
              <span className="plant-card-empty">+</span>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default PlantGrid;
