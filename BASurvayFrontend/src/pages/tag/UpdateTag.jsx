import React, { useState } from "react";
import Button from "../../components/Button";
import Input from "../../components/Input";
import Layout from "../../components/Layout";
import TagService from "../../services/TagService";
import Alert from "../../components/Alert";
import { useNavigate } from "react-router-dom";

const UpdateTag = () => {
  const navigate = useNavigate();
  const [updateTag, setUpdateTag] = useState({
    tagString: "Hibernate",
    newTagString: "",
  });

  const [alert, setAlert] = useState({ type: "", message: "" });
  const [loading, setLoading] = useState(false);

  //Input değerini değiştirirken kullanılan method
  const onChange = (e) => {
    setUpdateTag({ ...updateTag, newTagString: e.target.value });
  };

  //api'ye istek atılan method
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setAlert({ type: "", message: "" });

    try {
      if (!updateTag.newTagString) {
        setAlert({ type: "error", message: "Yeni Etiket boş olamaz." });
        setLoading(false);
        return;
      }

      const response = await TagService.updateTag(updateTag);

      if (response.status === 200) {
        setAlert({ type: "success", message: "Etiket başarıyla güncellendi." });
      } else {
        setAlert({
          type: "error",
          message: "Etiket güncelleme başarısız oldu.",
        });
      }
    } catch (error) {
      console.error("Error updating tag:", error);
      setAlert({
        type: "error",
        message: "Bir hata oluştu. Lütfen tekrar deneyin.",
      });
    }

    setLoading(false);
  };

  //vazgeç butonuna basarsa kullanılan method
  const navigateMain = (e) => {
    navigate("/etiket");
  };

  return (
    <Layout>
      <div className="flex flex-col bg-white h-full">
        <div className="p-5 text-start">Bitmesi bekleniyor</div>
        <form
          onSubmit={handleSubmit}
          className="class1 flex justify-center align-center"
        >
          <div className="class2 bg-[#F1F1F1] flex justify-center align-center m-auto ">
            <div className="class3 bg-[#FEFEFE] m-auto  flex flex-col justify-center gap-14 ">
              <div className="flex flex-col gap-14 justify-center items-center">
                <Input half disabled value={updateTag.tagString} />
                <Input
                  placeholder="Yeni Etiketi Giriniz"
                  half
                  required
                  onChange={onChange}
                  value={updateTag.newTagString}
                />
                <div className="w-1/2 flex justify-start"></div>
              </div>
              <div className="flex justify-center gap-7 flex-wrap">
                <Button
                  className=""
                  primary
                  rounded
                  bold
                  type="submit"
                  disabled={loading}
                >
                  {loading ? "Yükleniyor..." : "KAYDET"}
                </Button>
                <Button
                  onClick={navigateMain}
                  className=""
                  secondary
                  rounded
                  bold
                  disabled={loading}
                >
                  VAZGEÇ
                </Button>
              </div>
            </div>
          </div>
        </form>
        {/* burası düzenlenecek alan api isteği sonrası geri dönüş mesajının alert olarak paylaşılması */}
        {alert.type && <Alert type={alert.type} message={alert.message} />}
      </div>
    </Layout>
  );
};

export default UpdateTag;
