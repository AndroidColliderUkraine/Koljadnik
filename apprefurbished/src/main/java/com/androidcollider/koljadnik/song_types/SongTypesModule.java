package com.androidcollider.koljadnik.song_types;

import com.androidcollider.koljadnik.repository.SongTypesDataSource;
import com.androidcollider.koljadnik.repository.SongTypesFirebaseDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class SongTypesModule {

    @Provides
    public SongTypesActivityMVP.Presenter provideSongTypesActivityPresenter(SongTypesActivityMVP.Model model){
        return new SongTypesActivityPresenter(model);
    }

    @Provides
    public SongTypesActivityMVP.Model provideSongTypesActivityModel(SongTypesDataSource repository){
        return new SongTypesActivityModel(repository);
    }

    @Provides
    public SongTypesDataSource provideSongTypesRepository(){
        return new SongTypesFirebaseDataSource();
    }
}
