import HeaderAccordion from "./HeaderAccordion";
import { BiSolidUser } from "react-icons/bi";
import { FaExchangeAlt } from "react-icons/fa";
import { ImExit } from "react-icons/im";
import { FaUserCircle } from "react-icons/fa";
import Accordion from "../Accordion";
function Header() {
  const headerAccordion = [
    { label: "Profil Bilgileri", icon: <BiSolidUser /> },
    { label: "Rol Değiştir", icon: <FaExchangeAlt /> },
    { label: "Çıkış", icon: <ImExit /> },
  ];

  return (
    <div className=" h-28 xldektop:h-40 bg-firstColor flex items-center justify-between">
      <div className="text-white font-medium  text-3xl xldektop:text-4xl ml-35">
        Survey App
      </div>
      <div className="mr-10">
        <HeaderAccordion items={headerAccordion} />
      </div>
    </div>
  );
}
export default Header;
