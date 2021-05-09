package com.example.krypto.Interfaces;


import com.example.krypto.Crypto;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MyApi {
    String URL = "https://pro-api.coinmarketcap.com/";

    @Headers({
            "X-CMC_PRO_API_KEY: ef4c1488-b7ce-4c28-a3e4-4063c68b4ff9",
            "Accepts: application/json"
    })

    @GET("v1/cryptocurrency/listings/latest")
    retrofit2.Call<Crypto> createCrypto(@Query("start") String order1, @Query("limit") String order2, @Query("convert") String order3);
}
