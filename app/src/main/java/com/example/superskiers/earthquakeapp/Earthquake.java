package com.example.superskiers.earthquakeapp;

public class Earthquake {
    //Location of earthquake
    private String locationOfQuake;
    //Date of earthquake
    private String dateOfQuake;
    //Magnitude of earthquake
    private String magnitude;

    //Create a new Earthquake object
    //@param locQuake is the location of the earthquake
    //@param dateQuake is the date of the earthquake
    //@param magnitudeQuake is the magnitude of the earthquake
    public Earthquake(String magnitudeQuake, String locQuake, String dateQuake){
        locationOfQuake = locQuake;
        dateOfQuake = dateQuake;
        magnitude = magnitudeQuake;
    }
    //Get the magnitude of the quake
    public String getMagnitude() {
        return magnitude;
    }
    //Get the location of the quake
    public String getLocationOfQuake() {
        return locationOfQuake;
    }
    //Get the date of the quake
    public String getDateOfQuake() {
        return dateOfQuake;
    }


}
