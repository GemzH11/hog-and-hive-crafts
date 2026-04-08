import { useEffect, useState } from "react";
import type { Employee } from "../types/employee";
import {
  listEmployees,
  createEmployee,
  deleteEmployee,
} from "../api/employees";
import { Link } from "react-router-dom";

export function EmployeesListPage() {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function loadEmployees() {
    setLoading(true);
    setError(null);

    try {
      const data = await listEmployees();
      setEmployees(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  async function addEmployeeFromForm() {
    // basic validation
    if (!firstName.trim() || !lastName.trim()) {
      setError("Please enter both first and last name.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await createEmployee({
        firstName: firstName.trim(),
        lastName: lastName.trim(),
      });

      // clear the inputs after successful create
      setFirstName("");
      setLastName("");

      // refresh the list from the "API"
      await loadEmployees();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  async function deleteEmployeeAndReload(id: number) {
    setLoading(true);
    setError(null);

    try {
      await deleteEmployee(id);
      await loadEmployees();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    // useEffect runs after the component renders (`[]` means "only on mount").
    // We load data here (like you would do for a real API call).
    // Call the mock API and put the results into state
    void loadEmployees();
  }, []);

  return (
    <div className="rounded-lg border bg-white p-6">
      <div className="flex items-baseline justify-between">
        <h2 className="text-xl font-semibold">Employees</h2>
        <span className="text-sm text-slate-600">{employees.length} total</span>
      </div>

      {loading && (
        <p className="mt-3 text-sm text-slate-600">Loading employees...</p>
      )}
      {error && <p className="mt-3 text-sm text-red-700">Error: {error}</p>}

      <form
        onSubmit={(e) => {
          e.preventDefault();

          void addEmployeeFromForm();
        }}
        className="mt-4 flex gap-3"
      >
        <input
          disabled={loading}
          className="w-full rounded-md border px-3 py-2 text-sm disabled:bg-slate-100"
          placeholder="First name"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <input
          disabled={loading}
          className="w-full rounded-md border px-3 py-2 text-sm disabled:bg-slate-100"
          placeholder="Last name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <button
          type="submit"
          disabled={loading}
          className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
        >
          Add
        </button>
      </form>

      {!loading && !error && employees.length === 0 ? (
        <div className="mt-4 rounded-md border bg-slate-50 p-4 text-sm text-slate-700">
          No employees yet - add one above.
        </div>
      ) : (
        <table className="mt-4 w-full border-separate border-spacing-0">
          <thead>
            <tr className="text-left text-sm text-slate-600">
              <th className="border-b px-3 py-2">ID</th>
              <th className="border-b px-3 py-2">First name</th>
              <th className="border-b px-3 py-2">Last name</th>
              <th className="border-b px-3 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {/* Render a table row per employee */}
            {employees.map((e) => (
              <tr
                key={e.id}
                className="text-sm"
                data-testid={`employee-row-${e.id}`}
              >
                <td className="border-b px-3 py-2">{e.id}</td>
                <td className="border-b px-3 py-2">{e.firstName}</td>
                <td className="border-b px-3 py-2">{e.lastName}</td>
                <td className="border-b px-3 py-2 flex gap-3">
                  <button
                    type="button"
                    disabled={loading}
                    onClick={() => {
                      void deleteEmployeeAndReload(e.id);
                    }}
                    className="text-red-700 hover:underline disabled:opacity-50"
                  >
                    Delete
                  </button>
                  <Link
                    className="text-blue-700 hover:underline"
                    to={`/employees/${e.id}/edit`}
                  >
                    Edit
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
