package com.example.krypto.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.krypto.Crypto;
import com.example.krypto.Interfaces.MyApi;
import com.example.krypto.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private final String start = "1";
    private final String limit = "5000";
    private final String convert = "USD";
    private TextView currencyName;
    private TextView currencySymbol;
    private TextView currencyRank;
    private TextView currencyPrice;
    private TextView currencyChange24h;
    private TextView currencyChange7d;
    private TextView currencyChange30d;
    private TextView currencyMarketCap;
    private TextView currencyVolume;
    private ImageButton backButton;
    private String clickedCryptoName;
    private String clickedCryptoSymbol;
    private LinearProgressIndicator linearProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detail);

        clickedCryptoName = getIntent().getStringExtra("cryptoName");
        clickedCryptoSymbol = getIntent().getStringExtra("cryptoSymbol");

        linearProgressIndicator = (LinearProgressIndicator) findViewById(R.id.progressID);
        linearProgressIndicator.show();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        init();

        showDetail();

    }

    private void init() {
        currencyName = (TextView) findViewById(R.id.cryptoNameID);
        currencySymbol = (TextView) findViewById(R.id.cryptoSymbolID);
        currencyRank = (TextView) findViewById(R.id.cryptoRankID);
        currencyPrice = (TextView) findViewById(R.id.cryptoPriceID);
        currencyChange24h = (TextView) findViewById(R.id.cryptoChange24hID);
        currencyChange7d = (TextView) findViewById(R.id.cryptoChange7dID);
        currencyChange30d = (TextView) findViewById(R.id.cryptoChange30dID);
        currencyMarketCap = (TextView) findViewById(R.id.cryptoMarketCapID);
        currencyVolume = (TextView) findViewById(R.id.cryptoVolumeID);
        backButton = (ImageButton) findViewById(R.id.backButtonID);

        backButton.setOnClickListener(this);


    }

    private void showDetail(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApi.URL).addConverterFactory(GsonConverterFactory.create()).build();

        MyApi myApi = retrofit.create(MyApi.class);

        Call<Crypto> call = myApi.createCrypto(start, limit, convert);
        call.enqueue(new Callback<Crypto>() {
            @Override
            public void onResponse(Call<Crypto> call, Response<Crypto> response) {
                Crypto crypto = response.body();

                if (crypto != null) {
                    Gson gson = new Gson();

                    int end = crypto.getData().size();
                    for(int i = 0 ; i<end ; i++){
                        JsonObject jsonObject = gson.toJsonTree(crypto.getData().get(i)).getAsJsonObject();
                        if(jsonObject.get("name").toString().replace("\"", "").equalsIgnoreCase(clickedCryptoName)){
                            currencyName.setText(clickedCryptoName);
                            currencySymbol.setText(clickedCryptoSymbol);

                            int index1 = jsonObject.get("cmc_rank").toString().indexOf(".");
                            currencyRank.setText(jsonObject.get("cmc_rank").toString().substring(0,index1));

                            JsonObject jsonObject1 = jsonObject.get("quote").getAsJsonObject();
                            JsonObject jsonObject2 = jsonObject1.get("USD").getAsJsonObject();

                            int index = jsonObject2.get("price").toString().indexOf(".");
                            index += 6;
                            currencyPrice.setText(jsonObject2.get("price").toString().substring(0,index));

                            int index2 = jsonObject2.get("percent_change_24h").toString().indexOf(".");
                            index2 += 3;
                            currencyChange24h.setText(jsonObject2.get("percent_change_24h").toString().substring(0,index2));

                            int index3 = jsonObject2.get("percent_change_7d").toString().indexOf(".");
                            index3 += 3;
                            currencyChange7d.setText(jsonObject2.get("percent_change_7d").toString().substring(0,index3));

                            int index4 = jsonObject2.get("percent_change_30d").toString().indexOf(".");
                            index4 += 3;
                            currencyChange30d.setText(jsonObject2.get("percent_change_30d").toString().substring(0,index4));

                            int index5 = jsonObject2.get("market_cap").toString().indexOf(".");
                            index5 += 7;
                            currencyMarketCap.setText(jsonObject2.get("market_cap").toString().substring(0,index5));

                            int index6 = jsonObject2.get("volume_24h").toString().indexOf(".");
                            index6 += 7;
                            currencyVolume.setText(jsonObject2.get("volume_24h").toString().substring(0,index6));

                            break;
                        }

                    }

                }
                linearProgressIndicator.hide();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<Crypto> call, Throwable t) {
                Log.e("failed", t.toString());

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButtonID:
                finish();
                break;
        }
    }
}