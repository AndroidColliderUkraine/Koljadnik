package com.androidcollider.koljadnik.songs_list;

import com.androidcollider.koljadnik.storage.SongsDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class SongsModule {


    private int typeId;

    public SongsModule(int typeId) {
        this.typeId = typeId;
    }

    @Provides
    int provideTypeId() {
        return typeId;
    }

    @Provides
    public SongsActivityMVP.Presenter provideSongsActivityPresenter(SongsActivityMVP.Model model) {
        return new SongsActivityPresenter(model);
    }

    @Provides
    public SongsActivityMVP.Model provideSongTypesActivityModel(SongsDataSource repository) {
        return new SongsActivityModel(repository, typeId);
    }
}
