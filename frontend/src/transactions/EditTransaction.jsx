// src/transactions/EditTransaction.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "../api/axios";
import PageLayout from "../layout/PageLayout";

export default function EditTransaction() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [type, setType] = useState("EXPENSE");
    const [category, setCategory] = useState("MARKET");
    const [amount, setAmount] = useState("");
    const [description, setDescription] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const load = async () => {
            try {
                const res = await axios.get(`/api/transactions/${id}`);
                const tx = res.data;

                setType(tx.type);
                setCategory(tx.category);
                setAmount(tx.amount);
                setDescription(tx.description || "");
            } catch (err) {
                console.error("LOAD ERROR:", err.response?.data || err.message);
                alert("Transaction yüklenirken hata oluştu ❌");
                navigate("/transactions");
            } finally {
                setLoading(false);
            }
        };

        load();
    }, [id, navigate]);

    const save = async () => {
        try {
            const payload = {
                type,
                category,
                amount: Number(amount),
                description,
            };

            await axios.put(`/api/transactions/${id}`, payload);

            alert("Transaction güncellendi ✅");
            navigate("/transactions");
        } catch (err) {
            console.error("UPDATE ERROR:", err.response?.data || err.message);
            alert("Güncelleme sırasında hata oluştu ❌");
        }
    };

    if (loading) return <p>Loading...</p>;

    return (
        <PageLayout>
            <div style={{ maxWidth: 450, display: "flex", flexDirection: "column", gap: 12 }}>
                <h2>Edit Transaction #{id}</h2>

                <div style={grid}>
                    <select value={type} onChange={(e) => setType(e.target.value)}>
                        <option value="EXPENSE">EXPENSE</option>
                        <option value="INCOME">INCOME</option>
                    </select>

                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                    >
                        <option value="MARKET">MARKET</option>
                        <option value="RENT">RENT</option>
                        <option value="SALARY">SALARY</option>
                        <option value="BILL">BILL</option>
                        <option value="ENTERTAINMENT">ENTERTAINMENT</option>
                        <option value="SUBSCRIPTION">SUBSCRIPTION</option>
                        <option value="OTHER">OTHER</option>
                    </select>
                </div>

                <input
                    placeholder="Amount"
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                />
                <input
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <div style={{ marginTop: 8 }}>
                    <button onClick={save} style={{ marginRight: 8 }}>
                        Save
                    </button>
                    <button onClick={() => navigate("/transactions")}>Cancel</button>
                </div>
            </div>
        </PageLayout>
    );
}

const grid = {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 12,
};
