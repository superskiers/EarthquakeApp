package com.example.superskiers.earthquakeapp;


import android.app.usage.UsageEvents;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public final class QueryUtils {


    //Tag for log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }
    public static List<Earthquake> fetchEarthquakeData(String requestUrl){

        //Create URL object
        URL url = createUrl(requestUrl);


        //Perform HTTP request to the URL and receive a JSON response and return response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        //Extract relevant fields from the JSON response and create a list of Earthquakes
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        //Return the list of {@link Earthquakes}
        return earthquakes;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        //If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //create the root JSONobject by calling the constructor and store in variable
            //with the name baseJsonResponse
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < earthquakeArray.length(); i++) {

                //Pull out the JSONObject@ the specified position 0 (i)
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                //Properties will list out many of the earthquake attributes
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                //Extract the value for the key called "URL"
                String url = properties.getString("url");

                //Extract the vale for the key called "mag"
                double magnitude = properties.getDouble("mag");

                //Extract the value for the key called "place"
                String location = properties.getString("place");

                //Extract the properties for time as milliseconds and then create a new object
                //to allow the info to be changed into something legible
                long time = properties.getLong("time");
                Date dateObject = new Date(time);
                //Use the SimpleDateFormat to change the dateObject into an actual date String
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);
                //Create a new earthquake object from the above 3 strings
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                //Add the new earthquake to earthquakes
                earthquakes.add(earthquake);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }



}