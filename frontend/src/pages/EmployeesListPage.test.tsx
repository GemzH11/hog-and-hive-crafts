import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { http, HttpResponse } from "msw";
import { MemoryRouter } from "react-router-dom";
import { server } from "../mocks/server";
import { EmployeesListPage } from "./EmployeesListPage";
import type { Employee } from "../types/employee";

const API_BASE = "/api/employees/v1";

describe("EmployeesListPage", () => {
  it("renders Employees page and loads data", async () => {
    server.use(
      http.get(API_BASE, () => {
        return HttpResponse.json([
          { id: 1, firstName: "Ada", lastName: "Lovelace" },
          { id: 2, firstName: "Grace", lastName: "Hopper" },
        ]);
      }),
    );

    render(
      <MemoryRouter>
        <EmployeesListPage />
      </MemoryRouter>,
    );

    // Because the page loads employees asynchronously, we "await" the UI change.
    expect(
      await screen.findByRole("heading", { name: "Employees" }),
    ).toBeInTheDocument();

    // These headers are static, but checking them is a simple sanity check.
    expect(screen.getByText("First name")).toBeInTheDocument();
    expect(screen.getByText("Last name")).toBeInTheDocument();
    expect(await screen.findByText("Ada")).toBeInTheDocument();
    expect(await screen.findByText("Hopper")).toBeInTheDocument();
  });

  it("adds an employee via the form", async () => {
    const user = userEvent.setup();

    let lastPostBody: Omit<Employee, "id"> | null = null;

    // Local in-test data so this test is fully self-contained
    let employees: Employee[] = [
      { id: 1, firstName: "Ada", lastName: "Lovelace" },
    ];

    server.use(
      http.get(API_BASE, () => HttpResponse.json(employees)),

      http.post(API_BASE, async ({ request }) => {
        lastPostBody = (await request.json()) as Omit<Employee, "id">;
        const created: Employee = { id: 2, ...lastPostBody };
        employees = [...employees, created];
        return HttpResponse.json(created, { status: 201 });
      }),
    );

    render(
      <MemoryRouter>
        <EmployeesListPage />
      </MemoryRouter>,
    );

    // Wait for initial load (GET handler)
    expect(
      await screen.findByRole("heading", { name: "Employees" }),
    ).toBeInTheDocument();

    await user.type(screen.getByPlaceholderText("First name"), "Jane");
    await user.type(screen.getByPlaceholderText("Last name"), "Doe");
    await user.click(screen.getByRole("button", { name: /add/i }));

    expect(lastPostBody).toEqual({ firstName: "Jane", lastName: "Doe" });

    // The new row should appear (POST handler + rerender)
    expect(await screen.findByText("Jane")).toBeInTheDocument();
    expect(screen.getByText("Doe")).toBeInTheDocument();
  });
});
