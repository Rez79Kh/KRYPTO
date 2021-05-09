package com.example.krypto.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.krypto.R;
import java.util.ArrayList;

public class RemoveItemAdapter extends ArrayAdapter<String> {
    private final Activity context;
    public final ArrayList<String> cryptoName;
    public final ArrayList<String> cryptoSymbol;
    public final ArrayList<String> cryptoChange;

    public RemoveItemAdapter(Activity context, ArrayList<String> cryptoName, ArrayList<String> cryptoSymbol,ArrayList<String> cryptoChange) {
        super(context, R.layout.remove_items_list, cryptoName);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.cryptoName=cryptoName;
        this.cryptoSymbol=cryptoSymbol;
        this.cryptoChange = cryptoChange;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.remove_items_list, null,true);

        TextView cryptoNameText = (TextView) rowView.findViewById(R.id.nameID);
        TextView cryptoSymbolText = (TextView) rowView.findViewById(R.id.symbolID);
        TextView cryptoChangeText = (TextView) rowView.findViewById(R.id.change24hTextID);

        cryptoNameText.setText(cryptoName.get(position));
        cryptoSymbolText.setText(cryptoSymbol.get(position));
        cryptoChangeText.setText(cryptoChange.get(position));

        return rowView;

    }
}
