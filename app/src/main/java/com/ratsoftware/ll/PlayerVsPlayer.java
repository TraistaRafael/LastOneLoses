package com.ratsoftware.ll;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by RAF on 11/7/2017.
 */

public class PlayerVsPlayer extends AppCompatActivity {

    public static int total_moved_this_round = 0;
    public static int total_moved_prev_round = 0;

    public static TextView top_info;
    public static TextView bottom_info;

    RelativeLayout top_label;
    RelativeLayout bottom_label;

    public static int current_player = 0; //1 = top 2 = bottom
    public static int current_line = -1;

    public static int total_pressed = 0;

    View.OnClickListener dotsClickListener;

    public static LinearLayout[] lines;

    MessageDialog info_dialog;
    BinaryDialog binary_dialog;

    public static ImageView top_indicator_pause;
    public static ImageView bottom_indicator_pause;

    public static ImageView top_indicator_play;
    public static ImageView bottom_indicator_play;

    ObjectAnimator top_indicator_animation;
    ObjectAnimator bottom_indicator_animation;

    class Dot{
        public ImageView image;
        public int line = 0;
        public int index = 0;
        public int pressed = 0;

        public Dot(ImageView image, int line, int index, int pressed){
            this.image = image;
            this.line = line;
            this.index = index;
            this.pressed = pressed;
            this.image.setOnClickListener(dotsClickListener);
        }
    }

    public static ArrayList<Dot> dots ;

