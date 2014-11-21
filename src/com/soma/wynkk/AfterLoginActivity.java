package com.soma.wynkk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.soma.contraoller.SignupController;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.util.CustomDialog;
import com.soma.util.ServiceWork;

public class AfterLoginActivity extends Activity implements OnClickListener
{
    public static String mURL;
    public static Handler sGeoHandler;
    public int getServerResponse;
    public static int sPlacePosition;
    public static String sUSerName,sPhoneNum,sHomeLoc;
    public String mResult;
    EditText mUserName,mTeleNum;
    EditText mHomeLocation;
    LinearLayout mAfterLoginNext;
    ImageView mDownArrow;
    CustomDialog mCustomDialog=new CustomDialog();
    int SignUpValidateCheckResponse;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    Typeface typeface;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.after_login);

        // creating GPS Class object
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) 
        {
            // 35.1849925,-101.9502058
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            CommonFunction.sActivityName="GeoCode";
            mURL="https://maps.googleapis.com/maps/api/geocode/json?latlng="+gps.getLatitude()+","+gps.getLongitude()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
            methodThatStartsTheAsyncTask();
        } else 
        {

            return;
        }
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        prefs = PreferenceManager.getDefaultSharedPreferences(AfterLoginActivity.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        GetDownload task = new GetDownload();
        // Execute the task
        task.execute(new String[] { FirstActivity.mUserId });

        mUserName=(EditText)findViewById(R.id.mUserName);
        mTeleNum=(EditText)findViewById(R.id.mPhoneNum);

        mHomeLocation=(EditText)findViewById(R.id.setLocation);

        mAfterLoginNext=(LinearLayout)findViewById(R.id.mLoginNext);
        mDownArrow=(ImageView)findViewById(R.id.mDownArrow);

        mUserName.setTypeface(typeface);
        mTeleNum.setTypeface(typeface);
        mHomeLocation.setTypeface(typeface);
        mHomeLocation.setOnClickListener(this);
        mAfterLoginNext.setOnClickListener(this);
        mDownArrow.setOnClickListener(this);

        mHomeLocation.setText(ServiceWork.formated_Adres);
        mUserName.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) 
            {

                if(!hasFocus)
                {
                    String url="";

                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {

                            try
                            {
                                String a=mUserName.getText().toString().trim();
                                String url=CommonFunction.host+"checkUser?username="+URLEncoder.encode(a)+"&checkService=2";
                                URL mURL = new URL(url);
                                InputStream is=mURL.openConnection().getInputStream();
                                CommonFunction com=new CommonFunction();
                                mResult=com.converResponseToString(is);
                                Log.e("AFTER LOGIN RES",mResult);
                                JSONObject o = new JSONObject(mResult);
                                String status=o.getString("status");
                                Log.e("AFTER LOGIN",status);

                                if(status.equalsIgnoreCase("0"))
                                {
                                    SignUpValidateCheckResponse=1;
                                }
                                else 
                                {
                                    SignUpValidateCheckResponse=2;
                                }
                            }
                            catch(Exception e)
                            {

                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(String result) 
                        {
                            CustomDialog mCustomDialog=new CustomDialog();
                            switch (SignUpValidateCheckResponse) {
                                case 1:

                                    mCustomDialog.callCustomDialog(AfterLoginActivity.this, "Sorry, that user name is taken – can you try another", "no");

                                    break;

                                case 2:

                                    break;

                                default:
                                    break;
                            }

                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                };
            }


        });

    }

    @Override
    public void onClick(View v) 
    {

        switch (v.getId()) {
            case R.id.mLoginNext:

                sUSerName=mUserName.getText().toString().trim();
                sPhoneNum=mTeleNum.getText().toString().trim();
                sHomeLoc=mHomeLocation.getText().toString().trim();
                if(sUSerName.length()>0)
                {

                    if(sHomeLoc.length()>0)
                    {


                        new HttpAsyncTask().execute(CommonFunction.host+"registration");
                        //                           if(sPlacePosition==-1)
                        //                           {
                        //                               CommonFunction.sActivityName="GeoCode";
                        //                               String url="https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(sHomeLoc)+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                        //                               new ServiceWork(url, AfterLoginActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        //                           }
                        //                           else
                        //                           {
                        //                             new HttpAsyncTask().execute(CommonFunction.host+"registration");
                        //                           }
                    }
                    else
                    {
                        mCustomDialog.callCustomDialog(AfterLoginActivity.this, "select your location", "no");
                    }
                }


                else
                {
                    mCustomDialog.callCustomDialog(AfterLoginActivity.this, "set user name", "no");
                }

                break;

            case R.id.mDownArrow:

                Intent intent=new Intent(AfterLoginActivity.this,ListViewForPlaces.class);
                startActivity(intent);
                break;

            case R.id.setLocation:
                //  sPlacePosition=-1;
                // mHomeLocation.setEnabled(true);
                break;

            default:
                break;
        }



    }

    private class GetDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            try {

                URL imageURL;
                if(FirstActivity.sProvider.equalsIgnoreCase("1"))
                {
                    imageURL = new URL("https://graph.facebook.com/"+urls[0]+"/picture?type=normal");
                }
                else
                {
                    imageURL=new URL(FirstActivity.mImageUrl);
                }

                map = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return map;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) 
        {

            CommonFunction.storeImage(result);

            Log.d("file paattth","dsd"+CommonFunction.filePath);

        }
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            try
            {
                Log.d("hellllooooooooo","heeeeeeeeeell "+urls[0]);
                //Url of the server
                //String url = "";
                String boundary = "*****";
                URL url=new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setDoInput(true); // Allow Inputs
                connection.setDoOutput(true); // Allow Outputs
                connection.setUseCaches(false); // Don't use a Cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", urls[0]);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urls[0]);
                MultipartEntity mpEntity = new MultipartEntity();
                //Path of the file to be uploaded

                String filepath =CommonFunction.filePath ;
                if(filepath!=null)
                {
                    File file = new File(filepath);
                    ContentBody cbFile = new FileBody(file, "image/jpeg");  
                    Log.d("hellllooooooooo","heeeeeeeeeell "+CommonFunction.filePath);
                    //Add the data to the multipart entity
                    mpEntity.addPart("photo", cbFile);
                    mpEntity.addPart("social_type_id",new StringBody(FirstActivity.mUserId, Charset.forName("UTF-8")));
                    mpEntity.addPart("username",new StringBody(sUSerName, Charset.forName("UTF-8")));
                    mpEntity.addPart("phone", new StringBody(sPhoneNum, Charset.forName("UTF-8")));
                    mpEntity.addPart("home_location", new StringBody(sHomeLoc, Charset.forName("UTF-8")));

                    if(sHomeLoc.equalsIgnoreCase(ServiceWork.formated_Adres))
                    {
                        mpEntity.addPart("latitude", new StringBody(""+ServiceWork.lati, Charset.forName("UTF-8")));
                        mpEntity.addPart("longitude", new StringBody(""+ServiceWork.longi, Charset.forName("UTF-8")));

                    }
                    else
                    {
                        mpEntity.addPart("latitude", new StringBody(""+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lat, Charset.forName("UTF-8")));
                        mpEntity.addPart("longitude", new StringBody(""+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lng, Charset.forName("UTF-8")));
                    }
                    mpEntity.addPart("social_type", new StringBody(FirstActivity.sProvider, Charset.forName("UTF-8")));
                    //mpEntity.addPart("Description", new StringBody(textForContent.getText().toString(), Charset.forName("UTF-8")));
                    post.setEntity(mpEntity);
                    Log.d("hellllooooooooo","heeeeeeeeeell "+CommonFunction.filePath);
                }
                else
                {
                    mpEntity.addPart("socail_type_id",new StringBody(FirstActivity.mUserId, Charset.forName("UTF-8")));
                    mpEntity.addPart("username",new StringBody(sUSerName, Charset.forName("UTF-8")));
                    mpEntity.addPart("phone", new StringBody(sPhoneNum, Charset.forName("UTF-8")));
                    mpEntity.addPart("home_location", new StringBody(sHomeLoc, Charset.forName("UTF-8")));
                    if(sHomeLoc.equalsIgnoreCase(ServiceWork.formated_Adres))
                    {
                        mpEntity.addPart("latitude", new StringBody(""+ServiceWork.lati, Charset.forName("UTF-8")));
                        mpEntity.addPart("longitude", new StringBody(""+ServiceWork.longi, Charset.forName("UTF-8")));

                    }
                    else
                    {
                        mpEntity.addPart("latitude", new StringBody(""+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lat, Charset.forName("UTF-8")));
                        mpEntity.addPart("longitude", new StringBody(""+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lng, Charset.forName("UTF-8")));
                    }
                    mpEntity.addPart("social_type", new StringBody(FirstActivity.sProvider, Charset.forName("UTF-8")));
                    //mpEntity.addPart("Description", new StringBody(textForContent.getText().toString(), Charset.forName("UTF-8")));
                    post.setEntity(mpEntity);
                    Log.d("hellllooooooooo","heeeeeeeeeell "+CommonFunction.filePath); 
                }

                Log.d("Response:", "hey helloooooooo 1");
                //Execute the post request
                HttpResponse response1 = client.execute(post);
                //Get the response from the server
                Log.d("Response:", "hey helloooooooo 2");
                HttpEntity resEntity = response1.getEntity();
                Log.d("Response:", "hey helloooooooo 3");
                String Response=EntityUtils.toString(resEntity);
                Log.d("Response:", "hey helloooooooo "+Response);
                //Generate the array from the response
                JSONArray jsonarray = new JSONArray("["+Response+"]");
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                //Get the result variables from response 
                SignupController getSignupController = new SignupController();
                getSignupController.init(Response); 

                String result = (jsonobject.getString("status"));
                String msg = (jsonobject.getString("message"));
                if(result.equalsIgnoreCase("1"))
                {
                    ArrayList<com.soma.model.SignupModel.Data> userDeatail=getSignupController.findAll();
                    CommonFunction.sUserInfo.clear();
                    for(int i=0;i<userDeatail.size();i++)
                    {
                        CommonFunction.sUserInfo.add(i,userDeatail.get(i));
                        Log.d("userId",""+CommonFunction.sUserInfo.get(i).get_Id().toString());
                        prefsEditor.putString("userId",CommonFunction.sUserInfo.get(i).get_Id().toString());
                        prefsEditor.commit();
                    }
                    getServerResponse=1;
                }
                else
                {
                    getServerResponse=2;
                }
                //Close the connection
                client.getConnectionManager().shutdown();
            }
            catch(Exception e)
            {

            }

            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) 
        {

            switch (getServerResponse) {
                case 1:
                    Intent intent=new Intent(AfterLoginActivity.this,MainPageDrawerAcitivity.class);
                    //Intent intent=new Intent(AfterLoginActivity.this,MainPageWithMapActivity.class);
                    startActivity(intent);
                    AfterLoginActivity.this.finish();

                    break;

                case 2:
                    Toast.makeText(AfterLoginActivity.this,"Sorry, we could'nt register you – can you try one more time", Toast.LENGTH_SHORT).show();

                    break;

                default:
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(ListViewForPlaces.listCheckClick==1)
        {
            mHomeLocation.setText(AfterLoginActivity.sHomeLoc);
            ListViewForPlaces.listCheckClick=0;
        }
        else
        {
            if (gps.canGetLocation()) 
            {
                // 35.1849925,-101.9502058
                Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
                CommonFunction.sActivityName="GeoCode";
                mURL="https://maps.googleapis.com/maps/api/geocode/json?latlng="+gps.getLatitude()+","+gps.getLongitude()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                methodThatStartsTheAsyncTask();
            } else 
            {

                return;
            } 
        }



    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent=new Intent(AfterLoginActivity.this,FirstActivity.class);
        startActivity(intent);
        AfterLoginActivity.this.finish();
    }


    public void methodThatStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,AfterLoginActivity.this,new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {
                mHomeLocation.setText(ServiceWork.formated_Adres);
            }
        });
        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }

}
