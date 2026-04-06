import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { getEmployee, updateEmployee } from "../api/employees";

export function EmployeeEditPage() {
  const navigate = useNavigate();
  const params = useParams();

  // URL params are strings we convert to a number.
  const id = Number(params.id);

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [notFound, setNotFound] = useState(false);

  useEffect(() => {
    async function load() {
      if (!Number.isFinite(id)) {
        setError("Invalid employee id in URL.");
        return;
      }
      setLoading(true);
      setError(null);

      try {
        const emp = await getEmployee(id);
        setFirstName(emp.firstName);
        setLastName(emp.lastName);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Unknown error");
      } finally {
        setLoading(false);
      }
    }
    void load();
  }, [id]);

  async function save() {
    if (!firstName.trim() || !lastName.trim()) {
      setError("Please enter both first and last name.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await updateEmployee(id, {
        firstName: firstName.trim(),
        lastName: lastName.trim(),
      });
      void navigate("/employees");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  if (notFound) {
    return (
      <div className="rounded-lg border bg-white p-6">
        <h2 className="text-xl font-semibold">Employee not found</h2>
        <p className="mt-2 text-slate-700">
          The employee you’re trying to edit doesn’t exist.
        </p>
        <Link
          className="mt-4 inline-block text-blue-700 hover:underline"
          to="/employees"
        >
          Back to Employees
        </Link>
      </div>
    );
  }

  return (
    <div className="rounded-lg border bg-white p-6">
      <div className="flex items-baseline justify-between">
        <h2 className="text-xl font-semibold">Edit Employee #{id}</h2>
        <Link className="text-sm text-blue-700 hover:underline" to="/employees">
          Back
        </Link>
      </div>

      {loading && <p className="mt-3 text-sm text-slate-600">Loading…</p>}
      {error && <p className="mt-3 text-sm text-red-700">Error: {error}</p>}

      <div className="mt-4 grid gap-3 sm:grid-cols-2">
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
      </div>

      <div className="mt-4 flex gap-3">
        <button
          type="button"
          disabled={loading}
          onClick={() => {
            void save();
          }}
          className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
        >
          Save
        </button>

        <Link
          className="rounded-md border px-4 py-2 text-sm font-medium hover:bg-slate-50"
          to="/employees"
        >
          Cancel
        </Link>
      </div>
    </div>
  );
}