    public static Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_vs_player);

        startDate = Calendar.getInstance().getTime();

        top_indicator_pause = findViewById(R.id.top_indicator_pause);
        bottom_indicator_pause = findViewById(R.id.bottom_indicator_pause);

        top_indicator_play = findViewById(R.id.top_indicator_play);
        bottom_indicator_play = findViewById(R.id.bottom_indicator_play);

        if(Global.animation) {
            top_indicator_animation = ObjectAnimator.ofFloat(top_indicator_pause, "rotationY", 0.0f, 360f);
            top_indicator_animation.setDuration(2000);
            top_indicator_animation.setRepeatCount(ObjectAnimator.INFINITE);
            top_indicator_animation.setRepeatMode(ObjectAnimator.RESTART);
            top_indicator_animation.start();

            bottom_indicator_animation = ObjectAnimator.ofFloat(bottom_indicator_pause, "rotationY", 0.0f, 360f);
            bottom_indicator_animation.setDuration(2000);
            bottom_indicator_animation.setRepeatCount(ObjectAnimator.INFINITE);
            bottom_indicator_animation.setRepeatMode(ObjectAnimator.RESTART);
            bottom_indicator_animation.start();
        }

            info_dialog = new MessageDialog(this, "");
            binary_dialog = new BinaryDialog(this, "");

            dotsClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ObjectAnimator anim = null;

                    if(Global.animation) {
                        anim = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f);
                        anim.setDuration(400);
                    }

                    int pressed_index = -1;

                    switch (v.getId()) {
                        case R.id.dot_1:
                            pressed_index = 0;
                            break;
                        case R.id.dot_2:
                            pressed_index = 1;
                            break;
                        case R.id.dot_3:
                            pressed_index = 2;
                            break;
                        case R.id.dot_4:
                            pressed_index = 3;
                            break;
                        case R.id.dot_5:
                            pressed_index = 4;
                            break;
                        case R.id.dot_6:
                            pressed_index = 5;
                            break;
                        case R.id.dot_7:
                            pressed_index = 6;
                            break;
                        case R.id.dot_8:
                            pressed_index = 7;
                            break;
                        case R.id.dot_9:
                            pressed_index = 8;
                            break;
                        case R.id.dot_10:
                            pressed_index = 9;
                            break;
                        case R.id.dot_11:
                            pressed_index = 10;
                            break;
                        case R.id.dot_12:
                            pressed_index = 11;
                            break;
                        case R.id.dot_13:
                            pressed_index = 12;
                            break;
                        case R.id.dot_14:
                            pressed_index = 13;
                            break;
                        case R.id.dot_15:
                            pressed_index = 14;
                            break;
                        case R.id.dot_16:
                            pressed_index = 15;
                            break;
                        case R.id.dot_17:
                            pressed_index = 16;
                            break;
                        case R.id.dot_18:
                            pressed_index = 17;
                            break;
                        case R.id.dot_19:
                            pressed_index = 18;
                            break;
                        case R.id.dot_20:
                            pressed_index = 19;
                            break;
                        case R.id.dot_21:
                            pressed_index = 20;
                            break;
                        case R.id.dot_22:
                            pressed_index = 21;
                            break;
                    }

                    if (pressed_index > -1) {
                        if (dots.get(pressed_index).pressed == 0) {

                            if (current_line == -1) {
                                current_line = dots.get(pressed_index).line;
                                lines[current_line - 1].setBackgroundResource(R.color.colorLight3);
                            }

                            if (current_line == dots.get(pressed_index).line) {
                                if(!Global.mute) Global.sound_bubble.start();

                                if (current_player == 1) {
                                    dots.get(pressed_index).image.setImageResource(R.drawable.top_player_dot);
                                } else if (current_player == 2) {
                                    dots.get(pressed_index).image.setImageResource(R.drawable.bottom_player_dot);
                                }

                                dots.get(pressed_index).pressed = current_player;
                                total_pressed++;
                                total_moved_this_round++;

                                if (total_pressed >= 22) {
                                    win();
                                }
                            }else{
                                if(!Global.mute) Global.sound_pop.start();
                            }
                        } else if (dots.get(pressed_index).pressed == current_player) {
                            if (dots.get(pressed_index).line == current_line) {

                                if(!Global.mute) Global.sound_pop.start();

                                dots.get(pressed_index).image.setImageResource(R.drawable.empty_dot);
                                if(anim != null)  anim.start();

                                dots.get(pressed_index).pressed = 0;
                                total_pressed--;
                                total_moved_this_round--;
                            }else{
                                if(!Global.mute) Global.sound_pop.start();
                            }
                        }else{
                            if(!Global.mute) Global.sound_pop.start();
                        }

                        if(anim != null) anim.start();
                    }
                }
            };

            top_info = findViewById(R.id.top_info);
            bottom_info = findViewById(R.id.bottom_info);

            top_label = findViewById(R.id.top_label);
            bottom_label = findViewById(R.id.bottom_label);

            lines = new LinearLayout[6];
            lines[0] = findViewById(R.id.line_1);
            lines[1] = findViewById(R.id.line_2);
            lines[2] = findViewById(R.id.line_3);
            lines[3] = findViewById(R.id.line_4);
            lines[4] = findViewById(R.id.line_5);
            lines[5] = findViewById(R.id.line_6);

            dots = new ArrayList<>();

            dots.add(new Dot((ImageView) findViewById(R.id.dot_1), 1, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_2), 1, 2, 0));

            dots.add(new Dot((ImageView) findViewById(R.id.dot_3), 2, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_4), 2, 2, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_5), 2, 3, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_6), 2, 4, 0));

            dots.add(new Dot((ImageView) findViewById(R.id.dot_7), 3, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_8), 3, 2, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_9), 3, 3, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_10), 3, 4, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_11), 3, 5, 0));

            dots.add(new Dot((ImageView) findViewById(R.id.dot_12), 4, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_13), 4, 2, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_14), 4, 3, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_15), 4, 4, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_16), 4, 5, 0));

            dots.add(new Dot((ImageView) findViewById(R.id.dot_17), 5, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_18), 5, 2, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_19), 5, 3, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_20), 5, 4, 0));

            dots.add(new Dot((ImageView) findViewById(R.id.dot_21), 6, 1, 0));
            dots.add(new Dot((ImageView) findViewById(R.id.dot_22), 6, 2, 0));

            top_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_player == 1) {
                        if (current_line > -1 && total_moved_this_round > 0) {
                            current_player = 2;
                            current_line = -1;
                            switchPlayer();
                        } else {
                            info_dialog.message = "Player must select at least one dot, before switch";
                            info_dialog.show();
                        }
                    }
                }
            });

            bottom_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_player == 2) {
                        if (current_line > -1 && total_moved_this_round > 0) {
                            current_player = 1;
                            current_line = -1;
                            switchPlayer();
                        } else {
                            info_dialog.message = "Player must select at least one dot, before switch";
                            info_dialog.show();
                        }
                    }
                }
            });

            cleanBoard();

    }

    public static void switchPlayer(){

        total_moved_prev_round = total_moved_this_round;
        total_moved_this_round = 0;

        for(int i=0; i<6; i++){
            lines[i].setBackgroundResource(R.color.colorPrimaryDark);
        }

        if(current_player == 1){
            top_info.setText("Your turn\nTap here to switch");
            bottom_info.setText("Wait for RED player");
            top_indicator_pause.setVisibility(View.GONE);
            top_indicator_play.setVisibility(View.VISIBLE);
            bottom_indicator_pause.setVisibility(View.VISIBLE);
            bottom_indicator_play.setVisibility(View.GONE);
        }else if(current_player == 2){
            bottom_info.setText("Your turn\nTap here to switch");
            top_info.setText("Wait for BLUE player");
            top_indicator_pause.setVisibility(View.VISIBLE);
            top_indicator_play.setVisibility(View.GONE);
            bottom_indicator_pause.setVisibility(View.GONE);
            bottom_indicator_play.setVisibility(View.VISIBLE);
        }
    }

    FinalDialog final_dialog;

    void win(){

        if(!Global.mute) Global.sound_win.start();

        final_dialog = new FinalDialog(this, current_player);
        final_dialog.show();

    }


    public static void cleanBoard(){


        Random r = new Random();
        current_player = r.nextInt(2) + 1 ;

        total_moved_this_round = 0;
        total_moved_prev_round = 0;

        total_pressed = 0;
        current_line = -1;

        for(int i=0; i<6; i++){
            lines[i].setBackgroundResource(R.color.colorPrimaryDark);
        }

        for(int i=0; i<dots.size(); i++){
            dots.get(i).image.setImageResource(R.drawable.empty_dot);
            dots.get(i).pressed = 0;
        }

        switchPlayer();
    }

    void registerSession(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.PHP_URL + "insert_session.php",
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
                params.put("user_id", Global.prefs.getString("user_uid", ""));
                params.put("start_datetime", startDate.toString());
                params.put("end_datetime", Calendar.getInstance().getTime().toString());

                return params;
            }
        };

        Global.volley.add(postRequest);

    }

    @Override
    public void onResume(){
        super.onResume();

        if(bottom_indicator_animation != null && !bottom_indicator_animation.isRunning()){
            if(bottom_indicator_animation != null) bottom_indicator_animation.start();
        }

        if(top_indicator_animation != null && !top_indicator_animation.isRunning()){
            if(top_indicator_animation != null) top_indicator_animation.start();
        }

    }

    @Override
    public void onPause(){
        super.onPause();

        registerSession();

        if(top_indicator_animation != null && top_indicator_animation.isRunning()){
            top_indicator_animation.cancel();
        }

        if(bottom_indicator_animation != null && bottom_indicator_animation.isRunning()){
            bottom_indicator_animation.cancel();
        }

        if(info_dialog != null && info_dialog.isShowing()){
            info_dialog.dismiss();
        }

        if(final_dialog != null && final_dialog.isShowing()){
            final_dialog.dismiss();
        }

        if(binary_dialog != null && binary_dialog.isShowing()){
            binary_dialog.dismiss();
        }
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
                if(!Global.mute) {
                    Global.sound_alert.start();
                }

                binary_dialog.message = "Are you sure you want to cancel the game ?";
                binary_dialog.show();
                return true;
            default:
                return false;
        }
    }

}
