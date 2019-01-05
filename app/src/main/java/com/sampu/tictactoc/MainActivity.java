package com.sampu.tictactoc;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import static com.sampu.tictactoc.Sounds.soundPool;

public class MainActivity extends Activity {

    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
        Sounds.soundPool=new SoundPool(10,AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Sounds.LOADED=true;
            }
        });
        Sounds.PLAYER_TURN=soundPool.load(getApplicationContext(),R.raw.user_turn,1);
        Sounds.PLAYER_WIN=soundPool.load(getApplicationContext(),R.raw.user_win,1);
        Sounds.DRAW=soundPool.load(getApplicationContext(),R.raw.draw_game,1);
        Sounds.COMPUTER_TURN=soundPool.load(getApplicationContext(),R.raw.com_turn,1);
        Sounds.COMPUTER_WIN=soundPool.load(getApplicationContext(),R.raw.com_win,1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onplay(View V){
    	Intent i = new Intent(this, GameActivity.class);
    	startActivity(i);
    }
    public void exit(View V){
    	finish();
    }
    
}
