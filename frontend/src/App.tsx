import { Link, Route, Routes } from "react-router-dom";
import { HomePage } from "./pages/HomePage.tsx";
import { EmployeesListPage } from "./pages/EmployeesListPage.tsx";
import { EmployeeEditPage } from "./pages/EmployeeEditPage.tsx";

export default function App() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b bg-white">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-4 py-4">
          <div>
            <h1 className="text-2xl font-bold">Hog & Hive Crafts Admin</h1>
            <p className="text-sm text-slate-600">React + TS + Tailwind</p>
          </div>

          <nav className="flex gap-4 text-sm font-medium">
            <Link className="text-blue-700 hover:underline" to="/">
              Home
            </Link>
            {/* Navigates without reloading the page */}
            <Link className="text-blue-700 hover:underline" to="/employees">
              Employees
            </Link>
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-8">
        <Routes>
          {/* When the URL is ..., render that page component */}
          <Route path="/" element={<HomePage />} />
          <Route path="/employees" element={<EmployeesListPage />} />
          <Route path="/employees/:id/edit" element={<EmployeeEditPage />} />
        </Routes>
      </main>
    </div>
  );
}
