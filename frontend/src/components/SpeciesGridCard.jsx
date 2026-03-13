import React from "react";
import "../styles/SpeciesGridCard.css";

export default function SpeciesGridCard({plant}) {
    if (!plant) return null;
    return (
        <div className="species-grid-card">
            <div className="species-grid-card-row">
                <div className="plant-image"></div>
                <div className="species-grid-card-names">
                    <div className="plant-name plant-name-title">
                        {plant.commonName != null && plant.commonName !== ''
                            ? plant.commonName
                            : (plant.scientificName != null && plant.scientificName !== ''
                                ? plant.scientificName
                                : 'Unknown')}
                    </div>
                    <div className="plant-family-common">
                        {plant.familyCommonName || ''}
                    </div>
                </div>
            </div>
        </div>
    );
}
