package com.bookit.step_definitions;

import com.bookit.utilities.BookItUtils;
import com.bookit.utilities.ConfigurationReader;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {

    String token; //make it global

    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

      token = BookItUtils.generateToken(email, password);
       //called from BookItUtils
    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
        given()
                .header("Authorization",token)
                .accept(ContentType.JSON)
                .when()
                .get(ConfigurationReader.get("base_url") + "/api/users/me");
        //check if toke is working
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

}
