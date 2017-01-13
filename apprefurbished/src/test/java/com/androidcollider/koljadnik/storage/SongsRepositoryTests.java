package com.androidcollider.koljadnik.storage;

import com.androidcollider.koljadnik.contants.Settings;
import com.androidcollider.koljadnik.listeners.OnReadListener;
import com.androidcollider.koljadnik.listeners.OnWriteListener;
import com.androidcollider.koljadnik.models.SongType;
import com.androidcollider.koljadnik.storage.files.AssetsTextDataManager;
import com.androidcollider.koljadnik.storage.local.SongsLocalDataSource;
import com.androidcollider.koljadnik.storage.remote.SongsRemoteDataSource;
import com.androidcollider.koljadnik.storage.shared_prefs.SharedPreferencesManager;
import com.androidcollider.koljadnik.utils.ConnectionInternetManager;
import com.androidcollider.koljadnik.utils.TestModelsGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Severyn Parkhomenko <pseverin@ukr.net>
 */
public class SongsRepositoryTests {
    private final static long TIME_PROCESS_TOLERANCE = TimeUnit.SECONDS.toMillis(5);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SongsLocalDataSource songsLocalDataSource;
    @Mock
    private SongsRemoteDataSource songsRemoteDataSource;
    @Mock
    private SharedPreferencesManager sharedPreferencesManager;
    @Mock
    private ConnectionInternetManager connectionInternetManager;
    @Mock
    private AssetsTextDataManager assetsTextDataManager;

    private SongsDataSource repository;
    private List<SongType> testSongTypesDataList;

    @Before
    public void setup() {
        repository = new SongsRepository(
                songsLocalDataSource,
                songsRemoteDataSource,
                sharedPreferencesManager,
                connectionInternetManager,
                assetsTextDataManager);

        testSongTypesDataList = TestModelsGenerator.generateSongTypeList(3);
    }

    @Test
    public void songTypesChooseRepositoryTypeRemote() {
        when(connectionInternetManager.isNetworkConnected()).thenReturn(true);
        when(sharedPreferencesManager.getLastUpdateForClass(SongType.class)).thenReturn(0L);

        repository.getSongTypes(testOnReadListener);

        long currentTime = System.currentTimeMillis();
        verify(sharedPreferencesManager).setLastUpdateForClass(eq(SongType.class),
                or(gt(currentTime - TIME_PROCESS_TOLERANCE),leq(currentTime + TIME_PROCESS_TOLERANCE)));
        verify(songsRemoteDataSource).getSongTypes(or(gt(currentTime - TIME_PROCESS_TOLERANCE),leq(currentTime + TIME_PROCESS_TOLERANCE)), any());
    }

    @Test
    public void songTypesChooseRepositoryTypeLocal_internet() {
        when(connectionInternetManager.isNetworkConnected()).thenReturn(false);

        repository.getSongTypes(testOnReadListener);

        verify(songsLocalDataSource).getSongTypes();
    }

    @Test
    public void songTypesChooseRepositoryTypeLocal_updateTime() {
        when(connectionInternetManager.isNetworkConnected()).thenReturn(true);
        when(sharedPreferencesManager.getLastUpdateForClass(SongType.class)).thenReturn(getTestLastUpdateTime());

        repository.getSongTypes(testOnReadListener);

        verify(songsLocalDataSource).getSongTypes();
    }

    @Test
    public void songTypesChooseRepositoryMemory() {
        when(connectionInternetManager.isNetworkConnected()).thenReturn(true);
        when(sharedPreferencesManager.getLastUpdateForClass(SongType.class)).thenReturn(0L);

        repository.getSongTypes(testOnReadListener);

        ArgumentCaptor<OnReadListener<List<SongType>>> captorRead = ArgumentCaptor.forClass((Class)OnReadListener.class);
        verify(songsRemoteDataSource).getSongTypes(any(long.class), captorRead.capture());
        captorRead.getValue().onSuccess(testSongTypesDataList);

        ArgumentCaptor<OnWriteListener> captorWrite = ArgumentCaptor.forClass((Class)OnWriteListener.class);
        verify(songsLocalDataSource).saveSongTypes(eq(testSongTypesDataList), captorWrite.capture());
        captorWrite.getValue().onSuccess();

        verify(songsLocalDataSource).getSongTypes();


        when(sharedPreferencesManager.getLastUpdateForClass(SongType.class)).thenReturn(getTestLastUpdateTime());

        reset(songsRemoteDataSource);
        reset(songsLocalDataSource);
        repository.getSongTypes(testOnReadListener);
        verify(songsRemoteDataSource, never()).getSongTypes(any(long.class), any());
        verify(songsLocalDataSource, never()).getSongTypes();
    }


    private long getTestLastUpdateTime(){
        return System.currentTimeMillis() - Settings.DELTA_TIME_FOR_UPDATE + TIME_PROCESS_TOLERANCE;
    }

    private OnReadListener<List<SongType>> testOnReadListener = new OnReadListener<List<SongType>>() {
        @Override
        public void onSuccess(List<SongType> result) {

        }

        @Override
        public void onError(String error) {

        }
    };
}
