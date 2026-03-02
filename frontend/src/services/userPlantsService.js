const BASE = "/api/owned";

export async function getAllOwnedPlants(token, page = 0) {
  const res = await fetch(`${BASE}?page=${page}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error("Failed to fetch your plants");
  return res.json();
}

export async function createOwnedPlant(
  token,
  { nickname, description, lastWatered, waterFrequency, trefleId }
) {
  const res = await fetch(BASE, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ nickname, description, lastWatered, waterFrequency, trefleId }),
  });
  if (!res.ok) throw new Error("Failed to add plant");
  return res.json();
}

export default { getAllOwnedPlants, createOwnedPlant };
