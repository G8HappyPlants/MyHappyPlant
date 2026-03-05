import React, { useState, useEffect } from "react";
import { getSpeciesById } from "../services/speciesService";
import { patchOwnedPlant, deleteOwnedPlant } from "../services/userPlantsService";
import EditPlantModal from "./EditPlantModal";
import "../styles/UserPlantDetailsCard.css";

export default function UserPlantDetailsCard({ plant, onWatered, onDeleted }) {
  const [species, setSpecies] = useState(null);
  const [speciesLoading, setSpeciesLoading] = useState(false);
  const [wateringInProgress, setWateringInProgress] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

  useEffect(() => {
    if (!plant?.trefleId) {
      setSpecies(null);
      return;
    }
    setSpecies(null);
    setSpeciesLoading(true);
    const token = localStorage.getItem("token");
    getSpeciesById(plant.trefleId, token)
      .then(setSpecies)
      .catch(() => setSpecies(null))
      .finally(() => setSpeciesLoading(false));
  }, [plant?.trefleId]);

  // Reset delete confirm when plant changes
  useEffect(() => {
    setDeleteConfirm(false);
  }, [plant?.id]);

  if (!plant) {
    return (
      <>
        <div className="user-details-main-card">
          <div className="updc-empty">Select a plant to see details</div>
        </div>
        <div className="user-details-grid">
          <div className="user-details-square updc-square-empty">Species Info</div>
          <div className="user-details-square updc-square-empty">Watering Schedule</div>
          <div className="user-details-square updc-square-empty">Plant Notes</div>
          <div className="user-details-square updc-square-empty">Actions</div>
        </div>
      </>
    );
  }

  const msPerDay = 24 * 60 * 60 * 1000;
  const now = new Date();
  const last = plant.lastWatered ? new Date(plant.lastWatered) : null;
  const daysSince = last ? Math.floor((now - last) / msPerDay) : null;
  const freq = Number(plant.waterFrequency);
  const daysRemaining =
    daysSince !== null && !isNaN(freq) && freq > 0 ? freq - daysSince : null;
  const overdue = daysRemaining !== null && daysRemaining <= 0;
  const fillPercent =
    daysSince !== null && freq > 0
      ? Math.max(0, Math.min(100, 100 - (daysSince / freq) * 100))
      : 100;

  let progressColor = "#00f2ff";
  if (overdue) progressColor = "#e74c3c";
  else if (fillPercent < 50) progressColor = '#ff9800'; // orange if 1 day left
  else if (fillPercent < 80) progressColor = '#ffe066';

  const nextWateringDate =
    last && freq > 0
      ? new Date(last.getTime() + freq * msPerDay).toLocaleDateString()
      : "Unknown";

  const handleWaterNow = async () => {
    setWateringInProgress(true);
    try {
      const token = localStorage.getItem("token");
      await patchOwnedPlant(token, plant.id, {
        lastWatered: new Date().toISOString(),
      });
      if (onWatered) onWatered();
    } finally {
      setWateringInProgress(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteConfirm) {
      setDeleteConfirm(true);
      return;
    }
    const token = localStorage.getItem("token");
    await deleteOwnedPlant(token, plant.id);
    setDeleteConfirm(false);
    if (onDeleted) onDeleted();
  };

  return (
    <>
      {/* ── Main hero card ── */}
      <div className="user-details-main-card">
        <div className="updc-main-content">
          <div className="updc-image-wrapper">
            <img
              className="updc-image"
              src={plant.imageUrl || "/assets/plant-placeholder.png"}
              alt={plant.nickname}
            />
          </div>
          <div className="updc-plant-info">
            <h2 className="updc-nickname">
              {plant.nickname || "Unnamed Plant"}
            </h2>

            {plant.tags?.length > 0 && (
              <div className="updc-tags-row">
                {plant.tags.map((t) => (
                  <span key={t.id} className="updc-tag-chip">
                    {t.name}
                  </span>
                ))}
              </div>
            )}

            <div className="updc-progress-bar">
              <div
                  className="updc-progress-inner"
                  style={{ width: `${fillPercent}%`, background: progressColor }}
              />
            </div>
          </div>
        </div>
      </div>

      {/* ── 4 detail squares ── */}
      <div className="user-details-grid">

        {/* Species Info */}
        <div className="user-details-square updc-square">
          <div className="updc-square-title">Species Info</div>
          {speciesLoading ? (
            <div className="updc-square-loading">Loading...</div>
          ) : species ? (
            <div className="updc-species-rows">
              {species.scientificName && (
                <div className="updc-row">
                  <span className="updc-label">Scientific</span>
                  <span>{species.scientificName}</span>
                </div>
              )}
              {species.familyName && (
                <div className="updc-row">
                  <span className="updc-label">Family</span>
                  <span>{species.familyName}</span>
                </div>
              )}
              {species.genus && (
                <div className="updc-row">
                  <span className="updc-label">Genus</span>
                  <span>{species.genus}</span>
                </div>
              )}
              {species.nativeTo && (
                <div className="updc-row">
                  <span className="updc-label">Native to</span>
                  <span>{species.nativeTo}</span>
                </div>
              )}
              {species.edible !== null && species.edible !== undefined && (
                <div className="updc-row">
                  <span className="updc-label">Edible</span>
                  <span>{species.edible ? "Yes" : "No"}</span>
                </div>
              )}
            </div>
          ) : (
            <div className="updc-no-data">No species data</div>
          )}
        </div>

        {/* Watering Schedule */}
        <div className="user-details-square updc-square">
          <div className="updc-square-title">Watering Schedule</div>
          <div className="updc-species-rows">
            <div className="updc-row">
              <span className="updc-label">Frequency</span>
              <span>Every {plant.waterFrequency} days</span>
            </div>
            <div className="updc-row">
              <span className="updc-label">Last watered</span>
              <span>{last ? last.toLocaleDateString() : "Unknown"}</span>
            </div>
            <div className="updc-row">
              <span className="updc-label">Next watering</span>
              <span>{nextWateringDate}</span>
            </div>
          </div>
          <button
            className="updc-water-btn"
            onClick={handleWaterNow}
            disabled={wateringInProgress}
          >
            {wateringInProgress ? "Watering..." : " Water Now"}
          </button>
        </div>

        {/* Plant Notes */}
        <div className="user-details-square updc-square">
          <div className="updc-square-title">Plant Notes</div>
          <p className="updc-notes">
            {plant.description || "No notes added."}
          </p>
        </div>

        {/* Actions */}
        <div className="user-details-square updc-square">
          <div className="updc-square-title">Actions</div>
          <div className="updc-actions">
            <button
              className="updc-edit-btn"
              onClick={() => setShowEditModal(true)}
            >
              Edit Plant
            </button>
            <button
              className={`updc-delete-btn${deleteConfirm ? " updc-delete-confirm" : ""}`}
              onClick={handleDelete}
            >
              {deleteConfirm ? "Confirm Delete" : "Delete Plant"}
            </button>
            {deleteConfirm && (
              <button
                className="updc-cancel-btn"
                onClick={() => setDeleteConfirm(false)}
              >
                Cancel
              </button>
            )}
          </div>
        </div>

      </div>

      {showEditModal && (
        <EditPlantModal
          plant={plant}
          onClose={() => setShowEditModal(false)}
          onSaved={() => {
            setShowEditModal(false);
            if (onWatered) onWatered();
          }}
        />
      )}
    </>
  );
}
