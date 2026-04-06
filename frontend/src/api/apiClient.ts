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

export async function apiGet<T>(url: string): Promise<T> {
  const response = await fetch(url, { method: "GET" });
  return parseJson<T>(response);
}

export async function apiPost<T>(url: string, body: unknown): Promise<T> {
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return parseJson<T>(response);
}

export async function apiPut<T>(url: string, body: unknown): Promise<T> {
  const response = await fetch(url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return parseJson<T>(response);
}

export async function apiDelete<T>(url: string): Promise<T> {
  const response = await fetch(url, { method: "DELETE" });
  return parseJson<T>(response);
}
