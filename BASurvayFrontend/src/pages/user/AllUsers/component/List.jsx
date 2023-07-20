import React, { useState, useEffect } from "react";
import "./list.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import Layout from "../../../../components/Layout";

import EditIcon from "../svg/edit-svg.jsx";
import SortIcon from "../svg/sort-solid.jsx";
import DeleteIcon from "../svg/delete-svg";
import Input from "../../../../components/Input";

export default function List() {
  const [selectedCombo, setSelectedCombo] = useState(10);
  const [userList, setUserList] = useState([]);
  const [search, setSeach] = useState("");
  const [searchedList, setSearchedList] = useState([]);
  const token = localStorage.getItem("token");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemPerPage] = useState(10);
  const [paginationLength, setPaginationLength] = useState();
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const navigate = useNavigate();
  const paginate = (pageNumber) => setCurrentPage(pageNumber);
  const [sortName, setSortName] = useState();

  useEffect(() => {
    fetchData();
  }, []);

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
      setSearchedList(response.data);
      console.log(response.data);
      setPaginationLength(Math.ceil(searchedList.length / itemsPerPage));
    } catch (error) {
      console.error(error);
    }
  };

  const filterUsers = (userList, search, currentPage, itemsPerPage) => {
    const filteredList = userList.filter((item) =>
      item.firstName.toLowerCase().trim().includes(search.toLowerCase().trim())
    );
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredList.slice(indexOfFirstItem, indexOfLastItem);
    setSearchedList(currentItems);
    setPaginationLength(Math.ceil(filteredList.length / itemsPerPage));
  };

  useEffect(() => {
    filterUsers(userList, search, currentPage, itemsPerPage);
  }, [search, currentPage, itemsPerPage, userList]);

  const handleCombo = (event) => {
    setSelectedCombo(event.target.value);
    setItemPerPage(event.target.value);
  };

  const handleSearch = (e) => {
    setSeach(e.target.value);
  };

  const handleSortName = () => {
    const status = !sortName;
    setSortName(status);

    if (status) {
      const sortedList = userList.sort();
      console.log("Sort");
      console.log(sortName);
      setUserList(sortedList);
    } else {
      const reversedList = userList.reverse();
      setUserList(reversedList);
      console.log("reverse");
      console.log(sortName);
    }
  };

  const handleEditClick = async (oid) => {
    console.log(oid);
    localStorage.setItem("userId", oid);
    navigate("/kullanıcı-düzenleme"); //editleme url'i gelecek
  };

  const handleDeleteClick = async (oid) => {
    console.log(oid);
    await axios.delete(`http://localhost:8090/api/v1/user/delete/${oid}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    fetchData();
  };

  const currentItems = userList
    .slice(indexOfFirstItem, indexOfLastItem)
    .filter((item) =>
      item.firstName.toLowerCase().trim().includes(search.toLowerCase().trim())
    );

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
                      className="combo-box"
                      value={selectedCombo}
                      onChange={handleCombo}
                    >
                      <option value={2}>2</option>
                      <option value={5}>5</option>
                      <option value={10}>10</option>
                    </select>
                  </div>
                  <span> Satır</span>
                </div>
                <div className="listAra">
                  <span>Ara: </span>
                  <Input className="araButton" onChange={handleSearch} />
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
                        <button className="bottomSort" onClick={handleSortName}>
                          <SortIcon />
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
                        <td style={{ width: "13rem" }}>{user.firstName}</td>
                        <td style={{ width: "13rem" }}>{user.lastName}</td>
                        <td style={{ width: "13rem" }}>{user.email}</td>
                        <td style={{ width: "13rem" }}>
                          {user.authorizedRole}
                        </td>
                        <td style={{ width: "13rem" }}>{user.createdAt}</td>
                        <td style={{ width: "15rem" }}>
                          <button
                            className="editButton"
                            onClick={() => handleEditClick(user.oid)}
                          >
                            <EditIcon />
                          </button>
                          <button onClick={() => handleDeleteClick(user.oid)}>
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
                  <p style={{ marginBottom: "2rem" }}>
                    {userList.length} kullanıcıdan {currentItems.length}'sı
                    gösteriliryor.
                  </p>
                </div>
                <div>
                  {currentPage > 1 && (
                    <button onClick={() => paginate(currentPage - 1)}>
                      ÖNCEKİ
                    </button>
                  )}
                  {Array.from({
                    length: paginationLength,
                  }).map((_, index) =>
                    currentPage - 1 === index || currentPage === index ? (
                      <button
                        key={index}
                        className="paginationButton"
                        onClick={() => paginate(index + 1)}
                      >
                        {index + 1}
                      </button>
                    ) : null
                  )}

                  {currentPage < paginationLength && (
                    <button onClick={() => paginate(currentPage + 1)}>
                      SONRAKİ
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </Layout>
    </>
  );
}
