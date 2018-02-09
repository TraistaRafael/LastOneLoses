package com.ratsoftware.ll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by RAF on 2/1/2018.
 */

public class BinaryDialog extends Dialog{

    Activity activity;
    public String message = "";

    public BinaryDialog(Activity a, String message) {
        super(a);
        this.activity = a;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.binary_dialog);


        TextView info = findViewById(R.id.info);
        ImageView yes = findViewById(R.id.yes);
        ImageView no = findViewById(R.id.no);

        info.setText(message);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.mute) Global.sound_pop.start();
                dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Global.mute) Global.sound_pop.start();
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed(){

        dismiss();

    }

}

