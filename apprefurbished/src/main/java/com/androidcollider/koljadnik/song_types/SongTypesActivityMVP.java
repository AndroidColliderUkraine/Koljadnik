package com.androidcollider.koljadnik.song_types;


import android.support.v7.widget.RecyclerView;

import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.models.SongType;

import java.util.List;

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
        void setLinearLayoutManager();
    }

    interface Presenter {
        void setView(View view);
        void initData();
    }

    interface Model {
        void getSongTypes(OnReadListener<List<SongType>> listener);
    }
}
