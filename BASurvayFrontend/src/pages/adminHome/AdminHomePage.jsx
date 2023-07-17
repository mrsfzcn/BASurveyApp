import Sidebar from "../../components/Sidebar";
function AdminHome() {
  return (
    <div className="flex ">
      <Sidebar />
      <div className="flex-[8_8_0%] bg-black min-h-screen"></div>
    </div>
  );
}

export default AdminHome;
