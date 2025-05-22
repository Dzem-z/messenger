CREATE DATABASE springdb;

CREATE USER spring@'%' IDENTIFIED BY "password";

GRANT ALL ON springdb.* TO spring@'%';