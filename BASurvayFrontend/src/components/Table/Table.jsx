import React, { useState } from "react";
import { BsFillTrashFill, BsFillPencilFill } from "react-icons/bs";
import Dropdown from "../../components/Dropdown";
import "./Table.css";
import Input from "../Input";


///////  Tablo başlığında ön taraftan props olarak gelen useLabel false şekilde geldiğinde tablo baslıgı sılınırken satırlar silinmiyor düzenlenecek.



const Table = ({ data, header, useIcon, useLabel,deleteTableRows}) => {
  const [selectedCount, setSelectedCount] = useState(null);

  const renderedRow = header.map((item, index) => {
    return (
      <th className="px-6 py-3" key={index}>
        {item}
      </th>
    );
  });

  const handleRowClick = (value) => {
    setSelectedCount(value);
  };




  return (
    <>
    <div className="flex flex-col" >

     <div  className="first-column">
     <div className="filter-wrapper ">
        <label>Göster: </label>
        <select
          value={selectedCount || ""}
          onChange={(e) => setSelectedCount(e.target.value)}
        >
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="50">50</option>
          <option value="100">100</option>
        </select>
        <label>Satır</label>
      </div>
      <div>
        <label>Ara :</label>
        <Input/>
      </div>
     </div>
    
      <div className="table-wrapper mt-5">
        <div className="table-container">
          <table className="table">
            <thead>
              <tr>
                {renderedRow}
                {useLabel && <th>Anket Etiketleri</th>}
                {useIcon && <th>işlemler</th>}
              </tr>
            </thead>
            <tbody>
              {data
                .slice(0, selectedCount ? parseInt(selectedCount) : data.length)
                .map((datax, index) => {
                  return (
                    <tr key={index} onClick={() => handleRowClick(datax.value)}>
                      {Object.keys(datax).map((a, i) => (
                        <td key={i}>{datax[a]}</td>
                      ))}
                      {useIcon && (
                        <td>
                          <span className="actions">
                            <button >
                              <BsFillPencilFill className="edit-btn" />
                            </button>
                            <button onClick={()=>(deleteTableRows(index))} >
                              <BsFillTrashFill className="delete-btn" />
                            </button>
                          </span>
                        </td>
                      )}
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
    </>
  );
};

export default Table;
