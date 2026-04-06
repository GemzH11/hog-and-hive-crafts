import { setupWorker } from "msw/browser";
import { handlers } from "./handlers";
import { seedData } from "./seedData";

// Dev-only seed data:
// - This seeds the in-memory mock data so the app has realistic data when running with
//   `VITE_USE_MOCKS=true` and no backend is available.
// - Component/unit tests should NOT rely on this seed. Tests should set up their own
//   data by overriding handlers with `server.use(...)` (or by resetting/seeding the data
//   explicitly in the test).
seedData();

export const worker = setupWorker(...handlers);
