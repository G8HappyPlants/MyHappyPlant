import React from "react";
import "../styles/SpeciesGridCard.css";

export default function SpeciesGridCard({ plant, selected }) {
  if (!plant) return null;
  return (
    <div className={`species-grid-card${selected ? " selected" : ""}`}>
      <div className="plant-card-row">
        <img
            className="plant-image"
            src= "/assets/default_plant.png"
            alt={plant.commonName || plant.scientificName || "Plant" }
        />
        <div className="species-grid-card-names">
          <div className="plant-name plant-name-title">
            {plant.commonName || plant.scientificName || "Unknown"}
          </div>
          <div className="plant-family-common">
            {plant.familyCommonName || ''}
          </div>
        </div>
      </div>
    </div>
  );
}
