import ArrowCircleIcon from "../assets/styles/icons/ArrowCircleIcon";

export default function CreateSurvey(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Anket Oluşturma</p>
                <p>Anasayfa &gt; Anket İşlemleri &gt; Anket Oluşturma</p>
            </section>
            <div className="createSurvey-container">
                <div className="createSurvey-inbox">
                    <div className="create-survey">
                        <input type="text" placeholder="Yeni anket adını yazınız." />
                        <input type="text" placeholder="Konu başlığını yazınız." />
                        <div className="arrow-circle">
                            <span className="addQuestion">Soru Ekle/Düzenle <ArrowCircleIcon/></span> <br />
                        </div>
                        <button>VAZGEÇ</button>
                    </div>
                </div>
            </div>
        </div>
    );
}