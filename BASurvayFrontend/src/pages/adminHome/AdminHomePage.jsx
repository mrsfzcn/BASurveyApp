import { useState } from "react";
import Input from "../../components/Input";
import Sidebar from "../../components/Sidebar";
import Table from "../../components/Table";
function AdminHome() {
  const [name, setName] = useState("");

  const handleChange = (e) => {
    setName(e.target.value);
  };

  return (
    <div className="flex min-h-screen ">
      <Sidebar />

      <div className="flex-[8_8_0%]">
        <div className="flex flex-col">
          <Input
            type="text"
            placeholder="Adı"
            value={name}
            onChange={handleChange}
            className="w-3/5"
          />
          <Input
            type="text"
            placeholder="Adı"
            value={name}
            onChange={handleChange}
            half
          />
          <Input
            type="text"
            placeholder="Adı"
            value={name}
            onChange={handleChange}
            half
            required
          />
        </div>
      </div>
    </div>
  );
}

export default AdminHome;
