package com.androidcollider.koljadnik.song_types;

import dagger.Module;
import dagger.Provides;

@Module
public class SongTypesModule {

    @Provides
    public SongTypesActivityMVP.Presenter provideSongTypesActivityPresenter(SongTypesActivityMVP.Model model){
        return new SongTypesActivityPresenter(model);
    }

    @Provides
    public SongTypesActivityMVP.Model provideSongTypesActivityModel(SongTypesRepository repository){
        return new SongTypesActivityModel(repository);
    }

    @Provides
    public SongTypesRepository provideSongTypesRepository(){
        return new SongTypesFirebaseRepository();
    }
}
