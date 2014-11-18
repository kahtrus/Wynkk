package com.soma.wynkk;

import org.brickred.socialauth.android.SocialAuthAdapter.Provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.soma.synge.R;

public class WelcomeClass extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_xml);
        TextView tv=(TextView)findViewById(R.id.text);
        if(FirstActivity.sProvider.equalsIgnoreCase("1"))
        {
            tv.setText("You are login with Facebook"); 
        }
        else
        {
            if(FirstActivity.sProvider.equalsIgnoreCase("2"))
            {
                tv.setText("You are login with Twitter"); 
            }
            else
            {
                if(FirstActivity.sProvider.equalsIgnoreCase("3"))
                {
                    tv.setText("You are login with Google plus"); 
                }
                else
                {
                    tv.setText("You are login with Linked in"); 
                }
                }
        }

        Button logout=(Button)findViewById(R.id.logout);

        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(FirstActivity.mFbFlag)
                {
                    FirstActivity.mFbFlag=false;
                    //logout from Socail Networking provider
                    FirstActivity.adapter.signOut(getApplicationContext(), Provider.FACEBOOK.toString());
                }
                if(FirstActivity.mTwitterFlag)
                {
                    FirstActivity.mTwitterFlag=false;
                    //logout from Socail Networking provider
                    FirstActivity.adapter.signOut(getApplicationContext(), Provider.TWITTER.toString());
                }
                if(FirstActivity.mPlusFlag)
                {
                    FirstActivity.mPlusFlag=false;
                    //logout from Socail Networking provider
                    FirstActivity.adapter.signOut(getApplicationContext(), Provider.GOOGLEPLUS.toString());
                }
                if(FirstActivity.mLinkedFlag)
                {
                    FirstActivity.mLinkedFlag=false;
                    //logout from Socail Networking provider
                    FirstActivity.adapter.signOut(getApplicationContext(), Provider.LINKEDIN.toString());
                }
                Intent   intent =new Intent(WelcomeClass.this,FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();



            }
        });

    }
}
