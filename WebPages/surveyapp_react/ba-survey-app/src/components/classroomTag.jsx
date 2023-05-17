import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";
import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";

import React, { useState } from "react";


export default function ClassroomTag() {
  const [inputValue, setInputValue] = useState("");

  const handleInputChange = (e) => {
    const text = e.target.value.toUpperCase();
    setInputValue(text);
  };

  return (
    <div className="content-default-1">
      <section className="content-compass">
        <p>Sınıf Etiketi İşlemleri</p>
        <p>Anasayfa &gt; Sınıf İşlemleri &gt; Sınıf Etiketi İşlemleri</p>
      </section>
      <div className="tag-container">
        <div className="paging-searching">
          <PagingSelector />
          <SearchBar />
        </div>
        <div className="add-tag">
          <input
            type="text"
            placeholder="Sınıf Etiketi adı giriniz."
            value={inputValue}
            onChange={handleInputChange}
          />
          <button>EKLE</button>
        </div>
        <div className="table-div">
          <table className="tag-table">
            <thead>
              <tr>
                <th>No</th>
                <th>Sınıf Etiketi Adı</th>
                <th>İşlem</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>ETİKET 1</td>
                <td>
                  <button>
                    <EditIcon />
                  </button>
                  <button>
                    <DeleteIcon />
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div className="pagination-container">
          <Pagination />
        </div>
      </div>
    </div>
  );
}
