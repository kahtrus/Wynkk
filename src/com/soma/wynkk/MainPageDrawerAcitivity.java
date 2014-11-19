package com.soma.wynkk;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import com.soma.model.SearchWynkkModel;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.util.ServiceWork;

public class MainPageDrawerAcitivity extends Activity implements OnClickListener,SeekBar.OnSeekBarChangeListener
{
    private ListView lastWynkksList;
    String messageToShare="Hello From Wynkk";
    Marker homeMarker;
    private HashMap<LatLng,List<Marker>> wynkksMap = new HashMap<LatLng, List<Marker>>();
//    ImageButton mShareSocial;
//    Button mShareSocialInvis;
    ImageView twitterGreen;
    private SocialAuthAdapter adapter;
    private static final double DEFAULT_RADIUS = 1000000;
    public static final double RADIUS_OF_EARTH_METERS = 6371009;
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final String TAG = "MainPageDrawerAcitivity";
    DraggableCircle circle=null;
    public static LayoutInflater inflater;
    // Current visible info view
    public static View infoWindow = null;
    // Current selected marker
    LatLng infoWindowPos = null;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    RelativeLayout mBottomSlider;
    public static int sFlag=0,sFlag2=0,onMapCounter=0,onClockCounter=0,onMenuCounter=0,geoFlag=0;;
    public static String mURL;

    Animation mAnimationRight,mANimationLeft,mAnimation,mAnimDown,mANimUp;

    Typeface typeface;

    TextView mUserNameTxt;

    ImageButton mTopSLiderButton,mMiddleSliderButton,mBottomSLiderButton,mSliderLeftButtonTop,mSliderLeftButtonBottom;

    LinearLayout mTopSliderLayout,mBottomSliderLayout,mMiddleSliderLayout,mEmoticonLayout,mLocationLayout,mClockLayout;;
    LinearLayout mShareYourLocation,mEditYOurProfile,mLogout,mMapSetting;

    Float x1,x2,y1,y2;

    ImageView mMonekey1,mMonekey2,mMonekey3,mMonekey4,mMonekey5,mMonekey6,mMonekey7,mMonekey8,mNextPage;
    ImageView mDownArrow,mUserImage,mEmoticon,mSendComment,mSearchButton,mClock,mShareIcon,mMenuIcon,mUserProfileImage;
    ImageView mFbInvite,mGPlusInvite,mLinkedInInvite,mTwitterInvite,mValidateAddress;

    EditText mComments,mSearchTxt,mLocation;;
    ScrollView mScrollHori,mScrollView;
    TextView mOneHour,mOneWeek,mOneMonth,mUserNme;
    SeekBar radiusBar;
    public static int sLocPosition=0;
    public static String sMonkey="0",sSpinnerText,sCommentText; 
    ProgressDialog progressBar;
    ProgressBar mProgress;
    // GPS Location
    GPSTracker gps;
    // Google Places
    GooglePlaces googlePlaces;
    // Places List
    PlacesList nearPlaces;
    Marker marker;
    private GoogleMap map=null;
    public static Bitmap mProfilePIc;
    public static FrameLayout layoutMapOverlay = null;
    LatLng position1;

