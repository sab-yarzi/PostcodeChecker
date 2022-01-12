package com.company;

import com.google.gson.*;

import java.io.*;
import java.net.*;
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
        if (postcode.length() > 7 || postcode.length() < 5){
            throw new Exception("Postcode is not valid");
        }
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
        try {
            return JsonParser.parseReader(new InputStreamReader((InputStream) requestConnect.getContent())).getAsJsonObject();
        } catch (JsonIOException | IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
