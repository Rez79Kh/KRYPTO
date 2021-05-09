package com.example.krypto.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.krypto.R;
import java.util.ArrayList;
import java.util.List;

public class AllCurrencyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    public  ArrayList<String> name;
    private  ArrayList<String> symbol;
    private  ArrayList<String> change;

    public ArrayList<String> filteredName;
    private ArrayList<String> filteredSymbol;

    private ItemFilter mFilter = new ItemFilter();

    ArrayList<String> nlist2;
    ArrayList<String> nlist3;

    public AllCurrencyListAdapter(Activity context, ArrayList<String> name, ArrayList<String> symbol,ArrayList<String> change) {
        super(context, R.layout.all_item_list,name);
        this.context = context;
        this.name = name;
        this.symbol = symbol;
        this.filteredName = name;
        this.filteredSymbol = symbol;
        this.change = change;
    }

    @Override
    public int getCount() {
        return filteredName.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return filteredName.get(position);
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.all_item_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.nameID);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.symbolID);
        TextView changeText = (TextView) rowView.findViewById(R.id.change24hTextID);

        changeText.setText(change.get(position));
        titleText.setText(filteredName.get(position));
        subtitleText.setText(symbol.get(position));


        return rowView;

    }

    public ArrayList<String> getFilteredNames() {
        return filteredName;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = name;
            final List<String> list2 = symbol;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);
            nlist2 = new ArrayList<String>(count);
            nlist3 = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                    nlist2.add(list2.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }



        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredName = (ArrayList<String>) results.values;
            filteredSymbol = nlist2;
            notifyDataSetChanged();
        }

    }
}
