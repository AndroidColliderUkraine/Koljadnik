package com.androidcollider.koljadnik.song_types;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidcollider.koljadnik.R;
import com.androidcollider.koljadnik.common.CommonToolbarActivity;
import com.androidcollider.koljadnik.root.App;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import butterknife.BindView;


public class SongTypesActivity extends CommonToolbarActivity implements SongTypesActivityMVP.View {

    @BindView(R.id.rv_types)
    RecyclerView rvTypes;

    @Inject
    SongTypesActivityMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getComponent().inject(this);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_song_types;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.main_menu;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.initData();
    }

    @Override
    public void setAdapterToList(RecyclerView.Adapter adapter) {
        rvTypes.setAdapter(adapter);
    }

    @Override
    public void showErrorToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLinearLayoutManager() {
        rvTypes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_add) {
            encryptDecrypt();
        }
        return true;
    }

    private void encryptDecrypt(){
        try {
            File sourceFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/dk.png");

            FileInputStream fis = new FileInputStream(sourceFile);

            File outfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/dk_encrypted.png");
            int read;
            if(!outfile.exists()) {
                outfile.createNewFile();
            }
            File decfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/dk_decrypted.png");
            if(!decfile.exists()) {
                decfile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outfile);
            FileInputStream encfis = new FileInputStream(outfile);
            FileOutputStream decfos = new FileOutputStream(decfile);
            Cipher encipher = Cipher.getInstance("AES");
            Cipher decipher = Cipher.getInstance("AES");
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecretKey skey = kgen.generateKey();
            encipher.init(Cipher.ENCRYPT_MODE, skey);
            CipherInputStream cis = new CipherInputStream(fis, encipher);
            decipher.init(Cipher.DECRYPT_MODE, skey);
            CipherOutputStream cos = new CipherOutputStream(decfos,decipher);
            while((read = cis.read())!=-1)
            {
                fos.write((char)read);
                fos.flush();
            }
            fos.close();
            while((read=encfis.read())!=-1)
            {
                cos.write(read);
                cos.flush();
            }
            cos.close();
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e){
            e.printStackTrace();
        }
    }
}
