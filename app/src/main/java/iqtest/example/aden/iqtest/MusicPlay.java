package iqtest.example.aden.iqtest;

import android.media.MediaPlayer;


public class MusicPlay {
    private static MusicPlay mInstance = null;

    private static MediaPlayer mPlay;

    //getter for media player
    public static MediaPlayer getmPlay() {
        return mPlay;
    }

    //setter for media player
    public static void setmPlay(MediaPlayer mPlay) {
        MusicPlay.mPlay = mPlay;
    }

    private MusicPlay(){

    }

    public static MusicPlay getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new MusicPlay();
        }
        return mInstance;
    }
}
