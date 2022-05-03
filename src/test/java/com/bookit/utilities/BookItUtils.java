package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

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
}
