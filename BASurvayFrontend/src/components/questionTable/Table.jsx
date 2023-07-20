import React, { useEffect, useState } from 'react';
import { BsFillTrashFill, BsFillPencilFill } from "react-icons/bs";
import './table.css';
import Input from "../Input";

function Table({ data, header, useIcon, extraColumn, useLabel }) {
  const [selectedCount, setSelectedCount] = useState(null);

  useEffect(() => {
    const tableContainer = document.querySelector('.table-container');
    const dataTable = document.querySelector('.data-table');
    const prevPageBtn = document.querySelector('.prev-page');
    const nextPageBtn = document.querySelector('.next-page');
    const searchInput = document.querySelector('.search-input');

    let currentPage = 1;
    const rowsPerPage = 10;
    const totalRows = data.length;

    function showData(page) {
      const startIndex = (page - 1) * rowsPerPage;
      const endIndex = startIndex + rowsPerPage;
      const data = Array.from(dataTable.getElementsByTagName('tr')).slice(1);

      data.forEach((row, index) => {
        if (index >= startIndex && index < endIndex) {
          row.style.display = 'table-row';
        } else {
          row.style.display = 'none';
        }
      });
    }

    function updatePageNumber() {
      const pageNumber = document.querySelector('.page-number');
      pageNumber.textContent = `Sayfa ${currentPage}`;
    }

    function showPrevPage() {
      if (currentPage > 1) {
        currentPage--;
        showData(currentPage);
        updatePageNumber();
      }
    }

    function showNextPage() {
      const maxPage = Math.ceil(totalRows / rowsPerPage);
      if (currentPage < maxPage) {
        currentPage++;
        showData(currentPage);
        updatePageNumber();
      }
    }

    function filterData() {
      const filterValue = searchInput.value.toLowerCase();
      const data = Array.from(dataTable.getElementsByTagName('tr')).slice(1);

      data.forEach((row) => {
        const rowData = Array.from(row.getElementsByTagName('td')).map((cell) => cell.textContent.toLowerCase());
        if (rowData.some((value) => value.includes(filterValue))) {
          row.style.display = 'table-row';
        } else {
          row.style.display = 'none';
        }
      });
    }

    prevPageBtn.addEventListener('click', showPrevPage);
    nextPageBtn.addEventListener('click', showNextPage);
    searchInput.addEventListener('input', filterData);

    showData(currentPage);
    updatePageNumber();
  }, []);

  const renderedRow = header.map((item, index) => {
    return (
      <th className="px-6 py-3" key={index}>
        {item}
      </th>
    );
  });

  const handleRowClick = (value) => {
    setSelectedCount(value);
    console.log(value);
  };


  return (
    <>
    <div className="flex flex-col">
    <div className="flex justify-evenly">
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
        <Input className="search-input"/>
      </div>
     </div>
     <div className='table-wrapper mt-5'>
      <div className="table-container">
        <div className="table-header">
          <span className="page-number">Sayfa 1</span>
        </div>
        <table className="data-table">
          <thead>
            <tr>
              {renderedRow}
              {useLabel && <th></th>}
              {useIcon && <th></th>}
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
                            <button>
                              <BsFillPencilFill className="edit-btn" />
                            </button>
                            <button>
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
        <div className="pagination">
          <button className="prev-page">Önceki Sayfa</button>
        </div>
        <div className="pagination">
          <button className="next-page">Sonraki Sayfa</button>
        </div>
      </div>
    </div>
    </div>
    </>
  );
}

export default Table;
