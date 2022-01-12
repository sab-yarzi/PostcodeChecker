package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.nio.BufferOverflowException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {


        if (!postcodeValidation(args[0])) {
            throw new Exception("Postcode is not valid");
        }
        else{
            System.out.println("Postcode is valid");
        }
        System.out.println(countryAndRegion(args[0]));
        System.out.println("Nearest postcodes with countries and regions: ");

      nearestPostcodes(args[0]).forEach((k,v)-> System.out.println(k + " " + v));


    }

    public static boolean postcodeValidation(String postcode) throws Exception {
        URL postcodeURl = new URL("http://postcodes.io/postcodes/" + postcode + "/validate");
        return getJsonObject(postcodeURl).get("result").getAsString().equals("true");

    }
    public static String countryAndRegion(String postcode) throws Exception {
        URL postcodeURL = new URL("http://postcodes.io/postcodes/" + postcode);
        JsonObject returnedObject = getJsonObject(postcodeURL);
        return "Country: " + returnedObject.getAsJsonObject("result").get("country").getAsString()
                + " Region: " + returnedObject.getAsJsonObject("result").get("region").getAsString() ;
    }

    public static HashMap nearestPostcodes(String postcode) throws Exception{
        URL postcodeURL = new URL("http://postcodes.io/postcodes/" + postcode + "/nearest" );
        JsonObject returnedObject = getJsonObject(postcodeURL);
        JsonArray returnedArray = getJsonObject(postcodeURL).getAsJsonArray("result");
        HashMap<String,String> postcodesHash = new HashMap<>();
        for (int i = 1; i < returnedArray.size() ; i++) {
            postcodesHash.put(returnedArray.get(i).getAsJsonObject().get("postcode").getAsString(),countryAndRegion(returnedArray.get(i).getAsJsonObject().get("postcode").getAsString()));
        }
        return postcodesHash;
    }

    public static JsonObject getJsonObject(URL request) throws Exception {
        HttpURLConnection requestConnect = (HttpURLConnection) request.openConnection();
        requestConnect.connect();
        JsonObject root = JsonParser.parseReader(new InputStreamReader((InputStream) requestConnect.getContent())).getAsJsonObject();
        requestConnect.disconnect();
        return root;
    }

}
