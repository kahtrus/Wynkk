package com.soma.util;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.soma.model.SearchWynkkModel;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.soma.contraoller.SearchController;
import com.soma.contraoller.SignupController;
import com.soma.util.CommonFunction.FragmentCallback;
import com.soma.wynkk.AfterLoginActivity;
import com.soma.wynkk.ListViewPlacesList;
import com.soma.wynkk.MainPageDrawerAcitivity;


/********************************* Class to Register user through webservice ****************************/
public class ServiceWork extends AsyncTask<Void, Void, JSONObject> {

    public static String formated_Adres,sPageToken="";
    public static Double lati,longi;
    StringBuilder sb=null;
    String mResult = null;
    String[] mBusiness;
    JSONArray jArray;
    JSONArray dataArray; 
    String sMessage;
    String mCheckResponse;
    JSONArray jArray01;
    public static String mMessageForDealCheck="1",mAroundScreenCheck="1";
    int SignUpValidateCheckResponse;
    String regIds;
    CommonFunction com=new CommonFunction();
    ProgressDialog progressBar;
    String mUrl,mParameter;
    Context mContext;
    String sImg,sId,sName;
    InputStream mInputStreamis = null;
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    FragmentCallback fragmentCallback;
    public ServiceWork(String url,Context context) {
        // TODO Auto-generated constructor stub
        mUrl=url;
        mContext=context;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext); //initilize the shared preferences
        prefsEditor=prefs.edit();
    }


    //constructor with three argument
    public ServiceWork(String url,Context context,FragmentCallback fragmentCallback) {
        mUrl=url;
        mContext=context;
        this.fragmentCallback = fragmentCallback;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext); //initilize the shared preferences
        prefsEditor=prefs.edit();
    }

  //constructor with three argument
    public ServiceWork(String url,Context context,String a,FragmentCallback fragmentCallback) {
        mUrl=url;
        mContext=context;
        this.fragmentCallback = fragmentCallback;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext); //initilize the shared preferences
        prefsEditor=prefs.edit();
    }

    @Override
    protected void onPreExecute() {


        viewProgressVisible("Please Wait...");
        Log.d("the url is","onPreExecute");
    }

    @Override
    protected JSONObject doInBackground(Void... arg0) {

        // Log.d("the url is","activateSignupDetailUrl");
        if(CommonFunction.isInternetOn(mContext))
        {
            // Log.d("the url is 123","activateSignupDetailUrl");
            String activateSignupDetailUrl;
            activateSignupDetailUrl = com.GetServiceUrl(mUrl);
            Log.d("SERVICE WORK the url is",activateSignupDetailUrl);
            try{			
                mInputStreamis = com.connectionEstablished(activateSignupDetailUrl);

                if(mInputStreamis!=null)
                {
                    //convert response to string
                    mResult=com.converResponseToString(mInputStreamis);

                    if(!(mResult.equals("")))
                    {
                        Log.e("REGISTER RESULT YYOYOYO",mResult);
                        Log.e("REGISTER RESULT YYOYOYO",CommonFunction.sActivityName);
                        JSONObject ooo = new JSONObject(mResult);

                        String stat=ooo.getString("status");
                        Log.e("REGISTER RESULT",stat);
                        Log.e("REGISTER RESULT",mUrl);
                        Log.e("REGISTER RESULT",ooo.toString());

                        if(CommonFunction.sActivityName.equals("SocialMedia"))
                        {
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("1"))
                            {
                                SignupController getSignupController = new SignupController();
                                getSignupController.init(mResult);
                                ArrayList<com.soma.model.SignupModel.Data> userDeatail=getSignupController.findAll();
                                CommonFunction.sUserInfo.clear();
                                for(int i=0;i<userDeatail.size();i++)
                                {
                                    CommonFunction.sUserInfo.add(i,userDeatail.get(i));
                                    Log.d("userId",""+CommonFunction.sUserInfo.get(i).get_Id().toString());
                                    prefsEditor.putString("userId",CommonFunction.sUserInfo.get(i).get_Id().toString());
                                    prefsEditor.commit();
                                }
                                SignUpValidateCheckResponse=2;
                            }
                            else 
                            {
                                SignUpValidateCheckResponse=1;
                            }
                        }

                        if(CommonFunction.sActivityName.equals("MainPageWithMapActivity"))
                        {

                            Log.e("if blabla MainPageWithMapActivity",mResult);
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("1"))
                            {
                                SearchController getSignupController = new SearchController();
                                getSignupController.init(mResult);
                                ArrayList<SearchWynkkModel.Data> userDeatail=getSignupController.findAll();
                                CommonFunction.sSearchYammp.clear();
                                for(int i=0;i<userDeatail.size();i++)
                                {
                                    CommonFunction.sSearchYammp.add(i,userDeatail.get(i));

                                }
                                SignUpValidateCheckResponse=3;
                            }
                            else 
                            {
                                SignUpValidateCheckResponse=4;
                            }
                        }

                        if(CommonFunction.sActivityName.equals("GeoCode"))
                        {
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("ok"))
                            {

                                JSONArray a = o.getJSONArray("results");
                                JSONObject item = a.getJSONObject(0);
                                formated_Adres=item.getString("formatted_address");
                                //   Toast.makeText(SearchDialogActivity.this,formated_Adres, Toast.LENGTH_SHORT).show();
                                JSONObject location = item.getJSONObject("geometry")
                                        .getJSONObject("location");
                                lati= location.getDouble("lat");
                                longi=location.getDouble("lng");
                                SignUpValidateCheckResponse=6;
                            }
                            else
                            {
                                SignUpValidateCheckResponse=4;
                            }
                        }


                        if(CommonFunction.sActivityName.equals("SearchYampp"))
                        {
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("1"))
                            {
                                SearchController getSignupController = new SearchController();
                                getSignupController.init(mResult);
                                ArrayList<SearchWynkkModel.Data> userDeatail=getSignupController.findAll();
                                CommonFunction.sSearchYammp.clear();
                                for(int i=userDeatail.size()-1;i>=0;i--)
                                {
                                    CommonFunction.sSearchYammp.add(userDeatail.get(i));

                                }
                                SignUpValidateCheckResponse=5;
                            }
                            else 
                            {
                                SignUpValidateCheckResponse=4;
                            }
                        }


                        if(CommonFunction.sActivityName.equals("placeList"))
                        {
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("ok"))
                            {

                                JSONArray a = o.getJSONArray("results");
                                
                                if(o.has("next_page_token"))
                                {
                                    sPageToken=o.getString("next_page_token"); 
                                }
                                else
                                {
                                  sPageToken=null;  
                                }
                              
                                Log.d("Array is =",""+a);
                                Log.d("Array size is =",""+a.length());
                                ListViewPlacesList.sNearByCounter++;
                                for(int i=0;i<a.length();i++)
                                {
                                    JSONObject item = a.getJSONObject(i);
                                    String address;
                                    if(item.has("vicinity"))
                                    {
                                    address=item.getString("name")+", "+item.getString("vicinity");
                                    //   Toast.makeText(SearchDialogActivity.this,formated_Adres, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        address=item.getString("name");
                                    }
                                    
                                    String icon=item.getString("icon");
                                    Log.d("Array size is =12"," "+a.length());
                                    JSONObject location = item.getJSONObject("geometry")
                                            .getJSONObject("location");
                                    Log.d("Array size is =1234"," "+a.length());
                                    Double latitude= location.getDouble("lat");
                                    Double longitude=location.getDouble("lng");
                                    
                                    Double dist=distance(ListViewPlacesList.slatitude, ListViewPlacesList.sLongitude, latitude, longitude);
                                    Data data=new Data();
                                    
                                    data.setPlaceAddress(address);
                                    data.setLatitude(latitude);
                                    data.setLongitude(longitude);
                                    data.setDistance(dist);
                                    data.setIcon(icon);
                                    businessSearchData.add(data);
                                    Collections.sort(businessSearchData, Data.distanceOrderComparator);
                                    
                                    CommonFunction.sNearByPlace.add(address);
                                    CommonFunction.sNearLatitude.add(latitude);
                                    CommonFunction.sNearLongitude.add(longitude);
                                }
                                Log.d("Array size is =",""+a.length());
                                
                                SignUpValidateCheckResponse=7;
                                Log.d("Array size is =",""+a.length());
                            }
                            else
                            {
                                SignUpValidateCheckResponse=4;
                            }
                        }

                        if(CommonFunction.sActivityName.equals("CommentList"))
                        {
                            JSONObject o = new JSONObject(mResult);

                            String status=o.getString("status");

                            if(status.equalsIgnoreCase("1"))
                            {

                                SearchController getSignupController = new SearchController();
                                getSignupController.init(mResult);
                                ArrayList<SearchWynkkModel.Data> userDeatail=getSignupController.findAll();
                                CommonFunction.sSearchYammp.clear();
                                for(int i=0;i<userDeatail.size();i++)
                                {
                                    CommonFunction.sSearchYammp.add(i,userDeatail.get(i));
Log.d("size=",CommonFunction.sSearchYammp.size()+"");
                                }
                                SignUpValidateCheckResponse=5;
                            
                            }
                            else
                            {
                                SignUpValidateCheckResponse=4;
                            }
                        }

                    }
                    //Log.e("SIGNUP","NO RESULT (((((((((((((((((((9 ");
                }
            }catch (Exception e) {
                SignUpValidateCheckResponse=15;
            }

        }else
        {
            SignUpValidateCheckResponse=5000;
        }
        return null;
    }

    /**
     * 
     * 1 = Success
     * 2 = Error with HTTP connection
     * 3 = Error while convert into string 
     * 4 = Failure
     */
    protected void onPostExecute(JSONObject jso) {

//        if(CommonFunction.sActivityName.equals("placeList"))
//        {
//            
//        }
//        else
//        {
        
        //}
        Intent intent;
        Log.d("size=",SignUpValidateCheckResponse+"");
        switch(SignUpValidateCheckResponse)
        {
            
            case 500:
                viewProgressGone();
                fragmentCallback.onTaskDone();
                break;
            case 5000:
                viewProgressGone();
                Toast.makeText(mContext, "Please check your Internet Connection..!!", Toast.LENGTH_SHORT).show();
                break;

            case 1:
                viewProgressGone();
                intent=new Intent(mContext,AfterLoginActivity.class);
                mContext.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ((Activity)mContext).finish();

                break;
            case 2:
                viewProgressGone();
                intent=new Intent(mContext,MainPageDrawerAcitivity.class);
                mContext.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ((Activity)mContext).finish();

                break;

            case 3:
                viewProgressGone();
                Toast.makeText(mContext, "Sucessfully posted", Toast.LENGTH_SHORT).show();
                fragmentCallback.onTaskDone();
//                MainPageDrawerAcitivity ac=new MainPageDrawerAcitivity();
//                ac.messageHandler.sendEmptyMessage(0);
//                intent=new Intent(mContext,HappyPageActivityOnMap.class);
//                mContext.startActivity(intent);

                break;

            case 4:
                viewProgressGone();
                Toast.makeText(mContext, "fail to save...", Toast.LENGTH_SHORT).show();


                break;

            case 5:
                viewProgressGone();
                fragmentCallback.onTaskDone();

                break;

            case 6:
                viewProgressGone();
                fragmentCallback.onTaskDone();
                Log.d("formated address: "+formated_Adres,"lati="+lati+"longi="+longi);

                break;

            case 7:
                if(ServiceWork.sPageToken!=null)
                {
                    viewProgressGone();
                    fragmentCallback.onTaskDone();
                }
                else
                {
                    viewProgressGone();
                    fragmentCallback.onTaskDone();
                }
                
                break;
                
            case 8:
                 try {
                     viewProgressGone();
//                     MainPageDrawerAcitivity ac=new MainPageDrawerAcitivity();
//                 ac.messageHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                break;


        }
    }

    /***************************** clear Business List ***************************/


    /******************************************** Code To Show And Hide Progress Bar **********************************/
    public void viewProgressGone()
    {
        try
        {
            progressBar.dismiss();

        }
        catch(Exception e)
        {

        }
    }


    public void viewProgressVisible(String paramString)
    {
        try
        {
            progressBar = new ProgressDialog(mContext);
            progressBar.setCancelable(true);
            progressBar.setMessage(paramString);

            progressBar.show();
        }
        catch(Exception e)
        {

        }
    }
    
    
   public static ArrayList<Data> businessSearchData=new ArrayList<Data>();



    public ArrayList<Data> getBusinessSearchData() {
        return businessSearchData;
    }

    public void setBusinessSearchData(ArrayList<Data> businessSearchData) {
        this.businessSearchData = businessSearchData;
    }
    
    public static class Data
    {
        
        private String placeAddress;
        private Double latitude;
        private Double longitude;
        private Double distance;
        private String Icon;
        
        
        public String getIcon() {
            return Icon;
        }
        public void setIcon(String icon) {
            Icon = icon;
        }
        public Double getDistance() {
            return distance;
        }
        public void setDistance(Double distance) {
            this.distance = distance;
        }
        public String getPlaceAddress() {
            return placeAddress;
        }
        public void setPlaceAddress(String placeAddress) {
            this.placeAddress = placeAddress;
        }
        public Double getLatitude() {
            return latitude;
        }
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        public Double getLongitude() {
            return longitude;
        }
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
        
        
        public static Comparator<Data> distanceOrderComparator 
        = new Comparator<Data>() 
        {
            //ascending order
            //return fruitName1.compareTo(fruitName2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
            @Override
            public int compare(Data d1, Data d2) {
                // TODO Auto-generated method stub

                Double D1=d1.getDistance();
                Double D2=d2.getDistance();
                return D1.compareTo(D2);
            }

        };
        
    }
    
    
    
    
    
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
         return (dist);
      }

     private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
      }
     private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
      }
}

/******************************************** Code To Show And Hide Progress Bar **********************************/