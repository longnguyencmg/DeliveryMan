package com.planday.deliveroo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planday.deliveroo.R;
import com.planday.deliveroo.model.MapPlace;

import java.util.List;

/**
 * Created by longnguyen on 11:57 AM, 7/20/18.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MapItemViewHolder> {

    private Context        mContext;
    private List<MapPlace> mapPlaces;

    public PlaceAdapter(Context context, List<MapPlace> places) {
        this.mContext = context;
        this.mapPlaces = places;
    }

    public void updateData(List<MapPlace> places) {
        this.mapPlaces = places;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MapItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MapItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.place_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MapItemViewHolder holder, int position) {
        MapPlace item = mapPlaces.get(position);
        holder.title.setText(item.getName());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return mapPlaces.size();
    }

    class MapItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;

        MapItemViewHolder(@NonNull View v) {
            super(v);

            this.title = v.findViewById(R.id.list_item_name);
            this.description = v.findViewById(R.id.list_item_description);
        }


    }
}
