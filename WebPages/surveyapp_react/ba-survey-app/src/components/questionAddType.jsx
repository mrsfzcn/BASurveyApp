import React from "react";

export default function QuestionType({ value, onChange }) {
    return (
        <div className="question-type">
            <label htmlFor="question-type">Soru Tipi:</label>
            <select id="question-type" value={value} onChange={onChange}>
                <option value="">Seçiniz</option>
                <option value="çoktan-seçmeli">Çoktan Seçmeli</option>
                <option value="açık-uçlu">Açık Uçlu</option>
                <option value="likert-ölçekli">Likert Ölçekli</option>
                <option value="sıralama">Sıralama</option>
            </select>
        </div>
    );
}