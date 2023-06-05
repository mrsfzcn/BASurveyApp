export default function AddUser(){
    return(
        <div className="content-default-1">
            <section className="content-compass">
                <p>Kullanıcı ekle</p>
                <p>Anasayfa &gt; Kullanıcı İşlemleri &gt; Kullanıcı Ekle</p>
            </section>
            <div className="addUser-container">
                <div className="addUser-inbox">
                    <div className="add-user">
                        <span className="name">Adı</span> <br />
                        <input type="text" placeholder="Kullanıcı adı giriniz." />
                        <span className="lastname">Soyadı</span> <br />
                        <input type="text" placeholder="Kullanıcı soyadı giriniz." />
                        <span className="e-mail">E-Posta</span> <br />
                        <input type="text" placeholder="Kullanıcı e-posta giriniz." />
                        <span className="role">Kullanıcı Rolü</span> <br />
                        <input type="text" placeholder="Kullanıcı rolü giriniz." />
                        <div className="btn-addUser-section">
                            <button>KAYDET</button>
                            <button>VAZGEÇ</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}