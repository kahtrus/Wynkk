package com.soma.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.soma.model.SearchWynkkModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class CommonFunction {

    public static int whenInternetOff=0; 
    public static boolean checkviasplash = false;
    public static boolean isInternetConnected = false;
    public static int gridCount = 0;
    public static String sActivityName;
    public static String mUsername;
    public static int counter = 0;
    String languageToLoad;
    Context context;
    public static int sBunisnessNumber;
    public static double latitude;
    public static double longitude;
    public static boolean mSwipeFlag = false;
    public static MediaPlayer mp = new MediaPlayer();
    public static String download_title;
    static public ArrayList<String> itemsList = new ArrayList<String>();
    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    public static String filePath;

    //public static String host="http://14.102.54.190/wynkk/Webservices/";
    //public static String Image_Url="http://14.102.54.190/wynkk/app/webroot/img/photo/";
//CAAFWobZAaAAUBAAHPN89sViZCJx63x1AX0Q8BGakEOhagh9RcazbctiGHCCP3whAFldXmOiFYyWoCHBBztd4Lgpn2RSw4oMzCYy6WkJORSHTZCfvV2hzKHypagvpDfRT8yPWVz1QerB1vTirmoBXETCQNLongfr2AHDfQTMBPKB9PmWU3DNNeUN3ZCZB93Exw3CUSVIslswkZA1lWsAJ4W
//CAAFWobZAaAAUBAMwBRCZAcyCpHROqiY8jsC8Li59HgMGzBUl0blZAki7Fstv4VcxEPSyNDDYFiFBeJ89E0ufGO34iX4xaUjtLqsXXioANhE8S8v3P3hWO6GA3cZAJqLZBftOac3eP5wUE08AdfsYBP00x14Gk3NqVKGzNALddPTFr0EtH3I9LZBvJLtyQBbrZCYg3tr7qOhJWnxw0QMMOu6
    public static String host="http://wynkk.co/Webservices/";
    public static String Image_Url="http://wynkk.co/img/photo/";
   // public static String host="http://wynkk.net/Webservices/";
   // public static String Image_Url="http://wynkk.net/app/webroot/img/photo/";

    public  static ArrayList<com.soma.model.SignupModel.Data> sUserInfo=new ArrayList<com.soma.model.SignupModel.Data>();
    public  static ArrayList<SearchWynkkModel.Data> sSearchYammp=new ArrayList<SearchWynkkModel.Data>();

    public static ArrayList<String> sNearByPlace=new ArrayList<String>();
    public static ArrayList<Double> sNearLatitude=new ArrayList<Double>();
    public static ArrayList<Double> sNearLongitude=new ArrayList<Double>();
    public static final int REQUEST_CODE = 1000;
    public static int check=0;
    public CommonFunction(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public CommonFunction() {
        super();
    }

    public interface FragmentCallback 
    {
        public void onTaskDone();
    }

    public static boolean isEmailValid(String email)// function for email validation
    {
        final String regExpn =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        CharSequence inputStr = email;
        //pattern = Pattern.compile(EMAIL_PATTERN);
        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public boolean isFieldBlank(String field) {
        if (field.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String GetServiceUrl(String serverpath) {

        String mUrl = serverpath;
        return mUrl;
    }

    public InputStream connectionEstablished(String mUrl) {
        DefaultHttpClient sHttpclient;
        HttpPost sHttppost;
        HttpResponse sResponse;
        InputStream mInputStreamis = null;
        try {
            sHttppost = new HttpPost(mUrl);
            sHttppost.addHeader("Content-Type", "application/json");
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 16000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            int timeoutSocket = 16500;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            sHttpclient = new DefaultHttpClient(httpParameters);
            sResponse = sHttpclient.execute(sHttppost);
            HttpEntity sEntity = sResponse.getEntity();
            mInputStreamis = sEntity.getContent();
        } catch (ConnectTimeoutException e) {
            Log.e("Connection time Out", "Error");
        } catch (Exception e) {
            Log.e("Exception here", "Error" + e);
        }
        return mInputStreamis;
    }

    public String converResponseToString(InputStream InputStream) {
        String mResult = "";
        StringBuilder mStringBuilder;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    InputStream, "UTF8"), 8);
            mStringBuilder = new StringBuilder();
            mStringBuilder.append(reader.readLine() + "\n");
            String line = "0";
            while ((line = reader.readLine()) != null) {
                mStringBuilder.append(line + "\n");
            }
            InputStream.close();
            mResult = mStringBuilder.toString();
            Log.d("Event result Array==", mResult);
            return mResult;
        } catch (Exception e) {
            return mResult;
        }
    }

    public boolean isSharedPreferencesEmpty() {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String loggedinemail = prefs.getString("email", "");
        if (loggedinemail.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void ErrorMessageToPrint(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static final boolean isInternetOn(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        Log.d("Inside ","isInternetOn()");
        if(connect != null)
        {
            NetworkInfo result = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo result2 = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (result != null && result.isConnectedOrConnecting())
            {
                Log.d("Inside isInternetOn(1)",result.toString());
                Log.d("Inside isInternetOn(1)",String.valueOf(result.isConnectedOrConnecting()));
                return true;
            }
            if (result2 != null && result2.isConnectedOrConnecting()){
                Log.d("Inside isInternetOn(2)",result2.toString());
                Log.d("Inside isInternetOn(2)",String.valueOf(result2.isConnectedOrConnecting()));
                return true;
                } else return false;

        }
        else
            return false;

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public static String storeImage(Bitmap imageData) {
        // get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory()
                .toString() + "/YAMPP/";
        File sdIconStorageDir = new File(iconsStoragePath);
        // String filename=mNameTxt.getText().toString();
        // create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
      //  int days = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        try {
            filePath = sdIconStorageDir.toString() + "_" + year + month + hour
                    + minute + second + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(
                    fileOutputStream);
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return null;
        }

        return filePath;
    }

}
