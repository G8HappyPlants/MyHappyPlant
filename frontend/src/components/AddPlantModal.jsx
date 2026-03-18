import React, { useState, useEffect } from "react";
import "../styles/AddPlantModal.css";
import { createOwnedPlant, getAllTags } from "../services/userPlantsService";
import {useNavigate} from "react-router-dom";

export default function AddPlantModal({ onClose, onPlantAdded, preselectedSpecies }) {

  const [selectedSpecies] = useState(preselectedSpecies ?? null);
  const [nickname, setNickname] = useState("");
  const [lastWatered, setLastWatered] = useState(
    () => new Date().toISOString().slice(0, 10)
  );
  const [waterFrequency, setWaterFrequency] = useState(7);
  const [description, setDescription] = useState("");
  const [tags, setTags] = useState([]);
  const [tagInput, setTagInput] = useState("");
  const [allTags, setAllTags] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    getAllTags(token).then(setAllTags).catch(() => {});
  }, []);


  const tagSuggestions = allTags.filter(
    (t) =>
      tagInput &&
      t.name.toLowerCase().includes(tagInput.toLowerCase()) &&
      !tags.includes(t.name)
  );

  const addTag = (name) => {
    const trimmed = name.trim();
    if (!trimmed || tags.includes(trimmed)) return;
    setTags([...tags, trimmed]);
    setTagInput("");
  };

  const removeTag = (name) => setTags(tags.filter((t) => t !== name));


  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedSpecies) {
      setSubmitting("No species selected");
      return;
    }
    if (!nickname.trim()) {
      setSubmitError("Please enter a nickname.");
      return;
    }
    const freq = parseInt(waterFrequency, 10);
    if (isNaN(freq) || freq < 1) {
      setSubmitError("Please enter a valid water frequency (minimum 1 day).");
      return;
    }
    setSubmitError(null);
    setSubmitting(true);
    try {
      const token = localStorage.getItem("token");

      const lastWateredInstant = new Date(lastWatered + "T00:00:00Z").toISOString();
      const newPlant = await createOwnedPlant(token, {
        nickname: nickname.trim(),
        description: description.trim() || null,
        lastWatered: lastWateredInstant,
        waterFrequency: freq,
        trefleId: selectedSpecies.id,
        tagNames: tags,
      });
      onPlantAdded(newPlant);
      navigate("/user-library");

    } catch (e) {
      setSubmitError(e.message);
      setSubmitting(false);
    }
  };

  return (
    <div className="add-plant-overlay" onClick={onClose}>
      <div className="add-plant-modal" onClick={(e) => e.stopPropagation()}>
        <div className="add-plant-header">
          <h2 className="add-plant-title">Add Plant to Library</h2>
          <button
            type="button"
            className="add-plant-close"
            onClick={onClose}
            aria-label="Close"
          >
            ✕
          </button>
        </div>

        <div className="add-plant-body">
            {selectedSpecies && (
              <div className="add-plant-selected-badge">
                ✓ {selectedSpecies.commonName || selectedSpecies.scientificName}
              </div>
            )}

          {/* Plant details form */}
          <form className="add-plant-form" onSubmit={handleSubmit}>
            <label className="add-plant-field-label">
              Nickname <span className="add-plant-required">*</span>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                className="add-plant-input"
                placeholder="e.g. Sunny"
                maxLength={64}
              />
            </label>

            <label className="add-plant-field-label">
              Last Watered <span className="add-plant-required">*</span>
              <input
                type="date"
                value={lastWatered}
                onChange={(e) => setLastWatered(e.target.value)}
                className="add-plant-input"
                max={new Date().toISOString().slice(0, 10)}
              />
            </label>

            <label className="add-plant-field-label">
              Water Every <span className="add-plant-required">*</span>
              <div className="add-plant-freq-row">
                <input
                  type="number"
                  value={waterFrequency}
                  onChange={(e) => setWaterFrequency(e.target.value)}
                  className="add-plant-input add-plant-freq-input"
                  min={1}
                  max={365}
                />
                <span className="add-plant-freq-unit">days</span>
              </div>
            </label>

            <label className="add-plant-field-label">
              Description
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className="add-plant-input add-plant-textarea"
                placeholder="Optional notes..."
                maxLength={500}
              />
            </label>

            <label className="add-plant-field-label">
              Tags
              <div className="add-plant-tag-input-row">
                <input
                  type="text"
                  value={tagInput}
                  onChange={(e) => setTagInput(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      e.preventDefault();
                      addTag(tagInput);
                    }
                  }}
                  placeholder="Type a tag and press Enter..."
                  className="add-plant-input"
                />
                <button
                  type="button"
                  className="add-plant-tag-add-btn"
                  onClick={() => addTag(tagInput)}
                >
                  +
                </button>
              </div>
              {tagSuggestions.length > 0 && (
                <div className="add-plant-tag-suggestions">
                  {tagSuggestions.map((s) => (
                    <div
                      key={s.id}
                      className="add-plant-tag-suggestion"
                      onClick={() => addTag(s.name)}
                    >
                      {s.name}
                    </div>
                  ))}
                </div>
              )}
              {tags.length > 0 && (
                <div className="add-plant-tags-list">
                  {tags.map((tag) => (
                    <span key={tag} className="add-plant-tag-chip">
                      {tag}
                      <button
                        type="button"
                        className="add-plant-tag-chip-remove"
                        onClick={() => removeTag(tag)}
                      >
                        ×
                      </button>
                    </span>
                  ))}
                </div>
              )}
            </label>

            {submitError && (
              <div className="add-plant-error">{submitError}</div>
            )}

            <div className="add-plant-actions">
              <button
                type="button"
                className="add-plant-cancel-btn"
                onClick={onClose}
                disabled={submitting}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="add-plant-submit-btn"
                disabled={submitting}
              >
                {submitting ? "Adding..." : "Add Plant"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
