package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.iqchamp1.pack.iqtest.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import java.util.ArrayList;
import java.util.HashMap;


public class View_Finish extends Activity implements View.OnClickListener {

    DBClass dbAccess;
    Context context;
    ResultsAdapter listAdapter;
    ExpandableListView listView;
    ArrayList<String> questionsNamesList;
    HashMap<String, ArrayList<String>> questionsDetailsList;
    AlertDialog alertDialog;
    Button homeButton;
    String category;
    int total;
    View_Login login;
    GraphUser user;
    String fb_share_message;
    int noOfCorrectAnswers;
    private UiLifecycleHelper uiHelper;
    ArrayList<String> user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.view_finish);

        login = new View_Login();

        Button IqButton = (Button) findViewById(R.id.button_myIq);
        IqButton.setOnClickListener(this);

        homeButton = (Button) findViewById(R.id.button_home);
        homeButton.setOnClickListener(this);

        ArrayList<Model_Question> answeredQuestionList = (ArrayList<Model_Question>)getIntent().getSerializableExtra("QuestionsList");

        context = this;

        listView = (ExpandableListView) findViewById(R.id.results_list);
        questionsNamesList = new ArrayList<String>();
        questionsDetailsList = new HashMap<String, ArrayList<String>>();

        listAdapter = new ResultsAdapter(context, questionsNamesList, questionsDetailsList);
        listView.setAdapter(listAdapter);

        int QuestionNo = 1;
        int index = 0;

        noOfCorrectAnswers=0;
        String checkAnswer = "";

        //Check no of correct answers
        for(Model_Question q : answeredQuestionList){
            if(q.isSelectedAnswerCorrect()) {
                checkAnswer = "Correct";
                noOfCorrectAnswers++;
            }
            else
                checkAnswer = "Wrong";

            if(QuestionNo == 10) {
                questionsNamesList.add("Question " + QuestionNo++ + "\t\t\t\t\t" + checkAnswer);
            }
            else
                questionsNamesList.add("Question " + QuestionNo++ + "\t\t\t\t\t\t" + checkAnswer);

            ArrayList<String> details = new ArrayList<String>();
            details.add(q.getQuestion());
            details.add("Correct Answer: " + q.getAnswers().get(q.getCorrectAnswer() - 1) + "\nHint : \n" + q.getTips());

            questionsDetailsList.put(questionsNamesList.get(index++), details);
        }
        dbAccess = new DBClass(context);
        dbAccess.insertMarks(noOfCorrectAnswers);

        TextView marks = (TextView)findViewById(R.id.Marks);
        marks.setText(noOfCorrectAnswers+ " Correct out of 10 Questions");

        listAdapter.notifyDataSetChanged();

        total = noOfCorrectAnswers;

        if(total == 0){
            category = "Feeble-mindedness";
        }
        else if (total == 1){
            category = "Borderline deficiency in intelligence";
        }
        else if (total == 2 || total == 3){
            category = "Dullness";
        }
        else if (total == 4 || total == 5){
            category = "Average";
        }
        else if (total == 6 || total == 7){
            category = "Superior intelligence";
        }
        else if (total == 8 || total == 9){
            category = "Very superior intelligence";
        }
        else {
            category = "Genius ";
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
//                            Toast.makeText(View_Finish.this,
//                                    "Photo uploaded successfully",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//            uploadRequest.executeAsync();
//        } else {
//            login.requestPermissions();
//        }
//    }
//
//    public void postStatusMessage() {
//        if (login.checkPermissions()) {
//            //user_info = new ArrayList<>();
//            //if (user_info != null) {
//               //user_info = login.getUser_info();
//            //}
//            String n = login.get_info();
//
//            fb_share_message =  n + " scored " + noOfCorrectAnswers;
//            Request request = Request.newStatusUpdateRequest(
//                    Session.getActiveSession(), fb_share_message,
//                    new Request.Callback() {
//                        @Override
//                        public void onCompleted(Response response) {
//                            if (response.getError() == null)
//                                Toast.makeText(View_Finish.this,
//                                        "Status updated successfully",
//                                        Toast.LENGTH_LONG).show();
//                            else
//                                Toast.makeText(View_Finish.this,
//                                        "Login error",
//                                        Toast.LENGTH_LONG).show();
//                        }
//
//                    });
//            request.executeAsync();
//        } else {
//            login.requestPermissions();
//        }
//    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("View_Login", "Facebook session opened");
            } else if (state.isClosed()) {
                Log.d("View_Login", "Facebook session closed");
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view__finish, menu);
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

        if(v.getId() == R.id.button_myIq) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your IQ test score is " + total + "!");
            builder.setMessage("You fall in the category of '" + category + "'");

            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(View_Finish.this, View_Difficulty.class));
                }
            });

            builder.setNegativeButton("History", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(View_Finish.this, View_Marks.class));
                }
            });

            alertDialog = builder.create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            alertDialog.show();
        }



        switch (v.getId()){
            case R.id.button_home : startActivity(new Intent(View_Finish.this, View_Home.class));
                break;
        }

    }
    @Override
    public void onDestroy() {
        if (alertDialog!=null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        super.onDestroy();
    }
}
