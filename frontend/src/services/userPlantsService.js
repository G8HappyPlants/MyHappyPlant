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
  { nickname, description, lastWatered, waterFrequency, trefleId, tagNames, imageFile }
) {
  let imageBase64 = null;
  let imageContentType = null;

  if(imageFile) {
    imageBase64 = await fileToBase64(imageFile);
    imageContentType = imageFile.type;
  }

  const res = await fetch(BASE, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ nickname, description, lastWatered, waterFrequency, trefleId, tagNames, imageBase64, imageContentType }),
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

export async function fileToBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result.split(",")[1]);
    reader.onerror = reject;
    reader.readAsDataURL(file);
  })
}

export default { getAllTags, getAllOwnedPlants, createOwnedPlant, patchOwnedPlant, deleteOwnedPlant };
