import logo from "../assets/images/logo.png";
import Accordion from "./Accordion";

function Sidebar() {
  const items = [
    {
      label: "Kullanıcı İşlemleri",
      content: ["Tüm Kullanıcılar", "Kullanıcı Ekle", "Kullanıcı Düzenle"],
    },
    {
      label: "Anket İşlemleri",
      content: ["Tüm Anketler", "Anket Ekle", "Anket Düzenle"],
    },
    {
      label: "Soru İşlemleri",
      content: [
        "Soruları Listele",
        "Soru Ekle",
        "Soru Etiketi İşlemleri",
        "Soru Tipi İşlemleri",
      ],
    },
    {
      label: "Sınıf İşlemleri",
      content: [
        "Öğrenci Listleme",
        "Eğitmen Listeleme",
        "Sınıf Etiketi Oluşturma",
        "Sınıfa Eğitmen Atama",
        "Sınıfa Öğrenci Atama",
      ],
    },
    {
      label: "Raporlama",
      content: [
        "Sonuçlanmış Anketler",
        "Sınıfa Göre Anket Sonuçları",
        "Kişiye Göre Anket Sonuçları",
      ],
    },
  ];

  const cssForAccordion = [
    {
      deneme:
        "flex items-center justify-between gap-2 border px-3 py-1.5 bg-secondColor text-gray-900 font-semibold rounded-lg ",
    },
    { content: "flex flex-col border bg-secondColor rounded-md px-2 py-1" },
  ];

  return (
    <div className="flex-1 bg-firstColor p-4 flex flex-col gap-16 ">
      <div>
        <img src={logo} alt="logo" />
        <hr />
      </div>

      <Accordion items={items} css={cssForAccordion} />
    </div>
  );
}

export default Sidebar;
