package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.iqchamp1.pack.iqtest.R;


public class View_Difficulty extends Activity implements View.OnClickListener  {

    Button home;
    Context context;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_difficulty);

        context = this;

        linear = (LinearLayout) findViewById(R.id.dif_layout);

        SharedPreferences sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resource", 0); // the second parameter will be fallback if the preference is not found
        if (bg == 1)
            linear.setBackgroundResource(R.drawable.temp_one);
        else if (bg == 2)
            linear.setBackgroundResource(R.drawable.temp_two);
        else if (bg == 3)
            linear.setBackgroundResource(R.drawable.temp_three);

        Button ForwardBtn = (Button) findViewById(R.id.btn_forwardArrow);
        ForwardBtn.setOnClickListener(this);

        //Play Music
        /*BackgroundSound task = new BackgroundSound();
        task.execute();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_difficulty, menu);
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

        RadioGroup diffSelect = (RadioGroup)findViewById(R.id.difficulty);
        int radioButtonID = diffSelect.getCheckedRadioButtonId();
        String difficultyLevel;

        switch (v.getId()){
            case R.id.btn_forwardArrow :
                switch (radioButtonID){
                    case R.id.easy:
                        difficultyLevel = "Easy";
                        break;
                    case  R.id.medium:
                        difficultyLevel = "Medium";
                        break;
                    default:
                        difficultyLevel = "Hard";
                        break;
                }
                //Start activity and put extras
                startActivity(new Intent(View_Difficulty.this,View_Questions.class).putExtra("diffLevel",difficultyLevel));
                break;
        }
    }

    //Play Music
    /*public class BackgroundSound extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MusicPlay.getmPlay();
            player.start();
            return null;
        }
    }*/
}
