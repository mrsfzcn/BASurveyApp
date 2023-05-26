import PagingSelector from "./pagingSelector";
import SearchBar from "./searchBar";
import Pagination from "./pagination";

import EditIcon from "../assets/styles/icons/EditIcon";
import DeleteIcon from "../assets/styles/icons/DeleteIcon";
import AssignIcon from "../assets/styles/icons/AssignIcon";

import React, { useState } from "react";

export default function AllTrainers(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Eğitmenler</p>
                <p>Anasayfa &gt; Sınıf İşlemleri &gt; Tüm Eğitmenler</p>
            </section>
            <div className="allTrainers-container">
                <div className="paging-searching">
                    <PagingSelector />
                    <SearchBar />
                </div>
                <div className="allTrainer-table-div">
                    <table className="trainer-table">
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