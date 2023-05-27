import React from "react";
import Add from "../assets/styles/icons/add.png";
import Remove from "../assets/styles/icons/remove.png";
import Tag from "../assets/styles/icons/tag.png";

export default function QuestionSelected({
                                             selectedTags,
                                             tagInput,
                                             handleTagInput,
                                             handleAddTag,
                                             handleRemoveTag
                                         }) {
    return (
        <div className="question-tag">
            <label htmlFor="question-tag">Soru Etiketi:</label>
            <input
                type="text"
                placeholder="Giriniz."
                value={tagInput}
                onChange={handleTagInput}
            />

            <button
                onClick={handleAddTag}
                disabled={tagInput.trim() === ""}
                className="addIcon-button"
            >
                <img src={Add} alt="Add" className="addIcon" />
            </button>

            <div className="selected-tags">
                {selectedTags.length > 0 && (
                    <ul>
                        {selectedTags.map((tag, index) => {
                            const normalizedTag = tag.toLowerCase();
                            const acceptedTags = [
                                "java",
                                "spring",
                                "hibernate",
                                "oop",
                                "react",
                                "sql",
                                "javascript",
                                "css",
                                "html",
                                "c#",
                                ".net",
                            ];
                            const isAcceptedTag = acceptedTags.some((acceptedTag) =>
                                normalizedTag.startsWith(acceptedTag)
                            );

                            if (isAcceptedTag && selectedTags.indexOf(tag) === index) {
                                return (
                                    <li key={index}>
                                        <img src={Tag} alt="Tag" className="tagIcon" />
                                        {tag}
                                        <button
                                            onClick={() => handleRemoveTag(tag)}
                                            className="remove-button"
                                        >
                                            <img
                                                src={Remove}
                                                alt="Remove"
                                                className="removeIcon"
                                            />
                                        </button>
                                    </li>
                                );
                            }
                            return null;
                        })}
                    </ul>
                )}
            </div>
        </div>
    );
}