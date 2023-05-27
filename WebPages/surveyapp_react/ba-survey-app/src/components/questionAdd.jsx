import React, { useState } from "react";
import QuestionType from "./QuestionAddType";
import QuestionSelected from "./questionAddSelected";

export default function QuestionsAdd() {
    const [selectedTags, setSelectedTags] = useState([]);
    const [tagInput, setTagInput] = useState("");
    const [questionText, setQuestionText] = useState("");
    const [questionType, setQuestionType] = useState("");

    const handleTagSelect = (tag) => {
        if (selectedTags.includes(tag)) {
            setSelectedTags(selectedTags.filter((selectedTag) => selectedTag !== tag));
        } else {
            setSelectedTags([...selectedTags, tag]);
        }
    };

    const handleTagInput = (e) => {
        setTagInput(e.target.value);
    };

    const handleAddTag = () => {
        if (tagInput.trim() !== "") {
            setSelectedTags([...selectedTags, tagInput]);
            setTagInput("");
        }
    };

    const handleRemoveTag = (tag) => {
        setSelectedTags(selectedTags.filter((selectedTag) => selectedTag !== tag));
    };

    const handleQuestionTextChange = (e) => {
        setQuestionText(e.target.value);
    };

    const handleQuestionTypeChange = (e) => {
        setQuestionType(e.target.value);
    };

    const isFormValid = () => {
        return (
            questionText.trim() !== "" &&
            questionType !== "" &&
            selectedTags.length !== 0
        );
    };

    return (
        <div className="content-default-1">
            <section className="content-compass">
                <p>Soru Ekle İşlemleri</p>
                <p>Anasayfa &gt; Soru İşlemleri &gt; Soru Ekle</p>
            </section>
            <div className="tag-container">
                <div className="add-question">
                    <section className="content-com">
                        <p>Soru metnini giriniz:</p>
                    </section>
                    <input
                        type="text"
                        id="question-text"
                        value={questionText}
                        onChange={handleQuestionTextChange}
                        className="question-text-area"
                    />
                </div>
                <QuestionType
                    value={questionType}
                    onChange={handleQuestionTypeChange}
                />
                <div className="button-container">
                    <button
                        className={`button ${isFormValid() ? "active-button" : "inactive-button"}`}
                        disabled={!isFormValid()}
                        style={{
                            backgroundColor: isFormValid() ? "rgba(100, 233, 177, 1)" : "",
                        }}
                    >
                        EKLE
                    </button>
                </div>
                <QuestionSelected
                    selectedTags={selectedTags}
                    tagInput={tagInput}
                    handleTagInput={handleTagInput}
                    handleAddTag={handleAddTag}
                    handleRemoveTag={handleRemoveTag}
                />
            </div>
        </div>
    );
}

