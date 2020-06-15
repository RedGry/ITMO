package p3111.redgry.bundles;

import java.util.ListResourceBundle;

public class ClassLanguage_tr_TR extends ListResourceBundle {
    private static final Object[][] contents = {
            {"hello", "Merhaba!"},
            {"windowClose", "Mesaj kutusu kapalı"},
            {"err_login", "Yanlış giriş veya şifre! \nTekrar dene"},
            {"suc_login", "Başarılı bir şekilde oturum açtıktan!"},
            {"suc_register", "Kayıtlısınız! \nİçeri girmeyi dene."},
            {"err_register", "Böyle bir takma adı olan bir kullanıcı zaten oluşturuldu. \nTekrar deneyin!"},
            {"Entry", "Giriş"},
            {"Info", "Bilgi"},
            {"HairColor", "Saç rengi"},
            {"update", "Yenileme"},
            {"WriteId", "Girin ID"},
            {"uPick", "Sen seçtin"},
            {"PersonCreatedByUser", "Person kullanıcı tarafından oluşturulan: "},
            {"with", "\n ile "},
            {"Pname", "\n{Ad: '"},
            {"cooridanateX", ",\n Koordinatlar {x= "},
            {"Pheight", "},\n Büyüme: "},
            {"Pbirthday", ",\n Doğum Günü: "},
            {"PpassportID", ",\n ID pasaportlar: "},
            {"PhairColor", ",\n Saç rengi: "},
            {"LocationName", ",\n Konum adı: "},
            {"LocationX", ",\n Konum koordinatları{ x= "},

            //Блок arg.fxml:
            {"InputData", "Giriş verileri"},
            {"count_less_number", "Girin V"},
            {"input_id", "Girin ID"},
            {"delete_id", "Nesneyi Kaldır"},
            {"input_birthday", "Tarihi girin YYYY-MM-DD"},
            {"add", "Nesne Ekle"},

            //Блок authorization.fxml:
            {"selectLanguage", "Dil seç"},
            {"registrationLogin", "Kayıt / Giriş"},
            {"loginBut", "girmek"},
            {"regBut", "kayıt"},

            //Блок personDialog.fxml:
            {"parameters_person", "Parametreleri girin Person"},



            //Блок команд:
            {"suc_help", "remove_greater_key: Belirtilen anahtarı aşan tüm öğeleri koleksiyondan kaldırın.\n" +
                    "clear: Koleksiyonu Temizle (koleksiyondan size ait öğeleri kaldırır).\n" +
                    "show: Koleksiyondaki tüm öğeleri dize görünümünde standart çıktı akışına getirin.\n" +
                    "insert: Verilen anahtarla yeni bir öğe ekleyin.\n" +
                    "update: Kimliği belirtilen değere eşit olan bir koleksiyon öğesinin değerini güncelleştirin.\n" +
                    "history: Son 15 komutu çıktı (argüman yok).\n" +
                    "remove_greater: Koleksiyondan belirtilen öğeden daha büyük tüm öğeleri kaldırın.\n" +
                    "print_ascending: Koleksiyon öğelerinin kimliğini artan düzende görüntüleyin.\n" +
                    "count_less_than_location: Location alanının değeri belirtilen değerden küçük olan öğe sayısını görüntüleyin.\n" +
                    "exit: Kaydetmeden programı kapatma.\n" +
                    "help: Kullanılabilir komutlar için yardım görüntüler.\n" +
                    "remove_any_by_birthday: Koleksiyondan, birthday alanının değeri belirtilen değere eşdeğer olan tek bir öğeyi kaldırın. (Tarih biçimi: YYYY-MM-DD)\n" +
                    "remove_key: Koleksiyondaki bir öğeyi kendi kimliği ile silin.\n" +
                    "info: Koleksiyon hakkındaki bilgileri standart bir akışa görüntüler."},
            {"suc_info", "Bilgi: "},
            {"count_less", "Koleksiyondaki öğeler, belirtilen sayıda konum koordinatlarının ürününden daha küçüktür: "},
            {"collection_empty", "Koleksiyon boş!"},
            {"print_ascending", "ID artan düzende koleksiyonlar:\n"},
            {"suc_insert", "Karakter başarıyla eklendi!"},
            {"err_insert", "Karakter verilen anahtarla zaten var."},
            {"suc_update", "Karakter başarıyla güncellendi ID: "},
            {"err_updateUser", "Karakter size ait değil ID: "},
            {"err_update", "Bu karakteri güncelleyemezsiniz çünkü koleksiyonda yoktur."},
            {"suc_clear", "Koleksiyon başarıyla temizlendi! \nSize ait tüm öğeler kaldırıldı!"},
            {"suc_remove", "Karakter ile kaldırıldı ID: "},
            {"err_remove", "Karakter bulunamadı veya size ait değil ID: "},
            {"suc_history", "Takım geçmişi:\n"},
            {"err_removeBirthday", "Koleksiyonda belirli bir doğum tarihi olan karakter yok."},
            {"suc_removeBirthday", "Koleksiyondan size ait belirtilen doğum tarihi ile tüm karakterler kaldırıldı! \n"},
            {"err_greater", "Karakter zaten verilerle var ID."},
            {"error_greater", "Koleksiyonda daha büyük karakterler yok ID: "},
            {"suc_greater", "Size ait karakterler aşan ile kaldırıldı ID: "},

            //Блок Ошибок:
            {"Error", "Hata"},
            {"error_arg", "Takıma bir argüman verilmedi veya yanlış format vardı!"},
            {"InvalidData", "Yanlış komut verileri!"},
            {"IncorrectField", "Yanlış doldurulmuş alanlar!"},
            {"IncorrectNumber", "Sayısı çıktı aralık!"},
            {"IncorrectDataField", "Yanlış doldurulmuş alan!"},
            {"ErrorCreatingObject", "Nesne oluşturma hatası"},
            {"UnexpectedException", "Beklenmeyen istisna"},
            {"incorrectLogPas", "Yanlış giriş veya şifre."},
            {"uncorrectLogPas", "Farklı bir kullanıcı adı veya şifre girin!"},
            {"idNotFound", "Verilen kimliğe sahip karakter bulunamadı!"},
            {"error_argBirthday", "Yanlış tarih formatı! \nTarih biçimi: YYYY-MM-DD"},


            //Блок main.fxml:
            {"file", "Dosya"},
            {"exit", "Çıkış"},

            {"help", "Yardım"},
            {"info", "Koleksiyon bilgileri"},
            {"helpCom", "Takım bilgileri"},

            {"language", "Dil seç"},

            {"username", "Kullanıcı: "},
            {"updateTable", "Tablo değerlerini güncelle"},

            {"addCommands", "Ekleme Komutları:"},
            {"insertCom", "Karakter Ekle"},

            {"removeCommands", "Silme Komutları:"},
            {"removeID", "Yazılımı sil ID"},
            {"removeBirthday", "Doğum günü sil"},
            {"removeGreater", "Büyük karakteri Kaldır"},
            {"removeGreaterID", "Büyük kaldırmak ID"},
            {"clearCom", "Temizlemek"},

            {"updateCommands", "Güncelleme Komutları:"},
            {"updateCom", "Yenileme"},

            {"printCommands", "Çıkış Komutları:"},
            {"countLessCom", "Çıkış adet <V Yerler"},
            {"printCom", "ID Artan karakterler"},
            {"historyCom", "Takım geçmişi"},

            {"tabPerson", "Tablo Person"},
            {"tabVisual", "Görüntüleme alanı"}
    };


    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
