package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Objects;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @org.junit.jupiter.api.Test
    void nearestPostcodes() throws Exception { //make sure the original postcode isn't included

        HashMap<String, String[]> returnedMap = Main.nearestPostcodes("W87af");
        assertFalse(returnedMap.containsKey("W8 7AF"));


    }

    @org.junit.jupiter.api.Test
    void getJsonObject() {

    }
    @org.junit.jupiter.api.Test
    void postcodeValidation() { //make sure user validation is correct
        String testBigPostcode = "abdefghi";
        String testSmallPostcode="ds";

        ArithmeticException bigThrow = assertThrows(
                ArithmeticException.class,
                ()->Main.postcodeValidation(testBigPostcode),
                "Postcode is not valid"
        );


        ArithmeticException lowThrow = assertThrows(
                ArithmeticException.class,
                ()->Main.postcodeValidation(testSmallPostcode),
                "Postcode is not valid"
        );


    }
}