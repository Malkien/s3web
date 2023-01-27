CREATE DATABASE IF NOT EXISTS images;
USE images;
CREATE TABLE IF NOT EXISTS images(
    key varchar(100) PRIMARY KEY, 
    metadata LONGBLOB
    );
