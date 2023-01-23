package com.web.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.*;

@Service
public class RDSUtils {
    /**
     * Save the RDS instance ID
     */
    private static String dbInstanceIdentifier;
    /**
     * Save the region name
     */
    private static String REGION_NAME;
    /**
     * Save the RDS instance hostname
     */
    private static String RDS_INSTANCE_HOSTNAME;
    /**
     * Save the BBDD user
     */
    private static String DB_USER;
    /**
     * Save the DDBB name
     */
    private static String DATABASE;
    /**
     * Save the RDS instance port
     */
    private static int RDS_INSTANCE_PORT;
    /**
     * Save the DDBB password
     */
    private static String PASSWORD;
    /**
     * Save the jdbc url that connection need
     */
    private static String JDBC_URL;

    /**
     * load the variables values
     * @param id RDS isntance id
     * @param region RDS region
     * @param hostname RDS hostname
     * @param user DDBB user
     * @param pass DDBB password
     * @param database DDBB database name
     * @param port RDS port
     */
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
     * This method returns a connection to the ddbb
     * @return the connection
     * @throws Exception
     */
    private static Connection getDBConnectionUsingIam() throws Exception {
        return DriverManager.getConnection(JDBC_URL,DB_USER,PASSWORD);
    }

    /**
     * Make an insert in the DDBB
     * @param key the key in String
     * @param file the file in InputStream
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

    /**
     * Delete a DDBB row from the key
     * @param key the key
     * @return true if ok.
     */
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

    /**
     * Modify the DDBB row by param key
     * @param key the key
     * @param file the file
     * @return true ok false error
     */
    public static boolean modify(String key, InputStream file){
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            connection = getDBConnectionUsingIam();
            String sql = "UPDATE `"+DATABASE+"` SET `metadata` = `?` WHERE `key` = `?`";
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setBlob(1, file);
            prepareStatement.setString(2, key);
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
        return true;
    }
}
