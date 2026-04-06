import { afterEach, describe, expect, it, vi } from "vitest";
import { apiDelete, apiGet, apiPost } from "./apiClient";

describe("apiClient", () => {
  afterEach(() => {
    // Undo any mocks/spies we created in a test so tests don't leak into each other.
    vi.restoreAllMocks();
  });

  it("apiGet returns parsed JSON on 200", async () => {
    // Replace global fetch with a fake implementation that returns a controlled Response.
    const fetchSpy = vi.spyOn(globalThis, "fetch").mockResolvedValue(
      new Response(JSON.stringify({ ok: true }), {
        status: 200,
        headers: { "Content-Type": "application/json" },
      }),
    );

    // Call the real apiGet. It will use the mocked fetch.
    const data = await apiGet<{ ok: boolean }>("/some-url");

    // Assert the JSON got parsed and returned.
    expect(fetchSpy).toHaveBeenCalledTimes(1);
    expect(data.ok).toBe(true);
  });

  it("apiGet throws an Error with response text on non-2xx", async () => {
    // Mock fetch to return a 400 with a plain text body.
    const fetchSpy = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(new Response("Bad request", { status: 400 }));

    // apiGet should reject (throw) and the error message should include the body text.
    await expect(apiGet("/some-url")).rejects.toThrow(/Bad request/);
    expect(fetchSpy).toHaveBeenCalledTimes(1);
  });

  it("apiDelete<void> does not throw on 204 No Content", async () => {
    // Mock fetch to return 204 with no body (typical DELETE response).
    const fetchSpy = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(new Response(null, { status: 204 }));

    // apiDelete<void> should resolve successfully and yield undefined.
    await expect(apiDelete<void>("/some-url")).resolves.toBeUndefined();
    expect(fetchSpy).toHaveBeenCalledTimes(1);
  });

  it("apiPost sends JSON body with Content-Type header", async () => {
    // We'll capture how apiPost calls fetch by inspecting fetch's mock calls.
    const fetchSpy = vi.spyOn(globalThis, "fetch").mockResolvedValue(
      new Response(JSON.stringify({ id: 1 }), {
        status: 201,
        headers: { "Content-Type": "application/json" },
      }),
    );

    await apiPost<{ id: number }>("/some-url", { name: "Jane" });

    // fetchSpy.mock.calls is an array of [url, options] pairs.
    expect(fetchSpy).toHaveBeenCalledTimes(1);

    const [, options] = fetchSpy.mock.calls[0] as [
      string,
      RequestInit | undefined,
    ];

    expect(options?.method).toBe("POST");
    expect(options?.headers).toEqual({ "Content-Type": "application/json" });
    expect(options?.body).toBe(JSON.stringify({ name: "Jane" }));
  });
});
