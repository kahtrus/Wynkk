package com.soma.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchWynkkModel
{
    @JsonProperty("status")
    private String _status;
    

    @JsonProperty("message")
    private String _message;
    

    @JsonProperty("data")
    private ArrayList<Data> sSearchData=new ArrayList<Data>();
    
    
    
    
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


    public ArrayList<Data> getsSearchData() {
        return sSearchData;
    }




    public void setsSearchData(ArrayList<Data> sSearchData) 
    {
        this.sSearchData = sSearchData;
    }


    public static class Data
    {
        @JsonProperty("comment_id")
        private String _comment_id;

        @JsonProperty("user_id")
        private String _user_id;
        
        @JsonProperty("comment")
        private String _comment;
        
        @JsonProperty("monkey_icon")
        private String _monkey_icon;
        
        @JsonProperty("latitude")
        private String _latitude;
        
        @JsonProperty("longitude")
        private String _longitude;
        
        @JsonProperty("location")
        private String _location;
        
        @JsonProperty("username")
        private String _Username;
        
        @JsonProperty("time")
        private String _Time;


        public String get_comment_id() {
            return _comment_id;
        }

        public void set_comment_id(String _comment_id) {
            this._comment_id = _comment_id;
        }

        public String get_Time() {
            return _Time;
        }

        public void set_Time(String _Time) {
            this._Time = _Time;
        }

        public String get_Username() {
            return _Username;
        }

        public void set_Username(String _Username) {
            this._Username = _Username;
        }

        public String get_user_id() {
            return _user_id;
        }

        public void set_user_id(String _user_id) {
            this._user_id = _user_id;
        }

        public String get_comment() {
            return _comment;
        }

        public void set_comment(String _comment) {
            this._comment = _comment;
        }

        public String get_monkey_icon() {
            return _monkey_icon;
        }

        public void set_monkey_icon(String _monkey_icon) {
            this._monkey_icon = _monkey_icon;
        }

        public String get_latitude() {
            return _latitude;
        }

        public void set_latitude(String _latitude) {
            this._latitude = _latitude;
        }

        public String get_longitude() {
            return _longitude;
        }

        public void set_longitude(String _longitude) {
            this._longitude = _longitude;
        }

        public String get_location() {
            return _location;
        }

        public void set_location(String _location) {
            this._location = _location;
        }

        
        
    }
    
    
    
}
