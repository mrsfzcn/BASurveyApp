import { useState, useEffect, useRef } from "react";
import Panel from "./dropdown/Panel";
import { GoChevronDown } from "react-icons/go";
function Dropdown({ options, value, onChange }) {
  const [isOpen, setIsOpen] = useState(false);
  const divEl = useRef();

  useEffect(() => {
    const handler = (event) => {
      if (!divEl.current) {
        return;
      }
      if (!divEl.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener("click", handler, true);

    return () => {
      document.removeEventListener("click", handler);
    };
  }, []);

  const handleClick = () => {
    setIsOpen(!isOpen);
  };

  const handleOptionClick = (option) => {
    setIsOpen(false);
    onChange(option);
  };
  const renderedOptions = options.map((option) => {
    return (
      <div
        className="hover:bg-secondColor rounded cursor-pointer p-1"
        onClick={() => handleOptionClick(option)}
        key={option.value}
      >
        {option.label}
      </div>
    );
  });

  return (
    <div ref={divEl} className="w-48 relative">
      <Panel
        className="flex justify-between items-center cursor-pointer font-semibold"
        onClick={handleClick}
      >
        {value?.label || "Seçim yapınız"}
        <GoChevronDown className="text-lg" />
      </Panel>
      {isOpen && <Panel className=" absolute top-full ">{renderedOptions}</Panel>}
    </div>
  );
}
export default Dropdown;
