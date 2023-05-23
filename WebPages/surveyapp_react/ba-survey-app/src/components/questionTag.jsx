import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";

import React, { useState, useEffect } from "react";

export default function QuestionTag(props) {
  const { token } = props;
  const [tagList, setTagList] = useState([]);

  useEffect(() => {
    const fetchQuestionTags = async () => {
      try {
        const response = await fetch("http://localhost:8090/api/v1/questiontag/findall", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        });
        if (response.ok) {
          const data = await response.json();
          setTagList(data);
        } else {
          throw new Error("Failed to fetch question tags");
        }
      } catch (error) {
        console.error(error);
        alert("Failed to fetch question tags");
      }
    };

    fetchQuestionTags();
  }, [token]);

  const [inputValue, setInputValue] = useState("");

  const handleInputChange = (e) => {
    const text = e.target.value.toUpperCase();
    setInputValue(text);
  };

  return (
      <div className="content-default-1">
        <section className="content-compass">
          <p>Soru Etiketi İşlemleri</p>
          <p>Anasayfa &gt; Soru İşlemleri &gt; Soru Etiketi İşlemleri</p>
        </section>
        <div className="tag-container">
          <div className="paging-searching">
            <PagingSelector />
            <SearchBar />
          </div>
          <div className="add-tag">
            <input
                type="text"
                placeholder="Soru Etiketi adı giriniz."
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
                <th>Soru Etiketi Adı</th>
                <th>İşlem</th>
              </tr>
              </thead>
              <tbody>
              {tagList.map((tag, index) => (
                  <tr key={tag.tagStringId}>
                    <td>{index + 1}</td>
                    <td>{tag.tagString}</td>
                    <td>
                      <button>
                        <EditIcon />
                      </button>
                      <button>
                        <DeleteIcon />
                      </button>
                    </td>
                  </tr>
              ))}
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
