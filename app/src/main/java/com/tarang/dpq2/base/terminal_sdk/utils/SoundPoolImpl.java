package com.tarang.dpq2.base.terminal_sdk.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

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


    public static SoundPoolImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundPoolImpl();
        }
        return INSTANCE;
    }

    public void initLoad(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(context.getApplicationContext(),
                R.raw.click1, 1);
        soundPool1 = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        soundPool1.load(context.getApplicationContext(),
                R.raw.beep01, 1);
    }

    public void initListner(Context context, SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 100);
        soundPool.load(context.getApplicationContext(),
                R.raw.beep01, 1);
        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);


        soundPool1 = new SoundPool(3, AudioManager.STREAM_SYSTEM, 100);
        soundPool1.load(context.getApplicationContext(),
                R.raw.beep01, 1);
        soundPool1.setOnLoadCompleteListener(onLoadCompleteListener);

        soundPool2 = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        soundPool2.load(context.getApplicationContext(),
                R.raw.beep01, 1);
        soundPool2.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    public void play() {
        Logger.v("play_once");
        soundPool.play(1, 1, 1, 0, 0, 1);
    }

    public void playTwice() {
        Logger.v("Play_twice");
        soundPool.play(1, 1, 1, 0, 2, 1);
    }

    public void release() {
        Logger.v("Beep_Release");
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
