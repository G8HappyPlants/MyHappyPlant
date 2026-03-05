import React, { useState, useEffect } from "react";
import "../styles/AddPlantModal.css";
import { patchOwnedPlant, getAllTags } from "../services/userPlantsService";

export default function EditPlantModal({ plant, onClose, onSaved }) {
  const [nickname, setNickname] = useState(plant.nickname || "");
  const [description, setDescription] = useState(plant.description || "");
  const [waterFrequency, setWaterFrequency] = useState(plant.waterFrequency ?? 7);
  const [lastWatered, setLastWatered] = useState(
    plant.lastWatered
      ? new Date(plant.lastWatered).toISOString().slice(0, 10)
      : new Date().toISOString().slice(0, 10)
  );
  const [tags, setTags] = useState(plant.tags?.map((t) => t.name) || []);
  const [tagInput, setTagInput] = useState("");
  const [allTags, setAllTags] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

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
    if (!nickname.trim()) {
      setError("Nickname is required.");
      return;
    }
    const freq = parseInt(waterFrequency, 10);
    if (isNaN(freq) || freq < 1) {
      setError("Water frequency must be at least 1 day.");
      return;
    }
    setError(null);
    setSubmitting(true);
    try {
      const token = localStorage.getItem("token");
      const updated = await patchOwnedPlant(token, plant.id, {
        nickname: nickname.trim(),
        description: description.trim() || null,
        waterFrequency: freq,
        lastWatered: new Date(lastWatered + "T00:00:00Z").toISOString(),
        tagNames: tags,
      });
      onSaved(updated);
    } catch (e) {
      setError(e.message);
      setSubmitting(false);
    }
  };

  return (
    <div className="add-plant-overlay" onClick={onClose}>
      <div className="add-plant-modal" onClick={(e) => e.stopPropagation()}>

        <div className="add-plant-header">
          <h2 className="add-plant-title">Edit Plant</h2>
          <button className="add-plant-close" onClick={onClose} aria-label="Close">✕</button>
        </div>

        <div className="add-plant-body">
          <form className="add-plant-form" onSubmit={handleSubmit}>

            <label className="add-plant-field-label">
              Nickname <span className="add-plant-required">*</span>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                className="add-plant-input"
                maxLength={64}
              />
            </label>

            <label className="add-plant-field-label">
              Last Watered
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

            {error && <div className="add-plant-error">{error}</div>}

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
                {submitting ? "Saving..." : "Save"}
              </button>
            </div>

          </form>
        </div>

      </div>
    </div>
  );
}
