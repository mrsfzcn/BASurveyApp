import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";

import React, { useState } from "react";

export default function ListSurveys() {
    const [inputValue, setInputValue] = useState("");
    const handleTagClick = (e) => {
        const speechBubble = document.getElementById("speech-bubble");
        speechBubble.style.display = (speechBubble.style.display === 'none') ? 'block' : 'none';
    };
    const handleInputChange = (e) => {
        const text = e.target.value.toUpperCase();
        setInputValue(text);
    };

    return (
        <div className="content-default-1">
            <section className="content-compass">
                <p>Anket Etiketi İşlemleri</p>
                <p>Anasayfa &gt; Anket İşlemleri &gt; Anketleri Listele</p>
            </section>
            <div className="tag-container">
                <div className="paging-searching">
                    <PagingSelector />
                    <SearchBar />
                </div>

                <div className="table-div">
                    <table className="survey-table">
                        <thead>
                        <tr>
                            <th>No</th>
                            <th>Anket Adı</th>
                            <th>Anket Etiketleri</th>
                            <th> </th>
                            <th>Konu Başlığı</th>
                            <th>İşlem</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>Boost Değerlendirme Anketi - JAVA</td>
                            <td id="tag" onClick={handleTagClick}><i class="fa-solid fa-tags fa-2xl"></i></td>
                            <td><div id="speech-bubble"><span>HIBERNATE, REACT, SPRING</span></div></td>
                            <td>Java</td>
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
            <script>


            </script>
        </div>
    );
}