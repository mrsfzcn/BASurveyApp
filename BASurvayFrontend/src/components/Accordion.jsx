import { useState } from "react";
import { AiOutlinePlus } from "react-icons/ai";
import { AiOutlineMinus } from "react-icons/ai";

function Accordion({ items, css }) {
  const [expendedIndex, setExpendedIndex] = useState(-1);

  const handleClick = (index) => {
    setExpendedIndex((now) => (now == index ? -1 : index));
  };

  const rendered = items.map((item, index) => {
    const isExpanded = index === expendedIndex;
    const content = isExpanded && (
      <ul className="flex flex-col border bg-secondColor rounded-md px-2 py-1">
        {item.content.map((contentItem, contentIndex) => (
          <li
            className={` mt-2 mb-1 ${
              contentIndex !== item.content.length - 1
                ? "border-b border-slate-950 text-sm"
                : "text-sm"
            }`}
            key={contentItem}
          >
            {contentItem}
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
          className="flex items-center justify-between gap-2 border px-3 py-1.5 bg-secondColor text-gray-900 font-semibold rounded-lg "
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
