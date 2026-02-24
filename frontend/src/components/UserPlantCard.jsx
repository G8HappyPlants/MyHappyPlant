import React from "react";
import "../styles/UserPlantCard.css";

export default function UserPlantCard({ plant }) {
  if (!plant) return null;

  // Calculate progress bar fill based on lastWatered and waterFrequency
  let fillPercent = 100;
  let overdue = false;
  if (plant.lastWatered && plant.waterFrequency) {
    const now = new Date();
    const last = new Date(plant.lastWatered);
    const msPerDay = 24 * 60 * 60 * 1000;
    const daysSince = Math.floor((now - last) / msPerDay);
    const freq = Number(plant.waterFrequency);
    if (!isNaN(daysSince) && !isNaN(freq) && freq > 0) {
      fillPercent = Math.max(0, Math.min(100, 100 - (daysSince / freq) * 100));
      overdue = daysSince >= freq;
    }
  }

  return (
    <div className="user-plant-card">
      <div className="user-plant-nickname-row">
        <span className="user-plant-nickname">{plant.nickname || "Unnamed Plant"}</span>
        <span className="user-plant-status-dot"></span>
      </div>
      <div className="user-plant-image-container">
        <img className="user-plant-image" src="/assets/plant-placeholder.png" alt="Plant" />
      </div>
      <div className="user-plant-progress-bar">
        <div
          className="user-plant-progress-inner"
          style={{
            width: `${fillPercent}%`,
            background: overdue ? '#e74c3c' : '#00f2ff',
            transition: 'width 0.3s, background 0.3s',
          }}
        ></div>
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
