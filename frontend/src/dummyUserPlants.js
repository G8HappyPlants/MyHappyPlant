// Dummy user plant objects based on EditUserPlantRequest/CreateUserPlantRequest DTO
export const dummyUserPlants = [
  {
    id: 1,
    nickname: "Sunny",
    lastWatered: "2026-02-24", // today, bar full
    waterFrequency: 7,
    trefleId: 123456,
  },
  {
    id: 2,
    nickname: "Fernie",
    lastWatered: "2026-02-20", // 4 days ago, bar mostly full
    waterFrequency: 5,
    trefleId: 654321,
  },
  {
    id: 3,
    nickname: "Cactus Jack",
    lastWatered: "2026-02-10", // 14 days ago, bar empty
    waterFrequency: 14,
    trefleId: 789012,
  },
];
