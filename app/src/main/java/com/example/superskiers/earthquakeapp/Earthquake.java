package com.example.superskiers.earthquakeapp;

public class Earthquake {

    //Location of earthquake
    private String locationOfQuake;
    //Date of earthquake
    private long mTimeInMilliseconds;
    //Magnitude of earthquake
    private double mMagnitude;
    //Website url of the earthquake
    private String mUrl;




    //Create a new Earthquake object
    //@param locationOfQuake is the location of the earthquake
    //@param mTimeInMilliseconds is the date of the earthquake
    //@param mMagnitudeQuake is the magnitude of the earthquake
    //@param url is the website URL to find more details about the quake
    public Earthquake(double magnitude, String locQuake, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        locationOfQuake = locQuake;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;

    }

    //Get the magnitude of the quake
    public double getMagnitude() {
        return mMagnitude;
    }

    //Get the location of the quake
    public String getLocationOfQuake() {
        return locationOfQuake;
    }

    //Get the date of the quake
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    //Get the website url of the quake
    public String getUrl(){
        return mUrl;
    }
}