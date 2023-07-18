import { useState } from "react";
import { AiOutlineUser } from "react-icons/ai";
function HeaderAccordion({ items }) {
  const [isOpen, setIsOpen] = useState(false);

  const handleClick = () => {
    setIsOpen(!isOpen);
  };

  const handleSelect = (index) => {
    setIsOpen(false);
    console.log(index);
  };

  const render = items.map((item, index) => {
    return (
      <li
        className="flex items-center gap-4 xldektop:gap-8 mb-3  "
        key={index}
        onClick={() => handleSelect(index)}
      >
        <div>{item.icon}</div>

        <div className="text-start">
          <a className="text-start font-semibold" href="/login">
            {item.label}
          </a>
        </div>
      </li>
    );
  });

  return (
    <div className="relative">
      <div
        className="flex items-center justify-center xldektop:w-14 xldektop:h-14 w-10 h-10 border rounded-full bg-secondColor text-gray-900 font-semibold cursor-pointer"
        onClick={handleClick}
      >
        <AiOutlineUser size={24} />
      </div>
      {isOpen && (
        <div className="absolute mt-3 right-0 w-44 z-10 xldektop:w-56">
          <ul className="flex flex-col border bg-secondColor rounded-md px-4 pt-3 pb-1">
            {render}
          </ul>
        </div>
      )}
    </div>
  );
}
export default HeaderAccordion;
