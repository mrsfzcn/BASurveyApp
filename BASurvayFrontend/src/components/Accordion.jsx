// Accordion.js
import React, { useState } from "react";
import { AiOutlinePlus, AiOutlineMinus } from "react-icons/ai";

function Accordion({ items }) {
  const [expandedIndex, setExpandedIndex] = useState(-1);

  const handleClick = (index) => {
    setExpandedIndex((prevIndex) => (prevIndex === index ? -1 : index));
  };

  const rendered = items.map((item, index) => {
    const isExpanded = index === expandedIndex;
    const content = isExpanded && (
      <ul className="flex flex-col border-black bg-secondColor rounded-sm px-2 py-1">
        {item.content.map((contentItem) => (
          <li className="mt-2 mb-1 text-base" key={contentItem.href}>
            <a href={contentItem.href}>{contentItem.name}</a>
          </li>
        ))}
      </ul>
    );

    const icon = (
      <span>{isExpanded ? <AiOutlineMinus /> : <AiOutlinePlus />}</span>
    );

    return (
      <div key={index}>
        <div
          className="flex items-center justify-between gap-2  px-2 py-2 bg-secondColor text-gray-900 font-semibold rounded-sm"
          onClick={() => handleClick(index)}
        >
          {item.label}
          {icon}
        </div>
        {content}
      </div>
    );
  });

  return <div className="flex flex-col gap-6 mt-10">{rendered}</div>;
}

export default Accordion;
