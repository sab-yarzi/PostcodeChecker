package com.company;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Check for " + args[0]);
        if (!postcodeValidation(args[0])) {
            throw new Exception("Postcode is not valid");
        }
        else{
            System.out.println("Postcode is valid");
            String[] countryRegion;
            countryRegion = countryAndRegion(args[0]);
            System.out.println("Country: " + countryRegion[0] + " Region: " + countryRegion[1]);
            System.out.println("Nearest postcodes|Country|Region: ");
            nearestPostcodes(args[0]).forEach((k,v)-> System.out.println(k + "|" + v[0] + "|" + v[1]));
        }

    }

    public static boolean postcodeValidation(String postcode) throws Exception {
        if (postcode.length() > 7 || postcode.length() < 5){
            throw new ArithmeticException("Postcode is not valid");
        }
        URL postcodeURl = new URL("http://postcodes.io/postcodes/" + postcode + "/validate");
        return Objects.requireNonNull(getJsonObject(postcodeURl)).get("result").getAsString().equals("true");

    }
    public static String[] countryAndRegion(String postcode) throws Exception {
        URL postcodeURL = new URL("http://postcodes.io/postcodes/" + postcode);
        JsonObject returnedObject = getJsonObject(postcodeURL);

        String[] arr = new String[2];
        assert returnedObject != null; //if null error
        arr[0]= returnedObject.getAsJsonObject("result").get("country").getAsString();
        arr[1]=(returnedObject.getAsJsonObject("result").get("region").getAsString());
        return arr;
    }

    public static HashMap<String, String[]> nearestPostcodes(String postcode) throws Exception{
        URL postcodeURL = new URL("http://postcodes.io/postcodes/" + postcode + "/nearest" );
        //require not null -> error checking
        JsonArray returnedArray = Objects.requireNonNull(getJsonObject(postcodeURL)).getAsJsonArray("result");
        HashMap<String,String[]> postcodeArray = new HashMap<>();
        for (int i = 1; i < returnedArray.size() ; i++) {
            postcodeArray.put(returnedArray.get(i).getAsJsonObject().get("postcode").getAsString(),
                    countryAndRegion(returnedArray.get(i).getAsJsonObject().get("postcode").getAsString()));
        }
        return postcodeArray;
    }

    public static JsonObject getJsonObject(URL request) {
        try{
        HttpURLConnection requestConnect = (HttpURLConnection) request.openConnection();
        requestConnect.connect();
        if(requestConnect.getResponseCode() != 200){
            throw new IOException("API failure. HTTP Error Code: " + requestConnect.getResponseCode()); //error check for different htttp response
        }
        return JsonParser.parseReader(new InputStreamReader((InputStream) requestConnect.getContent())).getAsJsonObject();
        } catch (JsonIOException |  IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
