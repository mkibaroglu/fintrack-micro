// src/dashboard/Dashboard.jsx
import { useEffect, useState } from "react";
import axios from "../api/axios";
import { PieChart, Pie, Cell, Tooltip } from "recharts";
import PageLayout from "../layout/PageLayout";
import "./dashboard.css";

export default function Dashboard() {
    const [data, setData] = useState(null);

    useEffect(() => {
        axios.get("/api/transactions/dashboard").then((res) => {
            setData(res.data);
        });
    }, []);

    if (!data) return <p>Loading...</p>;

    const pieData = [
        { name: "Expense", value: data.totalExpense },
        { name: "Income", value: data.totalIncome },
    ].filter((item) => item.value > 0);

    const COLORS = ["#ff4d4f", "#00d4aa"];

    return (
        <PageLayout>
            <div className="dashboard-container">
                <h2>Dashboard</h2>

                <div className="stats-grid">
                    <div className="stat-card income">Income: {data.totalIncome} ₺</div>
                    <div className="stat-card expense">Expense: {data.totalExpense} ₺</div>
                    <div className="stat-card balance">Balance: {data.balance} ₺</div>
                    <div className="stat-card count">
                        Transactions: {data.totalTransactionCount}
                    </div>
                </div>

                <h3>Expense Distribution</h3>

                <div className="chart-container">
                    <PieChart width={300} height={300}>
                        <Pie data={pieData} dataKey="value" outerRadius={120}>
                            {pieData.map((entry, index) => (
                                <Cell key={index} fill={COLORS[index]} />
                            ))}
                        </Pie>
                        <Tooltip />
                    </PieChart>
                </div>
            </div>
        </PageLayout>
    );
}
