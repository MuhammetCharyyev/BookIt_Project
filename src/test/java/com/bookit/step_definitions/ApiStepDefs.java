package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookItUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {

    String token; //make it global

    Response response;//make it global

    String emailGlobal;//make it global

    int idToDelete;//make it global

    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

      token = BookItUtils.generateToken(email, password);
       //called from BookItUtils
        emailGlobal = email;//assign global variable to stated 'email'
    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
       response = given()
                .header("Authorization",token)
                .accept(ContentType.JSON)
                .when()
                .get(ConfigurationReader.get("base_url") + "/api/users/me");
        //check if toke is working

        response.prettyPrint();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        //get the status code from global response, which is stored from previous request
        //and verify if it matches with the status code from feature file
        System.out.println(response.statusCode());
        Assert.assertEquals(statusCode, response.statusCode());
    }


        @Then("the information about current user from api and database should match")
        public void the_information_about_current_user_from_api_and_database_should_match() {

        //get info from database: /which is our expected
            //with adding tag @db from Hooks class we will trigger our database info
            //and the Hooks will distroy after session

            String query = "select firstname, lastname, role from users\n" +
                            "where email = '"+emailGlobal+"'";
            //we made email global to allow us to change email in feature file, made it dynamic
            //launch the query to get info from DB

            Map<String, Object> dbMap = DBUtils.getRowMap(query);
            //create a Map and assign to DBUtils method to keep our query

            System.out.println(dbMap);

            //save database information into expected variables
            String expectedFirstName = (String) dbMap.get("firstname");
            String expectedLastName = (String) dbMap.get("lastname");
            String expectedRole= (String) dbMap.get("role");

            //get info from API which is our actual
            JsonPath jsonPath = response.jsonPath();

            String actualFirstName = jsonPath.getString("firstName");
            String actualLastName = jsonPath.getString("lastName");
            String actualRole = jsonPath.getString("role");

            //compare database vs api
            Assert.assertEquals(expectedFirstName,actualFirstName);
            Assert.assertEquals(expectedLastName,actualLastName);
            Assert.assertEquals(expectedRole,actualRole);

        }

    @Then("UI,API and Database user information must be match")
    public void uiAPIAndDatabaseUserInformationMustBeMatch() {
        //get info from database: /which is our expected
        //with adding tag @db from Hooks class we will trigger our database info
        //and the Hooks will distroy after session

        String query = "select firstname, lastname, role from users\n" +
                "where email = '"+emailGlobal+"'";
        //we made email global to allow us to change email in feature file, made it dynamic
        //launch the query to get info from DB

        Map<String, Object> dbMap = DBUtils.getRowMap(query);
        //create a Map and assign to DBUtils method to keep our query

        System.out.println(dbMap);

        //save database information into expected variables
        String expectedFirstName = (String) dbMap.get("firstname");
        String expectedLastName = (String) dbMap.get("lastname");
        String expectedRole= (String) dbMap.get("role");

        //get info from API which is our actual
        JsonPath jsonPath = response.jsonPath();

        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //compare database vs api
        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);

        //getting information from UI with SelfPage class we created
        //in Feature file it is stated 'user is on the my self page'
        SelfPage selfPage = new SelfPage();
        String actualFullNameUI = selfPage.name.getText();
        String actualRoleUI = selfPage.role.getText();

        System.out.println("actualFullNameUI = " + actualFullNameUI);
        System.out.println("actualRoleUI = " + actualRoleUI);

        //assertion UI vs DB -> DB is expected
        String expectedFullName = expectedFirstName+" "+expectedLastName;
        Assert.assertEquals(expectedFullName,actualFullNameUI);
        Assert.assertEquals(expectedRole,actualRoleUI);

        //assertion UI vs API -> API is expected here
        //create one api fullname variable
        String actualFullName = actualFirstName+" "+actualLastName;

        Assert.assertEquals(actualFullName,actualFullNameUI);
        Assert.assertEquals(actualRole,actualRoleUI);

    }

    @When("I send POST request {string} endpoint with following information")
    public void i_send_POST_request_endpoint_with_following_information
            (String path, Map<String, String> userInfo) { //we put our Scenario Outline in Map

        System.out.println(userInfo);

        //try to post something
        response = given()
                .accept(ContentType.JSON)
                .header("Authorization",token)
                .queryParams(userInfo)//place your Map inside the queryParams
                .log().all()
                .when()
                .post(ConfigurationReader.get("base_url") + path)
                //we put our params with 'post'
                .then().log().all().extract().response();

        idToDelete = response.path("entryId");
        //if we want to delete added entry by index number we need to assign to int

    }

    //delete previously added student
    @And("I delete previously added student")
    public void iDeletePreviouslyAddedStudent() {
        //we need id from previous post request

        given()
                .header("Authorization", token)
                .pathParam("id",idToDelete)//id number as stated above
                .when().delete(ConfigurationReader.get("base_url")+"/api/students/{id}")
                .then().statusCode(204);

    }

    @Given("I get environment information")
    public void iGetEnvironmentInformation() {

        System.out.println(Environment.URL);
        System.out.println(Environment.BASE_URL);
        System.out.println(Environment.TEACHER_EMAIL);
        System.out.println(Environment.TEACHER_PASSWORD);
        System.out.println(Environment.MEMBER_EMAIL);
        System.out.println(Environment.MEMBER_PASSWORD);

        System.out.println(System.getProperty("user.dir"));
    }


}
