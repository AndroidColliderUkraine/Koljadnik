package com.androidcollider.koljadnik;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidcollider.koljadnik.adapters.SongTypeAdapter;
import com.androidcollider.koljadnik.adapters.SortTypeAdapter;
import com.androidcollider.koljadnik.database.DataSource;
import com.androidcollider.koljadnik.objects.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SubmitActivity extends Activity {

    private final static String TAG = "Андроідний Коллайдер";
    private String[] submitArray;
    private Spinner spinner_submit;
    private EditText et_submit;
    private Button b_submit;
    private final static String[] MAILS = new String[]{"android.collider@gmail.com"};

   /* private TextView tv_song_title, tv_song_text, tv_song_remarks, tv_song_source, tv_song_comments;
    private DataSource dataSource;
    private Song song;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        submitArray = getResources().getStringArray(R.array.submit_array);
        initFields();

        List<String> list = Arrays.asList(submitArray);
        ArrayList<String> submitList = new ArrayList<>();
        submitList.addAll(list);

        SortTypeAdapter adapter = new SortTypeAdapter(this, submitList);
        //ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,submitArray);
        spinner_submit.setAdapter(adapter);
    }

    private void initFields(){
        spinner_submit = (Spinner)findViewById(R.id.spinner_submit);
        et_submit = (EditText)findViewById(R.id.et_submit_field);
        b_submit = (Button)findViewById(R.id.b_submit);
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        /*tv_song_title = (TextView)findViewById(R.id.tv_song_title);
        tv_song_title.setText(song.getName());*/

        /*tv_song_comments = (TextView)findViewById(R.id.tv_song_comments);
        if (song.getComments()!=null){
            for (String comment: song.getComments()){
                if (tv_song_comments.getText().toString()!=null){
                    tv_song_comments.setText(tv_song_comments.getText().toString()+"     "+ comment);
                }else {
                    tv_song_comments.setText(comment);
                }
            }
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_song_types, menu);
        //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.route_saver_actionbar_background));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    private void sendEmail(){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        // Кому
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                MAILS);
        // Зачем
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                (String)spinner_submit.getSelectedItem());
        // О чём
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                et_submit.getText().toString());
        // С чем
        /*emailIntent.putExtra(
                android.content.Intent.EXTRA_STREAM,
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory()
                        + "/Клипы/SOTY_ATHD.mp4"));*/

        //emailIntent.setType("text/video");
        // Поехали!
        startActivity(Intent.createChooser(emailIntent,
                "Відправка листа..."));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

}
