/*
 * Copyright (c) 2017 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.celox.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class Database {

    private static final String MYSQL_JDBC_DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_JDBC_DB_URL = "jdbc:mysql://localhost:8889/";
    private static final String MYSQL_JDBC_USER = "root";
    private static final String MYSQL_JDBC_PASSWORD = "root";

    private static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DERBY_CONNECTION_URL_CREATE = "jdbc:derby:samplefx;create=true";

    public static Connection getConnection(boolean embeddedDataSource) {
        return embeddedDataSource ? getConnection() : getMySqlConnection();
    }

    private static Connection getMySqlConnection() {
        System.out.println("Connecting to database...");
        try {
            Class.forName(MYSQL_JDBC_DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Missing MySQL JDBC Driver.");
            e.printStackTrace();
            return null;
        }

        try {
            Connection connection = DriverManager
                    .getConnection(MYSQL_JDBC_DB_URL, MYSQL_JDBC_USER, MYSQL_JDBC_PASSWORD);
            System.out.println("Connected!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    private static Connection getConnection() {
        System.out.println("Connecting to database...");
        try {
            Class.forName(DERBY_EMBEDDED_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Missing Derby Embedded Driver.");
            e.printStackTrace();
            return null;
        }

        try {
            Connection connection = DriverManager
                    .getConnection(DERBY_CONNECTION_URL_CREATE);
            System.out.println("Connected!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

}