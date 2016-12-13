package com.androidcollider.koljadnik;


import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;
import com.androidcollider.koljadnik.song_types.SongTypesActivityPresenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SongListPresenterTests {

    SongTypesActivityMVP.Model mockModel;
    SongTypesActivityMVP.View mockView;
    SongTypesActivityPresenter presenter;
    List<SongType> songTypeList;

    @Before
    public void setup(){
       /* mockModel = mock(SongTypesActivityMVP.Model.class);

        songTypeList = new ArrayList<>();
        songTypeList.add(new SongType(1, "dfsdf", 24));
        songTypeList.add(new SongType(2, "ffew", 4));
        songTypeList.add(new SongType(3, "btrbbtb", 655));

        when(mockModel.getSongTypes(null)).thenCallRealMethod();

        mockView = mock(SongTypesActivityMVP.View.class);

        presenter = new SongTypesActivityPresenter(mockModel);

        presenter.setView(mockView);*/
    }

    @Test
    public void noInteractionsWithView(){
       /* presenter.initData();
        verifyNoMoreInteractions(mockView);*/
    }
}
