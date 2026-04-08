import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { http, HttpResponse } from "msw";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { server } from "../mocks/server";
import { EmployeeEditPage } from "./EmployeeEditPage";

const API_BASE = "/api/employees/v1";

describe("EmployeeEditPage", () => {
  it("loads employee and pre-fills fields", async () => {
    server.use(
      http.get(`${API_BASE}/:id`, ({ params }) => {
        const id = Number(params.id);
        if (id !== 123) return HttpResponse.text("Not found", { status: 404 });
        return HttpResponse.json({
          id: 123,
          firstName: "Ada",
          lastName: "Lovelace",
        });
      }),
    );

    render(
      // initialEntries sets the starting URL
      <MemoryRouter initialEntries={["/employees/123/edit"]}>
        <Routes>
          <Route path="/employees/:id/edit" element={<EmployeeEditPage />} />
        </Routes>
      </MemoryRouter>,
    );

    expect(await screen.findByPlaceholderText("First name")).toHaveValue("Ada");
    expect(screen.getByPlaceholderText("Last name")).toHaveValue("Lovelace");
  });

  it("saves changes and navigates back to /employees", async () => {
    const user = userEvent.setup();

    let lastPutBody: unknown = null;

    server.use(
      http.get(`${API_BASE}/:id`, ({ params }) => {
        const id = Number(params.id);
        if (id !== 123) return HttpResponse.text("Not found", { status: 404 });
        return HttpResponse.json({
          id: 123,
          firstName: "Ada",
          lastName: "Lovelace",
        });
      }),

      http.put(`${API_BASE}/:id`, async ({ params, request }) => {
        const id = Number(params.id);
        if (id !== 123) return HttpResponse.text("Not found", { status: 404 });

        lastPutBody = await request.json();
        return HttpResponse.json({ id: 123, ...(lastPutBody as object) });
      }),
    );

    render(
      <MemoryRouter initialEntries={["/employees/123/edit"]}>
        <Routes>
          <Route path="/employees" element={<h1>Employees</h1>} />
          <Route path="/employees/:id/edit" element={<EmployeeEditPage />} />
        </Routes>
      </MemoryRouter>,
    );

    const firstNameInput = await screen.findByPlaceholderText("First name");
    const lastNameInput = screen.getByPlaceholderText("Last name");

    expect(firstNameInput).toHaveValue("Ada");
    expect(lastNameInput).toHaveValue("Lovelace");

    await user.clear(firstNameInput);
    await user.type(firstNameInput, "Grace");

    await user.click(screen.getByRole("button", { name: "Save" }));

    expect(lastPutBody).toEqual({ firstName: "Grace", lastName: "Lovelace" });
    expect(
      await screen.findByRole("heading", { name: "Employees" }),
    ).toBeInTheDocument();
  });
});
