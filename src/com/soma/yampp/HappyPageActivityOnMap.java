package com.soma.yampp;



import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.util.ServiceWork;

public class HappyPageActivityOnMap extends Activity implements OnClickListener,OnInfoWindowClickListener
{

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }
//    Marker marker = null;
//    public static int sSliderCount=0,sSliderBottom=0,sActivityChk=0;;
//    public Context mContext;
//    public  String sUrl;
//    public static FrameLayout layoutMapOverlay = null;
//    // Current visible info view
//    public static View infoWindow = null;
//    // Current selected marker
//    LatLng infoWindowPos = null;
//    Typeface typeface;
//    private GoogleMap map;
//    ImageView mMenu,mUserImage,mSearchIcon,mSliderUp,mSliderBottom;
//    Button mOneHour,mOneWeek,mOneMonth;
//    EditText mSearchText;
//    TextView mUserNameTxt,mUserNameTxt1;
//    LinearLayout mTopHeader,mProfileWithSearchLayuot,mBootomView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.map_fragment_xml);
//        typeface=Typeface.createFromAsset(getAssets(), "Semplicita-light.ttf");
//        mContext=HappyPageActivityOnMap.this;
//        layoutMapOverlay = (FrameLayout) findViewById(R.id.map_overlay_layout);
//        try{
//            if (map==null)
//                map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap(); 
//            // map.setMyLocationEnabled(true);
//            map.setIndoorEnabled(true);
//            
//        }
//        catch(Exception ex){}
//        //        BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
//        //        Bitmap b=bd.getBitmap();
//        //        Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
//       
//      
//        Log.d("Size of array",""+CommonFunction.sSearchYammp.size());
//        LatLng pos1 = new LatLng(Double.parseDouble(CommonFunction.sSearchYammp.get(0).get_latitude()),Double.parseDouble(CommonFunction.sSearchYammp.get(0).get_longitude()));
//        //        final Marker marker=map.addMarker(new MarkerOptions().icon(
//        //                BitmapDescriptorFactory
//        //                .fromBitmap(bhalfsize))
//        //                .position(pos1));
//        //        marker.showInfoWindow();
//        if(CommonFunction.sSearchYammp.get(0).get_monkey_icon().equalsIgnoreCase("monkey_1"))
//        {
//            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(pos1).draggable(true).title("0").anchor(0.0f, 1.0f));    
//            //marker.showInfoWindow();
//        }
//        if(CommonFunction.sSearchYammp.get(0).get_monkey_icon().equalsIgnoreCase("monkey_2"))
//        {
//            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m2)).position(pos1).draggable(true).title("0").anchor(0.0f, 1.0f));    
//            // marker.showInfoWindow();
//        }
//        if(CommonFunction.sSearchYammp.get(0).get_monkey_icon().equalsIgnoreCase("monkey_3"))
//        {
//            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m3)).position(pos1).draggable(true).title("0").anchor(0.0f, 1.0f));    
//            //  marker.showInfoWindow();
//        }
//        if(CommonFunction.sSearchYammp.get(0).get_monkey_icon().equalsIgnoreCase("monkey_4"))
//        {
//            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m4)).position(pos1).draggable(true).title("0").anchor(0.0f, 1.0f));    
//            // marker.showInfoWindow();
//        }
//        if(CommonFunction.sSearchYammp.get(0).get_monkey_icon().equalsIgnoreCase("monkey_5"))
//        {
//            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.m5)).position(pos1).draggable(true).title("0").anchor(0.0f, 1.0f));    
//            //marker.showInfoWindow();
//        }
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 12));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 12));
//       
//      
//
//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        mUserNameTxt=(TextView)findViewById(R.id.mUserName);
//
//        mUserNameTxt1=(TextView)findViewById(R.id.mUserName1);
//        mSliderUp=(ImageView)findViewById(R.id.mSLiderUpDown);
//
//        mTopHeader=(LinearLayout)findViewById(R.id.topHeader);
//        mProfileWithSearchLayuot=(LinearLayout)findViewById(R.id.ProfileWithSearchLayuot);
//
//        mUserNameTxt.setTypeface(typeface);
//        mUserNameTxt1.setTypeface(typeface);
//        mUserNameTxt.setText(CommonFunction.sUserInfo.get(0).get_UserName());
//        mUserNameTxt1.setText(CommonFunction.sUserInfo.get(0).get_UserName());
//        mMenu=(ImageView)findViewById(R.id.mMenu);
//        mUserImage=(ImageView)findViewById(R.id.mUserImage);
//        mSearchIcon=(ImageView)findViewById(R.id.searchIcon);
//
//        mOneHour=(Button)findViewById(R.id.mOneHour);
//        mOneMonth=(Button)findViewById(R.id.mOneMonth);
//        mOneWeek=(Button)findViewById(R.id.mOneWeek);
//
//        mSearchText=(EditText)findViewById(R.id.mSearch);
//
//        mOneHour.setTypeface(typeface);
//        mOneMonth.setTypeface(typeface);
//        mOneWeek.setTypeface(typeface);
//        mSearchText.setTypeface(typeface);
//
//        mMenu.setOnClickListener(this);
//        mSearchIcon.setOnClickListener(this);
//        mOneHour.setOnClickListener(this);
//        mOneMonth.setOnClickListener(this);
//        mOneWeek.setOnClickListener(this);
//
//        mSliderUp.setOnClickListener(this);
//
//        map.setOnInfoWindowClickListener(this);
//
//        mUserImage.setImageBitmap(MainPageWithMapActivity.mProfilePIc);
//        showWindorForInforation();
//
//        //        map.setOnMarkerClickListener(new OnMarkerClickListener() {
//        //
//        //            @Override
//        //            public boolean onMarkerClick(Marker arg0) {
//        //                // TODO Auto-generated method stub
//        //              //  removeInfoWindow();
//        //                //addInfoWindow(marker.getTitle(), marker.getPosition());
//        //                return true;
//        //            }
//        //        });
//
//
//        //        map.setOnCameraChangeListener(new OnCameraChangeListener() {
//        //
//        //            @Override
//        //            public void onCameraChange(CameraPosition arg0) {
//        //                moveInfoWindow();
//        //                // captureScreen();
//        //            }
//        //        });
//
//   
//
//    }
//
//
//
//
//
//
//    @Override
//    public void onClick(View v) 
//    {
//       
//        switch (v.getId()) {
//            case R.id.mMenu:
//
//                Toast.makeText(HappyPageActivityOnMap.this,"Hang on!  were about to implement that feature soon!", Toast.LENGTH_SHORT).show();
//                break;
//
//
//            case R.id.mOneHour:
//                sActivityChk=1;
//          
//                CommonFunction.sActivityName="SearchYampp";
//                sUrl=CommonFunction.host+"advanceSearch?keyword=hour";
//                map.clear();
//                MainPageWithMapActivity.sFlag2=0;
//                methodThatStartsTheAsyncTask();
//
//                break;
//
//            case R.id.mOneWeek:
//                sActivityChk=1;
//               
//                CommonFunction.sActivityName="SearchYampp";
//                sUrl=CommonFunction.host+"advanceSearch?keyword=week";
//                map.clear();
//                MainPageWithMapActivity.sFlag2=0;
//                methodThatStartsTheAsyncTask();
//
//                break;
//
//            case R.id.mOneMonth:
//                sActivityChk=1;
//               
//                CommonFunction.sActivityName="SearchYampp";
//                sUrl=CommonFunction.host+"advanceSearch?keyword=month";
//                map.clear();
//               
//               // map=null;
//                MainPageWithMapActivity.sFlag2=0;
//                methodThatStartsTheAsyncTask();
//
//                break;
//
//            case R.id.searchIcon:
//
//                if(mSearchText.getText().toString().trim().length()>0)
//                {
//                    String search=mSearchText.getText().toString().trim();
//                    CommonFunction.sActivityName="SearchYampp";
//                    sUrl=CommonFunction.host+"search?keyword="+URLEncoder.encode(search);
//                    map.clear();
//                    MainPageWithMapActivity.sFlag2=0;
//                    methodThatStartsTheAsyncTask();
//
//                }
//
//                break;
//            case R.id.mSLiderUpDown:
//                if(HappyPageActivityOnMap.sSliderCount==0)
//                {
//                    HappyPageActivityOnMap.sSliderCount++;
//                    mTopHeader.setVisibility(View.VISIBLE);
//                    mProfileWithSearchLayuot.setVisibility(View.GONE);
//                    mSliderUp.setRotation(0);
//                }
//                else
//                {
//                    HappyPageActivityOnMap.sSliderCount--;
//                    mTopHeader.setVisibility(View.GONE);
//                    mProfileWithSearchLayuot.setVisibility(View.VISIBLE);
//                    mSliderUp.setRotation(180);
//                }
//
//            default:
//                break;
//        }  
//
//    }
//
//
//    public void methodThatStartsTheAsyncTask() 
//    {
//
//        ServiceWork testAsyncTask = new ServiceWork(sUrl,mContext,new FragmentCallback() {
//
//            @Override
//            public void onTaskDone() 
//            {
//
//                //                BitmapDrawable bd=(BitmapDrawable) getResources().getDrawable(R.drawable.marker);;
//                //                Bitmap b=bd.getBitmap();
//                //                Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
//                //                removeInfoWindow();
////                Intent intnt=new Intent(HappyPageActivityOnMap.this,HappyPageActivityOnMap.class);
////                startActivity(intnt);
//          
//               
//                LatLng pos1;
//                int i;
//                for(i=0;i<CommonFunction.sSearchYammp.size();i++)
//                {
//
//                    double lat=Double.parseDouble(CommonFunction.sSearchYammp.get(i).get_latitude());
//                    double lon=Double.parseDouble(CommonFunction.sSearchYammp.get(i).get_longitude());
//                    pos1 = new LatLng(lat,lon);
//                    Log.d("hello ",""+lat);
//                    if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_1"))
//                    {
//                        Log.d("hello ",""+i);
//                        marker=map.addMarker(new MarkerOptions().title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(pos1).draggable(true).flat(true).anchor(0.0f, 1.0f));    
//                        
//                        //marker.showInfoWindow();
//                    }
//                    if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_2"))
//                    {
//                        Log.d("hello ",""+i);
//                        marker=map.addMarker(new MarkerOptions().title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.m2)).position(pos1).draggable(true).flat(true).anchor(0.0f, 1.0f));    
//                        // marker.showInfoWindow();
//                    }
//                    if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_3"))
//                    {
//                        Log.d("hello ",""+i);
//                        marker=map.addMarker(new MarkerOptions().title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.m3)).position(pos1).draggable(true).flat(true).anchor(0.0f, 1.0f));    
//                        //  marker.showInfoWindow();
//                    }
//                    if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_4"))
//                    {
//                        Log.d("hello ",""+i);
//                        marker=map.addMarker(new MarkerOptions().title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.m4)).position(pos1).draggable(true).flat(true).anchor(0.0f, 1.0f));    
//                        // marker.showInfoWindow();
//                    }
//                    if(CommonFunction.sSearchYammp.get(i).get_monkey_icon().equalsIgnoreCase("monkey_5"))
//                    {
//                        Log.d("hello ",""+i);
//                        marker=map.addMarker(new MarkerOptions().title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.m5)).position(pos1).draggable(true).flat(true).anchor(0.0f, 1.0f));    
//                        //marker.showInfoWindow();
//                    }
//
//
//
//
//
//                }
//                //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos1, 15));
//                //   map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos1, 15));
//                //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                marker.showInfoWindow();
//                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//            }
//        });
//        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
//    }
//
//
//void showWindorForInforation()
//{
//    try{
//        if (map==null)
//            Log.d("Phone map","is null");
//            map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap(); 
//       // showWindorForInforation();
//       
//            // map.setMyLocationEnabled(true);
//    
//    
//    // Setting a custom info window adapter for the google map       
//    map.setInfoWindowAdapter(new InfoWindowAdapter() {
//
//        // Use default InfoWindow frame
//        @Override
//        public View getInfoWindow(Marker arg0) {               
//            return null;
//        }           
//
//        // Defines the contents of the InfoWindow
//        @Override
//        public View getInfoContents(Marker marker) {
//
//            // Getting view from the layout file info_window_layout_xml
//            final View view = getLayoutInflater().inflate(R.layout.info_window_layout_xml, null);
//
//            TextView mCommentText = (TextView) view.findViewById(R.id.mCommentText);
//            TextView mAddress = (TextView) view.findViewById(R.id.mAddress);
//            ImageView mSmilingMonkey=(ImageView)view.findViewById(R.id.mSmilingMoneky);
//            TextView mMinimizeMarker=(TextView)view.findViewById(R.id.minimizeMarker);
//            String id=marker.getTitle();
//
//            if(MainPageWithMapActivity.sFlag2==1)
//            {
//
//                int pos=Integer.parseInt(id);
//                Log.d("Marker pos",""+marker.getPosition());
//                Log.d("your position is",""+id);
//                String txt="<font>\"</font>"+CommonFunction.sSearchYammp.get(pos).get_comment()+"<font>\"</font>";
//                mCommentText.setText(Html.fromHtml(txt));
//
//                String text = "<font color=#ffffff>"+CommonFunction.sSearchYammp.get(pos).get_Username()+"</font>"+"@"+CommonFunction.sSearchYammp.get(pos).get_location();
//                mAddress.setText(Html.fromHtml(text));
//
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_1"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_1);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_2"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_2);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_3"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_3);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_4"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_4);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_5"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_5);
//                }
//
//            }
//            else
//            {
//
//
//                int pos=Integer.parseInt(id);
//                Log.d("Marker pos",""+marker.getPosition());
//                Log.d("your position is",""+id);
//                String txt="<font>\"</font>"+CommonFunction.sSearchYammp.get(pos).get_comment()+"<font>\"</font>";
//                mCommentText.setText(Html.fromHtml(txt));
//
//                String text = "<font color=#ffffff>"+CommonFunction.sSearchYammp.get(pos).get_Username()+"</font>"+"@"+CommonFunction.sSearchYammp.get(pos).get_location();
//                mAddress.setText(Html.fromHtml(text));
//
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_1"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_1);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_2"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_2);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_3"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_3);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_4"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_4);
//                }
//                if(CommonFunction.sSearchYammp.get(pos).get_monkey_icon().equalsIgnoreCase("monkey_5"))
//                {
//                    mSmilingMonkey.setImageResource(R.drawable.monkey_5);
//                }
//
//            }
//            //                mMinimizeMarker.setOnClickListener(new OnClickListener() {
//            //
//            //                    @Override
//            //                    public void onClick(View arg0) 
//            //                    {
//            //
//            //                        // view.clearFocus();
//            //                        view.setVisibility(view.GONE);
//            //
//            //
//            //                    }
//            //                });
//
//            return view;
//
//        }
//
//    }); 
//    }
//    catch(Exception ex){}
//}
//
//
//
//    @Override
//    public void onInfoWindowClick(Marker arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//    @SuppressLint("NewApi")
//    private void addInfoWindow(String id, LatLng pos) {
//
//        infoWindowPos = pos;
//
//        try {
//            infoWindow = null;
//            infoWindow = MapInfoViewFactory.generateHelperInfoView(this
//                    .getApplicationContext());
//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int w = size.x;
//            int h = size.y;
//            int  width = (int) ImageHelper.dpToPixel(this, w-100);
//            int height = (int) ImageHelper.dpToPixel(this, 190);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    w-50, height);
//            layoutMapOverlay.addView(infoWindow, params);
//
//
//            moveInfoWindow();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    static void removeInfoWindow() {
//        if (infoWindow != null) {
//            layoutMapOverlay.removeAllViews();
//        }
//        infoWindow = null;
//    }
//
//    @SuppressLint("NewApi")
//    private void moveInfoWindow() {
//
//        if (infoWindow == null)
//            return;
//
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoWindow
//                .getLayoutParams();
//        Point screenPosition = map.getProjection().toScreenLocation(
//                infoWindowPos);
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int w = size.x;
//        int h = size.y;
//        params.leftMargin =40; //screenPosition.x - params.width+150;
//        params.topMargin = screenPosition.y - params.height+80 ;
//
//        infoWindow.setLayoutParams(params);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        super.onBackPressed();
//        removeInfoWindow();
//    }
//
//
//@Override
//protected void onResume() {
//    // TODO Auto-generated method stub
//    super.onResume();
//   
//}
//
}
