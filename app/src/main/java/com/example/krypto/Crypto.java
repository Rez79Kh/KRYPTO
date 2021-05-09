package com.example.krypto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Crypto {

    @SerializedName("data")
    public List data;
    @SerializedName("Authorization")
    public String auth;

    public Crypto(String auth) {
        this.auth = auth;
    }

    public List getData() {
        return data;
    }
}
