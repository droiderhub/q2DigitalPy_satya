package com.tarang.dpq2.base.terminal_sdk.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import com.tarang.dpq2.R;
import com.tarang.dpq2.base.Logger;

/**
 * android SoundPool
 *
 * @author chenkh
 * @date 2015-7-29
 * @time 上午9:24:18
 */
public class SoundPoolImpl {

    private static SoundPoolImpl INSTANCE;

    private SoundPool soundPool;
    private SoundPool soundPool2;
    private SoundPool soundPool1;
    public boolean isPlayedTwice = false;


    public static SoundPoolImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundPoolImpl();
        }
        return INSTANCE;
    }

    public void initLoadClick(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(context.getApplicationContext(),
                R.raw.click1, 1);
    }
    public void initLoad(Context context) {
        soundPool1 = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        soundPool1.load(context.getApplicationContext(),
                R.raw.beepmp3, 1);
//        soundPool1.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    public void initLoad(Context context, SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        soundPool1 = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        soundPool1.load(context.getApplicationContext(),
                R.raw.beepmp3, 1);
        soundPool1.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    public void initListner(Context context, SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        soundPool2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        soundPool2.load(context.getApplicationContext(),
                R.raw.beepmp3, 1);
        soundPool2.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    public void play() {
        Logger.v("Play 1");
        soundPool1.play(1, 1, 1, 0, 0, 1);
    }
    public void play2() {
        Logger.v("Play 2");
        soundPool1.play(2, 1, 1, 0, 0, 1);
    }
    public void play1() {
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    public void playTwice() {
        Logger.v("Play twice");
        play();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                play();
            }
        }, 600);
    }

    public void release() {
        Logger.v("Beep Release");
        if (soundPool != null)
            soundPool.release();
        if (soundPool1 != null)
            soundPool1.release();
        if (soundPool2 != null)
            soundPool2.release();
    }

    public void playBeep() {
        Logger.v("Play beep");
        int status = soundPool1.play(1, 1, 1, 0, 0, 1);
        Logger.v("Status --" + status);
    }

    int status = -1;

    public void loopPlay() {
        Logger.v("Play beep 00");
        status = soundPool2.play(1, 1, 1, 1, 5, 1);
        Logger.v("Status -loop-" + status);
    }

    public void removeBeep() {
        Logger.v("Play beep 11");
//		soundPool1.autoPause();
    }
}
