import React from "react";
import "../styles/PlantGridSpecies.css";
import SpeciesGridCard from "./SpeciesGridCard";

// Show all plants, grid scrolls if overflow

const PlantGrid = ({plants, onSelect, selectedId, style, cardStyle}) => {
    // Show all plants, no empty slots
    const gridItems = plants;

    return (
        <div style={{position: 'relative', width: '100%', height: '100%'}}>
            <div
                className="plant-grid"
                style={style}
            >
                {gridItems.filter(Boolean).map((plant, idx) => (
                    <div
                        key={plant.id}
                        className={`plant-card${selectedId === plant.id ? " selected" : ""}`}
                        onClick={() => onSelect && onSelect(plant)}
                        style={{cursor: 'pointer'}}
                    >
                        <SpeciesGridCard plant={plant}/>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PlantGrid;
