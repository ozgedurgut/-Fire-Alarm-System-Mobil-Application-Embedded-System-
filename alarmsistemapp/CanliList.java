package com.ozge.alarmsistemapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CanliList extends ArrayAdapter<Canli> {
    private Activity context;
    private List<Canli>canliList;

    public CanliList(Activity context, List<Canli> canliList){
        super(context, R.layout.benim_list_layout, canliList);
        this.context = context;
        this.canliList = canliList;
            }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.benim_list_layout,  null, true);
        TextView textViewAd = listViewItem.findViewById(R.id.text_view_ad);
        TextView textViewNumara = listViewItem.findViewById(R.id.text_view_no);
        TextView textViewWho = listViewItem.findViewById(R.id.text_view_who);

        Canli insan = canliList.get(position);
        textViewAd.setText(insan.getCanliAd());
        textViewNumara.setText(insan.getCanliNo());
        textViewWho.setText(insan.getCanliWho());

        return listViewItem;
    }
}
