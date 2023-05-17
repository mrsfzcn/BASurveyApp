export default function AssignStudentActive() {
    return (
      <div className="content-default-1">
        <div className="assignStudentActive-container">
            <div className="assignStudentActive-inbox">
                <div className="assign-studentActive">
                    <input type="text" placeholder="ali.veli@bilgeadamboost.com" />
                    <button>BUL</button>
                </div>
                <div className="assignStudent-table-div">
                    <table className="assignStudent-table">
                        <tbody>
                            <tr>
                                <td>Adı</td>
                            </tr>
                            <tr>
                                <td>Soyadı</td>
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
  