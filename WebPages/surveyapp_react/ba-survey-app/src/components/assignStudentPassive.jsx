export default function AssignStudentPassive(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Öğrenci Atama</p>
                <p>Anasayfa &gt; Sınıf İşlemleri &gt; Sınıfa Öğrenci Atama</p>
            </section>
            <div className="assignStudentPassive-container">
                <div className="assignStudentPassive-inbox">
                    <div className="assign-studentPassive">
                        <input type="text" placeholder="Öğrenci e-postasını giriniz." />
                        <button>BUL</button>
                    </div>
                </div>
            </div>
        </div>
    );
}