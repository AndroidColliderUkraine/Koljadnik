package com.androidcollider.koljadnik.song_types;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.utils.NumberConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongTypeAdapter extends RecyclerView.Adapter<SongTypeAdapter.Holder> {

    public List<SongTypeViewModel> songTypesList;

    public SongTypeAdapter(List<SongTypeViewModel> songTypesList) {
        this.songTypesList = songTypesList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_type, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SongTypeViewModel songTypeViewModel = songTypesList.get(position);

        holder.tvName.setText(songTypeViewModel.songType.getName());
        holder.tvQuantity.setText(String.valueOf(NumberConverter.convert(songTypeViewModel.quantity)));
    }

    @Override
    public int getItemCount() {
        return songTypesList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
