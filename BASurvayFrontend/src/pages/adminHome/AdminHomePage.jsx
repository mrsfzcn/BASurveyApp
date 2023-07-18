// AdminHome.js
import React, { useState } from "react";
import Layout from "../../components/Layout";
import Dropdown from "../../components/Dropdown";

function AdminHome() {
  const [name, setName] = useState("");
  const [selection, setSelection] = useState(null);

  const handleChange = (e) => {
    setName(e.target.value);
  };

  const options = [
    { label: "Red", value: "red" },
    { label: "Green", value: "green" },
    { label: "Blue", value: "blue" },
  ];

  const onChange = (option) => {
    setSelection(option);
  };

  return (
    <Layout>
      <div className="flex">
        <Dropdown value={selection} onChange={onChange} options={options} />
        <Dropdown value={selection} onChange={onChange} options={options} />
      </div>
    </Layout>
  );
}

export default AdminHome;
