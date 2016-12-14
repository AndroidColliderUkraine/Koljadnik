package com.androidcollider.koljadnik.songs;


import android.support.v7.widget.RecyclerView;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongTypeViewModel;

import java.util.List;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.acollider.apprefurbished.song_types
 */
public interface SongsActivityMVP {

    interface View {
        void updateAdapter(List<SongItemViewModel> songItemViewModels);
        void showErrorToast(String text);
        void showSongDetailsUI(int songId);
    }

    interface Presenter {
        void setView(View view);
        void initData(int typeId);
        void openSongDetailsUI(Object object);
    }

    interface Model {
        void getSongsByTypeId(int typeId, final OnReadListener<List<SongItemViewModel>> listener);
    }
}
