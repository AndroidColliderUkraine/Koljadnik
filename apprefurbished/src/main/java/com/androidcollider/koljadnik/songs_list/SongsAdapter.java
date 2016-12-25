package com.androidcollider.koljadnik.songs_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.Holder> {

    private List<SongItemViewModel> songItemViewModels = new ArrayList<>();
    private View.OnClickListener onClickListener;

    public SongsAdapter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new Holder(v);
    }

    public void updateData(List<SongItemViewModel> songTypesList) {
        this.songItemViewModels.clear();
        this.songItemViewModels.addAll(songTypesList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SongItemViewModel songItemViewModel = songItemViewModels.get(position);

        holder.tvName.setText(songItemViewModel.name);

        switch (songItemViewModel.rating) {
            case 1:
                holder.ivRating.setImageResource(R.drawable.rating_1);
                break;
            case 2:
                holder.ivRating.setImageResource(R.drawable.rating_2);
                break;
            case 3:
                holder.ivRating.setImageResource(R.drawable.rating_3);
                break;
            case 4:
                holder.ivRating.setImageResource(R.drawable.rating_4);
                break;
            case 5:
                holder.ivRating.setImageResource(R.drawable.rating_5);
                break;
        }

        holder.itemView.setTag(songItemViewModel.songId);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return songItemViewModels.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_song_name)
        TextView tvName;

        @BindView(R.id.iv_item_song_rating)
        ImageView ivRating;

        View itemView;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }
    }
}
