import React from "react";
import "../styles/UserPlantCard.css";

export default function UserPlantCard({ plant, isSelected }) {
  if (!plant) return null;

  // Calculate progress bar fill based on lastWatered and waterFrequency
  let fillPercent = 100;
  let overdue = false;
  let daysRemaining = null;
  if (plant.lastWatered && plant.waterFrequency) {
    const now = new Date();
    const last = new Date(plant.lastWatered);
    const msPerDay = 24 * 60 * 60 * 1000;
    const daysSince = Math.floor((now - last) / msPerDay);
    const freq = Number(plant.waterFrequency);
    if (!isNaN(daysSince) && !isNaN(freq) && freq > 0) {
      fillPercent = Math.max(0, Math.min(100, 100 - (daysSince / freq) * 100));
      overdue = daysSince >= freq;
      daysRemaining = freq - daysSince;
    }
  }

  // Determine progress bar color
  let progressColor = '#00f2ff'; // blue default
  if (overdue) progressColor = '#e74c3c'; // red if overdue
  else if (fillPercent < 50) progressColor = '#ff9800'; // orange if 1 day left
  else if (fillPercent < 80) progressColor = '#ffe066'; // yellow if 2 days left

  return (
    <div className={`user-plant-card${isSelected ? " selected" : ""}`}>
      <div className="user-plant-nickname-row">
        <span className="user-plant-nickname">{plant.nickname || "Unnamed Plant"}</span>
        <span className="user-plant-status-dot"></span>
      </div>
      <div className="user-plant-image-container">
          <img
              className="user-plant-image"
              src= "/assets/default_plant.png"
              alt={plant.commonName || plant.scientificName || "Plant" }
          />
      </div>
      <div className="user-plant-progress-bar">
        <div
          className="user-plant-progress-inner"
          style={{
            width: `${fillPercent}%`,
            background: progressColor,
            transition: 'width 0.3s, background 0.3s',
          }}
        ></div>
      </div>
      <div className="user-plant-days-remaining" style={{ fontWeight: 'bold', fontSize: '0.95em', marginTop: 2, marginBottom: 2, color: overdue ? '#e74c3c' : '#2e5d34' }}>
        {overdue
          ? 'Needs water now!'
          : daysRemaining === 0
            ? 'Water today!'
            : daysRemaining === 1
              ? '1 day until water'
              : daysRemaining > 1
                ? `${daysRemaining} days until water`
                : ''}
      </div>
      <div className="user-plant-trefle">
        <span className="user-plant-trefle-label">Trefle ID:</span> {plant.trefleId}
      </div>
      <div className="user-plant-lastwatered">
        Last watered at: {plant.lastWatered}
      </div>
    </div>
  );
}
