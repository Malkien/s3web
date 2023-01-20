package com.web.app.classes;

import org.springframework.beans.factory.annotation.Value;

public class Database {
    @Value("${RDS_INSTANCE_ID}")
    private String dbInstanceIdentifier;
    @Value("${RDS_REGION_NAME}")
    private String REGION_NAME;
    @Value("${RDS_INSTANCE_HOSTNAME}")
    private String RDS_INSTANCE_HOSTNAME;
    @Value("${RDS_DB_USER}")
    private String DB_USER;
    @Value("${RDS_DATABASE}")
    private String DATABASE;
    @Value("${RDS_INSTANCE_PORT}")
    private int RDS_INSTANCE_PORT;
    @Value("${RDS_PASSWORD}")
    private String password;
    private String JDBC_URL;

    public Database() {
    }

    public String getDbInstanceIdentifier() {
        return dbInstanceIdentifier;
    }

    public String getREGION_NAME() {
        return REGION_NAME;
    }

    public String getRDS_INSTANCE_HOSTNAME() {
        return RDS_INSTANCE_HOSTNAME;
    }

    public String getDB_USER() {
        return DB_USER;
    }

    public String getDATABASE() {
        return DATABASE;
    }

    public int getRDS_INSTANCE_PORT() {
        return RDS_INSTANCE_PORT;
    }

    public String getJDBC_URL() {
        return JDBC_URL;
    }

    public String getPassword() {
        return password;
    }
}
