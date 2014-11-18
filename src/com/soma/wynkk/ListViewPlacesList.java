package com.soma.wynkk;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soma.lazylist.ImageLoader;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.util.ServiceWork;

public class ListViewPlacesList extends Activity
{
    public static int listCheckClick=0,sNearByCounter=0;
    ProgressDialog progressBar;
    // GPS Location
    GPSTracker gps;
    // Google Places
    GooglePlaces googlePlaces;
    // Places List
    PlacesList nearPlaces;
    ListView list;
    Typeface typeface;
    String mURL;
    ProgressBar mProgress;
    public static Double slatitude=0.0,sLongitude=0.0;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.place_list_view);
        prefs = PreferenceManager.getDefaultSharedPreferences(ListViewPlacesList.this); //initilize the shared preferences
        prefsEditor=prefs.edit();
        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        list=(ListView)findViewById(R.id.listview);
        mProgress=(ProgressBar)findViewById(R.id.progressBar);
        //         CommonFunction.sNearByPlace.clear();
        //         CommonFunction.sNearLatitude.clear();
        //         CommonFunction.sNearLongitude.clear();
        // creating GPS Class object
        gps = new GPSTracker(this);
        // check if GPS location can get
        if (gps.canGetLocation()) 
        {
//            ServiceWork.businessSearchData.clear();
//            CommonFunction.sActivityName="placeList";
//            mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+gps.getLatitude()+","+gps.getLongitude()+"&radius=1500&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
//            methodThatStartsTheAsyncTask() ;

            
            Double latitude=Double.parseDouble(prefs.getString("saveLatitude", "0"));
            Double longitde=Double.parseDouble(prefs.getString("saveLongitude", "0"));
            if(latitude!=gps.latitude&&longitde!=gps.longitude)
            {
                ServiceWork.businessSearchData.clear();
                CommonFunction.sActivityName="placeList";
                mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+gps.getLatitude()+","+gps.getLongitude()+"&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
             //   mURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.2000143,-101.9556949&radius=250&key=AIzaSyBDbHzEkRUXsrf5g-7Yb_sJq40GzGbtyiI";
                
                methodThatStartsTheAsyncTask() ;
                prefsEditor.putString("saveLatitude",""+gps.getLatitude());
                prefsEditor.putString("saveLongitude",""+gps.getLongitude());
                prefsEditor.commit();
            }
            else
            {
                Log.d("old lat and long",latitude+ " "+ longitde);
                Log.d("new lat and long",gps.getLatitude()+ " "+ gps.getLongitude());
            }
        
        }
         ListAdapter adapter=new ListAdapter(ListViewPlacesList.this);
         list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() 
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
            {
                listCheckClick=1;
                AfterLoginActivity.sPlacePosition=arg2;
                AfterLoginActivity.sHomeLoc=ServiceWork.businessSearchData.get(arg2).getPlaceAddress();
                ListViewPlacesList.this.finish();    
            }


        });

    }



    public void methodThatStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,ListViewPlacesList.this,new FragmentCallback() {

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

                                methodThatStartsTheAsyncTask() ;
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
                    
//                    ArrayAdapter<String> locAdapter= new ArrayAdapter<String>(ListViewPlacesList.this,
//                            R.layout.city_list_item_row,CommonFunction.sNearByPlace);
//                    list.setAdapter(locAdapter);     
                    ListAdapter adapter=new ListAdapter(ListViewPlacesList.this);
                    list.setAdapter(adapter);
                    mProgress.setVisibility(View.GONE);
                }}
        });
        testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
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
            Log.d("places are","123   "+FirstActivity.mPlaceList);
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
                double radius = 250; // 1000 meters 
                Log.d("places are","12345   "+FirstActivity.mPlaceList);
                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types);
                Log.d("places are","12345678   "+FirstActivity.mPlaceList);

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
            /**
             * Updating parsed Places into LISTVIEW
             * */
            Log.d("places are","123 910111  "+FirstActivity.mPlaceList);
            // Get json response status
            String status = nearPlaces.status;
            Log.d("places are","123 910111 12 13 "+status);
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
                    ArrayAdapter<String> locAdapter= new ArrayAdapter<String>(ListViewPlacesList.this,
                            R.layout.city_list_item_row,FirstActivity.mPlaceList);
                    list.setAdapter(locAdapter);                     

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


    }





    public void viewProgressGone() {
        if(progressBar!=null)
            progressBar.dismiss();
    }

    public void viewProgressVisible(String paramString) {
        progressBar = new ProgressDialog(ListViewPlacesList.this);
        progressBar.setCancelable(true);
        progressBar.setMessage(paramString);

        progressBar.show();
    }
    
    
  class ListAdapter extends BaseAdapter
  {
      public ImageLoader imageLoader;
      Activity activity;
      public ListAdapter(Activity activity) 
      {
    this.activity=activity;
 // Create ImageLoader object to download and show image in list
    // Call ImageLoader constructor to initialize FileCache
    imageLoader = new ImageLoader(activity);
          // TODO Auto-generated constructor stub
    }
      
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ServiceWork.businessSearchData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        LayoutInflater inflator = activity.getLayoutInflater();
        convertView = inflator.inflate(R.layout.city_list_item_row, null);
        
        TextView place=(TextView)convertView.findViewById(R.id.mPlace);
        TextView placeAddr=(TextView)convertView.findViewById(R.id.mPlaceAddr);
        ImageView mPlaceImage=(ImageView)convertView.findViewById(R.id.mPlaceImage);
        imageLoader.DisplayImage(ServiceWork.businessSearchData.get(pos).getIcon(), mPlaceImage);
        Log.i("LIST VIEW ADDRESS",ServiceWork.businessSearchData.get(pos).getPlaceAddress());
        place.setText(ServiceWork.businessSearchData.get(pos).getPlaceAddress().split(",")[0]);
        placeAddr.setText(ServiceWork.businessSearchData.get(pos).getPlaceAddress());
        return convertView;
    }
      
  }
    
}
