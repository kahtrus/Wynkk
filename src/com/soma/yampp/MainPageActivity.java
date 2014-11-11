package com.soma.yampp;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.soma.synge.R;
import com.soma.util.CommonFunction;

public class MainPageActivity extends Activity implements OnClickListener
{
    ProgressDialog progressBar;
    // GPS Location
       GPSTracker gps;
    // Google Places
       GooglePlaces googlePlaces;
    // Places List
       PlacesList nearPlaces;
    public static Bitmap mProfilePIc;
Typeface typeface;
    public static int sLocPosition=0;
    public static String sMonkey="0",sSpinnerText,sCommentText; 
    Spinner mLocation;
    EditText mComments;
    ImageView mMonekey1,mMonekey2,mMonekey3,mMonekey4,mMonekey5,mMonekey6,mMonekey7,mMonekey8,mNextPage;
    ImageView mMenu,mDownArrow,mUserImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page_xml);
        
        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
          
            return;
        }
     // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
      
        new LoadPlaces().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        
        
        GetDownload task = new GetDownload();
        // Execute the task
        task.execute(new String[] { CommonFunction.Image_Url+CommonFunction.sUserInfo.get(0).get_Photo() });
    
        
        
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        mLocation=(Spinner)findViewById(R.id.setLocation);
        mComments=(EditText)findViewById(R.id.mComments);

        mMonekey1=(ImageView)findViewById(R.id.monkey1);
        mMonekey2=(ImageView)findViewById(R.id.monkey2);
        mMonekey3=(ImageView)findViewById(R.id.monkey3);
        mMonekey4=(ImageView)findViewById(R.id.monkey4);
        mMonekey5=(ImageView)findViewById(R.id.monkey5);
        mMonekey6=(ImageView)findViewById(R.id.monkey6);
        mMonekey7=(ImageView)findViewById(R.id.monkey7);
        mMonekey8=(ImageView)findViewById(R.id.monkey8);
        mNextPage=(ImageView)findViewById(R.id.mNextPage);
        mMenu=(ImageView)findViewById(R.id.mMenu);
        mDownArrow=(ImageView)findViewById(R.id.mDownArrow);
mUserImage=(ImageView)findViewById(R.id.mUserImage);
        mComments.setTypeface(typeface);
        ArrayAdapter<String> locAdapter= new ArrayAdapter<String>(MainPageActivity.this,
                android.R.layout.simple_list_item_1,FirstActivity.mPlaceList);
        mLocation.setAdapter(locAdapter);

        mMonekey1.setOnClickListener(this);
        mMonekey2.setOnClickListener(this);
        mMonekey3.setOnClickListener(this);
        mMonekey4.setOnClickListener(this);
        mMonekey5.setOnClickListener(this);
        mMonekey6.setOnClickListener(this);
        mMonekey7.setOnClickListener(this);
        mMonekey8.setOnClickListener(this);
        mNextPage.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mDownArrow.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monkey1:
                sMonkey="monkey_1";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey2:
                sMonkey="monkey_2";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey3:
                sMonkey="monkey_3";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey4:
                sMonkey="monkey_4";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey5:
                sMonkey="monkey_5";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey6:
                sMonkey="monkey_6";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey7:
                sMonkey="monkey_7";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey8:

                sMonkey="monkey_8";
                Toast.makeText(MainPageActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mNextPage:

                sSpinnerText=mLocation.getSelectedItem().toString();
                sCommentText=mComments.getText().toString();
                if(sSpinnerText.trim().length()>0)
                {
                    if(sCommentText.trim().length()>0)
                    {
                        if(mLocation.getSelectedItemPosition()>0)
                        {
                            if(sMonkey.equalsIgnoreCase("0"))
                            {
                                Toast.makeText(MainPageActivity.this, "Select your icon", Toast.LENGTH_SHORT).show();
                                
                            }
                            else
                            {
                                 sLocPosition=mLocation.getSelectedItemPosition();
                                Intent intent=new Intent(MainPageActivity.this,CommentActivity.class);
                                startActivity(intent);
                                MainPageActivity.this.finish();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainPageActivity.this, "Select your position", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(MainPageActivity.this, "Comment field is blank", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(MainPageActivity.this, "No location is selected", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.mMenu:

                Toast.makeText(MainPageActivity.this, "Hang on!  were about to implement that feature soon! ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mDownArrow:

                Toast.makeText(MainPageActivity.this, "Click over the text", Toast.LENGTH_SHORT).show();
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
                URL imageURL = new URL(urls[0]);


                map = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return map;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (OutOfMemoryError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {


            Log.d("resizedBitmap url","hiii " +result);
            if(result!=null)
            {
            Bitmap resized = Bitmap.createScaledBitmap(result, 100, 100, true);
            Bitmap conv_bm = getRoundedRectBitmap(resized,100);
            mProfilePIc=conv_bm;
            mUserImage.setImageBitmap(conv_bm);
            }
        }
    }



    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0,bitmap.getWidth(),bitmap.getHeight());
            RectF rectF=new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawCircle(50, 50, 50, paint);
            canvas.drawRoundRect(rectF, pixels, pixels, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    
    /**
     * Background Async Task to Load Google places
     * */
    public  class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          viewProgressVisible("Loading places...");
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();
            
            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                // 
                String types = null;//"bus_station"; // Listing places only cafes, restaurants
                
                // Radius in meters - increase this value if you don't find any places
                double radius = 1500; // 1000 meters 
                
                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);
                

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
           viewProgressGone();
//           String status = nearPlaces.status;
//           if(status.equalsIgnoreCase("ok"))
//           {
//               Log.d("near by places","dasd"+nearPlaces.results);
//           }
               // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;
                    
                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                           FirstActivity.mPlaceList.clear();
                           FirstActivity.sPlace.clear();
                           FirstActivity.placesListItems.clear();
                           FirstActivity.mPlaceList.add("Please choose location");
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                
                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(FirstActivity.KEY_REFERENCE, p.vicinity);
                                
                                // Place name
                                map.put(FirstActivity.KEY_NAME, p.name);
                                
                                String NameAdd=p.name+", "+p.vicinity;
                               
                                double a=p.geometry.location.lat;
                                FirstActivity.sPlace.add(p);
                                // adding HashMap to ArrayList
                                FirstActivity.placesListItems.add(map);
                                FirstActivity.mPlaceList.add(NameAdd);
                            }
                            Log.d("places ","="+                                FirstActivity.placesListItems);
//                            ArrayAdapter<String> adp= new ArrayAdapter<String>(FirstActivity.this,
//                                    android.R.layout.simple_list_item_1,mPlaceList);
//                            mSetHomeAddress.setAdapter(adp);
//                           
                            
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                  
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                    }
                    else
                    {
                       }
                }
            });

        }

    }

    

    public void viewProgressGone() {
        if(progressBar!=null)
        progressBar.dismiss();
    }

    public void viewProgressVisible(String paramString) {
        progressBar = new ProgressDialog(MainPageActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage(paramString);

        progressBar.show();
    }
    
}
