import React from "react";
import "../styles/SpeciesLibrary.css";

export default function SpeciesDetailsCard({ plantDetails, selectedPlant, onAddToLibrary }) {
  if (!plantDetails || !selectedPlant) return null;
  return (
    <>
        <div className="species-details-card">
      <h2 className="species-library-details-title">
        {plantDetails.commonName || plantDetails.scientificName || selectedPlant.name}
      </h2>
        <img
          src={"/assets/default_plant.png"}
          alt={plantDetails.commonName || plantDetails.scientificName || selectedPlant.name}
          className="species-library-details-image"
        />
        <div className="species-library-details-info">
          <strong>Group:</strong> {plantDetails.familyName || ""}<br />
          <strong>Class:</strong> {plantDetails.scientificName || ""}<br />
          <p className="species-library-details-description">
            {plantDetails.familyCommonName || plantDetails.nativeTo || selectedPlant.description}
          </p>
      </div>
        <button className="species-details-add-btn" onClick={onAddToLibrary}>
            Add to My Library
        </button>
        </div>
    </>
  );
}
