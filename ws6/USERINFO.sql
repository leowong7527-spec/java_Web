-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ITP4511_DB;

-- Switch to that database
USE ITP4511_DB;

-- Drop the table if it already exists (optional, for clean re-imports)
DROP TABLE IF EXISTS USERINFO;

-- Create the USERINFO table
CREATE TABLE USERINFO (
  id VARCHAR(5) PRIMARY KEY,
  username VARCHAR(25),
  password VARCHAR(25)
);

-- Insert test users
INSERT INTO USERINFO (id, username, password) VALUES ('1', 'abc', '123');
INSERT INTO USERINFO (id, username, password) VALUES ('2', 'xyz', '123');
