package com.soma.yampp;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.util.ServiceWork;

public class MainPageWithMapActivity extends Activity implements OnClickListener
{
    
    public static String mURL;
    public static int sSliderCount=0,sSliderBottom=0,sFlag=0,sFlag2=0;
    Animation mAnimation;
    public static FrameLayout layoutMapOverlay = null;
    // Current visible info view
    public static View infoWindow = null;
    // Current selected marker
    LatLng infoWindowPos = null;
    ProgressDialog progressBar;
    // GPS Location
    GPSTracker gps;
    // Google Places
    GooglePlaces googlePlaces;
    // Places List
    PlacesList nearPlaces;
    private GoogleMap map;
    public static Bitmap mProfilePIc;
    Typeface typeface;
    public static int sLocPosition=0;
    public static String sMonkey="0",sSpinnerText,sCommentText; 
    EditText mLocation;
    EditText mComments,mSearchTxt;
    ImageView mMonekey1,mMonekey2,mMonekey3,mMonekey4,mMonekey5,mMonekey6,mMonekey7,mMonekey8,mNextPage;
    ImageView mMenu,mDownArrow,mUserImage,mEmoticon,mSendComment,mSliderUp,mSliderBottom,mSearchButton;
    Marker marker;
    ScrollView mScrollHori;
    LinearLayout mEmoticonLayout;
    TextView mUserNameTxt,mUserNameTxt1;
    LinearLayout mTopHeader,mProfileWithSearchLayuot,mBootomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page_map_layout);
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        mScrollHori=(ScrollView)findViewById(R.id.mScroll);
        mEmoticonLayout=(LinearLayout)findViewById(R.id.mEmoticonLayout);
        mEmoticon=(ImageView)findViewById(R.id.mEmoticon);
        mUserNameTxt=(TextView)findViewById(R.id.mUserName);
        mUserNameTxt1=(TextView)findViewById(R.id.mUserName1);
        mSliderUp=(ImageView)findViewById(R.id.mSLiderUpDown);
        mSliderBottom=(ImageView)findViewById(R.id.mSLiderBottom);
        mTopHeader=(LinearLayout)findViewById(R.id.topHeader);
        mProfileWithSearchLayuot=(LinearLayout)findViewById(R.id.ProfileWithSearchLayuot);
        mBootomView=(LinearLayout)findViewById(R.id.BottomView);
        mSearchTxt=(EditText)findViewById(R.id.mSearch);
        mSearchButton=(ImageView)findViewById(R.id.searchIcon);
        mUserNameTxt.setTypeface(typeface);
        mUserNameTxt1.setTypeface(typeface);
        mUserNameTxt.setText(CommonFunction.sUserInfo.get(0).get_UserName());
        mUserNameTxt1.setText(CommonFunction.sUserInfo.get(0).get_UserName());
        // creating GPS Class object
        gps = new GPSTracker(this);
        // check if GPS location can get
        if (gps.canGetLocation()) 
        {
            // 35.1849925,-101.9502058
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            CommonFunction.sActivityName="GeoCode";
            mURL="https://maps.googleapis.com/maps/api/geocode/json?latlng="+gps.getLatitude()+","+gps.getLongitude()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
            //String url="https://maps.googleapis.com/maps/api/geocode/json?latlng=35.1849925,101.9502058&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
            sFlag=1;
            methodThatStartsTheAsyncTask();
        } else {

            return;
        }
        mAnimation=AnimationUtils.loadAnimation(MainPageWithMapActivity.this,R.anim.fade_in);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mEmoticon.startAnimation(mAnimation);
        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
     // new LoadPlaces().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        GetDownload task = new GetDownload();
        // Execute the task
        task.execute(new String[] { CommonFunction.Image_Url+CommonFunction.sUserInfo.get(0).get_Photo() });

        try{
            if (map==null)
            {
                map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap(); 
            }
        }
        catch(Exception ex)
        {
            Log.d("exception",""+ex);
        }

        if (gps.canGetLocation()) {
            BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
            Bitmap b=bd.getBitmap();
            Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
            LatLng pos1=new LatLng(gps.latitude, gps.longitude);
            marker =map.addMarker(new MarkerOptions().icon(
                    BitmapDescriptorFactory
                    .fromBitmap(bhalfsize))
                    .position(pos1));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 12));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 12));
             map.setTrafficEnabled(true);
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {

            return;
        }

        layoutMapOverlay = (FrameLayout) findViewById(R.id.map_overlay_layout);

        mLocation=(EditText)findViewById(R.id.setLocation);
        mComments=(EditText)findViewById(R.id.mComments);

        mMonekey1=(ImageView)findViewById(R.id.monkey1);
        mMonekey2=(ImageView)findViewById(R.id.monkey2);
        mMonekey3=(ImageView)findViewById(R.id.monkey3);
        mMonekey4=(ImageView)findViewById(R.id.monkey4);
        mMonekey5=(ImageView)findViewById(R.id.monkey5);
        mSendComment=(ImageView)findViewById(R.id.mSendComment);

        mMenu=(ImageView)findViewById(R.id.mMenu);
        mDownArrow=(ImageView)findViewById(R.id.mDownArrow);
        mUserImage=(ImageView)findViewById(R.id.mUserImage);
        mComments.setTypeface(typeface);
        mLocation.setTypeface(typeface);

        mEmoticonLayout.setOnClickListener(this);
        mMonekey1.setOnClickListener(this);
        mMonekey2.setOnClickListener(this);
        mMonekey3.setOnClickListener(this);
        mMonekey4.setOnClickListener(this);
        mMonekey5.setOnClickListener(this);
        mSendComment.setOnClickListener(this);
        mMenu.setOnClickListener(this);
        mDownArrow.setOnClickListener(this);
        mSliderUp.setOnClickListener(this);
        mSliderBottom.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
