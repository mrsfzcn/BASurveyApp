import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";

import React, { useState } from "react";

export default function AssignSurvey() {
    const [inputValue, setInputValue] = useState("");

    const handleInputChange = (e) => {
        const text = e.target.value.toUpperCase();
        setInputValue(text);
    };

    return (
        <div className="content-default-1">
            <section className="content-compass">
                <p>Sınıf İşlemleri</p>
                <p>Anasayfa &gt; Sınıf İşlemleri &gt; Sınıfa Atama Yap</p>
            </section>
            <div className="tag-container">
                <div className="add-tag">
                    <input
                        type="text"
                        placeholder="UI / UX ve Dijital Tasarım Değerlendirme Anketi"
                        value={inputValue}
                        onChange={handleInputChange}
                    />
                    <button>BUL</button>
                </div>

                <div className="assignSurvey">
                    <div className="surveyDetails">
                        <div className="left">
                            Anket Adı <br />  <br />
                            Konu Başlığı <br />  <br />
                            Atanacağı Sınıf
                        </div>
                        <div className="right">
                            :   UI / UX ve Dijital Tasarım Değerlendirme Anketi <br />  <br />
                            :   UI / UX <br />  <br />
                            <select name="" id="">
                                <option value="">Java 4</option>
                                <option value="">Java 3</option>
                                <option value="">Java 2</option>
                                <option value="" selected>Java 1</option>
                            </select>
                        </div>

                    </div>

                    <div className="surveyButtons">
                        <button className="btnAtamaYap">ATAMA YAP</button>
                        <button className="btnVazgec">VAZGEÇ</button>
                    </div>
                </div>

            </div>
        </div>
    );
}