package com.ratsoftware.ll;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.animation.AnimationSet;

import com.android.volley.RequestQueue;

/**
 * Created by RAF on 1/31/2018.
 */

public class Global {

    public static boolean animation = true;

    public static AudioManager audio;

    public static MediaPlayer sound_button;
    public static MediaPlayer sound_bubble;
    public static MediaPlayer sound_pop;
    public static MediaPlayer sound_alert;
    public static MediaPlayer sound_win;

    public static boolean mute;

    public static String PHP_URL = "http://javaoop.club/monitor_app/";

    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    public static RequestQueue volley;

    public static AnimationSet animationFlip;

}
