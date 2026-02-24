import React from "react";
import UserPlantCard from "./UserPlantCard";
import "../styles/UserPlantGrid.css";

export default function UserPlantGrid({ plants, onSelect, selectedId, style }) {
  return (
    <div className="user-plant-grid" style={style}>
      {plants && plants.map((plant) => (
        <div
          key={plant.id}
          className={`user-plant-card-wrapper${selectedId === plant.id ? " selected" : ""}`}
          onClick={() => onSelect && onSelect(plant)}
        >
          <UserPlantCard plant={plant} />
        </div>
      ))}
    </div>
  );
}
