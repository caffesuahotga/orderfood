package com.example.orderfood.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.orderfood.R;

public class MenuAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final int[] icons;
    private final String[] menuItems;

    public MenuAdapter(Context context, String[] menuItems, int[] icons) {
        super(context, R.layout.menu_item, menuItems);
        this.context = context;
        this.menuItems = menuItems;
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.menu_item, parent, false);

        TextView txtMenuItem = rowView.findViewById(R.id.txtMenuItem);
        ImageView imgIcon = rowView.findViewById(R.id.imgIcon);

        txtMenuItem.setText(menuItems[position]);
        imgIcon.setImageResource(icons[position]);

        return rowView;
    }
}
