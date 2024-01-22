## İçerik
Bu projede apiden gelen dataları kullanıcının ekranında listeliyorum.Kullanıcı istediği datayı save edip lokale kayıt edebiliyor.Bunu favori sayfasında görebiliyor.Iteme tıkladıgında detay sayfasına gidiyor
Gidilen detay sayfasında da yine favori işlemi yapabiliyor veya add to card diyerek sepete ekleme işlemini gerçekleştiriyor.Filtreleme sayfasında iste sıralama işlemi yapabiliyor veya marka modele göre filtreleme
işlemini gerçekleştirebiliyor.Sepet kısımında complete yaptığı işlemleride profil sayfasında görebiliyor.Bu projede ağırlıklı room kullanılmıştır.Proje yapılırken CLEAN olmasına ve MVVM mimarisi standartlarına
uygun olarak geliştirilmeye özen gösterilmiştir.Projede dataları listelerken Flow kullandım.Flow kullanma sebebim ise flow döndürdüğümüz fonksiyonlar bize asenkron olma özelliği tanıyor.Bunu sağladığı için
orada collect,birden fazla hesaplanmış asenktron değer döndürme özelliği,suspend fun çağırma gibi işlemler gerçekleştirebiliyoruz ve benimde gerçekleştirdiğim yerler mevcut.Fakat livedata da kullanılabilirdi.
Suspend fonksiyonlar ile de asenkron olan işlemleri yönettim böylelikle kullanıcının beklediği durumları,hata durumlarını,datanın gelmemesi gibi durumları uygulamayı bloklamadan halletmiş oldum.Örneğin ilk home ekranında internetiniz yoksa karşınıza error butonu ve texti çıkıyor.Butona tıklayarak tekrar API'ye istek atabiliyorsun.Böylelikle sonsuza kadar progressbar dönmüyor
Projede Kullanılan Teknolojiler : 


## Kullanılan Teknolojiler : 

-Retrofit,



-Room,



-Coroutine,



-Hilt,



-State

## Video : 


https://github.com/Cntrk01/EMarketApp/assets/98031686/2b57c8b0-1846-4362-80e7-14f5722f24d7


## Çıktılar :

![Ekran görüntüsü 2024-01-07 150421](https://github.com/Cntrk01/EMarketApp/assets/98031686/8870e3eb-46d3-40c1-bba6-661d6aeb872b)![Ekran görüntüsü 2024-01-06 203655](https://github.com/Cntrk01/EMarketApp/assets/98031686/ebb038fa-5d4a-42ef-8bfa-c44108d02004)


![Ekran görüntüsü 2024-01-06 203838](https://github.com/Cntrk01/EMarketApp/assets/98031686/9dfa07c1-8f5e-4b76-a49a-b778745ae438)![Ekran görüntüsü 2024-01-06 203822](https://github.com/Cntrk01/EMarketApp/assets/98031686/6adba3a0-8917-4745-936c-5dd5cbaae046)


![Ekran görüntüsü 2024-01-06 203814](https://github.com/Cntrk01/EMarketApp/assets/98031686/19ee416d-55f7-48c8-8f20-e976822efda7)![Ekran görüntüsü 2024-01-06 203810](https://github.com/Cntrk01/EMarketApp/assets/98031686/2bc6c70e-a2e9-448f-8596-f4bb5300bb16)


![Ekran görüntüsü 2024-01-06 203758](https://github.com/Cntrk01/EMarketApp/assets/98031686/88732bc8-8adb-497a-b06e-b186c7c08c5c)![Ekran görüntüsü 2024-01-06 203750](https://github.com/Cntrk01/EMarketApp/assets/98031686/e0a3edd7-4f27-4318-94cc-a4ca523a3c80)


![Ekran görüntüsü 2024-01-06 203744](https://github.com/Cntrk01/EMarketApp/assets/98031686/bf34d16b-27dc-4295-bb5c-3d56b7375900)![Ekran görüntüsü 2024-01-06 203737](https://github.com/Cntrk01/EMarketApp/assets/98031686/06c7e1f2-b3f2-4ae1-bf22-0d9f7173edbd)


![Ekran görüntüsü 2024-01-06 203725](https://github.com/Cntrk01/EMarketApp/assets/98031686/8f70a10c-8af4-470b-a526-04b143b196a0)![Ekran görüntüsü 2024-01-06 203715](https://github.com/Cntrk01/EMarketApp/assets/98031686/84647709-086c-4d55-9986-cf530fc73fa0)


![Ekran görüntüsü 2024-01-06 203708](https://github.com/Cntrk01/EMarketApp/assets/98031686/c8e2cf18-8e15-4916-bfb3-6670c1429173)
