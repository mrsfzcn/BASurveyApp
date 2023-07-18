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
      <ul className="flex flex-col border bg-secondColor rounded-md px-2 py-1">
        {item.content.map((contentItem) => (
          <li className="mt-2 mb-1 text-sm" key={contentItem.href}>
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
          className="flex items-center justify-between gap-2 border px-3 py-1.5 bg-secondColor text-gray-900 font-semibold rounded-lg"
          onClick={() => handleClick(index)}
        >
          {item.label}
          {icon}
        </div>
        {content}
      </div>
    );
  });

  return <div className="flex flex-col gap-4">{rendered}</div>;
}

export default Accordion;
