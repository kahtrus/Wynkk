package com.soma.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class SignupModel 
{

	@JsonProperty("status")
	private String _status;
	

	@JsonProperty("message")
	private String _message;
	

	@JsonProperty("data")
	private ArrayList<Data> sUserData=new ArrayList<Data>();
	
	
	
	
	public String get_status() {
		return _status;
	}




	public void set_status(String _status) {
		this._status = _status;
	}




	public String get_message() {
		return _message;
	}




	public void set_message(String _message) {
		this._message = _message;
	}




	public ArrayList<Data> getsUserData() {
		return sUserData;
	}




	public void setsUserData(ArrayList<Data> sUserData) {
		this.sUserData = sUserData;
	}




	public static class Data
	{
	    @JsonProperty("id")
	    private String _Id;
	    
	    @JsonProperty("username")
        private String _UserName;
	    
	    @JsonProperty("photo")
        private String _Photo;
	    
	    @JsonProperty("latitude")
        private String _Latitude;
	    
	    @JsonProperty("longitude")
        private String _Longitude;
        
	    @JsonProperty("home_location")
        private String _Home_Location;
        

        public String get_Latitude() {
            return _Latitude;
        }

        public void set_Latitude(String _Latitude) {
            this._Latitude = _Latitude;
        }

        public String get_Longitude() {
            return _Longitude;
        }

        public void set_Longitude(String _Longitude) {
            this._Longitude = _Longitude;
        }

        public String get_Home_Location() {
            return _Home_Location;
        }

        public void set_Home_Location(String _Home_Location) {
            this._Home_Location = _Home_Location;
        }

        public String get_Id() {
            return _Id;
        }

        public void set_Id(String _Id) {
            this._Id = _Id;
        }

        public String get_UserName() {
            return _UserName;
        }

        public void set_UserName(String _UserName) {
            this._UserName = _UserName;
        }

        public String get_Photo() {
            return _Photo;
        }

        public void set_Photo(String _Photo) {
            this._Photo = _Photo;
        }
	    
	    
	    
	}
	
}
