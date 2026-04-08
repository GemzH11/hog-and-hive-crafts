import { test, expect } from "@playwright/test";
import type { Employee } from "../src/types/employee";
import { API_BASE } from "./support/urls";
import {
  createEmployee,
  deleteEmployeeQuietly,
  uniqueName,
} from "./support/testData";

test("E2E: can add a new employee: Success", async ({ page, request }) => {
  let createdId: number | null = null;

  const firstName = uniqueName("Jane");
  const lastName = uniqueName("Doe");

  try {
    await page.goto(`/employees`);
    await expect(
      page.getByRole("heading", { name: "Employees" }),
    ).toBeVisible();

    await page.getByPlaceholder("First name").fill(firstName);
    await page.getByPlaceholder("Last name").fill(lastName);

    const postPromise = page.waitForResponse(
      (r) =>
        r.url() === API_BASE &&
        r.request().method() === "POST" &&
        r.status() === 201,
    );

    await page.getByRole("button", { name: "Add" }).click();

    const res = await postPromise;
    const json = (await res.json()) as Employee;
    createdId = json.id;

    await expect(page.getByText(firstName)).toBeVisible();
    await expect(page.getByText(lastName)).toBeVisible();
  } finally {
    if (createdId != null) {
      // cleanup: ignore failure (e.g. if it was already deleted for some reason)
      await deleteEmployeeQuietly(request, createdId);
    }
  }
});

test("E2E: can edit an existing employee: Success", async ({
  page,
  request,
}) => {
  const employee = await createEmployee(request, {
    firstName: uniqueName("EditFrom"),
    lastName: uniqueName("Setup"),
  });

  const updatedFirst = uniqueName("Updated");
  const updatedLast = uniqueName("Name");

  try {
    // Best practice: navigate from the table (more realistic)
    await page.goto(`/employees`);
    await expect(
      page.getByRole("heading", { name: "Employees" }),
    ).toBeVisible();

    const row = page.getByTestId(`employee-row-${employee.id}`);
    await row.getByRole("link", { name: "Edit" }).click();

    // If your edit page has a heading, keep this. If it doesn't, remove this assertion.
    // await expect(page.getByRole("heading", { name: "Edit Employee" })).toBeVisible();

    await expect(page.getByPlaceholder("First name")).toHaveValue(
      employee.firstName,
    );
    await expect(page.getByPlaceholder("Last name")).toHaveValue(
      employee.lastName,
    );

    await page.getByPlaceholder("First name").fill(updatedFirst);
    await page.getByPlaceholder("Last name").fill(updatedLast);

    await page.getByRole("button", { name: "Save" }).click();

    await expect(page).toHaveURL(/\/employees$/);
    await expect(page.getByText(updatedFirst)).toBeVisible();
    await expect(page.getByText(updatedLast)).toBeVisible();
  } finally {
    await deleteEmployeeQuietly(request, employee.id);
  }
});

test("E2E: can delete an employee: Success", async ({ page, request }) => {
  const employee = await createEmployee(request, {
    firstName: uniqueName("DeleteMe"),
    lastName: uniqueName("Employee"),
  });

  try {
    await page.goto(`/employees`);
    await expect(
      page.getByRole("heading", { name: "Employees" }),
    ).toBeVisible();

    const row = page.getByTestId(`employee-row-${employee.id}`);

    // sanity: ensure it exists before deleting
    await expect(row).toBeVisible();

    await row.getByRole("button", { name: "Delete" }).click();

    await expect(page.getByTestId(`employee-row-${employee.id}`)).toHaveCount(
      0,
    );
  } finally {
    // cleanup fallback (ignore failure if already deleted)
    await deleteEmployeeQuietly(request, employee.id);
  }
});
