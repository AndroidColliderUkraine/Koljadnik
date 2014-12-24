package com.androidcollider.koljadnik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.objects.SongType;
import java.util.ArrayList;

/**
 * Created by pseverin on 24.12.14.
 */
public class SongTypeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater lInflater;
    public ArrayList<SongType> songTypesList;
    //public ArrayList<Route> allRouteArrayList;

    public SongTypeAdapter(Context context, ArrayList<SongType> songTypesList) {
        this.context = context;
        this.songTypesList = songTypesList;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*//form an arraylist of objects Route for dynamic listview
    public void search(String search) {
        routeArrayList.clear();
        for (int i = 0; i < allRouteArrayList.size(); i++) {
            if ((allRouteArrayList.get(i).getName().contains(search))) {
                routeArrayList.add(allRouteArrayList.get(i));
            }
        }
        notifyDataSetChanged();
    }*/

    // the number of elements
    @Override
    public int getCount() {
        return songTypesList.size();
    }

    // element at position
    @Override
    public SongType getItem(int position) {
        return songTypesList.get(position);
    }

    // id at position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // create viewholder
    static class ViewHolder {
        public TextView tv_name;
        public TextView tv_quantity;
    }

    // list item_for_routes_listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_song_type, parent, false);

            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_item_song_type_name);
            holder.tv_quantity = (TextView) view.findViewById(R.id.tv_item_song_type_quantity);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SongType songType = getItem(position);
        holder.tv_name.setText(songType.getName());
        holder.tv_quantity.setText(String.valueOf(songType.getQuantity()));
        return view;
    }

    public void updateData(ArrayList<SongType> songTypesList) {
        this.songTypesList= songTypesList;
        this.notifyDataSetChanged();
    }

   /* public void updateRouteLists(ArrayList<Route> list){
        this.routeArrayList.clear();
        this.routeArrayList.addAll(list);
        this.allRouteArrayList.clear();
        this.allRouteArrayList.addAll(list);
    }*/
}
