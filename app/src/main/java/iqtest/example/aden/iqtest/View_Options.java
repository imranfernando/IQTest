package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.iqchamp1.pack.iqtest.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class View_Options extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener  {
    //Button home;
    //SeekBar
    SeekBar seekBar;
    Button aboutUs, historyBtn, template;
    Context context;
    //public static final String MY_PREF = "my_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_options);
        context=this;

        seekBar = (SeekBar)findViewById(R.id.brightness);
        seekBar.setMax(100);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(this);

        aboutUs = (Button)findViewById(R.id.btn_aboutUs);
        aboutUs.setOnClickListener(this);
        //home=(Button)findViewById(R.id.btn_home);

        //home.setOnClickListener(this);
        addListenerOnRatingBar();

        historyBtn = (Button) findViewById(R.id.btn_history);
        historyBtn.setOnClickListener(this);

        template = (Button) findViewById(R.id.button_template);
        template.setOnClickListener(this);
    }

    public void addListenerOnRatingBar() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String url = "http://iqtest.cu.cc/IQTest/Rating.php?rating="+String.valueOf(rating);
                                WebService task = new WebService();
                                // passes values for the urls string array
                                task.execute(url);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you like to rate IQ Champ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
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

    public class WebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);
            try {
                HttpResponse response = client.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Thanks for your rating!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_home : startActivity(new Intent(View_Options.this,View_Home.class));
                break;
            case R.id.btn_aboutUs :
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.view_aboutdevelopers,(ViewGroup) findViewById(R.id.developedDetails));
                Toast toast = new Toast(this);
                toast.setView(view);
                toast.show();
                break;

            case R.id.btn_history : startActivity(new Intent(View_Options.this, View_Marks.class));
                break;

            case R.id.button_template :
                /*SharedPreferences sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("background_resource", 2);
                editor.commit();*/
                startActivity(new Intent(View_Options.this, View_Templates.class));
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Change the brighness of the screen
        Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = progress;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