    private ViewGroup infoWindowClickable;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button shareButton;
    private Button closeButton;
    private ImageView infoImg;
    private OnInfoWindowElemTouchListener infoButtonListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_page_with_three_drawer);
        inflater= (LayoutInflater) MainPageDrawerAcitivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutMapOverlay = (FrameLayout) findViewById(R.id.map_overlay_layout);
        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
        prefs = PreferenceManager.getDefaultSharedPreferences(MainPageDrawerAcitivity.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        mAnimationRight = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
        mANimationLeft  = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
        mANimUp=AnimationUtils.loadAnimation(this,R.anim.anim_upp);
        mAnimDown=AnimationUtils.loadAnimation(this,R.anim.anim_down);

        mScrollHori=(ScrollView)findViewById(R.id.mScroll);
        mScrollView=(ScrollView)findViewById(R.id.scrollVIew);

        mProgress=(ProgressBar)findViewById(R.id.progressBar1);
        mUserNameTxt=(TextView)findViewById(R.id.mUserName);

        mBottomSlider=(RelativeLayout)findViewById(R.id.bottomSLider);

        mTopSLiderButton=(ImageButton)findViewById(R.id.topSliderButton);
        mMiddleSliderButton=(ImageButton)findViewById(R.id.middleSliderButton);
        mBottomSLiderButton=(ImageButton)findViewById(R.id.bottomSliderButton);
        mSliderLeftButtonTop=(ImageButton)findViewById(R.id.mSLiderLeftButton);
        mEmoticon=(ImageView)findViewById(R.id.mEmoticon);
        mUserImage=(ImageView)findViewById(R.id.mUserImage);
        mMonekey1=(ImageView)findViewById(R.id.monkey1);
        mMonekey2=(ImageView)findViewById(R.id.monkey2);
        mMonekey3=(ImageView)findViewById(R.id.monkey3);
        mMonekey4=(ImageView)findViewById(R.id.monkey4);
        mMonekey5=(ImageView)findViewById(R.id.monkey5);
        mSendComment=(ImageView)findViewById(R.id.mSendComment);
        mSearchButton=(ImageView)findViewById(R.id.searchIcon);
        mDownArrow=(ImageView)findViewById(R.id.mDownArrow);
        mClock=(ImageView)findViewById(R.id.mClock);
        mShareIcon=(ImageView)findViewById(R.id.mShareIcon);
        mMenuIcon=(ImageView)findViewById(R.id.mMenu);
        mUserProfileImage=(ImageView)findViewById(R.id.mProfileImage);
        mFbInvite=(ImageView)findViewById(R.id.fbGreen);
        mGPlusInvite=(ImageView)findViewById(R.id.googleGreen);
        mTwitterInvite=(ImageView)findViewById(R.id.twitterGreen);
        mLinkedInInvite=(ImageView)findViewById(R.id.linkedinGreen);

        mTopSliderLayout=(LinearLayout)findViewById(R.id.searchTab);
        mBottomSliderLayout=(LinearLayout)findViewById(R.id.below);
        mLocationLayout=(LinearLayout)findViewById(R.id.locationLayout);
        mClockLayout=(LinearLayout)findViewById(R.id.clockLayout);
        mShareYourLocation=(LinearLayout)findViewById(R.id.shareYourLocation);
        mEditYOurProfile=(LinearLayout)findViewById(R.id.editYourProfile);
        mMapSetting=(LinearLayout)findViewById(R.id.mapSetting);
        mLogout=(LinearLayout)findViewById(R.id.logout);

        mLocation=(EditText)findViewById(R.id.setLocation);
        mComments=(EditText)findViewById(R.id.mComments);
        mSearchTxt=(EditText)findViewById(R.id.mSearch);

        //mLocation.setEnabled(false);

        mOneHour=(TextView)findViewById(R.id.oneHour);
        mOneMonth=(TextView)findViewById(R.id.oneMonth);
        mOneWeek=(TextView)findViewById(R.id.oneWeek);
        mUserNme=(TextView)findViewById(R.id.mUserNme);
        mUserNme.setTypeface(typeface);
        mUserNameTxt.setTypeface(typeface);
        mLocation.setTypeface(typeface);
        mLocation.setTextSize(14.0f);
        mLocation.setTypeface(Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD));
        mComments.setTypeface(typeface);
        mSearchTxt.setTypeface(typeface);
        mOneHour.setTypeface(typeface);
        mOneWeek.setTypeface(typeface);
        mOneMonth.setTypeface(typeface);

        mUserNameTxt.setText(CommonFunction.sUserInfo.get(0).get_UserName());
        mUserNme.setText(CommonFunction.sUserInfo.get(0).get_UserName());

        mAnimation=AnimationUtils.loadAnimation(MainPageDrawerAcitivity.this,R.anim.fade_in);
        mAnimation.setRepeatCount(Animation.INFINITE);
        mEmoticon.startAnimation(mAnimation);

        mTopSLiderButton.setOnClickListener(this);
        mMiddleSliderButton.setOnClickListener(this);
        mBottomSLiderButton.setOnClickListener(this);
        mSliderLeftButtonTop.setOnClickListener(this);
        mEmoticon.setOnClickListener(this);
        mMonekey1.setOnClickListener(this);
        mMonekey2.setOnClickListener(this);
        mMonekey3.setOnClickListener(this);
        mMonekey4.setOnClickListener(this);
        mMonekey5.setOnClickListener(this);
        mSendComment.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mOneHour.setOnClickListener(this);
        mOneMonth.setOnClickListener(this);
        mOneWeek.setOnClickListener(this);
        mDownArrow.setOnClickListener(this);
        mClock.setOnClickListener(this);
        mShareIcon.setOnClickListener(this);
        mMenuIcon.setOnClickListener(this);
        mShareYourLocation.setOnClickListener(this);
        mEditYOurProfile.setOnClickListener(this);
        mMapSetting.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mFbInvite.setOnClickListener(this);
        mTwitterInvite.setOnClickListener(this);
        mGPlusInvite.setOnClickListener(this);
        mLinkedInInvite.setOnClickListener(this);
        mLocation.setOnClickListener(this);


        lastWynkksList = (ListView)findViewById(R.id.lastCommentsList);

        LinearLayout inviteYourFriendsLayout = (LinearLayout)findViewById(R.id.inviteYourFriendsLayout);
        //inviteYourFriendsLayout.setOnClickListener(this);
        adapter = new SocialAuthAdapter(new ResponseListener());
        // Add providers
        adapter.addProvider(Provider.FACEBOOK, R.drawable.fb_green);
        adapter.addProvider(Provider.TWITTER, R.drawable.twitter_green);
       // adapter.addProvider(Provider.GOOGLEPLUS, R.drawable.google_plus_green);
       // adapter.addProvider(Provider.INSTAGRAM, R.drawable.linkdin_green);
        twitterGreen = (ImageView)findViewById(R.id.twitterGreen);

        //adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);

        // Add email and mms providers
        //adapter.addProvider(Provider.EMAIL, R.drawable.email);
        //adapter.addProvider(Provider.MMS, R.drawable.mms);
        adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
        //adapter.addCallBack(Provider.GOOGLEPLUS, "http://wynkk.co");
        //adapter.addCallBack(Provider.INSTAGRAM,"http://wynkk.co/");
        adapter.addCallBack(Provider.FACEBOOK, "http://wynkk.co/");
       // adapter.addCallBack(Provider.YAMMER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
       // Log.e(TAG,mShareSocial.getContext().toString());
        // adapter.enable(twitterGreen);
        //adapter.enable(mShareSocialInvis);

        mBottomSliderLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent touchevent) {
                // TODO Auto-generated method stub

                switch (touchevent.getAction())

                {
                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN:
                    {
                        x1 = touchevent.getX();
                        y1 = touchevent.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        x2 = touchevent.getX();
                        y2 = touchevent.getY(); 

                        if (x1 > x2)
                        {
                            onMapCounter--;
                            mScrollHori.setVisibility(View.GONE);
                            mBottomSlider.startAnimation(mANimationLeft);
                            mBottomSlider.setVisibility(View.GONE);
                        }
                        break;
                    }
                }  
                return true;
            }
        });

        mTopSliderLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent touchevent) {
                // TODO Auto-generated method stub

                switch (touchevent.getAction())

                {




                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN:
                    {
                        x1 = touchevent.getX();
                        y1 = touchevent.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        x2 = touchevent.getX();
                        y2 = touchevent.getY(); 

                        if (x1 > x2)
                        {
                            mScrollHori.setVisibility(View.GONE);
                            mBottomSlider.startAnimation(mANimationLeft);
                            mBottomSlider.setVisibility(View.GONE);
                        }
                        break;
                    }
                }  
                return true;
            }
        });


        //creating GPS Class object
        gps = new GPSTracker(this);
        // check if GPS location can get


        GetDownload task = new GetDownload();
        // Execute the task
        task.execute(new String[] { CommonFunction.Image_Url+CommonFunction.sUserInfo.get(0).get_Photo() });

        //HERE STARTS THE SHIIIIIT
        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        try{
            if (map==null)
            {
                map = mapFragment.getMap();
                //map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
            }
            }
            catch(Exception ex)
                {
                    Log.d("exception",""+ex);
                }
        mapWrapperLayout.init(map, getPixelsFromDp(this, 30 + 20));
        this.infoWindowClickable = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window_layout_xml, null);
        this.infoTitle = (TextView)infoWindowClickable.findViewById(R.id.mCommentText);
        this.infoSnippet = (TextView)infoWindowClickable.findViewById(R.id.mAddress);
        this.shareButton = (Button)infoWindowClickable.findViewById(R.id.mShareSocial);
        shareButton.setTextColor(Color.WHITE);
        this.closeButton = (Button)infoWindowClickable.findViewById(R.id.mCloseBtn);
        this.infoImg = (ImageView)infoWindowClickable.findViewById(R.id.mSmilingMoneky);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        infoWindowClickable.setLayoutParams(new RelativeLayout.LayoutParams(width*2/3, RelativeLayout.LayoutParams.WRAP_CONTENT));

        if (gps.canGetLocation()) 
        {
            BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
            Bitmap b=bd.getBitmap();
            Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/1,b.getHeight()/1, false);
            LatLng pos1=new LatLng(gps.latitude, gps.longitude);
            marker =map.addMarker(new MarkerOptions().icon(
                    BitmapDescriptorFactory
                    .fromBitmap(bhalfsize))
                    .position(pos1));
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 15));
            ListViewPlacesList.slatitude=gps.getLatitude();
            ListViewPlacesList.sLongitude=gps.getLongitude();
            ServiceWork.businessSearchData.clear();
            CommonFunction.sActivityName="placeList";
            mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+gps.getLatitude()+","+gps.getLongitude()+"&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
         //   mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.2000143,-101.9556949&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
            
            //  CommonFunction.sActivityName="GeoCode";
           // mURL="https://maps.googleapis.com/maps/api/geocode/json?location="+gps.getLatitude()+","+gps.getLongitude()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";   
       
            methodWillStartsTheAsyncTask() ;
            prefsEditor.putString("saveLatitude",""+gps.getLatitude());
            prefsEditor.putString("saveLongitude",""+gps.getLongitude());
            prefsEditor.commit();
        } 
        else 
        {
            return;
        }

