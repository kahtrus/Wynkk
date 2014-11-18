package com.soma.wynkk;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.ServiceWork;

public class CommentActivity extends Activity implements OnClickListener
{

    ImageView mUserImage,mEditText,mSmilingMonkey,mNextCommentPage;
    TextView mCommentText,mAddress;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment_xml);
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        mUserImage=(ImageView)findViewById(R.id.mUserImage);
        mEditText=(ImageView)findViewById(R.id.mEditText);
        mSmilingMonkey=(ImageView)findViewById(R.id.mSmilingMoneky);
        mNextCommentPage=(ImageView)findViewById(R.id.mNextCommentPage);

        mCommentText=(TextView)findViewById(R.id.mCommentText);
        mAddress=(TextView)findViewById(R.id.mAddress);
        
        mUserImage.setImageBitmap(MainPageActivity.mProfilePIc);
        Log.d("Selcted place lat long=",MainPageActivity.sLocPosition+" "+FirstActivity.sPlace.get(MainPageActivity.sLocPosition-1).geometry.location.lat+"  long="+FirstActivity.sPlace.get(MainPageActivity.sLocPosition-1).geometry.location.lng);
        mCommentText.setTypeface(typeface);
        mAddress.setTypeface(typeface);
        
        String txt="<font>\"</font>"+MainPageActivity.sCommentText+"<font>\"</font>";
        mCommentText.setText(Html.fromHtml(txt));
        
        String text = "<font color=#1c7f6c>"+CommonFunction.sUserInfo.get(0).get_UserName()+"</font>"+"@"+FirstActivity.sPlace.get(MainPageActivity.sLocPosition-1).name+","+FirstActivity.sPlace.get(MainPageActivity.sLocPosition-1).vicinity;
        mAddress.setText(Html.fromHtml(text));
        
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_1"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_1);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_2"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_2);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_3"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_3);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_4"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_4);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_5"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_5);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_6"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_6);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_7"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_7);
        }
        if(MainPageActivity.sMonkey.equalsIgnoreCase("monkey_8"))
        {
        mSmilingMonkey.setImageResource(R.drawable.monkey_8);
        }
        mEditText.setOnClickListener(this);
        mNextCommentPage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mEditText:
                
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Comment");
                alert.setMessage("Enter your text here");

                // Set an EditText view to get user input 
                final EditText input = new EditText(this);
               // input.setHint("Enter your text");
                input.setText(MainPageActivity.sCommentText);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(input.getText().toString().trim().length()>0)
                    {
                    MainPageActivity.sCommentText = input.getText().toString();
                 
                  String txt="<font>\"</font>"+MainPageActivity.sCommentText+"<font>\"</font>";
                  mCommentText.setText(Html.fromHtml(txt));
                    }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                  }
                });

                alert.show();
                break;

            case R.id.mNextCommentPage:

//                Intent intent=new Intent(CommentActivity.this,HappyPageActivityOnMap.class);
//                startActivity(intent);

                CommonFunction.sActivityName="CommentActivity";
                String url=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageActivity.sCommentText)+"&icon="+MainPageActivity.sMonkey+"&lat="+FirstActivity.sPlace.get(MainPageActivity.sLocPosition).geometry.location.lat+"&lng="+FirstActivity.sPlace.get(MainPageActivity.sLocPosition).geometry.location.lng;
                new ServiceWork(url, CommentActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;

            default:
                break;
        }
    }
    
    
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    MainPageActivity.sMonkey="0";
    Intent intent=new Intent(CommentActivity.this,MainPageActivity.class);
    startActivity(intent);
    CommentActivity.this.finish();
    }
    
    
    
}
