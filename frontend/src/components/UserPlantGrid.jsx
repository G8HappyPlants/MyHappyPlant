import React from "react";
import UserPlantCard from "./UserPlantCard";
import "../styles/UserPlantGrid.css";

export default function UserPlantGrid({ plants, onSelect, selectedId, style, showAddButton, onAddClick }) {
  const handleCardClick = (plant) => {
    if (plant && plant.id) {
      if (onSelect) onSelect(plant);
    }
  };
  return (
    <div className="user-plant-grid" style={style}>
      {plants && plants.map((plant) => (
        <div
          key={plant.id}
          className="user-plant-card-wrapper"
          onClick={() => handleCardClick(plant)}
          style={{ cursor: 'pointer' }}
        >
          <UserPlantCard plant={plant} isSelected={selectedId === plant.id} />
        </div>
      ))}
      {showAddButton && (
        <div className="user-plant-card-wrapper add-button-wrapper">
          <button className="user-plant-add-button" title="Add new plant" onClick={onAddClick}>
            <span style={{ fontSize: '2.5em', fontWeight: 'bold', color: '#2e5d34' }}>+</span>
          </button>
        </div>
      )}
    </div>
  );
}
