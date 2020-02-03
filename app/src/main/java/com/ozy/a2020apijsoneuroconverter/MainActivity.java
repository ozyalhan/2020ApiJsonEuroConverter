package com.ozy.a2020apijsoneuroconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

// manifest e internet erişim iznini ekledik.
//android:usesCleartextTraffic="true" ekledik
// sorun yok ama security için res altında xml dosyası oluşturduk.
//network security conf,g xml i oluşturduk
    /*
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
         <domain-config cleartextTrafficPermitted = "true">
            <domain includeSubdomains = "true">data.fixer.io</domain>
        </domain-config>
    </network-security-config>
     */
//ve manifest e android:networkSecurityConfig="@xml/network_security_config" ekleyerek güvwnlik yapılandırmasını tamamladık. httpden veri çekimi garanti altına alındı.


public class MainActivity extends AppCompatActivity {

    TextView tryView;
    TextView dollarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryView = findViewById(R.id.tryView);
        dollarView = findViewById(R.id.dollarView);
    }

    public void getRates(View View){

        DownloadData downloadData = new DownloadData();// yeni obje oluşturduk
        try {
            String url = "http://data.fixer.io/api/latest?access_key=a181bae52b30a5f639402cee428abc52&format=1";
            downloadData.execute(url); // burada verdiğimiz url strings[0] a denk geliyor ve doInBackGrounds ta bu değer çağrılacak.
        }catch (Exception e) {



        }
    }


    //uri - progress bar (void) - answer
    // senkronize olmayan çekilde ççalışan yapı  //dif threat

    private class DownloadData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) { //strings bir param dizisi

            String result=""; //sonucu bu değişkene atayacağız.
            URL url;
            HttpURLConnection httpURLConnection;

            try{
                url = new URL(strings[0]);  // strings dizisi içindeki ilke elemanı aldık burada 1 tane uri olduğu için bu şekilde yaklaşım yaptık
                httpURLConnection = (HttpURLConnection) url.openConnection(); // bağlantı açıldı.

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //gelen veriyi değerlendirme ve okuma.

                int data =inputStreamReader.read();

                while (data >0){  //datayı bir karakter olarak tanımlayıp tek tek bütün karakterleri result string'ine ekleyeceğiz.
                    char character = (char) data; //data yı karakter olarak kaydettik
                    result +=character; // gelen tüm dataları result'a tek tek ekledik.

                    data = inputStreamReader.read(); // bir sonraki karaktere geçmemizi sağlar.
                }

                return result; //hata yok ise result'ı verecek.

            }catch(Exception e){
                return null; // hata yakalar ise null dönecek.
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //System.out.println("alınan data " + s); // s yukarıdaki result

            try {

                // [] array // { object.

                JSONObject jsonObject = new JSONObject(s); // s den json objecti aldık.
                String base = jsonObject.getString("base");
                //System.out.println("base: " +base);

                String rates = jsonObject.getString("rates");
                //System.out.println("rates: + " +rates);

                JSONObject jsonObject1 = new JSONObject(rates); // bu sefer rates den veri alarak seçim yapmayı sağlayacağız.
                String tlValue = jsonObject1.getString("TRY");
                tryView.setText("TRY: " +tlValue);
                String dollarValue = jsonObject1.getString("USD");
                dollarView.setText("USD: "+ dollarValue);

            }catch (Exception e){

            }
        }
    }

}
