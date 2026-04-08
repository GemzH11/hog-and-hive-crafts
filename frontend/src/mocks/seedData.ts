import type { Employee } from "../types/employee";

let employees: Employee[] = [];

export function seedData() {
  employees = [
    { id: 1, firstName: "Ada", lastName: "Lovelace" },
    { id: 2, firstName: "Grace", lastName: "Hopper" },
    { id: 3, firstName: "Alan", lastName: "Turing" },
  ];
}

export function resetSeedData() {
  employees = [];
}

export function listEmployees() {
  return employees;
}

export function getEmployeeById(id: number) {
  return employees.find((e) => e.id === id) ?? null;
}

export function createEmployee(firstName: string, lastName: string) {
  const nextId =
    employees.length > 0 ? Math.max(...employees.map((e) => e.id)) + 1 : 1;
  const created: Employee = { id: nextId, firstName, lastName };
  employees = [...employees, created];
  return created;
}

export function updateEmployeeById(
  id: number,
  patch: Partial<Omit<Employee, "id">>,
) {
  const idx = employees.findIndex((e) => e.id === id);
  if (idx === -1) return null;
  employees[idx] = { ...employees[idx], ...patch, id };
  return employees[idx];
}

export function deleteEmployeeById(id: number) {
  const before = employees.length;
  employees = employees.filter((e) => e.id !== id);
  return employees.length !== before;
}