//        map.setOnMarkerClickListener(new OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker arg0) {
//                // TODO Auto-generated method stub
//                //   removeInfoWindow();
//                //addInfoWindow(marker.getTitle(), marker.getPosition());
//                return true;
//            }
//        });


//        map.setOnCameraChangeListener(new OnCameraChangeListener() {
//
//            @Override
//            public void onCameraChange(CameraPosition arg0) {
//                moveInfoWindow();
//                // captureScreen();
//            }
//        });


        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng pos) 
            {
                Double lat=pos.latitude;
                Double longi=pos.longitude;
                CommonFunction.sActivityName="GeoCode";
                mURL="https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+longi+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                methodThatStartsTheAsyncTask();   
            }
        });
        
if(CommonFunction.sSearchYammp.size()>0)
{
     // Setting a custom info window adapter for the google map       
        map.setInfoWindowAdapter(new InfoWindowAdapter() 
        {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {               
                return null;
            }           

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                final View view = getLayoutInflater().inflate(R.layout.info_window_layout_xml, null);

                TextView mCommentText = (TextView) view.findViewById(R.id.mCommentText);
                TextView mAddress = (TextView) view.findViewById(R.id.mAddress);
                ImageView mSmilingMonkey=(ImageView)view.findViewById(R.id.mSmilingMoneky);
                TextView mMinimizeMarker=(TextView)view.findViewById(R.id.minimizeMarker);
                String id=marker.getId();
                int pos=Integer.parseInt(id.substring(1));
                String txt="<font>\"</font>"+CommonFunction.sSearchYammp.get(pos).get_comment()+"<font>\"</font>";
                mCommentText.setText(Html.fromHtml(txt));

                String text = "<font color=#ffffff>"+CommonFunction.sUserInfo.get(0).get_UserName()+"</font>"+"@"+CommonFunction.sSearchYammp.get(pos).get_location();
                mAddress.setText(Html.fromHtml(text));

                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_1"))
                {
                    mSmilingMonkey.setImageResource(R.drawable.monkey_1);
                }
                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_2"))
                {
                    mSmilingMonkey.setImageResource(R.drawable.monkey_2);
                }
                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_3"))
                {
                    mSmilingMonkey.setImageResource(R.drawable.monkey_3);
                }
                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_4"))
                {
                    mSmilingMonkey.setImageResource(R.drawable.monkey_4);
                }
                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_5"))
                {
                    mSmilingMonkey.setImageResource(R.drawable.monkey_5);
                }


                mMinimizeMarker.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) 
                    {

                        // view.clearFocus();
                        view.setVisibility(view.GONE);


                    }
                });

                return view;

            }

        }); 
}   

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.monkey1:
                sMonkey="monkey_1";
                mEmoticon.setImageResource(R.drawable.monkey_1);
                mScrollHori.setVisibility(View.GONE);
                break;

            case R.id.monkey2:
                sMonkey="monkey_2";
                mEmoticon.setImageResource(R.drawable.monkey_2);
                mScrollHori.setVisibility(View.GONE);
                break;

            case R.id.monkey3:
                sMonkey="monkey_3";
                mEmoticon.setImageResource(R.drawable.monkey_3);
                mScrollHori.setVisibility(View.GONE);
               break;

            case R.id.monkey4:
                sMonkey="monkey_4";

                mEmoticon.setImageResource(R.drawable.monkey_4);
                mScrollHori.setVisibility(View.GONE);
                break;

            case R.id.monkey5:
                sMonkey="monkey_5";
                mEmoticon.setImageResource(R.drawable.monkey_5);
                mScrollHori.setVisibility(View.GONE);
                break;

            case R.id.monkey6:
                sMonkey="monkey_6";
                Toast.makeText(MainPageWithMapActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey7:
                sMonkey="monkey_7";
                Toast.makeText(MainPageWithMapActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.monkey8:

                sMonkey="monkey_8";
                Toast.makeText(MainPageWithMapActivity.this, sMonkey+" is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mSendComment:

                sSpinnerText=mLocation.getText().toString().trim();
                sCommentText=mComments.getText().toString();
                if(sSpinnerText.trim().length()>0)
                {


                    if(sMonkey.equalsIgnoreCase("0"))
                    {
                        Toast.makeText(MainPageWithMapActivity.this, "Select your icon", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        String url;
                        CommonFunction.sActivityName="MainPageWithMapActivity";
                        if(sSpinnerText.equalsIgnoreCase(ServiceWork.formated_Adres))
                        {
                            url=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageWithMapActivity.sCommentText)+"&icon="+MainPageWithMapActivity.sMonkey+"&lat="+ServiceWork.lati+"&lng="+ServiceWork.longi+"&location="+URLEncoder.encode(sSpinnerText);
                        }
                        else
                        {
                            url=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageWithMapActivity.sCommentText)+"&icon="+MainPageWithMapActivity.sMonkey+"&lat="+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lat+"&lng="+FirstActivity.sPlace.get(AfterLoginActivity.sPlacePosition).geometry.location.lng+"&location="+URLEncoder.encode(sSpinnerText);

                        }
                        ListViewForPlaces.listCheckClick=0;
                        sFlag2=1;
                        new ServiceWork(url, MainPageWithMapActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }

                }

                else
                {
                    Toast.makeText(MainPageWithMapActivity.this, "No location is selected", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.mMenu:

                Toast.makeText(MainPageWithMapActivity.this, "Hang on!  were about to implement that feature soon! ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mDownArrow:

                Intent intent=new Intent(MainPageWithMapActivity.this,ListViewForPlaces.class);
                startActivity(intent);
                break;

            case R.id.mEmoticonLayout:

                if(mScrollHori.getVisibility()== View.VISIBLE)
                {
                    mScrollHori.setVisibility(View.GONE);
                }
                else
                {
                    mScrollHori.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.mSLiderUpDown:

                if(sSliderCount==0)
                {
                    sSliderCount++;
                    mTopHeader.setVisibility(View.VISIBLE);
                    mProfileWithSearchLayuot.setVisibility(View.GONE);
                    mSliderUp.setRotation(0);
                }
                else
                {
                    sSliderCount--;
                    mTopHeader.setVisibility(View.GONE);
                    mProfileWithSearchLayuot.setVisibility(View.VISIBLE);
                    mSliderUp.setRotation(180);
                }

                break;

            case R.id.mSLiderBottom:

                if(sSliderBottom==0)
                {
                    sSliderBottom++;    
                    mBootomView.setVisibility(View.VISIBLE);
                    mSliderBottom.setRotation(0);
                }
                else
                {
                    mBootomView.setVisibility(View.GONE);
                    mSliderBottom.setRotation(180);
                    sSliderBottom--;
                }
                break;

            case R.id.searchIcon:

                if(mSearchTxt.getText().toString().trim().length()>0)
                {
                    sFlag=2;
                    String search=mSearchTxt.getText().toString().trim();
                    CommonFunction.sActivityName="SearchYampp";
                    mURL=CommonFunction.host+"search?keyword="+URLEncoder.encode(search);
                    methodThatStartsTheAsyncTask();

                }
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
            viewProgressVisible("GPS... sorry...the delay isn't us!");
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
                double radius = 500; // 1000 meters 

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
                            // FirstActivity.mPlaceList.add("Please choose location");
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
        progressBar = new ProgressDialog(MainPageWithMapActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage(paramString);

        progressBar.show();
    }



//    private void addInfoWindow(String id, LatLng pos) {
//
//        infoWindowPos = pos;
//
//        try {
//            infoWindow = null;
//            infoWindow = MapInfoViewFactory.generateHelperInfoView(this
//                    .getApplicationContext());
//            int  width = (int) ImageHelper.dpToPixel(this, 250);
//            int height = (int) ImageHelper.dpToPixel(this, 190);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    width, height);
//            layoutMapOverlay.addView(infoWindow, params);
//
//
//            moveInfoWindow();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    }


    static void removeInfoWindow() {
        if (infoWindow != null) {
            layoutMapOverlay.removeAllViews();
        }
        infoWindow = null;
    }

    private void moveInfoWindow() {

        if (infoWindow == null)
            return;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoWindow
                .getLayoutParams();
        Point screenPosition = map.getProjection().toScreenLocation(
                infoWindowPos);

        params.leftMargin = screenPosition.x - params.width+100;
        params.topMargin = screenPosition.y - params.height ;

        infoWindow.setLayoutParams(params);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        removeInfoWindow();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if(ListViewForPlaces.listCheckClick==1)
        {
            mLocation.setText(AfterLoginActivity.sHomeLoc);

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

    public void methodThatStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,MainPageWithMapActivity.this,new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {
               if(sFlag==2)
               {
                  drawMarkerOnMap(); 
                  sFlag=0;
               }
               else
               {
                mLocation.setText(ServiceWork.formated_Adres);
                ListViewForPlaces.listCheckClick=0;
               }
            }
        });
        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }
    
    
    public void drawMarkerOnMap()
    {
//        BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
//        Bitmap b=bd.getBitmap();
//        Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
       // removeInfoWindow();
        map.clear();
        LatLng pos1;
        for(int i=0;i<CommonFunction.sSearchYammp.size();i++)
        {

            double lat=Double.parseDouble(CommonFunction.sSearchYammp.get(i).get_latitude());
            double lon=Double.parseDouble(CommonFunction.sSearchYammp.get(i).get_longitude());
            pos1 = new LatLng(lat,lon);
            
            if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_1"))
            {
                marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(pos1).draggable(true));    
                marker.showInfoWindow();
            }
            if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_2"))
            
            {
                marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m2)).position(pos1).draggable(true));    
                marker.showInfoWindow();
            }
            if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_3"))
            {
                marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m3)).position(pos1).draggable(true));    
                marker.showInfoWindow();
            }
            if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_4"))
            {
                marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m4)).position(pos1).draggable(true));    
                marker.showInfoWindow();
            }
            if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_5"))
            {
                marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m5)).position(pos1).draggable(true));    
                marker.showInfoWindow();
            }


        }
       
    }
}
