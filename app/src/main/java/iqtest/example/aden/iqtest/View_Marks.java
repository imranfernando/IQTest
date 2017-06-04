package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iqchamp1.pack.iqtest.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class View_Marks extends Activity {

    Context context;
    DBClass dbAccess;
    private Button shareBtn, tellFriendBtn, homeBtn;
    String fb_share_message;
    View_Login login;
    String category;
    Model_Mark currentMark;
    Double averageMark;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    //private static String message = "Sample status posted from android app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.view_login);


        setContentView(R.layout.view_marks);
        context = this;
        dbAccess = new DBClass(context);
        Cursor marksCursor = dbAccess.getAllMarks();

        homeBtn = (Button) findViewById(R.id.home);
        login = new View_Login();

        //Set marks listview
        ArrayList<Model_Mark> marks = new ArrayList<Model_Mark>();
        ArrayList<String> marksString = new ArrayList<String>();
        marksCursor.moveToFirst();
        Double totalMarks = 0.0;
        int postion=0;
        while (marksCursor.isAfterLast()==false){
            currentMark = new Model_Mark(marksCursor.getString(0),Integer.parseInt(marksCursor.getString(1)));
            totalMarks+=currentMark.getMarks();
            marksString.add(postion,currentMark.getMarkString());
            marks.add(postion++,currentMark);
            marksCursor.moveToNext();
        }
        ArrayAdapter<String> marksAdapter = new ArrayAdapter<String>(this,R.layout.view_mark,R.id.markDisplay,marksString);
        ListView marksV = (ListView)findViewById(R.id.marksView);
        marksV.setAdapter(marksAdapter);

        //set Average mark
        averageMark = totalMarks/marks.size();
        TextView averageTextView = (TextView)findViewById(R.id.averageMark);
        if(!averageMark.isNaN()) {
            averageTextView.setText("Your Average : " + String.format("%.2f", averageMark) + "/10");
        } else {
            averageTextView.setText("No Marks Available");
        }

        //Set position
        TextView positionTextView = (TextView)findViewById(R.id.position);
        if(averageMark == 0){
            category = "Feeble-mindedness";
            positionTextView.setText("You fall in the category of Feeble-mindedness");
        }
        else if (averageMark < 2 ){
            category = "Borderline deficiency in intelligence";
            positionTextView.setText("You fall in the category of Borderline deficiency in intelligence");
        }
        else if (averageMark >= 2 && averageMark < 4){
            category = "Dullness";
            positionTextView.setText("You fall in the category of Dullness");
        }
        else if (averageMark >= 4 && averageMark < 5){
            category = "Average";
            positionTextView.setText("You fall in the category of Average");
        }
        else if (averageMark >= 5 && averageMark < 7){
            category = "Superior intelligence";
            positionTextView.setText("You fall in the category of Superior intelligence");
        }
        else if (averageMark >= 7 && averageMark <9){
            category = "Very superior intelligence";
            positionTextView.setText("You fall in the category of Very superior intelligence");
        }
        else if (averageMark>=9){
            category = "Genius";
            positionTextView.setText("You fall in the category of Genius");
        } else {
            category = "NoCat";
            positionTextView.setText("Please try out IQ Test for get your marks");
        }

        shareBtn = (Button) findViewById(R.id.share);
        tellFriendBtn = (Button) findViewById(R.id.tell_friend);
        /*postBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //postStatusMessage();
                //postImage();

            }
        });*/

        buttonsEnabled(false);

    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                buttonsEnabled(true);
                Log.d("View_Login", "Facebook session opened");
            } else if (state.isClosed()) {
                TextView loginErrTextView = (TextView)findViewById(R.id.loginError);
                loginErrTextView.setText("Please Login to Facebook to share from Home page's Login Menu");
                buttonsEnabled(false);
                Log.d("View_Login", "Facebook session closed");
            }
        }
    };

    public void buttonsEnabled(boolean isEnabled) {
        shareBtn.setEnabled(isEnabled);
        tellFriendBtn.setEnabled(isEnabled);
    }

    public void postStatusMessage() {
        if (checkPermissions()) {
            String username = login.get_info();

            if(category.compareTo("NoCat")!=0) {
                fb_share_message = "IQ Test \n" + username + " falls in the category of " + category + "\n" + currentMark.getMarkString() + "\nAverage : " + String.format("%.2f", averageMark);
                Request request = Request.newStatusUpdateRequest(
                        Session.getActiveSession(), fb_share_message, new Request.Callback() {
                            @Override
                            public void onCompleted(Response response) {
                                if (response.getError() == null)
                                    Toast.makeText(View_Marks.this, "Status updated successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                request.executeAsync();
            } else {
                Toast.makeText(View_Marks.this, "Try out IQ test for get your marks", Toast.LENGTH_LONG).show();
            }
        }
        else {
            requestPermissions();
        }
    }

//    public void postImage() {
//        if (login.checkPermissions()) {
//            Bitmap img = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.ic_launcher);
//            Request uploadRequest = Request.newUploadPhotoRequest(
//                    Session.getActiveSession(), img, new Request.Callback() {
//                        @Override
//                        public void onCompleted(Response response) {
//                            Toast.makeText(View_Marks.this,
//                                    "Photo uploaded successfully",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//            uploadRequest.executeAsync();
//        } else {
//            login.requestPermissions();
//        }
//    }

    // When Send Message button is clicked
    public void share(View v) {
        String shareText = "I fall in the category of " + category + ". Average : " + String.format("%.2f", averageMark);

        if (FacebookDialog.canPresentShareDialog(View_Marks.this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(View_Marks.this)
                    .setName("IQ Champ : Check your IQ Now!!")
                    .setLink("https://play.google.com/store/apps/details?id=com.iqchamp1.pack.iqtest&hl=en")
                    .setDescription(shareText)
                    .setPicture("http://iqtest.cu.cc/IQTest/icon.png")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
        else {
            Log.d("View_Marks", "Success!");
        }
    }

    public void tell_friend (View v){
        String shareText = "I fall in the category of " + category + ". Average : " + String.format("%.2f", averageMark);
        FacebookDialog.MessageDialogBuilder builder = new FacebookDialog.MessageDialogBuilder(
                this)
                .setLink("https://play.google.com/store/apps/details?id=com.iqchamp1.pack.iqtest&hl=en")
                .setName("IQ Champ")
                .setCaption("Check your IQ Now!!")
                .setPicture("http://iqtest.cu.cc/IQTest/icon.png")
                .setDescription(shareText);

        // If the Facebook Messenger app is installed and we can present the share dialog
        if (builder.canPresent()) {
            // Enable button or other UI to initiate launch of the Message Dialog
            FacebookDialog dialog = builder.build();
            uiHelper.trackPendingDialogCall(dialog.present());
        } else {
            // Disable button or other UI for Message Dialog
            Toast.makeText(getApplicationContext(), "Facebook Messenger app is not installed in your device, so disabling the button", Toast.LENGTH_SHORT).show();
            v.setEnabled(false);
        }
    }

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        buttonsEnabled(Session.getActiveSession().isOpened());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data,
                new FacebookDialog.Callback() {

                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall,
                                        Exception error, Bundle data) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Occured\nMost Common Errors:\n1. Device not connected to Internet\n2.Faceboook APP Id is not changed in Strings.xml",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(
                            FacebookDialog.PendingCall pendingCall, Bundle data) {
                        Toast.makeText(getApplicationContext(), "Done!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    public void home (View v){
        startActivity(new Intent(View_Marks.this, View_Home.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view__marks, menu);
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
}
