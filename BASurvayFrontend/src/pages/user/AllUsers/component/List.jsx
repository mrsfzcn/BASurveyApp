import React, { useState, useEffect } from "react";
import "./list.css";
import axios from "axios";
import Layout from "../../../../components/Layout";

import EditIcon from "../svg/edit-svg.jsx";
import SortIcon from "../svg/sort-solid.jsx";
import DeleteIcon from "../svg/delete-svg";

export default function List() {
  const [selectedCombo, setSelectedCombo] = useState(10);
  const [userList, setUserList] = useState([]);
  const [search, setSeach] = useState("");
  const token = localStorage.getItem("token");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemPerPage] = useState(10);
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = userList
    .slice(indexOfFirstItem, indexOfLastItem)
    .filter((item) =>
      item.firstName.toLowerCase().trim().includes(search.toLowerCase().trim())
    );
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8090/api/v1/user/find-all-user-details`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setUserList(response.data);
        console.log(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, []);

  const handleCombo = (event) => {
    setSelectedCombo(event.target.value);
    setItemPerPage(event.target.value);
  };

  const handleSearch = (e) => {
    setSeach(e.target.value);
  };

  const handleSortName = () => {
    let sortedList = currentItems.sort();
  };

  return (
    <>
      <Layout>
        <div className="background">
          <div>
            <div className="allUsersHeaderDiv">
              <div>
                <p className="allUsersHeader">Tüm Kullanıcılar </p>
              </div>
              <div className="allUsersHeader">
                <p>|</p>
              </div>
              <div>
                <p className="allUsersSubHeader">
                  Ana Sayfa &gt; Kullanıcı İşlemleri &gt; Tüm Kullanıcılar
                </p>
              </div>
            </div>
          </div>
          <div className="list">
            <div>
              <div className="flexColumnContainer">
                <div className="listPageShow">
                  <span>Göster: </span>
                  <div>
                    <select
                      id="combo-box"
                      value={selectedCombo}
                      onChange={handleCombo}
                    >
                      <option value={5}>5</option>
                      <option value={10}>10</option>
                    </select>
                  </div>
                  <span> Satır</span>
                </div>
                <div className="listAra">
                  <span>Ara: </span>
                  <input onChange={handleSearch} />
                </div>
              </div>
              <div
                style={{
                  justifyContent: "center",
                  alignItems: "center",
                  marginTop: "3rem",
                  height: "58vh",
                  marginLeft: "5%",
                }}
              >
                <table>
                  <thead>
                    <tr>
                      <th style={{ width: "13rem", paddingBottom: "2rem" }}>
                        <span>Adı</span>
                        <button className="bottomSort">
                          <SortIcon onClick={handleSortName} />
                        </button>
                      </th>
                      <th style={{ width: "13rem", paddingBottom: "2rem" }}>
                        <span>Soyadı</span>
                        <button className="bottomSort">
                          <SortIcon />
                        </button>
                      </th>
                      <th style={{ width: "15rem", paddingBottom: "2rem" }}>
                        <span>Eposta</span>
                        <button className="bottomSort">
                          <SortIcon />
                        </button>
                      </th>
                      <th style={{ width: "13rem", paddingBottom: "2rem" }}>
                        <span>Kullanıcı Rolü</span>
                        <button className="bottomSort">
                          <SortIcon />
                        </button>
                      </th>
                      <th style={{ width: "13rem", paddingBottom: "2rem" }}>
                        <span>Kayıt Tarihi</span>
                        <button className="bottomSort">
                          <SortIcon />
                        </button>
                      </th>
                      <th style={{ width: "15rem", paddingBottom: "2rem" }}>
                        <span>İşlem</span>
                      </th>
                    </tr>
                  </thead>
                  <tbody className="lineTableBody">
                    {currentItems.map((user, index) => (
                      <tr className="tableRow" key={index}>
                        <td>{user.firstName}</td>
                        <td>{user.lastName}</td>
                        <td>{user.email}</td>
                        <td>{user.authorizedRole}</td>
                        <td>{user.createdAt}</td>
                        <td>
                          <button className="editButton">
                            <EditIcon />
                          </button>
                          <button>
                            <DeleteIcon />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="footer">
                <div>
                  <p>
                    {userList.length} kullanıcıdan {currentItems.length}'sı
                    gösteriliryor.
                  </p>
                </div>
                <div>
                  <span>ÖNCEKİ</span>
                  {Array.from({
                    length: Math.ceil(userList.length / itemsPerPage),
                  }).map((_, index) => (
                    <button
                      key={index}
                      className="paginationButton"
                      onClick={() => paginate(index + 1)}
                    >
                      {index + 1}
                    </button>
                  ))}
                  <span>SONRAKİ</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Layout>
    </>
  );
}
