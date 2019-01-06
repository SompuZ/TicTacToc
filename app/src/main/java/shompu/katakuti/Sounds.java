package shompu.katakuti;

import android.media.SoundPool;

/**
 * Created by Sampu on 05-01-2019.
 */

public class Sounds {
    public static boolean LOADED=false;

    public static int PLAYER_TURN;
    public static int PLAYER_WIN;
    public static int DRAW;
    public static int COMPUTER_TURN;
    public static int COMPUTER_WIN;

    public static SoundPool soundPool;

    public static void playSound(int soundId){

//        Commenting as of now. Will not use sounds.
//
//       if(LOADED){
//           soundPool.play(soundId,1,1,1,0,1f);
//       }
    }
}
