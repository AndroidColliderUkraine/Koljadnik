package com.androidcollider.koljadnik.song_types;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.contants.Tags;
import com.androidcollider.koljadnik.utils.NumberConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongTypeAdapter extends RecyclerView.Adapter<SongTypeAdapter.Holder> {

    private List<SongTypeViewModel> songTypesList = new ArrayList<>();
    private View.OnClickListener onClickListener;

    public SongTypeAdapter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

    public void updateData(List<SongTypeViewModel> songTypesList) {
        this.songTypesList.clear();
        this.songTypesList.addAll(songTypesList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SongTypeViewModel songTypeViewModel = songTypesList.get(position);

        holder.tvName.setText(songTypeViewModel.songType.getName());
        holder.tvQuantity.setText(String.valueOf(NumberConverter.convert(songTypeViewModel.quantity)));

        holder.itemView.setTag(songTypeViewModel.songType.getId());
        holder.itemView.setTag(Tags.SONG_TYPE_NAME_TAG, songTypeViewModel.songType.getName());
        holder.itemView.setOnClickListener(onClickListener);
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

        View itemView;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }
    }
}
