export default function CreateSurvey(){
    return(
        <div className="content-default-1">
            <div className="createSurvey-container">
                <div className="createSurvey-inbox">
                    <div className="create-survey">
                        <input type="text" placeholder="Yeni anket adını yazınız." />
                        <input type="text" placeholder="Konu başlığını yazınız." />
                            {/*<div className="addQuestionAndRight">
                            
                                <span className="name">Soru Ekle/Düzenle</span> <br />
                                <img src={arrowCircleRight} alt="arrowCircleRight" />
                            </div>*/}
                        <button>VAZGEÇ</button>   
                    </div>
                </div>
            </div>
        </div>
    );
}