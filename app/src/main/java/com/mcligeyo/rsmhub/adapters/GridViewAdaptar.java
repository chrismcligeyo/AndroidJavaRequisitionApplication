package com.mcligeyo.rsmhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.model.MainGriddViewMenu;

import java.util.ArrayList;

public class GridViewAdaptar extends BaseAdapter {
    private Context context;
    private ArrayList<MainGriddViewMenu> mainGriddViewMenuList;
    private LayoutInflater mgvmListInflater;


    public GridViewAdaptar(Context context, ArrayList<MainGriddViewMenu> mainGriddViewMenuList) {
        this.mainGriddViewMenuList = mainGriddViewMenuList;
        mgvmListInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mainGriddViewMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mainGriddViewMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if (convertView == null) {
            convertView = mgvmListInflater.inflate(R.layout.main_grid_view, null);
            holder = new ViewHolder();
            holder.mainGridCardView = convertView.findViewById(R.id.cardview_main_grid);
            holder.mainGridImageView = convertView.findViewById(R.id.grid_image_view);
            holder.mainGridTextView = convertView.findViewById(R.id.grid_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mainGridImageView.setImageResource(mainGriddViewMenuList.get(position).getMainGridMenuIcon());
        holder.mainGridImageView.setBackgroundResource(mainGriddViewMenuList.get(position).getBackGroundColor());
        holder.mainGridTextView.setText(mainGriddViewMenuList.get(position).getMainGridMenuName());

        return convertView;
    }

    public static class ViewHolder {
        CardView mainGridCardView;
        ImageView mainGridImageView;
        TextView mainGridTextView;

    }
}
