package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.iqchamp1.pack.iqtest.R;


public class View_Templates extends Activity implements View.OnClickListener {

    ImageButton defaultBtn, tempOneBtn, tempTwoBtn;
    Context context;
    AlertDialog alertDialog;
    LinearLayout linear;
    Drawable.ConstantState cons = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_templates);

        context = this;

        defaultBtn = (ImageButton) findViewById(R.id.temp_one);
        defaultBtn.setOnClickListener(this);

        tempOneBtn = (ImageButton) findViewById(R.id.temp_two);
        tempOneBtn.setOnClickListener(this);

        tempTwoBtn = (ImageButton) findViewById(R.id.temp_three);
        tempTwoBtn.setOnClickListener(this);

        linear = (LinearLayout) findViewById(R.id.template_layout);

        if (linear.getBackground() == null){
            linear.setBackgroundResource(R.drawable.temp_one);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view__templates, menu);
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

        //final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        //final Drawable wallpaper = wallpaperManager.getDrawable();
        cons = linear.getBackground().getConstantState();

        SharedPreferences sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        switch (v.getId()){

            case R.id.temp_one:
                linear.setBackgroundResource(R.drawable.temp_one);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Default Theme");
                builder.setMessage("Apply theme?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putInt("background_resource", 1);
                        editor.commit();
                        //linear.setBackgroundResource(R.drawable.temp_one);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(linear.getBackground() == null ) {
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_one).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_two).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_two);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else {
                            linear.setBackgroundResource(R.drawable.temp_three);
                            //cons = linear.getBackground().getConstantState();
                        }
                    }
                });

                alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alertDialog.show();

                break;

            case R.id.temp_two:
                linear.setBackgroundResource(R.drawable.temp_two);
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Theme one");
                builder.setMessage("Apply theme?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putInt("background_resource", 2);
                        editor.commit();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(linear.getBackground() == null ) {
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_one).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_two).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_two);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else {
                            linear.setBackgroundResource(R.drawable.temp_three);
                            //cons = linear.getBackground().getConstantState();
                        }
                    }
                });

                alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alertDialog.show();

                break;

            case R.id.temp_three:
                linear.setBackgroundResource(R.drawable.temp_three);
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Theme two");
                builder.setMessage("Apply theme?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putInt("background_resource", 3);
                        editor.commit();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(linear.getBackground() == null ) {
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_one).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_one);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else if (cons.equals(getResources().getDrawable(R.drawable.temp_two).getConstantState())){
                            linear.setBackgroundResource(R.drawable.temp_two);
                            //cons = linear.getBackground().getConstantState();
                        }
                        else {
                            linear.setBackgroundResource(R.drawable.temp_three);
                            //cons = linear.getBackground().getConstantState();
                        }
                    }
                });

                alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                alertDialog.show();

                break;

        }

    }
}
