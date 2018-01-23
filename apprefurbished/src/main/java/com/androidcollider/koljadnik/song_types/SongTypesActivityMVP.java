package com.androidcollider.koljadnik.song_types;


import com.androidcollider.koljadnik.common.CoomonView;
import com.androidcollider.koljadnik.constants.UiAction;
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
        void showSongListUI(int typeId, String typeName);
    }

    interface Presenter {
        void setView(View view);
        void initData();
        void openSongListUI(Object tag1, Object tag2);
    }

    interface Model {
        UiAction getSongTypes(OnReadListener<List<SongTypeViewModel>> listener);
    }
}
