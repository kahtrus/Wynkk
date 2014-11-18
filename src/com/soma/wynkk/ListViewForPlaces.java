package com.soma.wynkk;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.soma.synge.R;

public class ListViewForPlaces extends Activity
{
public static int listCheckClick=0;
ProgressDialog progressBar;
// GPS Location
GPSTracker gps;
// Google Places
GooglePlaces googlePlaces;
// Places List
PlacesList nearPlaces;
ListView list;
Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.place_list_view);
        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        list=(ListView)findViewById(R.id.listview);
         
        // creating GPS Class object
        gps = new GPSTracker(this);
        // check if GPS location can get
        if (gps.canGetLocation()) 
        {
            
        }
        Log.d("places are","place="+FirstActivity.mPlaceList);
      new LoadPlaces().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      

        list.setOnItemClickListener(new OnItemClickListener() 
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
            {
                listCheckClick=1;
                AfterLoginActivity.sPlacePosition=arg2;
                AfterLoginActivity.sHomeLoc=FirstActivity.mPlaceList.get(arg2);
                ListViewForPlaces.this.finish();    
            }


        });

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
                double radius = 1000; // 1000 meters 
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
                            ArrayAdapter<String> locAdapter= new ArrayAdapter<String>(ListViewForPlaces.this,
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
        progressBar = new ProgressDialog(ListViewForPlaces.this);
        progressBar.setCancelable(true);
        progressBar.setMessage(paramString);

        progressBar.show();
    }
}
