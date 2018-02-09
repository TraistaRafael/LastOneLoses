package com.ratsoftware.ll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by RAF on 2/1/2018.
 */

public class FinalDialog extends Dialog{

    Activity activity;
    int current_player = 0;

    public FinalDialog(Activity a, int current_player) {
        super(a);
        this.activity = a;
        this.current_player = current_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.final_dialog);

        ImageView center = findViewById(R.id.center);
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        ImageView replay = findViewById(R.id.repeat);
        ImageView exit = findViewById(R.id.exit);


        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerVsPlayer.cleanBoard();
                dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerVsPlayer.cleanBoard();
                activity.finish();
            }
        });


        if(current_player == 2) {
            center.setImageResource(R.drawable.top_player_dot);
            text1.setText("RED WINS");
            text1.setTextColor(activity.getResources().getColor(R.color.colorAccent));
            text2.setText("RED WINS");
            text2.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        }else if(current_player == 1) {
            center.setImageResource(R.drawable.bottom_player_dot);
            text1.setText("BLUE WINS");
            text1.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            text2.setText("BLUE WINS");
            text2.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public void onBackPressed(){

        if(!Global.mute) Global.sound_alert.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to cancel the game ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(!Global.mute) Global.sound_pop.start();
                activity.finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Global.mute) Global.sound_pop.start();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

}

