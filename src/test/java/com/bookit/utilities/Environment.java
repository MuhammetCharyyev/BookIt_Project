package com.bookit.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Environment {

    public static final String URL;
    public static final String BASE_URL;
    public static final String DB_USERNAME;
    public static final String DB_PASSWORD;
    public static final String DB_URL;
    public static final String TEACHER_EMAIL;
    public static final String TEACHER_PASSWORD;
    public static final String MEMBER_EMAIL;
    public static final String MEMBER_PASSWORD;
    public static final String LEADER_EMAIL;
    public static final String LEADER_PASSWORD;
    //constant Enums to call as they will replace data from 'environments' package with all
    // 'qa' environments which concern to properities


    static{
        Properties properties = null;
        String environment = System.getProperty("environment") != null ?
                environment = System.getProperty("environment") :
                ConfigurationReader.get("environment");
        //it is ternary to state 'if' condition to get our config.props data
        //if there is no entry in Terminal -> System.getProperty("environment") != null
        //then get it from 'properties' ->ConfigurationReader.get("environment")
        //if !null then read ->environment = System.getProperty("environment")

        try {

            String path = System.getProperty("user.dir") +
                    "/src/test/resources/Environments/" + environment + ".properties";
            //string for assign a path/ make it dynamic with indication 'environment'
            //string path as above, to read exact value from config.prop

            FileInputStream input = new FileInputStream(path);
            //input our 'path'
            properties = new Properties();
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL = properties.getProperty("url");
        BASE_URL = properties.getProperty("base_url");
        DB_USERNAME = properties.getProperty("dbUsername");
        DB_PASSWORD = properties.getProperty("dbPassword");
        DB_URL = properties.getProperty("dbUrl");
        TEACHER_EMAIL = properties.getProperty("teacher_email");
        TEACHER_PASSWORD = properties.getProperty("teacher_password");
        MEMBER_EMAIL = properties.getProperty("team_member_email");
        MEMBER_PASSWORD = properties.getProperty("team_member_password");
        LEADER_EMAIL = properties.getProperty("team_leader_email");
        LEADER_PASSWORD = properties.getProperty("team_leader_password");
        //assign our created above constant Enum to properties data






    }


}
