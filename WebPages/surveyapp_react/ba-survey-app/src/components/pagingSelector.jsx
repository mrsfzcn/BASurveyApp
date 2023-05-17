export default function PagingSelector() {
  
  return (
    <div className="paging-selector">
      Göster
      <select >
        <option value={16}>16</option>
        <option value={32}>32</option>
        <option value={48}>48</option>
      </select>
      satır
    </div>
  );
}
