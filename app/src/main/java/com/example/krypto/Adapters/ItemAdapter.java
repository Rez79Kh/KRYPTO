package com.example.krypto.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.krypto.R;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<String> {
    private final Activity context;
    public final ArrayList<String> cryptoName;
    public final ArrayList<String> cryptoSymbol;
    public ArrayList<String> change24h;

    public TextView change;

    public ItemAdapter(Activity context, ArrayList<String> cryptoName, ArrayList<String> cryptoSymbol,ArrayList<String> change24h) {
        super(context, R.layout.items_list, cryptoName);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.cryptoName=cryptoName;
        this.cryptoSymbol=cryptoSymbol;
        this.change24h=change24h;

    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.items_list, null,true);

        TextView cryptoNameText = (TextView) rowView.findViewById(R.id.cryptoNameID);
        TextView cryptoSymbolText = (TextView) rowView.findViewById(R.id.cryptoSymbolID);
        change = (TextView) rowView.findViewById(R.id.change24hTextID);

        cryptoNameText.setText(cryptoName.get(position));
        cryptoSymbolText.setText(cryptoSymbol.get(position));
        change.setText(change24h.get(position));

        return rowView;

    };


    public void addData(String name , String symbol , String change) {
        cryptoName.add(name);
        cryptoSymbol.add(symbol);
        change24h.add(change);
    }
    public void removeData(String name , String symbol, String change){
        cryptoName.remove(name);
        cryptoSymbol.remove(symbol);
        change24h.remove(change);
    }

}
