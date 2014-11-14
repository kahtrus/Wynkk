package com.soma.yampp;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CustomDialog;
import com.soma.util.ServiceWork;

public class FirstActivity extends Activity implements OnClickListener
{

    public static int counter=0;
    public static final String CLIENT_ID = "1440831";
    private static final String TAG = "First Activity";
    private static final String IMAGE_SOURCE = "http://placekitten.com/500/400";
    private static final String WEB_URL = "http://placekitten.com";
    
    // ListItems data
    public static ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
    public static ArrayList<String> mPlaceList=new ArrayList<String>();
    public static ArrayList<Place> sPlace=new ArrayList<Place>();
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name
    public static String sPhoneNumber;
    ImageView mTwitter,mFacebook,mGooglePlus,mLinkedIn,mInstagram,mPinterest;
    public static String mUserName, mLocation, mRegion, mEmail, mUserId,
    mImageUrl, firstName;
    public static String providerName, uName, uEmail;
    public static boolean mFbFlag = false;
    public static boolean mTwitterFlag = false;
    public static boolean mPlusFlag = false,mLinkedFlag=false,mInstaFlag=false,mPinterestFlag=false;
    public static Bitmap mProfileImage;
    Profile profileMap;
    public String url;
    public static String sProvider;
    ProgressDialog progressBar;
    // GPS Location
    GPSTracker gps;
    // Google Places
    GooglePlaces googlePlaces;

    // Places List
    PlacesList nearPlaces;
    CustomDialog mCustomDialog=new CustomDialog();
    public static Activity activity;
    private Animation animUp;
    private Animation animDown,fadeIN,fadeOut;
    public static Handler shandler;
    public static SocialAuthAdapter adapter;
    public  Spinner mSetHomeAddress;
    EditText mPhoneNumber,mUser;
    ImageView mNext;
    Typeface typeface;
    TextView mSocialTxt,mShowMapTxt;
    LinearLayout mBottomLayout;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_xml);
        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_upp);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);
//        PinItButton.setDebugMode(true);
//        PinItButton.setPartnerId(CLIENT_ID);
        try{
            if (map==null)
                map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap(); 
            // map.setMyLocationEnabled(true);
            map.setIndoorEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            
        }
        catch(Exception ex){}
        
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        activity=FirstActivity.this;
        mSocialTxt=(TextView)findViewById(R.id.mSocial);
        mShowMapTxt=(TextView)findViewById(R.id.mShowMap);
        mBottomLayout=(LinearLayout)findViewById(R.id.bottomLayout);
        mSocialTxt.setTypeface(typeface);
        mShowMapTxt.setTypeface(typeface);
        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) 
        {
            LatLng pos1 = new LatLng(gps.latitude,gps.longitude);
            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(pos1).draggable(true).title("0").anchor(0.0f, 0.6f).title("Your position"));    
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 16));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 16));
          //Instantiates a new CircleOptions object +  center/radius
//            CircleOptions circleOptions = new CircleOptions()
//              .center( pos1 )
//              .radius( 100 )
//              .fillColor(0x40ff0000)
//              .strokeColor(Color.TRANSPARENT)
//              .strokeWidth(2);
//             
//            // Get back the mutable Circle
//             map.addCircle(circleOptions);
//           // 35.1849925,-101.9502058
//            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
//        CommonFunction.sActivityName="GeoCode";
//        String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+gps.getLatitude()+","+gps.getLongitude()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
//        //String url="https://maps.googleapis.com/maps/api/geocode/json?latlng=35.1849925,101.9502058&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
//        
//        new ServiceWork(url, FirstActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        
        } else {
            buildDialog(this,"enable gps please").show();
            return;
        }
        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
      //  new LoadPlaces().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        mFacebook=(ImageView)findViewById(R.id.mFbShare);
        mTwitter=(ImageView)findViewById(R.id.mTweetShare);
        mGooglePlus=(ImageView)findViewById(R.id.mGplusShare);
        mLinkedIn=(ImageView)findViewById(R.id.mLinkedInShare);
        mInstagram=(ImageView)findViewById(R.id.mInstagram);
        mPinterest=(ImageView)findViewById(R.id.mPinterest);