//        this.infoButtonListener = new OnInfoWindowElemTouchListener(shareButton,
//                getResources().getDrawable(R.drawable.ic_menu_share_down),
//                getResources().getDrawable(R.drawable.ic_menu_share_up))
//        {
//            @Override
//            protected void onClickConfirmed(View v, Marker marker) {
//                // Here we can perform some action triggered after clicking the button
//                //shareButton.performClick();
////                marker.hideInfoWindow();
//
//            }
//        };
//        this.shareButton.setOnTouchListener(infoButtonListener);
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_menu_share_up));
            }
        });
        adapter.enable(shareButton);

        this.infoButtonListener = new OnInfoWindowElemTouchListener(closeButton,
                getResources().getDrawable(R.drawable.ic_wynkk_close_down),
                getResources().getDrawable(R.drawable.ic_wynkk_close_up))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                marker.hideInfoWindow();
                //Toast.makeText(MainPageDrawerAcitivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.closeButton.setOnTouchListener(infoButtonListener);
        infoWindowClickable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFOWINDOWCLICKABLE","clicked");
                // TODO Auto-generated method stub
            }
        });
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

                // Getting view from the layout file info_window_layout_xml
//                View view = getLayoutInflater().inflate(R.layout.info_window_layout_xml, null);
//                mShareSocial = (ImageButton)view.findViewById(R.id.mShareSocial);
//                mShareSocialInvis = (Button)view.findViewById(R.id.mCloseBtn);
//                adapter.enable(mShareSocialInvis);
//                DisplayMetrics displaymetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//                int width = displaymetrics.widthPixels;
//                view.setLayoutParams(new RelativeLayout.LayoutParams(width*2/3, RelativeLayout.LayoutParams.WRAP_CONTENT));

//                TextView mCommentText = (TextView) view.findViewById(R.id.mCommentText);
//                TextView mAddress = (TextView) view.findViewById(R.id.mAddress);
//                ImageView mSmilingMonkey=(ImageView)view.findViewById(R.id.mSmilingMoneky);
//                TextView mMinimizeMarker=(TextView)view.findViewById(R.id.minimizeMarker);
                String id=marker.getTitle();


                if(id==null) {
                    return null;
                }else
                if(id.equals("Your position")) {
                        return null;
                }else {
                    Log.d(TAG, id);


                    int pos=Integer.parseInt(id);
                    Log.d("Marker pos",""+marker.getPosition());
                    Log.d("your position is",""+id);
                    String txt="<font>\"</font>"+CommonFunction.sSearchYammp.get(pos).get_comment()+"<font>\"</font>";
                    infoTitle.setText(Html.fromHtml(txt));

                    String text = "<font color=#ffffff>"+CommonFunction.sSearchYammp.get(pos).get_Username()+"</font>"+"@"+CommonFunction.sSearchYammp.get(pos).get_location();
                    infoSnippet.setText(Html.fromHtml(text));
                    messageToShare = infoTitle.getText().toString() +" @"+ infoSnippet.getText().toString()+
                    "\ncheck out this awesome app! http://wynkk.co";
                    Log.d("your texts is",infoTitle.getText()+" "+ infoSnippet.getText());
                    if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_1"))
                    {
                        infoImg.setImageResource(R.drawable.monkey_1);
                    }else
                    if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_2"))
                    {
                        infoImg.setImageResource(R.drawable.monkey_2);
                    }else
                    if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_3"))
                    {
                        infoImg.setImageResource(R.drawable.monkey_3);
                    }else
                    if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_4"))
                    {
                        infoImg.setImageResource(R.drawable.monkey_4);
                    }else
                    if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_5"))
                    {
                        infoImg.setImageResource(R.drawable.monkey_5);
                    }
                }
                infoButtonListener.setMarker(marker);
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindowClickable);
                return infoWindowClickable;

            }

        }); 

