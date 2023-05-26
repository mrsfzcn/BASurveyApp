import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";
import AssignIcon from "../assets/styles/icons/AssignIcon";

import React, { useState } from "react";

export default function AllStudents(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Öğrenciler</p>
                <p>Anasayfa &gt; Sınıf İşlemleri &gt; Tüm Öğrenciler</p>
            </section>
            <div className="allStudents-container">
                <div className="paging-searching">
                    <PagingSelector />
                    <SearchBar />
                </div>
                <div className="allStudent-table-div">
                    <table className="student-table">
                        <thead>
                        <tr>
                            <th>Adı </th>
                            <th>Soyadı</th>
                            <th>E-Posta</th>
                            <th>Kullanıcı Rolü </th>
                            <th>Kayıt Tarihi</th>
                            <th>İşlem</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Ezgi</td>
                            <td>Akkaş</td>
                            <td>ezgi.akkas@bilgeadamboost.com</td>
                            <td>STUDENT</td>
                            <td>01.08.2022</td>
                            <td>
                                <button>
                                    <AssignIcon/>
                                </button>
                                <button>
                                    <EditIcon />
                                </button>
                                <button>
                                    <DeleteIcon />
                                </button>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>
                <div className="pagination-container">
                    <Pagination />
                </div>
            </div>
        </div>
    );
}