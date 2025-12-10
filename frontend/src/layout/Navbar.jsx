import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.clear();
        navigate("/");
    };

    return (
        <div style={styles.bar}>
            <div style={styles.left}>
                <span style={styles.dot} />
                <span>FinTrack</span>
            </div>

            <div style={styles.center}>
                <Link to="/dashboard" style={styles.link}>Dashboard</Link>
                <Link to="/transactions" style={styles.link}>Transactions</Link>
                <Link to="/transactions/create" style={styles.link}>New Transaction</Link>
            </div>

            <button onClick={logout} style={styles.logout}>
                Logout
            </button>
        </div>
    );
}

const styles = {
    bar: {
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        height: 56,
        background: "#020615",
        color: "#fff",
        display: "flex",
        alignItems: "center",
        padding: "0 24px",
        zIndex: 1000,
    },
    left: {
        display: "flex",
        alignItems: "center",
        gap: 8,
        fontSize: 18,
        fontWeight: 700,
    },
    dot: {
        width: 8,
        height: 8,
        borderRadius: "50%",
        background: "#00e0b8",
    },
    center: {
        display: "flex",
        gap: 24,
        marginLeft: 40,   // FinTrack ile Dashboard arasına boşluk
    },
    link: {
        color: "#fff",
        textDecoration: "none",
        fontWeight: 500,
        fontSize: 14,
    },
    logout: {
        marginLeft: "auto",   // sağa yapıştır
        background: "#ffffff",
        color: "#020615",
        border: "none",
        borderRadius: 999,    // küçük pill buton
        padding: "6px 18px",
        cursor: "pointer",
        fontWeight: 600,
        fontSize: 13,
        width: "auto",        // genişlemesin
        minWidth: "unset",
    },
};
