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

public class MessageDialog extends Dialog{

    Activity activity;
    public String message = "";

    public MessageDialog(Activity a, String message) {
        super(a);
        this.activity = a;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.message_dialog);


        TextView info = findViewById(R.id.info);
        ImageView check = findViewById(R.id.check);

        info.setText(message);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onBackPressed(){

       dismiss();

    }

}

