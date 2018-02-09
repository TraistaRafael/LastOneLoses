package com.ratsoftware.ll;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    boolean first_animation1;
    boolean first_animation2;

    boolean running_anim1;
    boolean running_anim2;

    ImageView sound;

    public Context context;

    ObjectAnimator icon_anim = null;
    ObjectAnimator animation1 = null;
    ObjectAnimator animation2 = null;
    ObjectAnimator header_anim = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

       // Global.animation = areSystemAnimationsEnabled();

        String deviceManufacturer = android.os.Build.MANUFACTURER;

        if(deviceManufacturer.contains("huawei") || deviceManufacturer.contains("Huawei")){
            Global.animation = false;
        }

        Global.audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Global.sound_alert = MediaPlayer.create(this, R.raw.raw_alert);
        Global.sound_bubble = MediaPlayer.create(this, R.raw.raw_bubble);
        Global.sound_button = MediaPlayer.create(this, R.raw.raw_button);
        Global.sound_pop = MediaPlayer.create(this, R.raw.raw_pop);
        Global.sound_win = MediaPlayer.create(this, R.raw.win);

        running_anim1 = false;
        running_anim2 = false;

        final ImageView icon = findViewById(R.id.icon);
        ImageView header = findViewById(R.id.header);


        if(Global.animation) {
            icon_anim = ObjectAnimator.ofFloat(icon, "rotationY", 0.0f, 360f);
            icon_anim.setDuration(2000);
            icon_anim.setRepeatCount(ObjectAnimator.INFINITE);
            icon_anim.setRepeatMode(ObjectAnimator.RESTART);
            icon_anim.start();

            header_anim = ObjectAnimator.ofFloat(header,"rotationX",0.0f, -360f);
            header_anim.setDuration(1200);
            header_anim.start();
        }

        first_animation1 = true;
        first_animation2 = true;

        final ImageView how_to_play = findViewById(R.id.how_to_play);
        final ImageView player_vs_player = findViewById(R.id.player_vs_player);

        if(Global.animation) {

            animation1 = ObjectAnimator.ofFloat(how_to_play, "rotationX", 0.0f, 360f);

            animation1.setDuration(1500);
            animation1.setInterpolator(new AccelerateDecelerateInterpolator());
            animation1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    running_anim1 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    running_anim1 = false;
                    if (first_animation1) {
                        first_animation1 = false;
                        animation1.setDuration(500);
                    } else {
                        startActivity(new Intent(MainActivity.this, HowToPlay.class));
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    running_anim1 = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if(first_animation1){
            if(Global.animation) animation1.start();
        }

        if(Global.animation) {

            animation2 = ObjectAnimator.ofFloat(player_vs_player, "rotationX", 0.0f, 360f);

            animation2.setDuration(2000);
            animation2.setInterpolator(new AccelerateDecelerateInterpolator());
            animation2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    running_anim2 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    running_anim2 = false;
                    if (first_animation2) {
                        first_animation2 = false;
                        animation2.setDuration(500);
                    } else {
                        startActivity(new Intent(MainActivity.this, PlayerVsPlayer.class));
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    running_anim2 = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if(first_animation2){
            if(animation2 != null) animation2.start();
        }


        how_to_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(first_animation1){
                    first_animation1 = false;
                    if(animation1 != null) animation1.setDuration(500);
                    startActivity(new Intent(MainActivity.this, HowToPlay.class));
                }else if(!running_anim1){
                    running_anim1 = true;
                    if(!Global.mute)  Global.sound_pop.start();
                    if(animation1 != null)  animation1.start();
                }
            }
        });

        player_vs_player.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(first_animation2){
                    first_animation2 = false;
                    if(animation2 != null) animation2.setDuration(500);
                    startActivity(new Intent(MainActivity.this, PlayerVsPlayer.class));
                }else if(!running_anim2) {
                    running_anim2 = false;
                    if(!Global.mute) Global.sound_pop.start();
                    if(animation2 != null) animation2.start();
                }
            }
        });

        Global.volley = Volley.newRequestQueue(this);

        Global.prefs = MainActivity.this.getSharedPreferences("5hj87dn877df", Context.MODE_PRIVATE);
        Global.editor = Global.prefs.edit();

        if(Global.prefs.getString("user_uid", "").equals("")){
            registerDevice();
        }

        sound = findViewById(R.id.sound);

        Global.mute = Global.prefs.getBoolean("mute", false);
        if(Global.mute){
            sound.setImageResource(R.drawable.ic_volume_off);
        }else{
            sound.setImageResource(R.drawable.ic_volume_up);
        }

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.mute = !Global.mute;

                if(Global.mute){
                    sound.setImageResource(R.drawable.ic_volume_off);
                }else{
                    sound.setImageResource(R.drawable.ic_volume_up);
                }

                Global.editor.putBoolean("mute", Global.mute);
                Global.editor.commit();
            }
        });

    }

    public boolean areSystemAnimationsEnabled() {
        float duration, transition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            duration = Settings.Global.getFloat(
                    context.getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE, 1);
        } else {
            duration = Settings.System.getFloat(
                    context.getContentResolver(),
                    Settings.System.ANIMATOR_DURATION_SCALE, 1);
        }
        return (duration != 0);
    }

    void registerDevice(){

        Log.d("Monitor.class", "registerDevice");

        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.PHP_URL + "insert_device.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.length() > 0){
                           Global.editor.putString("user_uid", response);
                           Global.editor.commit();
                       }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("device", android.os.Build.MANUFACTURER + android.os.Build.MODEL);


                return params;
            }
        };

        Global.volley.add(postRequest);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                Global.audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Global.audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
            default:
                return false;
        }
    }

}
