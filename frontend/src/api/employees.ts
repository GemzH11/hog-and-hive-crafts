import type { Employee } from '../types/employee'
import { addEmployee, getAllEmployees, getEmployeeById, removeEmployee, updateEmployee as updateEmployeeInStore } from './employeesStore'

// eslint-disable-next-line @typescript-eslint/require-await
export async function listEmployees(): Promise<Employee[]> {
    return getAllEmployees()
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function getEmployee(id: number): Promise<Employee | undefined> {
    return getEmployeeById(id)
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function createEmployee(
    input: Omit<Employee, 'id'>,
): Promise<Employee> {
    return addEmployee(input)
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function deleteEmployee(id: number): Promise<boolean> {
    return removeEmployee(id)
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function updateEmployee(id: number, patch: Partial<Omit<Employee, 'id'>>): Promise<Employee | undefined> {
    return updateEmployeeInStore(id, patch)
}