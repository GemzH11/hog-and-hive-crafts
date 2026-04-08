import type { APIRequestContext } from "@playwright/test";
import type { Employee } from "../../src/types/employee";
import { API_BASE } from "./urls";

/**
 * - Use request (API) for setup/teardown so tests are independent
 * - Use page (UI) for the behavior under test
 * - Use try/finally so teardown runs even if assertions fail
 */

export function uniqueName(prefix: string) {
  return `${prefix}-${Date.now()}`;
}

export async function createEmployee(
  request: APIRequestContext,
  data: Omit<Employee, "id">,
) {
  const res = await request.post(API_BASE, { data });
  if (!res.ok())
    throw new Error(
      `createEmployee failed: ${res.status()} ${await res.text()}`,
    );
  return (await res.json()) as Employee;
}

export async function deleteEmployeeQuietly(
  request: APIRequestContext,
  id: number,
) {
  const res = await request.delete(`${API_BASE}/${id}`);
  // ignore 404 etc. in cleanup
  return res;
}
