CREATE DATABASE springdb;

CREATE USER spring@172.17.0.1 IDENTIFIED BY "password";

GRANT ALL ON springdb.* TO spring@172.17.0.1;