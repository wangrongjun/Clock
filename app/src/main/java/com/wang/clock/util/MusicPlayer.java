package com.wang.clock.util;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * by wangrongjun on 2017/2/3.
 */
public class MusicPlayer {

    private static MediaPlayer mediaPlayer;

    public synchronized static void play(String musicPath) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized static void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
