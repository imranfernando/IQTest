package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.iqchamp1.pack.iqtest.R;


public class View_Home extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Button login, option, quickStart, help, exit;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home);

        audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        option = (Button) findViewById(R.id.btn_options);
        option.setOnClickListener(this);
        quickStart = (Button) findViewById(R.id.btn_quickStart);
        quickStart.setOnClickListener(this);
        help = (Button) findViewById(R.id.btn_help);
        help.setOnClickListener(this);
        exit = (Button) findViewById(R.id.btn_exit);
        exit.setOnClickListener(this);

        //Play Music
        /*BackgroundSound task = new BackgroundSound();
        task.execute();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login :
                //if(isNetworkAvailable()) {
                startActivity(new Intent(View_Home.this, View_Login.class));
                //}else {
                //    Toast.makeText(getApplicationContext(), "Please connect to the internet first", Toast.LENGTH_LONG).show();
                //}
                break;
            case R.id.btn_options : startActivity(new Intent(View_Home.this,View_Options.class));
                break;
            case R.id.btn_quickStart :
                //if(isNetworkAvailable()) {
                startActivity(new Intent(View_Home.this, View_Difficulty.class));
                //}else {
                //    Toast.makeText(getApplicationContext(), "Please connect to the internet first", Toast.LENGTH_LONG).show();
                //}
                break;
            case R.id.btn_help : startActivity(new Intent(View_Home.this,View_Help.class));
                break;
            /*case R.id.btn_sound :
                final Dialog dialog = new Dialog(this);

                dialog.setContentView(R.layout.view_soundcontrol);
                dialog.setTitle("Volume Control");

                //Set Volume Seekbar
                SeekBar soundControlSeek = (SeekBar) dialog.findViewById(R.id.soundController);
                soundControlSeek.setMax(100);

                soundControlSeek.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                soundControlSeek.setOnSeekBarChangeListener(this);

                dialog.show();

                break;*/
            case R.id.btn_exit :
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    //Method to check internet connection availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.soundController:
                //Change volume
                //MediaPlayer player = MusicPlay.getmPlay();
                //player.setVolume(progress,progress);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);

                //Toast.makeText(getApplicationContext(), progress,
                // Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //Play Music
    /*public class BackgroundSound extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MusicPlay.getmPlay();
            if(player==null) {
                player = MediaPlayer.create(View_Home.this, R.raw.track1);
                MusicPlay.setmPlay(player);
                player.setLooping(true); // Set looping
                player.start();
            }
            return null;
        }
    }*/
}
