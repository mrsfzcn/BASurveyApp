export default function SendSurveyActive() {
    return (
        <div className="content-default-1">
            <div className="sendSurveyActive-container">
                <div className="sendSurveyActive-inbox">
                    <div className="send-surveyActive">
                        <input type="text" placeholder="UI / UX Eğitimi Değerlendirme Anketi" />
                        <button>BUL</button>
                    </div>
                    <div className="sendSurvey-table-div">
                        <table className="sendSurvey-table">
                            <tbody>
                            <tr>
                                <td>Anket Adı</td>
                            </tr>
                            <tr>
                                <td>Konu Başlığı</td>
                            </tr>
                            <tr>
                                <td>Atanacağı Sınıf</td>
                            </tr>
                            </tbody>
                        </table>
                        <div className="btn-sendSurvey-section">
                            <button>ATAMA YAP</button>
                            <button>VAZGEÇ</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