//        PinItButton pinIt = (PinItButton) findViewById(R.id.mPinterest);
//        pinIt.setImageUrl(IMAGE_SOURCE);
//        pinIt.setUrl(WEB_URL);
//        pinIt.setDescription("Yampp");
//        pinIt.setListener(_listener);

       
        

        mFacebook.setOnClickListener(this);
        mTwitter.setOnClickListener(this);
        mGooglePlus.setOnClickListener(this);
        mLinkedIn.setOnClickListener(this);
        mInstagram.setOnClickListener(this);
        mPinterest.setOnClickListener(this);        
mSocialTxt.setOnClickListener(this);
mShowMapTxt.setOnClickListener(this);

        adapter = new SocialAuthAdapter(new ResponseListener());

        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        sPhoneNumber=telephonyManager.getLine1Number();
        Log.d("Phone number","= "+telephonyManager.getLine1Number());
        Log.d("Cell voice mail number","= "+telephonyManager.getVoiceMailNumber());
        Log.d("Cell location","= "+telephonyManager.getCellLocation());


        
     // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        LocationListener locationListerner=new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLocationChanged(Location location) {
                map.clear();
              LatLng pos1 = new LatLng(location.getLatitude(),location.getLongitude());
              map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(pos1).title("Your position"));    
              map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 16));
              map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 16));
            }
        };
        locationManager.requestLocationUpdates(provider, 20000, 0, locationListerner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
        Log.e(TAG,"CHECK THIS UP ");
    }
    //    PinItListener _listener = new PinItListener() {
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            Log.i(TAG, "PinItListener.onStart");
//        }
//
//        @Override
//        public void onComplete(boolean completed) {
//            super.onComplete(completed);
//            Log.i(TAG, "PinItListener.onComplete");
//        }
//
//        @Override
//        public void onException(Exception e) {
//            super.onException(e);
//            Log.i(TAG, "PinItListener.onException");
//        }
//
//    };
    
    
    
    // To receive the response after authentication
    private final class ResponseListener implements DialogListener {

        @Override
        public void onComplete(Bundle values) {


            try {
                adapter.getUserProfileAsync(new ProfileDataListener());

                // Get the provider

                providerName = values.getString(SocialAuthAdapter.PROVIDER);
                if (providerName.equalsIgnoreCase("facebook")) {
                    mFbFlag = true;
                }
                if (providerName.equalsIgnoreCase("twitter")) {
                    mTwitterFlag = true;
                }
                if (providerName.equalsIgnoreCase("googleplus")) {
                    mPlusFlag = true;
                }
                if (providerName.equalsIgnoreCase("linkedin")) {
                    mLinkedFlag = true;
                }
                if (providerName.equalsIgnoreCase("instagram")) {
                    mInstaFlag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//                    adapter.updateStatus("hello from wynkk", new SocialAuthListener<Integer>(){
//                        public void onExecute(String provider,Integer t) {
//                            Integer status = t;
//                            if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
//                                Log.d("ShareButton" , "Cancelled");
//                            else
//                                Log.d("ShareButton" , "Cancelled");
//                        }
//
//                        public void onError(SocialAuthError e) {
//
//                        }
//                    },false);
//                    Log.d("Response Listener","onComplete() executing!");



            // Toast.makeText(getApplicationContext(),
            // "login with"+providerName, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocialAuthError error) 
        {
            Log.d("Custom-UIttgtgg", "Error");
            error.printStackTrace();
            viewProgressGone();
        }

        @Override
        public void onCancel() {
            Log.d("Custom-UI", "Cancelled");
            viewProgressGone();
        }

        @Override
        public void onBack() 
        {
            Log.d("Custom-UI", "Dialog Closed by pressing Back Key");
            viewProgressGone();
        }
    } 


    private final class ProfileDataListener implements
    SocialAuthListener<Profile> {

        @Override
        public void onExecute(String provider, Profile t) {

            try {
                Log.d("Custom-UI", "Receiving Data");
                profileMap = t;

                if (provider.equalsIgnoreCase("FACEBOOk")) {
                    mUserName = URLEncoder.encode(profileMap.getFirstName()
                            + " " + profileMap.getLastName());

                    Log.d("name-UI", "name=" + uName + "-email=" + uEmail);
                    Log.d("Custom-UI",
                            "Validate ID = " + profileMap.getValidatedId());
                    Log.d("Custom-UI",
                            "First Name  = " + profileMap.getFirstName());
                    Log.d("Custom-UI",
                            "Last Name   = " + profileMap.getLastName());
                    Log.d("DisplayName", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI", "Email       = " + profileMap.getEmail());
                    Log.d("Custom-UI", "Gender       = " + profileMap.getGender());
                    Log.d("Custom-UI",
                            "Country     = " + profileMap.getCountry());
                    Log.d("Custom-UI",
                            "Language    = " + profileMap.getLanguage());
                    Log.d("Custom-UI",
                            "First Name          = " + profileMap.getFullName());
                    Log.d("Custom-UI", "Location                 = "
                            + profileMap.getLocation());
                    Log.d("Custom-UI",
                            "Profile Image URL  = "
                                    + profileMap.getProfileImageURL());

                    Log.d("display name", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI",
                            "Contact info          = " + profileMap.getContactInfo());
                    mEmail = profileMap.getEmail();

                    if (mEmail == null) {
                        mEmail = profileMap.getFirstName()
                                + profileMap.getValidatedId()
                                .substring(
                                        profileMap.getValidatedId()
                                        .length() - 4)
                                        + "@facebook.com";
                        Log.d("Custom-UI", "Email       = "
                                + mEmail);
                    } else {
                        mEmail = profileMap.getEmail();

                    }

                }

                if (provider.equalsIgnoreCase("TWITTER")||provider.equalsIgnoreCase("INSTAGRAM")) {
                    Log.d("Custom-UI",
                            "Validate ID         = "
                                    + profileMap.getValidatedId());
                    Log.d("Custom-UI",
                            "First Name          = " + profileMap.getFullName());
                    Log.d("Custom-UI",
                            "First Name  = " + profileMap.getFirstName());
                    Log.d("DisplayName", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI", "Language                 = "
                            + profileMap.getLanguage());
                    Log.d("Custom-UI", "Location                 = "
                            + profileMap.getLocation());
                    Log.d("Custom-UI",
                            "Profile Image URL  = "
                                    + profileMap.getProfileImageURL());
                    Log.d("Custom-UI", "email  = " + t.getEmail());
                    mUserName = profileMap.getDisplayName();
                    Log.d("display name", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI",
                            "Contact info          = " + profileMap.getContactInfo());
                    mEmail = profileMap.getEmail();
                    if (mEmail == null) {
                        mEmail = profileMap.getDisplayName()
                                + "@twitter.com";
                    } else {
                        mEmail = profileMap.getEmail();

                    }
                }

                if (provider.equalsIgnoreCase("GOOGLEPLUS")) {
                    mUserName = URLEncoder.encode(profileMap.getFirstName()
                            + " " + profileMap.getLastName());
                    Log.d("Custom-UI",
                            "Validate ID         = "
                                    + profileMap.getValidatedId());
                    Log.d("Custom-UI",
                            "First Name          = " + profileMap.getFullName());
                    Log.d("Custom-UI",
                            "First Name  = " + profileMap.getFirstName());
                    Log.d("DisplayName", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI", "Language                 = "
                            + profileMap.getLanguage());
                    Log.d("Custom-UI", "Location                 = "
                            + profileMap.getLocation());
                    Log.d("Custom-UI",
                            "Profile Image URL  = "
                                    + profileMap.getProfileImageURL());
                    Log.d("Custom-UI", "email  = " + t.getEmail());
                    mUserName = profileMap.getFullName();
                    mEmail = profileMap.getEmail();
                    Log.d("display name", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI",
                            "Contact info          = " + profileMap.getContactInfo());
                    if (mEmail == null) {
                        mEmail = profileMap.getDisplayName()
                                + profileMap.getValidatedId()
                                .substring(
                                        profileMap.getValidatedId()
                                        .length() - 4)
                                        + "@googleplus.com";
                    } else {
                        mEmail = profileMap.getEmail();

                    }

                }


                if (provider.equalsIgnoreCase("LINKEDIN")) {
                    mUserName = URLEncoder.encode(profileMap.getFirstName()
                            + " " + profileMap.getLastName());
                    Log.d("Custom-UI",
                            "Validate ID         = "
                                    + profileMap.getValidatedId());
                    Log.d("Custom-UI",
                            "First Name          = " + profileMap.getFullName());
                    Log.d("Custom-UI",
                            "First Name  = " + profileMap.getFirstName());
                    Log.d("DisplayName", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI", "Language                 = "
                            + profileMap.getLanguage());
                    Log.d("Custom-UI", "Location                 = "
                            + profileMap.getLocation());
                    Log.d("Custom-UI",
                            "Profile Image URL  = "
                                    + profileMap.getProfileImageURL());
                    Log.d("Custom-UI", "email  = " + t.getEmail());
                    mUserName = profileMap.getFullName();
                    mEmail = profileMap.getEmail();
                    Log.d("display name", "" + profileMap.getDisplayName());
                    Log.d("Custom-UI",
                            "Contact info          = " + profileMap.getContactInfo());
                    if (mEmail == null) {
                        mEmail = profileMap.getDisplayName()
                                + profileMap.getValidatedId()
                                .substring(
                                        profileMap.getValidatedId()
                                        .length() - 4)
                                        + "@googleplus.com";
                    } else {
                        mEmail = profileMap.getEmail();

                    }

                }
                if (provider.equalsIgnoreCase("INSTAGRAM")) {
                    Log.d(TAG,"INSTAGRAMSTA!!!!  ");
                }
                viewProgressGone();
                mRegion = profileMap.getCountry();
                firstName = profileMap.getFirstName();
                mUserId = profileMap.getValidatedId();
                mLocation = profileMap.getLocation();
                mImageUrl = profileMap.getProfileImageURL();

                CommonFunction.sActivityName="SocialMedia";
                String url=CommonFunction.host+"checkUser?social_id="+mUserId+"&checkService=1";
                new ServiceWork(url, FirstActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SocialAuthError e) {

        }
    }



    @Override
    public void onClick(View v) 
    {
     if(!CommonFunction.isInternetOn(FirstActivity.this)){
        buildDialog(this,"You need to enable internet connection!").show();
      }else {
         CommonFunction com = new CommonFunction();
         switch (v.getId()) {

             case R.id.mSocial:
                 if (counter == 0) {

                     mSocialTxt.setText("Show map");
                     mBottomLayout.setVisibility(View.VISIBLE);
                     mBottomLayout.startAnimation(animUp);
                     counter++;
                 } else {
                     counter--;
                     mSocialTxt.setText("login/register with social media");
                     mBottomLayout.startAnimation(animDown);
                     mBottomLayout.setVisibility(View.GONE);
                 }
                 break;


             case R.id.mFbShare:

                 if (CommonFunction.isInternetOn(FirstActivity.this)) {
                     sProvider = "1";
                     viewProgressVisible("Please Wait");
                     adapter.authorize(FirstActivity.this, Provider.FACEBOOK);
                 }
                 break;

             case R.id.mTweetShare:
                 if (CommonFunction.isInternetOn(FirstActivity.this)) {
                     sProvider = "2";
                     viewProgressVisible("Please Wait");
                     adapter.authorize(FirstActivity.this, Provider.TWITTER);
                 }
                 break;

             case R.id.mGplusShare:
                 if (CommonFunction.isInternetOn(FirstActivity.this)) {
                     sProvider = "3";
                     viewProgressVisible("Please Wait");
                     adapter.authorize(FirstActivity.this, Provider.GOOGLEPLUS);
                 }
                 break;

             case R.id.mLinkedInShare:
                 if (CommonFunction.isInternetOn(FirstActivity.this)) {
                     sProvider = "4";
                     viewProgressVisible("Please Wait");
                     adapter.authorize(FirstActivity.this, Provider.LINKEDIN);
                 }

                 break;

             case R.id.mInstagram:
                 if (CommonFunction.isInternetOn(FirstActivity.this)) {
                     sProvider = "5";
                     viewProgressVisible("Please Wait");
                     adapter.authorize(FirstActivity.this, Provider.INSTAGRAM);
                 }
                 break;

             case R.id.mPinterest:

                 Toast.makeText(FirstActivity.this, "Hang on! were about to implement that feature soon!", Toast.LENGTH_SHORT).show();

                 break;

             default:
                 break;
         }
     }

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
                            mPlaceList.clear();

                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.vicinity);

                                // Place name
                                map.put(KEY_NAME, p.name);

                                String NameAdd=p.name+", "+p.vicinity;

                                double a=p.geometry.location.lat;
                                sPlace.add(p);
                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                                mPlaceList.add(NameAdd);
                            }
                            Log.d("places ","="+placesListItems);
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
        progressBar.dismiss();
    }

    public void viewProgressVisible(String paramString) {
        progressBar = new ProgressDialog(FirstActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage(paramString);

        progressBar.show();
    }

    public AlertDialog.Builder buildDialog(Context c, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection.");
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }
}
