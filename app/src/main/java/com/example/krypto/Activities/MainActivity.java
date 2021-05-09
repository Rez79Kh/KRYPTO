package com.example.krypto.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.krypto.Adapters.AllCurrencyListAdapter;
import com.example.krypto.Adapters.ItemAdapter;
import com.example.krypto.Adapters.RemoveItemAdapter;
import com.example.krypto.Crypto;
import com.example.krypto.Interfaces.MyApi;
import com.example.krypto.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ListView cryptoList;
    private ListView allCryptoCurrencyList;
    private String start = "1";
    private String limit = "5000";
    private String convert = "USD";
    private MaterialToolbar appTopBar;
    private BottomSheetBehavior addItemSheetBehaviour;
    private RelativeLayout addItemBottomSheet;
    private ArrayList<String> addCurrencyName = new ArrayList<>();
    private ArrayList<String> addCurrencySymbol = new ArrayList<>();
    private ArrayList<String> addCurrencyChange = new ArrayList<>();
    private Button addCurrencyButton;
    public ArrayList<String> defaultCurrencyListName = new ArrayList<>();
    public ArrayList<String> defaultCurrencyListSymbol = new ArrayList<>();
    private AllCurrencyListAdapter allCurrencyListAdapter;
    private EditText searchButton;
    public ItemAdapter currentItemAdapter;
    public RemoveItemAdapter removeItemAdapter;
    private ArrayList<String> nameToRemoveList;
    private ArrayList<String> symbolToRemoveList;
    private ArrayList<String> changeToRemoveList;
    private TextView yearText;
    private TextView monthText;
    private TextView dayNumText;
    private TextView dayText;
    LinearProgressIndicator linearProgressIndicator;
    private ArrayList<String> addedItemPosition = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setUpDate();
        getDefaultDetails();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        linearProgressIndicator.show();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        searchButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MainActivity.this.allCurrencyListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cryptoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currencyName = ((TextView) view.findViewById(R.id.cryptoNameID)).getText().toString();
                String currencySymbol = ((TextView) view.findViewById(R.id.cryptoSymbolID)).getText().toString();
                Intent intent = new Intent(MainActivity.this, CurrencyDetailActivity.class);
                intent.putExtra("cryptoName", currencyName);
                intent.putExtra("cryptoSymbol", currencySymbol);
                // Pass data object in the bundle and populate details activity.
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, (View) findViewById(R.id.cryptoCurrencyCardID), "profile");
                startActivity(intent, options.toBundle());
            }
        });


        allCryptoCurrencyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ColorDrawable viewColor = (ColorDrawable) view.getBackground();
                int colorId = viewColor.getColor();

                String name = ((TextView) view.findViewById(R.id.nameID)).getText() + "";
                String symbol = ((TextView) view.findViewById(R.id.symbolID)).getText() + "";
                String change = ((TextView) view.findViewById(R.id.change24hTextID)).getText() + "";
                if (colorId != getResources().getColor(R.color.near_black)) {
                    view.setBackgroundColor(getResources().getColor(R.color.near_black));
                    addCurrencyName.add(name);
                    addCurrencySymbol.add(symbol);
                    addCurrencyChange.add(change);
                    addedItemPosition.add(String.valueOf(position));

                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.grey));
                    addCurrencyName.remove(name);
                    addCurrencySymbol.remove(symbol);
                    addCurrencyChange.remove(change);
                    addedItemPosition.remove(String.valueOf(position));
                }
            }
        });


        addCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int end = addCurrencyName.size();

                for (int i = 0; i < end; i++) {
                    if (!defaultCurrencyListName.contains(addCurrencyName.get(i))) {
                        currentItemAdapter.addData(addCurrencyName.get(i), addCurrencySymbol.get(i), addCurrencyChange.get(i));
                        v.setBackgroundColor(getResources().getColor(R.color.grey));
                        currentItemAdapter.notifyDataSetChanged();
                    }
                }

                for (int j = 0; j < allCryptoCurrencyList.getCount(); j++) {
                    if (allCryptoCurrencyList.getChildAt(j) != null)
                        allCryptoCurrencyList.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.grey));
                }

                addItemSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        appTopBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addItemID:
                        addItemSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                        return true;

                    case R.id.removeItemID:
                        Intent intent = new Intent(MainActivity.this, RemoveActivity.class);
                        intent.putExtra("currencyNamesList", currentItemAdapter.cryptoName);
                        intent.putExtra("currencySymbolsList", currentItemAdapter.cryptoSymbol);
                        intent.putExtra("currencyChangesList", currentItemAdapter.change24h);
                        startActivityForResult(intent, 1);

                        return true;

                    case R.id.refreshListID:
                        linearProgressIndicator.show();

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        updateHomeCurrencies();

                        return true;
                }
                return false;
            }
        });

    }

    private void updateHomeCurrencies() {

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

                    for (int i = 0; i < end; i++) {
                        JsonObject jsonObject = gson.toJsonTree(crypto.getData().get(i)).getAsJsonObject();
                        int endItems = cryptoList.getCount();
                        for (int j = 0; j < endItems; j++) {
                            if (jsonObject.get("name").toString().replace("\"", "").equals(currentItemAdapter.cryptoName.get(j))) {
                                JsonObject jsonObject3 = jsonObject.get("quote").getAsJsonObject();
                                JsonObject jsonObject4 = jsonObject3.get("USD").getAsJsonObject();
                                DecimalFormat df = new DecimalFormat("###.##");
                                TextView textView = (TextView) cryptoList.getAdapter().getView(j, null, cryptoList).findViewById(R.id.change24hTextID);
                                if (jsonObject4.get("percent_change_24h").toString().replace("\"", "").charAt(0) == '-') {
                                    currentItemAdapter.change24h.set(j, df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                                    textView.setText(df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                                } else {
                                    currentItemAdapter.change24h.set(j, "+" + df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                                    textView.setText("+" + df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                                }
                                currentItemAdapter.notifyDataSetChanged();
                                break;
                            }
                        }

                    }
                    linearProgressIndicator.hide();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }

            @Override
            public void onFailure(Call<Crypto> call, Throwable t) {
                Log.e("failed", t.toString());

            }
        });

    }

    private void getDefaultDetails() {
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
                    ArrayList<String> addItemSymbol = new ArrayList<>(end);
                    ArrayList<String> addItemName = new ArrayList<>(end);
                    ArrayList<String> change24h = new ArrayList<>(end);
                    ArrayList<String> allChange24h = new ArrayList<>(end);

                    for (int i = 0; i < end; i++) {
                        JsonObject jsonObject = gson.toJsonTree(crypto.getData().get(i)).getAsJsonObject();
                        addItemName.add(i, jsonObject.get("name").toString().replace("\"", ""));
                        addItemSymbol.add(i, jsonObject.get("symbol").toString().replace("\"", ""));
                        JsonObject jsonObject3 = jsonObject.get("quote").getAsJsonObject();
                        JsonObject jsonObject4 = jsonObject3.get("USD").getAsJsonObject();
                        DecimalFormat df = new DecimalFormat("###.##");
                        if (jsonObject4.get("percent_change_24h").toString().replace("\"", "").charAt(0) == '-') {
                            allChange24h.add(i, df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                        } else {
                            allChange24h.add(i, "+" + df.format(Double.parseDouble(jsonObject4.get("percent_change_24h").toString().replace("\"", ""))));
                        }
                        if (i < defaultCurrencyListName.size()) {
                            for (int j = 0; j < end; j++) {
                                JsonObject jsonObject2 = gson.toJsonTree(crypto.getData().get(j)).getAsJsonObject();
                                if (jsonObject2.get("name").toString().replace("\"", "").equalsIgnoreCase(defaultCurrencyListName.get(i))) {
                                    JsonObject jsonObject5 = jsonObject2.get("quote").getAsJsonObject();
                                    JsonObject jsonObject6 = jsonObject5.get("USD").getAsJsonObject();
                                    DecimalFormat df2 = new DecimalFormat("###.##");
                                    if (jsonObject6.get("percent_change_24h").toString().replace("\"", "").charAt(0) == '-') {
                                        change24h.add(i, df2.format(Double.parseDouble(jsonObject6.get("percent_change_24h").toString().replace("\"", ""))));
                                    } else {
                                        change24h.add(i, "+" + df2.format(Double.parseDouble(jsonObject6.get("percent_change_24h").toString().replace("\"", ""))));
                                    }

                                    break;
                                }

                            }
                        }
                    }
                    allCurrencyListAdapter = new AllCurrencyListAdapter(MainActivity.this, addItemName, addItemSymbol, allChange24h);
                    allCryptoCurrencyList.setAdapter(allCurrencyListAdapter);

                    currentItemAdapter = new ItemAdapter(MainActivity.this, defaultCurrencyListName, defaultCurrencyListSymbol, change24h);
                    removeItemAdapter = new RemoveItemAdapter(MainActivity.this, defaultCurrencyListName, defaultCurrencyListSymbol, change24h);
                    cryptoList.setAdapter(currentItemAdapter);

                    linearProgressIndicator.hide();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }

            @Override
            public void onFailure(Call<Crypto> call, Throwable t) {
                Log.e("failed", t.toString());

            }
        });

    }

    private void setUpDate() {
        Date date = new Date();

        String currentDay = (String) DateFormat.format("EEEE", date);
        String currentDayNumber = (String) DateFormat.format("dd", date);
        String currentMonth = (String) DateFormat.format("MMM", date);
        String currentYear = (String) DateFormat.format("yyyy", date);

        yearText.setText(currentYear);
        monthText.setText(currentMonth);
        dayNumText.setText(currentDayNumber);
        dayText.setText(currentDay + ", ");
    }

    private void init() {
        appTopBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        addItemBottomSheet = (RelativeLayout) findViewById(R.id.add_item_bottom_sheet);
        addItemSheetBehaviour = BottomSheetBehavior.from(addItemBottomSheet);
        allCryptoCurrencyList = (ListView) findViewById(R.id.allCryptoListID);
        addCurrencyButton = (Button) findViewById(R.id.addButtonID);
        searchButton = (EditText) findViewById(R.id.searchButtonID);
        yearText = (TextView) findViewById(R.id.yearTextID);
        monthText = (TextView) findViewById(R.id.monthTextID);
        dayNumText = (TextView) findViewById(R.id.dayNumTextID);
        dayText = (TextView) findViewById(R.id.dayTextID);
        cryptoList = (ListView) findViewById(R.id.itemsListID);
        linearProgressIndicator = (LinearProgressIndicator) findViewById(R.id.progressID);

        defaultCurrencyListName.add("Bitcoin");
        defaultCurrencyListName.add("Ethereum");
        defaultCurrencyListName.add("Cardano");
        defaultCurrencyListName.add("Binance Coin");
        defaultCurrencyListName.add("Tether");
        defaultCurrencyListSymbol.add("(BTC)");
        defaultCurrencyListSymbol.add("(ETH)");
        defaultCurrencyListSymbol.add("(ADA)");
        defaultCurrencyListSymbol.add("(BNB)");
        defaultCurrencyListSymbol.add("(USDT)");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                nameToRemoveList = data.getExtras().getStringArrayList("selectedToRemoveName");
                symbolToRemoveList = data.getExtras().getStringArrayList("selectedToRemoveSymbol");
                changeToRemoveList = data.getExtras().getStringArrayList("selectedToRemoveChange");
                removeSelectedCurrency(nameToRemoveList, symbolToRemoveList, changeToRemoveList);
            }
        }
    }

    private void removeSelectedCurrency(ArrayList<String> nameList, ArrayList<String> symbolList, ArrayList<String> changeList) {
        int end = nameList.size();
        for (int i = 0; i < end; i++) {
            if (currentItemAdapter.cryptoName.contains(nameList.get(i))) {
                currentItemAdapter.removeData(nameList.get(i), symbolList.get(i), changeList.get(i));
                currentItemAdapter.notifyDataSetChanged();
            }
        }

    }

}