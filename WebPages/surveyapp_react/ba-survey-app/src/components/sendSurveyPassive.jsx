export default function SendSurveyPassive(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Anket Atama</p>
                <p>Anasayfa &gt; Anket İşlemleri &gt; Sınıfa Anket Atama</p>
            </section>
            <div className="sendSurveyPassive-container">
                <div className="sendSurveyPassive-inbox">
                    <div className="send-surveyPassive">
                        <input
                            type="text"
                            placeholder="Göndermek istediğiniz anket adını yazınız."

                        />
                        <button >BUL</button>

                    </div>
                </div>
            </div>
        </div>
    );
}