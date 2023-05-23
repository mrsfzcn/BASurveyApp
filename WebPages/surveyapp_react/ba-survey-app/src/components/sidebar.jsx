import { useState } from "react";

export default function Sidebar({ onSubMenuClick, token }){

    const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
    const [isPollMenuOpen, setIsPollMenuOpen] = useState(false);
    const [isQuestionMenuOpen, setIsQuestionMenuOpen] = useState(false);
    const [isClassMenuOpen, setIsClassMenuOpen] = useState(false);
    const [isReportMenuOpen, setIsReportMenuOpen] = useState(false);

    const toggleUserMenu = () => {
        setIsUserMenuOpen(!isUserMenuOpen);
        setIsPollMenuOpen(false);
        setIsQuestionMenuOpen(false);
        setIsClassMenuOpen(false);
        setIsReportMenuOpen(false);
    };
    const togglePollMenu = () => {
        setIsPollMenuOpen(!isPollMenuOpen);
        setIsUserMenuOpen(false);
        setIsQuestionMenuOpen(false);
        setIsClassMenuOpen(false);
        setIsReportMenuOpen(false);
    };
    const toggleQuestionMenu = () => {
        setIsQuestionMenuOpen(!isQuestionMenuOpen);
        setIsUserMenuOpen(false);
        setIsPollMenuOpen(false);
        setIsClassMenuOpen(false);
        setIsReportMenuOpen(false);
    };
    const toggleClassMenu = () => {
        setIsClassMenuOpen(!isClassMenuOpen);
        setIsUserMenuOpen(false);
        setIsPollMenuOpen(false);
        setIsQuestionMenuOpen(false);
        setIsReportMenuOpen(false);
    };
    const toggleReportMenu = () => {
        setIsReportMenuOpen(!isReportMenuOpen);
        setIsUserMenuOpen(false);
        setIsPollMenuOpen(false);
        setIsQuestionMenuOpen(false);
        setIsClassMenuOpen(false);
    };

    const handleSubMenuClick = (submenu, token) => {
        onSubMenuClick(submenu, token);
        setIsUserMenuOpen(false);
        setIsPollMenuOpen(false);
        setIsQuestionMenuOpen(false);
        setIsClassMenuOpen(false);
        setIsReportMenuOpen(false);
    };

    return(
        <>
            <div className="sidebar">
                <div className="sb-btn-container">
                    <section className="sb-btn">
                        <button onClick={toggleUserMenu}>
                            <span>Kullanıcı İşlemleri</span>
                            <span>{isUserMenuOpen ? "-" : "+"}</span>
                        </button>
                        {isUserMenuOpen && (
                            <div className="sub-menu">
                                <button onClick={() => handleSubMenuClick("all-users")}>
                                    <span>Tüm Kullanıcılar</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("add-user")}>
                                    <span>Kullanıcı Ekle</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("edit-user")}>
                                    <span>Kullanıcı Düzenle</span>
                                </button>
                            </div>
                        )}
                        <button onClick={togglePollMenu}>
                            <span>Anket İşlemleri</span>
                            <span>{isPollMenuOpen ? "-" : "+"}</span>
                        </button>
                        {isPollMenuOpen && (
                            <div className="sub-menu">
                                <button onClick={() => handleSubMenuClick("all-surveys")}>
                                    <span>Tüm Anketler</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("create-survey")}>
                                    <span>Anket Oluştur</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("send-survey")}>
                                    <span>Anket Gönder</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("survey-tags", token)}>
                                    <span>Anket Etiketi İşlemleri</span>
                                </button>
                            </div>
                        )}
                        <button onClick={toggleQuestionMenu}>
                            <span>Soru İşlemleri</span>
                            <span>{isQuestionMenuOpen ? "-" : "+"}</span>
                        </button>
                        {isQuestionMenuOpen && (
                            <div className="sub-menu">
                                <button onClick={() => handleSubMenuClick("list-questions")}>
                                    <span>Soruları Listele</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("add-question")}>
                                    <span>Soru Ekle</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("question-tags", token)}>
                                    <span>Soru Etiketi İşlemleri</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("question-types")}>
                                    <span>Soru Tipi İşlemleri</span>
                                </button>
                            </div>
                        )}
                        <button onClick={toggleClassMenu}>
                            <span>Sınıf İşlemleri</span>
                            <span>{isClassMenuOpen ? "-" : "+"}</span>
                        </button>
                        {isClassMenuOpen && (
                            <div className="sub-menu">
                                <button onClick={() => handleSubMenuClick("all-students")}>
                                    <span>Tüm Öğrenciler</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("all-trainers")}>
                                    <span>Tüm Eğitmenler</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("classroom-tags", token)}>
                                    <span>Sınıf Etiketi İşlemleri</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("assign-classroom")}>
                                    <span>Sınıfa Atama Yap</span>
                                </button>
                            </div>
                        )}
                        <button onClick={toggleReportMenu}>
                            <span>Raporlama</span>
                            <span>{isReportMenuOpen ? "-" : "+"}</span>
                        </button>
                        {isReportMenuOpen && (
                            <div className="sub-menu">
                                <button onClick={() => handleSubMenuClick("reporting-1")}>
                                    <span>Raporlama 1</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("reporting-2")}>
                                    <span>Raporlama 2</span>
                                </button>
                                <button onClick={() => handleSubMenuClick("reporting-3")}>
                                    <span>Raporlama 3</span>
                                </button>
                            </div>
                        )}
                    </section>
                </div>
            </div>
        </>
    );
}