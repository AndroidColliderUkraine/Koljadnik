package com.androidcollider.koljadnik.song_types;


import android.support.v7.widget.RecyclerView;

import com.androidcollider.koljadnik.common.CoomonView;
import com.androidcollider.koljadnik.contants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;

import java.util.List;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.acollider.apprefurbished.song_types
 */
public interface SongTypesActivityMVP {

    interface View extends CoomonView{
        void updateAdapter(List<SongTypeViewModel> songTypeViewModelList);
        void showErrorToast(String text);
        void showSongListUI(int typeId);
    }

    interface Presenter {
        void setView(View view);
        void initData();
        void openSongListUI(Object tag);
    }

    interface Model {
        UiAction getSongTypes(OnReadListener<List<SongTypeViewModel>> listener);
    }
}
