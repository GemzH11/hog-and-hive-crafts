import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import "./index.css";
import App from "./App.tsx";

async function enableMocks() {
  const { worker } = await import("./mocks/browser");
  console.log("[MSW] Mocking enabled");
  // If a request is not mocked, let it go to the real network (ability to only mock some endpoints)
  await worker.start({ onUnhandledRequest: "bypass" });
}

async function bootstrap() {
  // Only ever run in development mode
  if (import.meta.env.DEV && import.meta.env.VITE_USE_MOCKS === "true") {
    await enableMocks();
  }

  // Mount React into `<div id="root">` in index.html
  createRoot(document.getElementById("root")!).render(
    <StrictMode>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </StrictMode>,
  );
}

void bootstrap();
