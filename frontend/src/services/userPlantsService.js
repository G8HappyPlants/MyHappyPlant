const BASE = "/api/owned";

export async function getAllTags(token) {
  const res = await fetch("/api/tags", {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Failed to fetch tags");
  return res.json();
}

export async function getAllOwnedPlants(token, page = 0) {
  const res = await fetch(`${BASE}?page=${page}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Failed to fetch your plants");
  return res.json();
}

export async function createOwnedPlant(
  token,
  { nickname, description, lastWatered, waterFrequency, trefleId, tagNames }
) {
  const res = await fetch(BASE, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ nickname, description, lastWatered, waterFrequency, trefleId, tagNames }),
  });
  if (!res.ok) throw new Error("Failed to add plant");
  return res.json();
}

export async function patchOwnedPlant(token, id, fields) {
  const res = await fetch(`${BASE}/${id}`, {
    method: "PATCH",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(fields),
  });
  if (!res.ok) throw new Error("Failed to update plant");
  return res.json();
}

export async function deleteOwnedPlant(token, id) {
  const res = await fetch(`${BASE}/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Failed to delete plant");
}

export async function waterAllOwnedPlants(token, plants) {
  const now = new Date().toISOString();
  return Promise.all(
      plants.map((p) =>
      fetch(`${BASE}/${p.id}`, {
        method: "PATCH",
        headers: {Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
        body: JSON.stringify({lastWatered: now}),
      })
      )
  );
}

export default { getAllTags, getAllOwnedPlants, createOwnedPlant, patchOwnedPlant, deleteOwnedPlant };
