import type { Employee } from '../types/employee'
import employeesData from '../data/employees.json'

// Keep a mutable copy in memory (not the JSON directly)
// The spread (...) creates a new array.
let employees: Employee[] = [...(employeesData as Employee[])]

// Read all employees
export function getAllEmployees(): Employee[] {
    return employees
}

export function getEmployeeById(id: number): Employee | undefined {
    return employees.find((e) => e.id === id)
}

// Add an employee (auto-generate a new numeric id)
export function addEmployee(input: Omit<Employee, 'id'>): Employee {
    const nextId =
        employees.length > 0 ? Math.max(...employees.map((e) => e.id)) + 1 : 1

    const newEmployee: Employee = {
        id: nextId,
        firstName: input.firstName,
        lastName: input.lastName,
    }

    // push mutates the array *in place*; it returns the new length (a number).
    employees.push(newEmployee)

    return newEmployee
}

// Remove by id, returns true if something was removed
export function removeEmployee(id: number): boolean {
    const oldLength = employees.length
    employees = employees.filter((e) => e.id !== id)
    return employees.length !== oldLength
}

export function updateEmployee(id: number, patch: Partial<Omit<Employee, 'id'>>): Employee | undefined {
    const idx = employees.findIndex((e) => e.id === id)
    if (idx === -1) {
        return undefined
    }
    const updatedEmployee: Employee = { ...employees[idx], ...patch, id }
    employees[idx] = updatedEmployee
    return updatedEmployee
}