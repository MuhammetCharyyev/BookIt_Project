package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookItUtils {

    public static String generateToken (String email, String password) {

        Response response = given()
                .accept(ContentType.JSON)
                .queryParam("email", email)
                .and()
                .queryParam("password", password)
                .when()
                .get(ConfigurationReader.get("base_url") + "/sign");
        //first we get response with token here, because we need to use token further

        String token = "Bearer " + response.path("accessToken");
        //we assign to String to have token
        // and concatenate to Bearer to get correct way

        return token;

    }

    //hw
    //create method that accepts String like "teacher", "student-leader","student-member"
    // you will get one valid email and password for each from configuration.properties
    //and return valid token

    public static String getTokenByRole(String role) {
        String email = "";
        String password = "";

        switch (role) { //switch statement for each role
            case "teacher":
                email = Environment.TEACHER_EMAIL; //called Enum to call properties
                password = Environment.TEACHER_PASSWORD;
                break;

            case "student-member":
                email = Environment.MEMBER_EMAIL;
                password = Environment.MEMBER_PASSWORD;
                break;
            case "student-leader":
                email = Environment.LEADER_EMAIL;
                password = Environment.LEADER_PASSWORD;
                break;
            default:

                throw new RuntimeException("Invalid Role Entry :\n>> " + role +" <<");
        }

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        String path = given()
                .queryParams(credentials)
                .when().get( Environment.BASE_URL+"/sign").path("accessToken");

        return  "Bearer " + path;

    }
}
