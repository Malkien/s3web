package com.web.app.utils;

import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
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
    private static final Region REGION_NAME = Region.EU_WEST_3;
    private static final String RDS_INSTANCE_HOSTNAME = "my-ddbb-practice.cger0yceks3s.eu-west-3.rds.amazonaws.com";
    private static final String DB_USER = "admin";
    private static final String DATABASE = "images";
    private static final int RDS_INSTANCE_PORT = 3306;
    private static final String JDBC_URL = "jdbc:mysql://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT;




    /**
     * This method returns a connection to the db instance authenticated using IAM Database Authentication
     * @return
     * @throws Exception
     */
    private static Connection getDBConnectionUsingIam() throws Exception {
        return DriverManager.getConnection(JDBC_URL, DB_USER, "admin1234");//getAuthToken());
    }

    private static String getAuthToken() {
        RdsClient rdsClient = RdsClient.builder()
                .region(REGION_NAME)
                .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
                .build();

        RdsUtilities utilities = rdsClient.utilities();
        try {
            GenerateAuthenticationTokenRequest tokenRequest = GenerateAuthenticationTokenRequest.builder()
                    .credentialsProvider(InstanceProfileCredentialsProvider.builder().build())
                    .username(DB_USER)
                    .port(RDS_INSTANCE_PORT)
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

/*
    private RDSUtils() {
        url = "jdbc:mysql://"+HOSTNAME+">:"+PORT+"/"+DATABASE+"?useSSL=false";
    }




    private static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new RDSUtils();
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            return DriverManager.getConnection(instance.url, USERNAME,instance.getAuthToken());

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.getStackTrace();
        }
        return null;
    }
    */
    public static void insertData(String key, InputStream file){
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            connection = getDBConnectionUsingIam();
            String sql = "INSERT INTO `"+DATABASE+"` VALUES (?,?)";
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, key);
            prepareStatement.setBlob(2, file);
            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*finally {
            try {
                connection.close();
                prepareStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*/
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
