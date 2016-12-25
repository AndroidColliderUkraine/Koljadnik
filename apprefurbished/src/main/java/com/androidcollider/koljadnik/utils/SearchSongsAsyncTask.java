package com.androidcollider.koljadnik.utils;

import android.os.AsyncTask;

import com.androidcollider.koljadnik.songs_list.SongItemViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchSongsAsyncTask extends AsyncTask<String, Void, List<SongItemViewModel>> {

    private List<SongItemViewModel> songItemViewModels;

    public SearchSongsAsyncTask(List<SongItemViewModel> songItemViewModels) {
        this.songItemViewModels = songItemViewModels;
    }

    @Override
    protected List<SongItemViewModel> doInBackground(String... strings) {
        String searchStr = strings[0];

        List<SongItemViewModel> searchResult = new ArrayList<>();
        for (SongItemViewModel songItemViewModel: songItemViewModels){
            if (songItemViewModel.name.toLowerCase().contains(searchStr.toLowerCase()) ||
                    songItemViewModel.text.toLowerCase().contains(searchStr.toLowerCase())){
                searchResult.add(songItemViewModel);
            }
        }
        return searchResult;
    }

    @Override
    protected void onPostExecute(List<SongItemViewModel> songItemViewModels) {
        super.onPostExecute(songItemViewModels);
        onSuccess(songItemViewModels);
    }

    public abstract void onSuccess(List<SongItemViewModel> songItemViewModels);
}
