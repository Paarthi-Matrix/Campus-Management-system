package com.ideas2it.cms.helper;


import com.ideas2it.cms.customexception.DatabaseConnectionException;
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * <p>
 *
 * Responsible for getting the `connection` from the database.
 * Developed through Singleton design pattern so that all parts of the application can share the same database connection, 
 * -preventing the unnecessary creation of multiple connections. 
 *
 * </p>
 * 
 */

public class DatabaseConnectionManager {

    private static String url;
    private static String userName;
    private static String password = "";
    private Connection connection;
    private static DatabaseConnectionManager instance;


    /**
     * <p>
     *
     * Static block responsible for reading the `.env` file to fetch the 
     * userName, password and url.
     * Exception might occur if the `.env` file is not read properly.
     *
     * </p>
     *
     * @throws DatabaseConnectionException
     *         A custom class RunTimeException that occurs when `.env` file not found. 
     *
     */

    static {
        
        try {
           BufferedReader reader = new BufferedReader(new FileReader(".env"));
           String line;
  
           while((line = reader.readLine()) != null) {
 
               String[] pairs = line.split("=");
               if(pairs.length >= 1) {

                   String key = pairs[0].trim();

                   if(pairs.length == 1 && key.equals("DB_PASSWORD")) {
                       break;
                   }

                   String value = pairs[1].trim();
 
                   switch (key) {
                   
                       case "DB_URL":
                             url = value;
                             break;
 
                       case "DB_USERNAME":
                             userName = value;
                             break;

                       case "DB_PASSWORD":                          
                             password = value;
                             break;
                       
                   }
               }
           }
           reader.close(); 
     
           Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnectionManager::closeConnection));

        } catch(IOException e) {
              String errorMesage = "Error occured while reading the `.env` file. File not found!";          
              throw new DatabaseConnectionException(errorMesage , e);
        }
    }


    /**
     * <p>
     *
     * Responsible for creating connection to the database.
     *
     * </p> 
     *
     * @throws DatabaseConnectionException
     *         A custom class RuntimeException, which occurs if the driver not found
     *         -or if database credentials were invalid.
     * </p>
     *
     */

    private DatabaseConnectionManager() {
 
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

        } catch (Exception e) {
            
            String errorMesage = "Error occured while connecting to database. Check database credentials and database connector!";
            throw new DatabaseConnectionException(errorMesage , e);
 
        }

    }

     /**
     * <p>
     *
     * Gets the instance of `DatabaseConnectionManager`.
     * If the instance is null or the `connection` is null it creates a new `DatabaseConnectionManager` instance.
     * Else returns the same instance of `DatabaseConnectionManager`.
     *  
     * </p>
     *
     */

    public static DatabaseConnectionManager getInstance() {

        if (instance == null || !isValid(instance.getConnection())) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }
    
    /**
     * <p>
     *
     * Responsible for geting the connection instance.
     *
     * </p>
     *
     */

    public Connection getConnection()  {
            
         return connection;

    }


    /**
     * <p>
     *
     * Responsible for checking if the `connection` is null or closed.
     *
     * </p>
     *
     * @param connection
     *        A connection reference variable to check if it is `null` or closed.
     *
     * @return boolean
     *         If `connection` is `null` or closed returns `true`.
     *         Else returns `false`.
     *
     */

    private static boolean isValid(Connection connection) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }



}