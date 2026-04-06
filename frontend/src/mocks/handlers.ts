import { http, HttpResponse } from "msw";
import {
  createEmployee,
  deleteEmployeeById,
  getEmployeeById,
  listEmployees,
  updateEmployeeById,
} from "./seedData";

const API_BASE = "/api/employees/v1";

export const handlers = [
  // GET /api/employees/v1  -> list employees
  http.get(API_BASE, () => {
    return HttpResponse.json(listEmployees());
  }),

  // GET /api/employees/v1/:id -> single employee
  http.get(`${API_BASE}/:id`, ({ params }) => {
    const id = Number(params.id);
    const emp = getEmployeeById(id);
    if (!emp) return HttpResponse.text("Not found", { status: 404 });
    return HttpResponse.json(emp);
  }),

  // POST /api/employees/v1 -> create employee
  http.post(API_BASE, async ({ request }) => {
    const body = (await request.json()) as {
      firstName: string;
      lastName: string;
    };
    const created = createEmployee(body.firstName, body.lastName);
    return HttpResponse.json(created, { status: 201 });
  }),

  // PUT /api/employees/v1/:id -> update employee
  http.put(`${API_BASE}/:id`, async ({ params, request }) => {
    const id = Number(params.id);
    const patch = (await request.json()) as {
      firstName?: string;
      lastName?: string;
    };

    const updated = updateEmployeeById(id, patch);
    if (!updated) return HttpResponse.text("Not found", { status: 404 });
    return HttpResponse.json(updated);
  }),

  // DELETE /api/employees/v1/:id -> delete employee (204)
  http.delete(`${API_BASE}/:id`, ({ params }) => {
    const id = Number(params.id);
    const ok = deleteEmployeeById(id);
    if (!ok) return HttpResponse.text("Not found", { status: 404 });
    return new HttpResponse(null, { status: 204 });
  }),
];
