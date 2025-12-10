import { useEffect, useState } from "react";
import axios from "../api/axios";
import PageLayout from "../layout/PageLayout";
import "./transactions.css";

export default function Transactions() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        axios.get("/api/transactions/my").then((res) => {
            setTransactions(res.data.content);
        });
    }, []);

    return (
        <PageLayout>
            <h2>Transactions</h2>

            <div className="tx-list">
                {transactions.map((t) => (
                    <div
                        key={t.id}
                        className={`tx-card ${t.type === "INCOME" ? "income" : "expense"}`}
                    >
                        <b>{t.type}</b> | {t.category} | {t.amount} ₺
                        <div>{t.description}</div>
                    </div>
                ))}
            </div>
        </PageLayout>
    );
}