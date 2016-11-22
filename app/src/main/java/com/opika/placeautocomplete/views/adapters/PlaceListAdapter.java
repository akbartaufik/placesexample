package com.opika.placeautocomplete.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opika.placeautocomplete.R;
import com.opika.placeautocomplete.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Taufik Akbar on 20/11/2016.
 */

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListHolder> {

    private ArrayList<Place> placeArrayList;

    public PlaceListAdapter(ArrayList<Place> placeArrayList) {
        this.placeArrayList = placeArrayList;
        System.out.println("PLACE SIZE adapter : " + placeArrayList.size());
    }

    @Override
    public PlaceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_place_list, parent, false);
        return new PlaceListHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceListHolder holder, int position) {
        Place data = placeArrayList.get(position);

        System.out.println("PLACE NAME : " + data.getName());
        if(data != null) {
            holder.txtName.setText(data.getName());
        }

    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    class PlaceListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;

        public PlaceListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
