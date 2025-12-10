// src/router/AppRouter.jsx
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import Login from "../auth/Login";
import Register from "../auth/Register";
import Transactions from "../transactions/Transactions";
import CreateTransaction from "../transactions/CreateTransaction";
import EditTransaction from "../transactions/EditTransaction";
import Dashboard from "../dashboard/Dashboard";
import Navbar from "../layout/Navbar";
import ProtectedRoute from "../components/ProtectedRoute";

function Layout() {
    const token = localStorage.getItem("token");
    const location = useLocation();

    const hideNavbar =
        !token || location.pathname === "/" || location.pathname === "/register";

    return (
        <>
            {!hideNavbar && <Navbar />}

            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/transactions"
                    element={
                        <ProtectedRoute>
                            <Transactions />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/transactions/create"
                    element={
                        <ProtectedRoute>
                            <CreateTransaction />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/transactions/:id/edit"
                    element={
                        <ProtectedRoute>
                            <EditTransaction />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </>
    );
}

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Layout />
        </BrowserRouter>
    );
}
