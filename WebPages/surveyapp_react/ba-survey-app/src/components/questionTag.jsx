import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";
import CancelIcon from "../assets/styles/icons/CancelIcon";
import TickIcon from "../assets/styles/icons/TickIcon";

import React, { useState, useEffect } from "react";

export default function QuestionTag(props) {
  const { token } = props;
  const [tagList, setTagList] = useState([]);

  useEffect(() => {
    const fetchQuestionTags = async () => {
      try {
        const response = await fetch(
            "http://localhost:8090/api/v1/questiontag/findall",
            {
              method: "GET",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
              },
            }
        );
        if (response.ok) {
          const data = await response.json();
          const sortedData = data.reverse();
          setTagList(sortedData);
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

  const handleAddTag = async () => {
    try {
      const response = await fetch(
          "http://localhost:8090/api/v1/questiontag/create",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ tagString: inputValue }),
          }
      );
      if (response.ok) {
        const newTag = await response.json();
        setTagList([...tagList, newTag]);
        setInputValue("");
      } else {
        throw new Error("Failed to add question tag");
      }
    } catch (error) {
      console.error(error);
      alert("Failed to add question tag");
    }
  };

  const [editIndex, setEditIndex] = useState(-1); // Varsayılan olarak -1 (düzenleme yok)
  const [editValue, setEditValue] = useState(""); // Düzenleme modunda bulunan tag değeri

  const handleEditClick = (index, tagString) => {
    setEditIndex(index);
    setEditValue(tagString);
  };

  const handleSaveClick = async (tag) => {
    try {
      const index = tagList.findIndex((t) => t.tagStringId === tag.tagStringId);
      if (index === -1) {
        throw new Error("Tag not found in tagList");
      }
      const response = await fetch(
          `http://localhost:8090/api/v1/questiontag/updatebytagstring/${tag.tagString}`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ tagString: editValue }),
          }
      );
      if (response.ok) {
        const updatedTag = { ...tag, tagString: editValue };
        const updatedTagList = [...tagList];
        updatedTagList[index] = updatedTag;
        setTagList(updatedTagList);
        setEditIndex(-1); // Düzenleme modunu sonlandır
      } else {
        throw new Error("Failed to update question tag");
      }
    } catch (error) {
      console.error(error);
      alert("Failed to update question tag");
    }
  };

  const handleCancelClick = () => {
    // Düzenlemeyi iptal et ve edit modunu sonlandır
    setEditIndex(-1);
    setEditValue("");
  };

  const handleDeleteClick = (tag) => {
    const confirmation = window.confirm(
        `"${tag.tagString}" etiketini silmek istediğinizden emin misiniz?`
    );

    if (confirmation) {
      deleteTag(tag);
    }
  };

  const deleteTag = async (tag) => {
    try {
      const response = await fetch(
          `http://localhost:8090/api/v1/questiontag/delete/${tag.tagString}`,
          {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
      );
      if (response.ok) {
        const updatedTagList = tagList.filter(
            (t) => t.tagString !== tag.tagString
        );
        setTagList(updatedTagList);
      } else {
        throw new Error("Failed to delete question tag");
      }
    } catch (error) {
      console.error(error);
      alert("Failed to delete question tag");
    }
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
            <button onClick={handleAddTag}>EKLE</button>
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
                  <tr key={`${tag.tagStringId}-${index}`}>
                    <td>{index + 1}</td>
                    <td>
                      {editIndex === index ? (
                          <input
                              type="text"
                              value={editValue}
                              autoFocus
                              onChange={(e) => setEditValue(e.target.value)}
                          />
                      ) : (
                          tag.tagString
                      )}
                    </td>
                    <td>
                      {editIndex === index ? (
                          <>
                            <button onClick={() => handleSaveClick(tag)}>
                              <TickIcon />
                            </button>
                            <button onClick={handleCancelClick}>
                              <CancelIcon />
                            </button>
                          </>
                      ) : (
                          <button
                              onClick={() => handleEditClick(index, tag.tagString)}
                          >
                            <EditIcon />
                          </button>
                      )}
                      {editIndex !== index && (
                          <button onClick={() => handleDeleteClick(tag)}>
                            <DeleteIcon />
                          </button>
                      )}
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
