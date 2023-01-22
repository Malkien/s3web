package com.web.app.utils;

import com.web.app.classes.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;
import software.amazon.awssdk.services.rds.model.RdsException;

import java.io.InputStream;
import java.sql.*;

@Service
public class RDSUtils {

/*
    private String url;
    private static RDSUtils instance;
    private static final String dbInstanceIdentifier = "my-ddbb-practice";
    private static final Region REGION_NAME = Region.EU_WEST_3;
    private static final String RDS_INSTANCE_HOSTNAME = "my-ddbb-practice.cger0yceks3s.eu-west-3.rds.amazonaws.com";
    private static final String DB_USER = "admin";
    private static final String DATABASE = "images";
    private static final int RDS_INSTANCE_PORT = 3306;
    private static final String JDBC_URL = "jdbc:mysql://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT + "/" + DATABASE;

 */
    private static String dbInstanceIdentifier;
    private static String REGION_NAME;
    private static String RDS_INSTANCE_HOSTNAME;
    private static String DB_USER;
    private static String DATABASE;
    private static int RDS_INSTANCE_PORT;
    private static String PASSWORD;
    private static String JDBC_URL;

    public RDSUtils(@Value("${RDS_INSTANCE_ID}") String id,
                    @Value("${RDS_REGION_NAME}") String region,
                    @Value("${RDS_INSTANCE_HOSTNAME}") String hostname,
                    @Value("${RDS_DB_USER}") String user,
                    @Value("${RDS_PASSWORD}") String pass,
                    @Value("${RDS_DATABASE}") String database,
                    @Value("${RDS_INSTANCE_PORT}") int port) {
        dbInstanceIdentifier = id;
        REGION_NAME = region;
        RDS_INSTANCE_HOSTNAME = hostname;
        DB_USER = user;
        DATABASE = database;
        RDS_INSTANCE_PORT = port;
        PASSWORD = pass;
        JDBC_URL = "jdbc:mysql://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT + "/" + DATABASE;
    }

    /**
     * This method returns a connection to the db instance authenticated using IAM Database Authentication
     * @return
     * @throws Exception
     */
    private static Connection getDBConnectionUsingIam() throws Exception {
        return DriverManager.getConnection(JDBC_URL,DB_USER,PASSWORD);//getAuthToken());
    }

    private static String getAuthToken() {
        RdsClient rdsClient = RdsClient.builder()
                .region(Region.of(REGION_NAME))
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
        finally {
            try {
                connection.close();
                prepareStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean delete(String key){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getDBConnectionUsingIam();
            String sql = "DELETE FROM `"+DATABASE+"` WHERE `key` = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, key);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
