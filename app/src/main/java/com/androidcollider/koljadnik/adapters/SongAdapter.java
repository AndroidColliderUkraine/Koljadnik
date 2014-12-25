package com.androidcollider.koljadnik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.objects.Song;
import com.androidcollider.koljadnik.objects.SongType;

import java.util.ArrayList;

/**
 * Created by pseverin on 24.12.14.
 */
public class SongAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater lInflater;
    public ArrayList<Song> songsList;
    //public ArrayList<Route> allRouteArrayList;

    public SongAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
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
        return songsList.size();
    }

    // element at position
    @Override
    public Song getItem(int position) {
        return songsList.get(position);
    }

    // id at position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // create viewholder
    static class ViewHolder {
        public TextView tv_name;
        public ImageView iv_rating;
    }

    // list item_for_routes_listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_song, parent, false);

            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_item_song_name);
            holder.iv_rating= (ImageView) view.findViewById(R.id.iv_item_song_rating);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Song song = getItem(position);
        holder.tv_name.setText(song.getName());

        long ratingRange = Song.current_max_rating-Song.current_min_rating;

        if (ratingRange==0){
            holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_5));
        } else if (ratingRange<5){
            switch ((int)(Song.current_max_rating-song.getRating())){
                case 0: holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_5));
                    break;
                case 1: holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_4));
                    break;
                case 2: holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_3));
                    break;
                case 3: holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_2));
                    break;
                case 4: holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_1));
                    break;
                default:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_5));
                    break;
            }
        } else {
            double onePoint = ratingRange/5;
            long relativeRating = song.getRating()-Song.current_min_rating;
            double currentRating = relativeRating/onePoint;

            long curentRatingRound = Math.round(currentRating+0.5);

            switch ((int)curentRatingRound) {
                case 1:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_1));
                    break;
                case 2:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_2));
                    break;
                case 3:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_3));
                    break;
                case 4:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_4));
                    break;
                case 5:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_5));
                    break;
                default:
                    holder.iv_rating.setImageDrawable(context.getResources().getDrawable(R.drawable.rating_5));
                    break;


            }
        }

        return view;
    }

    public void updateData(ArrayList<Song> songsList) {
        this.songsList= songsList;
        this.notifyDataSetChanged();
    }

   /* public void updateRouteLists(ArrayList<Route> list){
        this.routeArrayList.clear();
        this.routeArrayList.addAll(list);
        this.allRouteArrayList.clear();
        this.allRouteArrayList.addAll(list);
    }*/
}
