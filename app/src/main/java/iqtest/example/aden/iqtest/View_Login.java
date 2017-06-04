package iqtest.example.aden.iqtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iqchamp1.pack.iqtest.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class View_Login extends Activity  {
    
    Button home, login;
    private LoginButton loginBtn;
    private Button postImageBtn;
    private Button updateStatusBtn;
    Facebook fb;
    Bitmap bitmap;
    ProfilePictureView profile_pic;
    public static String user_name;
    ArrayList<String> user_info;
    //ImageView profile_pic;

    private TextView userName;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private static String message = "Sample status posted from android app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.view_login);

        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        profile_pic = (ProfilePictureView) findViewById(R.id.profile_pic);
        home = (Button) findViewById(R.id.btn_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(View_Login.this, View_Home.class));
            }
        });
        //profile_pic = (ImageView) findViewById(R.id.pro_pic);

        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {

                    //user_info = new ArrayList<>();
                    //user_info.add(user.getName());
                    //user_info.add(user.getId());
                    user_name = user.getName();
                    get_info();

                    userName.setText("Hello, " + user.getName());
                    profile_pic.setVisibility(View.VISIBLE);
                    profile_pic.setDrawingCacheEnabled(true);
                    profile_pic.setProfileId(user.getId());
                } else {
                    userName.setText("You are not logged");
                    profile_pic.setVisibility(View.INVISIBLE);
                }
            }
        });

        /*postImageBtn = (Button) findViewById(R.id.post_image);
        postImageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                postImage();
                /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
                startActivity(Intent.createChooser(sharingIntent,"Share using"));*/

                /*String appLinkUrl, previewImageUrl;

                appLinkUrl = "https://www.mydomain.com/myapplink";
                previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(this, content);
                }
            }
        });

        updateStatusBtn = (Button) findViewById(R.id.update_status);
        updateStatusBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                postStatusMessage();
            }
        });*/

        buttonsEnabled(false);

    }



    public String get_info(){
        if (checkPermissions())
            return user_name;
        else {
            Toast.makeText(View_Login.this, "Logged into Facebook", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                buttonsEnabled(true);
                Log.d("View_Login", "Facebook session opened");
            } else if (state.isClosed()) {
                buttonsEnabled(false);
                Log.d("View_Login", "Facebook session closed");
            }
        }
    };

    public void buttonsEnabled(boolean isEnabled) {
        //postImageBtn.setEnabled(isEnabled);
        //updateStatusBtn.setEnabled(isEnabled);
    }

//    public void postImage() {
//        if (checkPermissions()) {
//            Bitmap img = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.ic_launcher);
//            Request uploadRequest = Request.newUploadPhotoRequest(
//                    Session.getActiveSession(), img, new Request.Callback() {
//                        @Override
//                        public void onCompleted(Response response) {
//                            Toast.makeText(View_Login.this,
//                                    "Photo uploaded successfully",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//            uploadRequest.executeAsync();
//        } else {
//            requestPermissions();
//        }
//    }
//
//    public void postStatusMessage() {
//        if (checkPermissions()) {
//            Request request = Request.newStatusUpdateRequest(
//                    Session.getActiveSession(), message,
//                    new Request.Callback() {
//                        @Override
//                        public void onCompleted(Response response) {
//                            if (response.getError() == null)
//                                Toast.makeText(View_Login.this,
//                                        "Status updated successfully",
//                                        Toast.LENGTH_LONG).show();
//                        }
//                    });
//            request.executeAsync();
//        } else {
//            requestPermissions();
//        }
//    }

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
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }



    /*private void updateProPicImage(){
        if(fb.isSessionValid()){
            loginBtn.setImageResource(R.drawable.logout_button);
        }
        else {
            loginBtn.setImageResource(R.drawable.login_button);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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


    /*public void onClick(View v) {

        if (fb.isSessionValid()){
            //close the session - log out
            try {
                fb.logout(getApplicationContext());
                //updateProPicImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //login to facebook
            fb.authorize(View_Login.this, new Facebook.DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    //updateProPicImage();
                }

                @Override
                public void onFacebookError(FacebookError e) {
                    //Toast.makeText(View_Login.this,"fbError",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(DialogError e) {
                    //Toast.makeText(View_Login.this,"onError",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    //Toast.makeText(View_Login.this,"onCancel",Toast.LENGTH_SHORT).show();
                }
            });
        }

        /*switch (v.getId()){
            case R.id.btn_home : startActivity(new Intent(View_Login.this,View_Home.class));
                break;

        }
    }*/
}
