import React from "react";
import UserPlantCard from "./UserPlantCard";
import "../styles/UserPlantGrid.css";

export default function UserPlantGrid({ plants, onSelect, selectedId, style, showAddButton }) {
  const handleCardClick = (plant) => {
    if (plant && plant.id) {
      console.log('Clicked plant ID:', plant.id);
      if (onSelect) onSelect(plant);
    }
  };
  return (
    <div className="user-plant-grid" style={style}>
      {plants && plants.map((plant) => (
        <div
          key={plant.id}
          className={`user-plant-card-wrapper${selectedId === plant.id ? " selected" : ""}`}
          onClick={() => handleCardClick(plant)}
          style={{ cursor: 'pointer' }}
        >
          <UserPlantCard plant={plant} />
        </div>
      ))}
      {showAddButton && (
        <div className="user-plant-card-wrapper add-button-wrapper">
          <button className="user-plant-add-button" title="Add new plant">
            <span style={{ fontSize: '2.5em', fontWeight: 'bold', color: '#2e5d34' }}>+</span>
          </button>
        </div>
      )}
    </div>
  );
}