//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                if(mShareSocial!=null)
//                    mShareSocialInvis.performClick();
//            }
//        });

        map.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng pos) {
                // TODO Auto-generated method stub
                Double lat=pos.latitude;
                Double longi=pos.longitude;
                //                CommonFunction.sActivityName="GeoCode";
                //                mURL="https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+longi+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                //                methodThatStartsTheAsyncTask();
                ServiceWork.businessSearchData.clear();
                CommonFunction.sActivityName="placeList";
                mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+longi+"&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                methodWillStartsTheAsyncTask() ;
            }
        });

        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng pos) 
            {
//                if(mLocationLayout.getVisibility()== View.VISIBLE)
//                {
//                    mLocationLayout.startAnimation(mANimationLeft);
//                    mLocationLayout.setVisibility(View.GONE);
//
//                }
//                else

                if(mLocationLayout.getVisibility()!= View.VISIBLE){
                    mLocationLayout.setVisibility(View.VISIBLE);
                    mLocationLayout.startAnimation(mAnimationRight);
                }
                if(mClockLayout.getVisibility()==View.VISIBLE)
                {
                   // mClockLayout.startAnimation(mANimUp);
                    mClockLayout.setVisibility(View.GONE);
                    onClockCounter--;
                }
//                if(mScrollView.getVisibility()==View.VISIBLE)
//                {
//                    mScrollView.startAnimation(mANimationLeft);
//                    mScrollView.setVisibility(View.GONE);
//                    onMenuCounter--;
//                }
            }
        });


        mLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s)
            {

                //                CommonFunction.sActivityName="GeoCode";
                //                mURL="https://maps.googleapis.com/maps/api/geocode/json?address="+s.toString()+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";   
                //                new ServiceWork(mURL, MainPageDrawerAcitivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) 
            {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) 
            {


            } 

        });
        
        
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
                //map.clear();
                if(homeMarker!=null)
                    homeMarker.remove();
                circle=null;

                LatLng pos1 = new LatLng(location.getLatitude(),location.getLongitude());
                homeMarker = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(pos1).title("Your position"));
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                // map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 16));
            }
        };
        locationManager.requestLocationUpdates(provider, 20000, 0, locationListerner);


        radiusBar=(SeekBar)findViewById(R.id.radiusBar);
        radiusBar.setOnSeekBarChangeListener(this);

    }


    public void commentMethodStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,MainPageDrawerAcitivity.this,"a",new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {
                if(CommonFunction.sActivityName.equalsIgnoreCase("GeoCode"))
                {
                    mURL=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageDrawerAcitivity.sCommentText)+"&icon="+MainPageDrawerAcitivity.sMonkey+"&lat="+ServiceWork.lati+"&lng="+ServiceWork.longi+"&location="+URLEncoder.encode(sSpinnerText);
                    ListViewForPlaces.listCheckClick=0;
                    sFlag2=1;
                    geoFlag=1;
                    mComments.setText("");
                    CommonFunction.sActivityName="MainPageWithMapActivity";
                    mLocation.setText(ServiceWork.businessSearchData.get(0).getPlaceAddress());
                    mEmoticon.setImageResource(R.drawable.monkey_5);
                    mScrollHori.setVisibility(View.GONE);
                    commentMethodStartsTheAsyncTask();
                }
                else
                {
                    map.clear();
                    circle = null;
                    double lat;
                    double lon;
                    if(geoFlag==1)
                    {
                        lat=ServiceWork.lati;
                        lon=ServiceWork.longi;
                        geoFlag=0;
                    }
                    else
                    {
                        if(sSpinnerText.equalsIgnoreCase(ServiceWork.businessSearchData.get(0).getPlaceAddress()))
                        {
                            lat=Double.parseDouble(ServiceWork.businessSearchData.get(0).getLatitude().toString());
                            lon=Double.parseDouble(ServiceWork.businessSearchData.get(0).getLongitude().toString());
                        }
                        else
                        {
                            lat=Double.parseDouble(ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLatitude().toString());
                            lon=Double.parseDouble(ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLongitude().toString());

                        }
                    }
                    final LatLng pos1 = new LatLng(lat,lon);
                    Log.d("hello ",""+lat+" , "+lon);
                    Log.e("please add marker","on map");
                    if(sMonkey.equalsIgnoreCase("monkey_1"))
                    {
                        Log.d("hello ",""+sMonkey);
                        marker=map.addMarker(new MarkerOptions().title("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(pos1));    
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                        marker.showInfoWindow();
                    }
                    if(sMonkey.equalsIgnoreCase("monkey_2"))
                    {
                        Log.d("hello ",""+sMonkey);
                        marker=map.addMarker(new MarkerOptions().title("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.m2)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                    }
                    if(sMonkey.equalsIgnoreCase("monkey_3"))
                    {
                        Log.d("hello ",""+sMonkey);
                        marker=map.addMarker(new MarkerOptions().title("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.m3)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                    }
                    if(sMonkey.equalsIgnoreCase("monkey_4"))
                    {
                        Log.d("hello ",""+sMonkey);
                        marker=map.addMarker(new MarkerOptions().title("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.m4)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                    }
                    if(sMonkey.equalsIgnoreCase("monkey_5"))
                    {
                        Log.d("hello ",""+sMonkey);
                        marker=map.addMarker(new MarkerOptions().title("0").icon(BitmapDescriptorFactory.fromResource(R.drawable.m5)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                    }


                    sMonkey="0";

                } }
        });testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }

    public void methodWillStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,MainPageDrawerAcitivity.this,"a",new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {

                if(ServiceWork.sPageToken!=null)
                {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                Thread.sleep(2000);
                                CommonFunction.sActivityName="placeList";
                                mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+ServiceWork.sPageToken+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";

                                methodWillStartsTheAsyncTask() ;
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                        }
                    }).start();}
                else
                {

                    for(int i=0;i<ServiceWork.businessSearchData.size();i++)
                    {
                        Log.e("hello data",ServiceWork.businessSearchData.get(i).getPlaceAddress()+" "+ServiceWork.businessSearchData.get(i).getDistance()+"  "+ServiceWork.businessSearchData.get(i).getLatitude()+"  "+ServiceWork.businessSearchData.get(i).getLongitude());
                    }
                    mProgress.setVisibility(View.GONE);
                    mLocation.setText(ServiceWork.businessSearchData.get(0).getPlaceAddress());
                    mLocationLayout.setVisibility(View.VISIBLE);
                    mLocationLayout.startAnimation(mAnimationRight);
                }}
        });
        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(circle==null)
            circle=new DraggableCircle(new LatLng(gps.getLatitude(),gps.getLongitude()),progress);
        else
            circle.onStyleChange();
       // drawCircle();


    }
    private void onMarkerMoved(Marker marker) {
        circle.onMarkerMoved(marker);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                mUserProfileImage.setImageBitmap(conv_bm);
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



    @Override
    public void onClick(View v) 
    {

        switch(v.getId())
        {


            case R.id.setLocation:

                mLocation.setEnabled(true);
                mLocation.setText("");
                break;

            case R.id.mShareIcon:
                if(mScrollView.getVisibility()==View.VISIBLE)
                {
                    mScrollView.startAnimation(mANimationLeft);
                    mScrollView.setVisibility(View.GONE);
                    onMenuCounter--;
                }
                if(mClockLayout.getVisibility()==View.VISIBLE)
                {
                    mClockLayout.setVisibility(View.GONE);
                    onClockCounter--;
                }
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mMenu:
                if(mClockLayout.getVisibility()==View.VISIBLE)
                {
                    mClockLayout.setVisibility(View.GONE);
                    onClockCounter--;
                }
                if(onMenuCounter==0)
                {
                    mScrollView.setVisibility(View.VISIBLE);
                    mScrollView.startAnimation(mAnimationRight);
                    onMenuCounter++;
                }
                else
                {
                    onMenuCounter--;
                    mScrollView.startAnimation(mANimationLeft);
                    mScrollView.setVisibility(View.GONE);

                }
                break;    

            case R.id.mClock:
                if (circle==null){
                    drawCircle();
                }
                //circle=new DraggableCircle(new LatLng(gps.getLatitude(),gps.getLongitude()),100);
                circle.onStyleChange();
                if(mScrollView.getVisibility()==View.VISIBLE)
                {
                   // mScrollView.startAnimation(mAnimDown);
                    mScrollView.setVisibility(View.GONE);
                    onMenuCounter--;
                }
                if(mClockLayout.getVisibility()!=View.VISIBLE)
                {
                    //mClockLayout.startAnimation(mANimUp);
                    mClockLayout.setVisibility(View.VISIBLE);
                    onClockCounter++;
                }
                else{
                    mClockLayout.setVisibility(View.INVISIBLE);
                    onClockCounter--;

                }
//                else
//                {
//                    onClockCounter--;
//                    //mClockLayout.startAnimation(mANimUp);
//                    mClockLayout.setVisibility(View.GONE);
//
//                }

                break;


            case R.id.mDownArrow:

                Intent intnt=new Intent(MainPageDrawerAcitivity.this,ListViewPlacesList.class);
                startActivity(intnt);
                break;
            case R.id.mSLiderLeftButton:

                mTopSliderLayout.setVisibility(View.GONE);
                mTopSliderLayout.startAnimation(mANimationLeft);

                break;


            case R.id.topSliderButton:

                mTopSliderLayout.setVisibility(View.VISIBLE);
                mTopSliderLayout.startAnimation(mAnimationRight);

                break;

            case R.id.middleSliderButton:


                break;

            case R.id.bottomSliderButton:
                onMapCounter++;
                mBottomSlider.setVisibility(View.VISIBLE);
                mBottomSlider.startAnimation(mAnimationRight);
                break;


            case R.id.mEmoticon:

                if(mScrollHori.getVisibility()== View.VISIBLE)
                {
                    mScrollHori.setVisibility(View.GONE);
                }
                else
                {
                    mScrollHori.setVisibility(View.VISIBLE);
                }

                break;


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


            case R.id.mSendComment:

                sSpinnerText=mLocation.getText().toString().trim();
                sCommentText=mComments.getText().toString();
                if(sSpinnerText.trim().length()>0)
                {
                    if(sMonkey.equalsIgnoreCase("0"))
                    {
                        Toast.makeText(MainPageDrawerAcitivity.this, "Select your icon", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        String url;
                        //  ServiceWork.businessSearchData.contains(sSpinnerText);
                        Log.e("contain",""+CommonFunction.sNearByPlace.contains(mLocation.getText().toString()));
                        if(CommonFunction.sNearByPlace.contains(sSpinnerText))
                        {  

                            CommonFunction.sActivityName="MainPageWithMapActivity";
                            if(sSpinnerText.equalsIgnoreCase(ServiceWork.businessSearchData.get(0).getPlaceAddress()))
                            {
                                mURL=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageDrawerAcitivity.sCommentText)+"&icon="+MainPageDrawerAcitivity.sMonkey+"&lat="+ServiceWork.businessSearchData.get(0).getLatitude()+"&lng="+ServiceWork.businessSearchData.get(0).getLongitude()+"&location="+URLEncoder.encode(sSpinnerText);
                            }
                            else
                            {
                                mURL=CommonFunction.host+"locationComments?user_id="+CommonFunction.sUserInfo.get(0).get_Id()+"&comment="+URLEncoder.encode(MainPageDrawerAcitivity.sCommentText)+"&icon="+MainPageDrawerAcitivity.sMonkey+"&lat="+ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLatitude()+"&lng="+ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLongitude()+"&location="+URLEncoder.encode(sSpinnerText);

                            }

                            //  onMapCounter--;

                            ListViewForPlaces.listCheckClick=0;
                            sFlag2=1;
                            mComments.setText("");
                            mComments.clearFocus();

                            mSendComment.requestFocus();
                            mLocation.setText(ServiceWork.businessSearchData.get(0).getPlaceAddress());
                            mEmoticon.setImageResource(R.drawable.monkey_5);
                            mScrollHori.setVisibility(View.GONE);
                            commentMethodStartsTheAsyncTask();
                            InputMethodManager inputManager =
                                    (InputMethodManager) this.
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(
                                    this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        else
                        {
                            CommonFunction.sActivityName="GeoCode";
                            mURL="https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(sSpinnerText)+"&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";   
                            commentMethodStartsTheAsyncTask();
                        }
                    }

                }

                else
                {
                    Toast.makeText(MainPageDrawerAcitivity.this, "No location is selected", Toast.LENGTH_SHORT).show();

                }

                break;

            case R.id.searchIcon:

                if(mSearchTxt.getText().toString().trim().length()>0)
                {
                    onMapCounter--;
                    mBottomSlider.setVisibility(View.GONE);
                    mBottomSlider.startAnimation(mANimationLeft);
                    sFlag=2;
                    String search=mSearchTxt.getText().toString().trim();
                    CommonFunction.sActivityName="SearchYampp";
                    mURL=CommonFunction.host+"search?keyword="+URLEncoder.encode(search);
                    methodThatStartsTheAsyncTask();


                }
                break;

            case R.id.oneHour:
                onClockCounter--;
                //mClockLayout.startAnimation(mANimUp);
                mClockLayout.setVisibility(View.GONE);
                sFlag=2;
                CommonFunction.sActivityName="SearchYampp";
                mURL=CommonFunction.host+"advanceSearch?keyword=hour";
                map.clear();
                circle = null;
                methodThatStartsTheAsyncTask();


                break;

            case R.id.oneWeek:
                onClockCounter--;
                //mClockLayout.startAnimation(mANimUp);
                mClockLayout.setVisibility(View.GONE);
                sFlag=2;
                CommonFunction.sActivityName="SearchYampp";
                mURL=CommonFunction.host+"advanceSearch?keyword=week";
                map.clear();
                circle = null;
                methodThatStartsTheAsyncTask();

                break;

            case R.id.oneMonth:
                onClockCounter--;
                //mClockLayout.startAnimation(mANimUp);
                mClockLayout.setVisibility(View.GONE);
                sFlag=2;
                CommonFunction.sActivityName="SearchYampp";
                mURL=CommonFunction.host+"advanceSearch?keyword=month";
                map.clear();
                circle = null;
                methodThatStartsTheAsyncTask();

                break;


            case R.id.shareYourLocation:
                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.editYourProfile:
                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.mapSetting:
                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.logout:
                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;

                //Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();
                logout();

                break;

            case R.id.fbGreen:

                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
              //  adapter.updateStatus("Check out this awesome app!"+ Calendar.getInstance().getTime(), new MessageListener(),true);
                Log.d("Response Listener", "onComplete() executinggg!");
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.googleGreen:

                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.twitterGreen:

                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                adapter.updateStatus("hello from wynkk"+Calendar.getInstance().getTime(), new MessageListener(),false);
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;

            case R.id.linkedinGreen:

                mScrollView.startAnimation(mANimationLeft);
                mScrollView.setVisibility(View.GONE);
                onMenuCounter--;
                Toast.makeText(MainPageDrawerAcitivity.this,"Hang on!  will about to implement that feature soon!", Toast.LENGTH_SHORT).show();


                break;


        }

    }


    public void methodThatStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,MainPageDrawerAcitivity.this,new FragmentCallback() {

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


                    //                   if(s)
                    //                    
                    //                    map.clear();
                    //                    BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
                    //                    Bitmap b=bd.getBitmap();
                    //                    Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
                    //                    LatLng pos1=new LatLng(ServiceWork.lati,ServiceWork.longi );
                    //                    marker =map.addMarker(new MarkerOptions().icon(
                    //                            BitmapDescriptorFactory
                    //                            .fromBitmap(bhalfsize))
                    //                            .position(pos1));
                    //                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    //                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));
                    //                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 15));
                    //                    mLocation.setText(ServiceWork.formated_Adres);
                    //                    ListViewForPlaces.listCheckClick=0;
                }
            }
        });
        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }


    public void drawMarkerOnMap()
    {
        ArrayList<SearchWynkkModel.Data> wynks = CommonFunction.sSearchYammp;
        map.clear();
        circle = null;
        wynkksMap.keySet();
        LatLng pos1;
        float meters = 10000.0f;
        float [] res= new float[10];
        BitmapDescriptor bitmapDescriptor=null;
//        double lat=Double.parseDouble(wynks.get(wynks.size()-1).get_latitude());
//        double lon=Double.parseDouble(wynks.get(wynks.size()-1).get_longitude());
//        pos1 = new LatLng(lat,lon);
        wynkksMap.put(new LatLng(0,0),new ArrayList<Marker>());
        for(int i=wynks.size()-1;i>=0;i--)
        {
            double lat=Double.parseDouble(wynks.get(i).get_latitude());
            double lon=Double.parseDouble(wynks.get(i).get_longitude());
            pos1 = new LatLng(lat,lon);
            Log.d("hello ",lat+" "+lon);

            if(wynks.get(i).get_monkey_icon().equalsIgnoreCase("monkey_1"))
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.m1);

            }
            if(wynks.get(i).get_monkey_icon().equalsIgnoreCase("monkey_2"))
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.m2);

            }
            if(wynks.get(i).get_monkey_icon().equalsIgnoreCase("monkey_3"))
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.m3);

            }
            if(wynks.get(i).get_monkey_icon().equalsIgnoreCase("monkey_4"))
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.m4);
            }
            if(wynks.get(i).get_monkey_icon().equalsIgnoreCase("monkey_5"))
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.m5);
            }

            marker=map.addMarker(new MarkerOptions().title(""+i).icon(bitmapDescriptor).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                for(LatLng ltlg:wynkksMap.keySet()){
                    Log.e("RADIUSSSS",String.valueOf(toRadiusMeters(pos1,ltlg)));
                    if(toRadiusMeters(pos1,ltlg)>meters) {
                        List<Marker> tmplst = new ArrayList<Marker>(5);
                        tmplst.add(marker);
                        wynkksMap.put(pos1,tmplst);
                    }else{
                        wynkksMap.get(ltlg).add(marker);
                        marker.remove();
                    }
            }
            position1=pos1;

        }
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 8));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position1, 12));
        setupAdapter();

    }


    private void addInfoWindow() {

        // infoWindowPos = pos;

        try {
            //infoWindow = null;
            MapInfoViewFactory fact=new MapInfoViewFactory();
            infoWindow = fact.generateHelperInfoView(MainPageDrawerAcitivity.this);
            int  width = (int) ImageHelper.dpToPixel(this, 250);
            int height = (int) ImageHelper.dpToPixel(this, 190);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    width, height);
            layoutMapOverlay.addView(infoWindowClickable, params);


            moveInfoWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void moveInfoWindow() {

        if (infoWindow == null)
            return;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoWindowClickable
                .getLayoutParams();
        Point screenPosition = map.getProjection().toScreenLocation(
                infoWindowPos);

        params.leftMargin = screenPosition.x - params.width+100;
        params.topMargin = screenPosition.y - params.height ;

        infoWindowClickable.setLayoutParams(params);

    }

    private void drawCircle(){
        circle = new DraggableCircle(SYDNEY, 10000);
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 4.0f));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e(TAG,"CHECK THIS UP ");
        Log.e("hiiii reusme",AfterLoginActivity.sPlacePosition+" "+AfterLoginActivity.sHomeLoc);
        //  mLocation.setText(ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getPlaceAddress());
        if(ListViewPlacesList.listCheckClick==1)
        {
            mLocation.setText(AfterLoginActivity.sHomeLoc);
            // ListViewForPlaces.listCheckClick=0;
        }
        else
        {
            if(sFlag2==1)
            {
                sFlag2=0;
            }
            else
            {
                if (gps.canGetLocation()) 
                {

                    Double latitude=Double.parseDouble(prefs.getString("saveLatitude", "0"));
                    Double longitde=Double.parseDouble(prefs.getString("saveLongitude", "0"));
                    if(latitude!=gps.latitude&&longitde!=gps.longitude)
                    {
                        mLocationLayout.setVisibility(View.GONE);
                        ServiceWork.businessSearchData.clear();
                        CommonFunction.sActivityName="placeList";
                        mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+gps.getLatitude()+","+gps.getLongitude()+"&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                    //    mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.2000143,-101.9556949&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                        
                        methodWillStartsTheAsyncTask() ;
                        prefsEditor.putString("saveLatitude",""+gps.getLatitude());
                        prefsEditor.putString("saveLongitude",""+gps.getLongitude());
                        prefsEditor.commit();
                    }
                    else
                    {
                        Log.d("old lat and long",latitude+ " "+ longitde);
                        Log.d("new lat and long",gps.getLatitude()+ " "+ gps.getLongitude());
                    }
                } else 
                {

                    return;
                }
            }}

    }


    @SuppressLint("HandlerLeak")
    public  Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try
            {
                // addInfoWindow();

                double lat;
                double lon;
                if(sSpinnerText.equalsIgnoreCase(ServiceWork.businessSearchData.get(0).getPlaceAddress()))
                {
                    lat=Double.parseDouble(ServiceWork.businessSearchData.get(0).getLatitude().toString());
                    lon=Double.parseDouble(ServiceWork.businessSearchData.get(0).getLongitude().toString());
                }
                else
                {
                    lat=Double.parseDouble(ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLatitude().toString());
                    lon=Double.parseDouble(ServiceWork.businessSearchData.get(AfterLoginActivity.sPlacePosition).getLongitude().toString());

                }
                final LatLng pos1 = new LatLng(lat,lon);
                Log.d("hello ",""+lat+" , "+lon);
                Log.e("please add marker","on map");
                if(sMonkey.equalsIgnoreCase("monkey_1"))
                {
                    Log.d("hello ",""+sMonkey);
                    marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(pos1));    

                    //marker.showInfoWindow();
                }
                if(sMonkey.equalsIgnoreCase("monkey_2"))
                {
                    Log.d("hello ",""+sMonkey);
                    marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m2)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                    // marker.showInfoWindow();
                }
                if(sMonkey.equalsIgnoreCase("monkey_3"))
                {
                    Log.d("hello ",""+sMonkey);
                    marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m3)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                    //  marker.showInfoWindow();
                }
                if(sMonkey.equalsIgnoreCase("monkey_4"))
                {
                    Log.d("hello ",""+sMonkey);
                    marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m4)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                    // marker.showInfoWindow();
                }
                if(sMonkey.equalsIgnoreCase("monkey_5"))
                {
                    Log.d("hello ",""+sMonkey);
                    marker=map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m5)).position(pos1).draggable(false).flat(true).anchor(0.5f, 0.5f));
                    //marker.showInfoWindow();
                }

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 14));


            }
            catch(Exception e){}
        }
    };
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);

        return result[0];
    }
    private class DraggableCircle {
        private int mWidthBar=10;
        private int mStrokeColor= Color.argb(95,113,208,200);
        private int mFillColor = Color.argb(70,113,208,200);

        private Marker centerMarker=null;
        private Marker radiusMarker=null;
        private Circle circle;
        private LatLng center;
        private double radius;
        public DraggableCircle(LatLng center, double radius) {
            this.center=center;
            this.radius = radius;
//            centerMarker = map.addMarker(new MarkerOptions()
//                    .position(center)
//                    .draggable(true));
//            radiusMarker = map.addMarker(new MarkerOptions()
//                    .position(toRadiusLatLng(center, radius))
//                    .draggable(true)
//                    .icon(BitmapDescriptorFactory.defaultMarker(
//                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public DraggableCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            centerMarker = map.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = map.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }
            if (marker.equals(radiusMarker)) {
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                circle.setRadius(radius);
                return true;
            }
            return false;
        }
        public void onStyleChange() {

            circle.setRadius(radiusBar.getProgress());
            //radiusMarker.setPosition(toRadiusLatLng(center, radiusBar.getProgress()));
            circle.setCenter(new LatLng(gps.getLatitude(),gps.getLongitude()));
            circle.setStrokeWidth(mWidthBar);
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
        }
    }


    //LISTENER FOR SOCIALAUTH ANDROID LIB
    private final class ResponseListener implements DialogListener
    {
        public void onComplete(Bundle values) {

            adapter.updateStatus(messageToShare, new MessageListener(),false);
            //adapter.updateStatus("hello from wynkk"+Calendar.getInstance().getTime(), new MessageListener(),false);
            adapter.getUserProfileAsync(new ProfileDataListener());

            Log.d("Response Listener","onComplete() executing!");

        }

        @Override
        public void onError(SocialAuthError e) {
            Log.e("ResponseListener",e.toString());
        }


        public void onCancel() {
            Log.d("ShareButton" , "Cancelled");
        }

        @Override
        public void onBack() {

        }
    }

    // To get status of message after authentication
    private final class MessageListener implements SocialAuthListener<Integer> {

        public void onExecute(String provider,Integer t) {
            Integer status = t;
            if (provider.equalsIgnoreCase("FACEBOOk")) {
                if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
                    Toast.makeText(MainPageDrawerAcitivity.this, "Message posted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainPageDrawerAcitivity.this, "Message not posted" + status.intValue(), Toast.LENGTH_LONG).show();
            }
        }

        public void onError(SocialAuthError e) {

        }
    }

    private final class ProfileDataListener implements
            SocialAuthListener<Profile> {
        Profile profileMap;
        String mUserName="a",uName="b",uEmail="c",mEmail="d";

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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void onError(SocialAuthError e) {

        }
    }

    public void setupAdapter(){
        if(CommonFunction.sSearchYammp!=null) {
            WynkkListAdapter adapter = new WynkkListAdapter(this, R.layout.last_comments_list_item, CommonFunction.sSearchYammp);
            lastWynkksList.setVisibility(View.VISIBLE);

            lastWynkksList.setAdapter(adapter);
        }



    }
    private class WynkkListAdapter extends ArrayAdapter<SearchWynkkModel.Data>{

        public WynkkListAdapter(Context context, int resource, List<SearchWynkkModel.Data> wynkks) {
            super(context, resource, wynkks);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.last_comments_list_item,null);
            }
            SearchWynkkModel.Data wynkk = getItem(position);
            TextView place = (TextView)convertView.findViewById(R.id.list_item_placeText);
            TextView time = (TextView)convertView.findViewById(R.id.list_item_timeText);
            ImageView image = (ImageView)convertView.findViewById(R.id.list_item_imageView);
            try {
                //Log.d(TAG,wynkk.get_Time());
                place.setText(wynkk.get_location().split(",")[0]);
                time.setText(wynkk.get_comment());
                int imgResId=R.drawable.monkey_5;
                if(wynkk.get_monkey_icon().equalsIgnoreCase("monkey_5")){

                }else if(wynkk.get_monkey_icon().equalsIgnoreCase("monkey_4")){
                    imgResId = R.drawable.monkey_4;
                }else if(wynkk.get_monkey_icon().equalsIgnoreCase("monkey_3")){
                    imgResId = R.drawable.monkey_3;
                }else if(wynkk.get_monkey_icon().equalsIgnoreCase("monkey_2")){
                    imgResId = R.drawable.monkey_2;
                }else if(wynkk.get_monkey_icon().equalsIgnoreCase("monkey_1")){
                    imgResId = R.drawable.monkey_1;
                }
                image.setImageResource(imgResId);
            }catch(Exception e ){
                e.printStackTrace();
            }
            return convertView;
        }

    }
    private void logout(){
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
        Intent   intent =new Intent(MainPageDrawerAcitivity.this,FirstActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }


}


