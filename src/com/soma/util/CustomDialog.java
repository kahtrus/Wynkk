package com.soma.util;




import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soma.synge.R;
import com.soma.yampp.FirstActivity;


public class CustomDialog {

    EditText quantityTxt;
    public static	String sContactNum;
    public void callCustomDialog(Context context,String msg,String fav)
    {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView text1 = (TextView) dialog.findViewById(R.id.header);
        if(fav.equalsIgnoreCase("yes"))
        {
            text1.setVisibility(View.GONE);
        }

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public  void CustomDialogWdEditTxt(final Activity context,String msg)
    {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);


        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        quantityTxt=(EditText) dialog.findViewById(R.id.quantityTxt);
        quantityTxt.setVisibility(View.VISIBLE);
        text.setGravity(Gravity.LEFT);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantityTxt.getText().toString().trim().length()>0)
                {
                    sContactNum=quantityTxt.getText().toString();
                    FirstActivity.shandler.sendEmptyMessage(0);
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(context, "please enter your contact number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}