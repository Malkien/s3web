package com.web.app.utils;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;
import software.amazon.awssdk.services.rds.model.RdsException;

import java.io.InputStream;
import java.sql.*;

public class RDSUtils {


    private String url;
    private static RDSUtils instance;
    private static final String dbInstanceIdentifier = "my-ddbb-practice";
    private static final Region REGION = Region.EU_WEST_3;
    private static final String HOSTNAME = "my-ddbb-practice.cger0yceks3s.eu-west-3.rds.amazonaws.com";
    private static final int PORT = 3306;
    private static final String USERNAME = "admin";
    private RdsClient rdsClient = RdsClient.builder()
            .region(REGION)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    private String getAuthToken() {

        RdsUtilities utilities = rdsClient.utilities();
        try {
            GenerateAuthenticationTokenRequest tokenRequest = GenerateAuthenticationTokenRequest.builder()
                    .credentialsProvider(ProfileCredentialsProvider.create())
                    .username(USERNAME)
                    .port(PORT)
                    .hostname(dbInstanceIdentifier)
                    .build();

            return utilities.generateAuthenticationToken(tokenRequest);

        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
        return "";
    }
    //////////////////////////////////////////////


    private RDSUtils() {
        url = "jdbc:mysql://"+HOSTNAME+">:"+PORT+"/mydb?useSSL=false";
    }


    private static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new RDSUtils();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return DriverManager.getConnection(instance.url, USERNAME,instance.getAuthToken());

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.getStackTrace();
        }
        return null;
    }
    public static void insertData(String key, InputStream file){
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            connection = getConnection();
            String sql = "INSERT INTO images(key, metadada) VALUES (?,?)";
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, key);
            prepareStatement.setBlob(2, file);
            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                prepareStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
