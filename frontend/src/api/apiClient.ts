async function parseJson<T>(response: Response): Promise<T> {
  // If the API returns non-2xx, throw an Error so our UI shows it via `catch`.
  if (!response.ok) {
    const text = await response.text().catch(() => "");
    throw new Error(text || `Request failed (${response.status})`);
  }

  // Handle 204 No Content (common for DELETE)
  if (response.status === 204) {
    return undefined as T;
  }
  return response.json() as Promise<T>;
}

function withApiOrigin(url: string) {
  const useMocks =
    String(import.meta.env.VITE_USE_MOCKS).toLowerCase() === "true";
  const origin = import.meta.env.VITE_API_ORIGIN as string | undefined;

  // If MSW dev mocks are enabled, force relative URLs so the worker can intercept them.
  if (useMocks) return url;

  // If caller passed an absolute URL already, don't touch it.
  if (/^https?:\/\//.test(url)) return url;

  // If no origin configured, keep relative URL.
  if (!origin) return url;

  // Only prefix root-relative URLs like "/api/..."
  if (!url.startsWith("/")) return url;

  return `${origin}${url}`;
}

export async function apiGet<T>(url: string): Promise<T> {
  const response = await fetch(withApiOrigin(url), { method: "GET" });
  return parseJson<T>(response);
}

export async function apiPost<T>(url: string, body: unknown): Promise<T> {
  const response = await fetch(withApiOrigin(url), {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return parseJson<T>(response);
}

export async function apiPut<T>(url: string, body: unknown): Promise<T> {
  const response = await fetch(withApiOrigin(url), {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return parseJson<T>(response);
}

export async function apiDelete<T>(url: string): Promise<T> {
  const response = await fetch(withApiOrigin(url), { method: "DELETE" });
  return parseJson<T>(response);
}
