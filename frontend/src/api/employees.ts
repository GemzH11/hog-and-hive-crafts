import type { Employee } from "../types/employee";
import { apiDelete, apiGet, apiPost, apiPut } from "./apiClient";

const API_BASE = "/api/employees/v1";

export async function listEmployees(): Promise<Employee[]> {
  return apiGet<Employee[]>(API_BASE);
}

export async function getEmployee(id: number): Promise<Employee> {
  return apiGet<Employee>(`${API_BASE}/${id}`);
}

export async function createEmployee(
  input: Omit<Employee, "id">,
): Promise<Employee> {
  return apiPost<Employee>(API_BASE, input);
}

export async function updateEmployee(
  id: number,
  patch: Partial<Omit<Employee, "id">>,
): Promise<Employee> {
  return apiPut<Employee>(`${API_BASE}/${id}`, patch);
}

export async function deleteEmployee(id: number): Promise<void> {
  return apiDelete<void>(`${API_BASE}/${id}`);
}
