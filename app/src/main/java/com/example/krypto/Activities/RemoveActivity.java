package com.example.krypto.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.krypto.Adapters.RemoveItemAdapter;
import com.example.krypto.R;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;

public class RemoveActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener{
    private ListView removeItemListView;
    private MaterialToolbar appTopBarRemove;
    private ArrayList<String> selectedToRemoveName = new ArrayList<>();
    private ArrayList<String> selectedToRemoveSymbol = new ArrayList<>();
    private ArrayList<String> selectedToRemoveChange = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> namesList = bundle.getStringArrayList("currencyNamesList");
        ArrayList<String> symbolsList = bundle.getStringArrayList("currencySymbolsList");
        ArrayList<String> changesList = bundle.getStringArrayList("currencyChangesList");

        init();

        RemoveItemAdapter removeItemAdapter = new RemoveItemAdapter(this,namesList, symbolsList,changesList);
        removeItemListView.setAdapter(removeItemAdapter);

    }

    private void init(){
        removeItemListView = (ListView) findViewById(R.id.removeItemsListID);
        appTopBarRemove = (MaterialToolbar) findViewById(R.id.topAppBarRemoveID);
        appTopBarRemove.setOnMenuItemClickListener(this);
        removeItemListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteButtonID:
                Intent intent = new Intent();
                intent.putExtra("selectedToRemoveName",selectedToRemoveName);
                intent.putExtra("selectedToRemoveSymbol",selectedToRemoveSymbol);
                intent.putExtra("selectedToRemoveChange",selectedToRemoveChange);
                setResult(RESULT_OK,intent);
                finish();

                break;

            case R.id.cancelButtonID:
                finish();

                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ColorDrawable viewColor = (ColorDrawable) view.getBackground();
        int colorId = viewColor.getColor();

        if(colorId!= getResources().getColor(R.color.near_black)){
            view.setBackgroundColor(getResources().getColor(R.color.near_black));
            String name = ((TextView) view.findViewById(R.id.nameID)).getText()+"";
            String symbol = ((TextView) view.findViewById(R.id.symbolID)).getText()+"";
            String change = ((TextView) view.findViewById(R.id.change24hTextID)).getText()+"";
            selectedToRemoveName.add(name);
            selectedToRemoveSymbol.add(symbol);
            selectedToRemoveChange.add(change);

        }
        else{
            view.setBackgroundColor(Color.TRANSPARENT);
            String name = ((TextView) view.findViewById(R.id.nameID)).getText()+"";
            String symbol = ((TextView) view.findViewById(R.id.symbolID)).getText()+"";
            String change = ((TextView) view.findViewById(R.id.change24hTextID)).getText()+"";
            selectedToRemoveName.remove(name);
            selectedToRemoveSymbol.remove(symbol);
            selectedToRemoveChange.remove(change);
        }

    }
}