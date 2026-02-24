const BASE = "http://localhost:8080/api/species";

export async function getAllSpecies(token) {
  const res = await fetch(`${BASE}/all`, {
    headers: {
      "Authorization": `Bearer ${token}`
    }
  });
  if (!res.ok) throw new Error("Failed to fetch species");
  return res.json();
}

export async function getSpeciesById(id, token) {
  const res = await fetch(`${BASE}/${id}`, {
    headers: {
      "Authorization": `Bearer ${token}`
    }
  });
  if (!res.ok) throw new Error("Failed to fetch plant details");
  return res.json();
}

export default { getAllSpecies, getSpeciesById };
