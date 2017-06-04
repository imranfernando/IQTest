package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iqchamp1.pack.iqtest.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Services.InternetCheck;


public class View_Questions extends Activity implements View.OnClickListener  {

    ArrayList<Model_Question> Questions;
    TextView qNo;
    //Variable to track selected question
    int lastSelectedQuestion;
    String difficulty;
    CountDownTimer timerForQuestions;

    LinearLayout linear;
    Context context;
    Dialog ratingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_questions);

        context = this;

        ratingDialog = new Dialog(context);
        ratingDialog.setCanceledOnTouchOutside(false);
        ratingDialog.setContentView(R.layout.question_loading_progress);

        ratingDialog.setTitle("Please wait!");

        linear = (LinearLayout) findViewById(R.id.layout_questions);

        SharedPreferences sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resource", 0); // the second parameter will be fallback if the preference is not found
        if (bg == 1)
            linear.setBackgroundResource(R.drawable.temp_one);
        else if (bg == 2)
            linear.setBackgroundResource(R.drawable.temp_two);
        else if (bg == 3)
            linear.setBackgroundResource(R.drawable.temp_three);

        qNo = (TextView) findViewById(R.id.qNo);

        difficulty = getIntent().getStringExtra("diffLevel");
        if(InternetCheck.isNetworkAvailable(context)) {
            ratingDialog.show();
            //Set questions according to the difficulty
            switch (difficulty) {
                case "Easy":
                    accessWebService("http://takelk.com/iqtest/EasyQuestions.php");
                    break;
                case "Medium":
                    accessWebService("http://takelk.com/iqtest/HardQuestions.php");
                    break;
                case "Hard":
                    accessWebService("http://takelk.com/iqtest/MediumQuestions.php");
                    break;
            }

            //Set Question navigation buttons
            Button Question1 = (Button) findViewById(R.id.q1);
            Question1.setOnClickListener(this);
            buttonDisable(Question1);

            Button Question2 = (Button) findViewById(R.id.q2);
            Question2.setOnClickListener(this);
            buttonDisable(Question2);

            Button Question3 = (Button) findViewById(R.id.q3);
            Question3.setOnClickListener(this);
            buttonDisable(Question3);

            Button Question4 = (Button) findViewById(R.id.q4);
            Question4.setOnClickListener(this);
            buttonDisable(Question4);

            Button Question5 = (Button) findViewById(R.id.q5);
            Question5.setOnClickListener(this);
            buttonDisable(Question5);

            Button Question6 = (Button) findViewById(R.id.q6);
            Question6.setOnClickListener(this);
            buttonDisable(Question6);

            Button Question7 = (Button) findViewById(R.id.q7);
            Question7.setOnClickListener(this);
            buttonDisable(Question7);

            Button Question8 = (Button) findViewById(R.id.q8);
            Question8.setOnClickListener(this);
            buttonDisable(Question8);

            Button Question9 = (Button) findViewById(R.id.q9);
            Question9.setOnClickListener(this);
            buttonDisable(Question9);

            Button Question10 = (Button) findViewById(R.id.q10);
            Question10.setOnClickListener(this);
            buttonDisable(Question10);

            //Set Home Button
            Button HomeButton = (Button) findViewById(R.id.btn_home);
            HomeButton.setOnClickListener(this);
            buttonDisable(HomeButton);

            //Finish Button
            Button FinishButton = (Button) findViewById(R.id.btn_finish);
            FinishButton.setOnClickListener(this);
            buttonDisable(FinishButton);

            Button PreviousButton = (Button) findViewById(R.id.btn_previous);
            PreviousButton.setOnClickListener(this);
            buttonDisable(PreviousButton);

            Button NextButton = (Button) findViewById(R.id.btn_next);
            NextButton.setOnClickListener(this);
            buttonDisable(NextButton);
        } else {
            Toast.makeText(context, "Internet Connection Error!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questions, menu);
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

    //Start activity to display results
    private void DisplayResultsPage(){
        timerForQuestions.cancel();
        //Set last question's answer to counter variable
        SetQuestion(lastSelectedQuestion);

        Intent intent = new Intent(View_Questions.this, View_Finish.class);
        intent.putExtra("QuestionsList",Questions);
        startActivity(intent);
    }

    public void onClick(View v) {

        int Previousno;
        int NextNo;
        if(v.getId() == R.id.btn_previous) {
            Previousno = Integer.parseInt(qNo.getText().toString()) - 2;
            if (Previousno == -1) {
                Previousno = 9;
            }
            qNo.setText(String.valueOf(Previousno+1));
            SetQuestion(Previousno);
        }
        if(v.getId() == R.id.btn_next){
            NextNo = Integer.parseInt(qNo.getText().toString());
            if (NextNo == 10) {
                NextNo = 0;
            }
            qNo.setText(String.valueOf(NextNo+1));
            SetQuestion(NextNo);
        }

        switch (v.getId()){
            //Home Button
            case R.id.btn_home :
                startActivity(new Intent(View_Questions.this,View_Home.class));
                timerForQuestions.cancel();
                finish();
                break;
            //Early Finish Button
            case R.id.btn_finish : DisplayResultsPage();
                timerForQuestions.cancel();
                finish();
                break;
            //Bind questions according to the button
            case R.id.q1 :
                SetQuestion(0);
                qNo.setText("1");
                break;
            case R.id.q2 :
                SetQuestion(1);
                qNo.setText("2");
                break;
            case R.id.q3 :
                SetQuestion(2);
                qNo.setText("3");
                break;
            case R.id.q4 :
                SetQuestion(3);
                qNo.setText("4");
                break;
            case R.id.q5 :
                SetQuestion(4);
                qNo.setText("5");
                break;
            case R.id.q6 :
                SetQuestion(5);
                qNo.setText("6");
                break;
            case R.id.q7 :
                SetQuestion(6);
                qNo.setText("7");
                break;
            case R.id.q8 :
                SetQuestion(7);
                qNo.setText("8");
                break;
            case R.id.q9 :
                SetQuestion(8);
                qNo.setText("9");
                break;
            case R.id.q10 :
                SetQuestion(9);
                qNo.setText("10");
                break;
        }
    }

    //Set specified Question
    private void SetQuestion(int qNo){
        Model_Question q = Questions.get(qNo);

        TextView qTextView = (TextView)findViewById(R.id.question);
        //Clear textview
        qTextView.clearComposingText();
        qTextView.setText(q.getQuestion());

        RadioGroup answersGroup = (RadioGroup) findViewById(R.id.answers);

        //Check radio group is empty or not selected
        if(answersGroup.getCheckedRadioButtonId()!=-1) {
            /*Model_Question lastQuestion = Questions.remove(lastSelectedQuestion);
            int selectedAns = answersGroup.getCheckedRadioButtonId();
            lastQuestion.setSelectedAnswer(++selectedAns);
            Questions.add(lastSelectedQuestion,lastQuestion);*/
            int selectedAns = answersGroup.getCheckedRadioButtonId();
            Questions.get(lastSelectedQuestion).setSelectedAnswer(++selectedAns);
        }

        //Clear radiogroup
        answersGroup.removeAllViews();
        int noOfAnswers = q.getAnswers().size();

        for(int currentAns = 0;currentAns<noOfAnswers;currentAns++){
            RadioButton ansRadiobutton = new RadioButton(View_Questions.this);
            ansRadiobutton.setTextColor(Color.BLACK);
            ansRadiobutton.setId(currentAns);
            ansRadiobutton.setText(q.getAnswers().get(currentAns));
            answersGroup.addView(ansRadiobutton);
        }

        //Check current question has selected answer or not
        if(q.getSelectedAnswer()!=-1){
            answersGroup.check(q.getSelectedAnswer()-1);
        }else{
            answersGroup.check(-1);
        }
        lastSelectedQuestion = qNo;
    }

    public class WebService extends AsyncTask<String, Void, String> {
        String jsonResult;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder questionString = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    questionString.append(rLine);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return questionString;
        }

        @Override
        protected void onPostExecute(String result) {
            //textView.setText(jsonResult.toString());
            QuestionListPreparation();
            //Display first question
            SetQuestion(0);

            final TextView timerTextView = (TextView)findViewById(R.id.timeRemaining);
            //get total time for questions
            long totalTime=0;
            for(Model_Question q:Questions) {
                totalTime+=q.getTimeLimit();
            }
            //Convert to miliseconds
            totalTime*=1000;
            //Set CountDownTimer
            timerForQuestions = new CountDownTimer(totalTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    int remTime = (int)millisUntilFinished / 1000;
                    int remMinites = remTime/60;
                    int remSeconds = remTime%60;

                    if(remMinites!=0){
                        timerTextView.setText("Time Remaining : " + remMinites + " mins and " + remSeconds + " seconds");
                    } else {
                        timerTextView.setText("Time Remaining : " + remSeconds + " seconds");
                    }
                }

                public void onFinish() {
                    DisplayResultsPage();
                }
            }.start();

            //Enable buttons
            Button Question1 = (Button) findViewById(R.id.q1);
            buttonEnable(Question1);

            Button Question2 = (Button) findViewById(R.id.q2);
            buttonEnable(Question2);

            Button Question3 = (Button) findViewById(R.id.q3);
            buttonEnable(Question3);

            Button Question4 = (Button) findViewById(R.id.q4);
            buttonEnable(Question4);

            Button Question5 = (Button) findViewById(R.id.q5);
            buttonEnable(Question5);

            Button Question6 = (Button) findViewById(R.id.q6);
            buttonEnable(Question6);

            Button Question7 = (Button) findViewById(R.id.q7);
            buttonDisable(Question7);

            Button Question8 = (Button) findViewById(R.id.q8);
            buttonEnable(Question8);

            Button Question9 = (Button) findViewById(R.id.q9);
            buttonEnable(Question9);

            Button Question10 = (Button) findViewById(R.id.q10);
            buttonEnable(Question10);

            //Set Home Button
            Button HomeButton = (Button) findViewById(R.id.btn_home);
            buttonEnable(HomeButton);

            //Finish Button
            Button FinishButton = (Button) findViewById(R.id.btn_finish);
            buttonEnable(FinishButton);

            Button PreviousButton = (Button) findViewById(R.id.btn_previous);
            buttonEnable(PreviousButton);

            Button NextButton = (Button) findViewById(R.id.btn_next);
            buttonEnable(NextButton);

            ratingDialog.dismiss();
        }

        public void QuestionListPreparation() {
            Questions = new ArrayList<Model_Question>();
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("questions");

                int position=0;
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String question = (i+1) + ". " + jsonChildNode.optString("question");
                    String ans = jsonChildNode.optString("answers");
                    //Set Answers (Seperated by "," in the db)
                    String[] answersArr = ans.split(",");
                    ArrayList<String> answers = new ArrayList<String>();
                    for(int j=0;j<answersArr.length;j++) {
                        answers.add(j, answersArr[j]);
                    }

                    int correctAns = Integer.parseInt(jsonChildNode.optString("correctAnswer"));
                    double timeLimit = Double.parseDouble(jsonChildNode.optString("timeLimit"));
                    String tip = jsonChildNode.optString("tip");

                    //Create new question
                    Model_Question q = new Model_Question(question, answers, correctAns, timeLimit, tip);

                    //Add current question to List
                    Questions.add(position++, q);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void accessWebService(String url) {
        WebService task = new WebService();
        // passes values for the urls string array
        task.execute(url);
    }

    //Method to disable the button
    public void buttonDisable(Button button) {
        button.setEnabled(false);
    }

    //Method to enable the button
    public void buttonEnable(Button button){
        button.setEnabled(true);
    }
}


