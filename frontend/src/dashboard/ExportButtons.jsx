export default function ExportButtons() {
  const downloadCsv = () => {
    window.open("http://localhost:8083/api/transactions/export/csv");
  };

  const downloadExcel = () => {
    window.open("http://localhost:8083/api/transactions/export/excel");
  };

  return (
    <div>
      <button onClick={downloadCsv}>⬇ CSV</button>
      <button onClick={downloadExcel}>⬇ Excel</button>
    </div>
  );
}