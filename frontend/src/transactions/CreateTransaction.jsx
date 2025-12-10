// src/transactions/CreateTransaction.jsx
import { useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import PageLayout from "../layout/PageLayout";

export default function CreateTransaction() {
    const [type, setType] = useState("EXPENSE");
    const [category, setCategory] = useState("MARKET");
    const [amount, setAmount] = useState("");
    const [description, setDescription] = useState("");

    const navigate = useNavigate();

    const save = async () => {
        const payload = { type, category, amount: Number(amount), description };
        await axios.post("/api/transactions", payload);
        navigate("/transactions");
    };

    return (
        <PageLayout>
            <div style={box}>
                <h2>New Transaction</h2>

                <div style={grid}>
                    <select value={type} onChange={(e) => setType(e.target.value)}>
                        <option>EXPENSE</option>
                        <option>INCOME</option>
                    </select>

                    <select value={category} onChange={(e) => setCategory(e.target.value)}>
                        <option>MARKET</option>
                        <option>RENT</option>
                        <option>SALARY</option>
                        <option>BILL</option>
                        <option>OTHER</option>
                    </select>
                </div>

                <input
                    type="number"
                    placeholder="Amount"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                />

                <input
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <button onClick={save}>Save</button>
            </div>
        </PageLayout>
    );
}

const box = {
    display: "flex",
    flexDirection: "column",
    gap: 12,
    maxWidth: 400,
};

const grid = {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 12,
};
