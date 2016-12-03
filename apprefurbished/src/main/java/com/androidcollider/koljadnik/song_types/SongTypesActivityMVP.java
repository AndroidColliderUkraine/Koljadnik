package com.androidcollider.koljadnik.song_types;


import android.support.v7.widget.RecyclerView;

/**
 * Description of ${CLASS_NAME}
 *
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 * @package com.acollider.apprefurbished.song_types
 */
public interface SongTypesActivityMVP {

    interface View {
        void setAdapterToList(RecyclerView.Adapter adapter);
        void setListLayoutManager(RecyclerView.LayoutManager listLayoutManager);
    }

    interface Presenter {
        void setView(View view);
    }
}
