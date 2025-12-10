// src/layout/PageLayout.jsx
export default function PageLayout({ children }) {
    return (
        <div style={styles.wrapper}>
            <div style={styles.container}>{children}</div>
        </div>
    );
}

const NAVBAR_HEIGHT = 60;

const styles = {
    wrapper: {
        minHeight: "100vh",
        width: "100%",
        background: "linear-gradient(135deg, #0f2027, #203a43, #2c5364)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        paddingTop: NAVBAR_HEIGHT + 20, // navbar sabit olduğu için biraz boşluk
        boxSizing: "border-box",
    },
    container: {
        width: "100%",
        maxWidth: 1100,
        background: "rgba(0,0,0,0.1)",
        borderRadius: 18,
        padding: 32,
        boxShadow: "0 18px 50px rgba(0,0,0,0.45)",
        backdropFilter: "blur(8px)",
        color: "#fff",
    },
};
