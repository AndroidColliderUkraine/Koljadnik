package com.androidcollider.koljadnik.presenters;


import com.androidcollider.koljadnik.constants.UiAction;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.song_types.SongTypeViewModel;
import com.androidcollider.koljadnik.song_types.SongTypesActivityMVP;
import com.androidcollider.koljadnik.song_types.SongTypesActivityPresenter;
import com.androidcollider.koljadnik.utils.TestModelsGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SongListPresenterTests {

    private SongTypesActivityMVP.Presenter presenter;
    private List<SongTypeViewModel> testDataList;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SongTypesActivityMVP.Model mockModel;
    @Mock
    private SongTypesActivityMVP.View mockView;

    @Captor
    ArgumentCaptor<OnReadListener<List<SongTypeViewModel>>> callbackCaptor;

    @Before
    public void setup() {
        testDataList = TestModelsGenerator.generateSongTypeViewModelsList(3);

        presenter = new SongTypesActivityPresenter(mockModel);
        presenter.setView(mockView);
    }

    @Test
    public void initWithDataError() {
        presenter.initData();
        verify(mockModel).getSongTypes(callbackCaptor.capture());
        callbackCaptor.getValue().onError("Error");

        verify(mockView, times(1)).showErrorToast(any());
        verify(mockView, never()).updateAdapter(any());
    }

    @Test
    public void initWithDataSuccessWithBlockUi() {
        when(mockModel.getSongTypes(any())).thenReturn(UiAction.BLOCK_UI);

        presenter.initData();
        verify(mockModel).getSongTypes(callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess(testDataList);

        InOrder inOrder = inOrder(mockView);
        inOrder.verify(mockView, times(1)).blockUi();
        inOrder.verify(mockView, atLeast(1)).unblockUi();

        verify(mockView, never()).showErrorToast(any());

        ArgumentCaptor<List<SongTypeViewModel>> captor = ArgumentCaptor.forClass((Class)List.class);
        verify(mockView, times(1)).updateAdapter(captor.capture());
        assertTrue(captor.getValue().size() == 3);
    }

    @Test
    public void initWithDataSuccessWithDoNotBlockUi() {
        when(mockModel.getSongTypes(any())).thenReturn(UiAction.DONT_BLOCK_UI);

        presenter.initData();
        verify(mockModel).getSongTypes(callbackCaptor.capture());
        callbackCaptor.getValue().onSuccess(testDataList);

        verify(mockView, never()).blockUi();
        verify(mockView, never()).showErrorToast(any());

        ArgumentCaptor<List<SongTypeViewModel>> captor = ArgumentCaptor.forClass((Class)List.class);
        verify(mockView, times(1)).updateAdapter(captor.capture());
        assertTrue(captor.getValue().size() == 3);
    }

    @Test
    public void openSongListUi() {
        presenter.openSongListUI(0, "");
        verify(mockView, times(1)).showSongListUI(any(int.class), any(String.class));
    }
}
