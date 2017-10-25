package com.globokas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author pvasquez
 */
public class OraConnection {

    private static final Logger logger = Logger.getLogger(OraConnection.class);

    public static Connection OracleConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            logger.trace("Couldn't find the driver!");
            logger.trace("Let'f print a stack trace, and exit.");
            cnfe.printStackTrace();
        }

//        logger.trace("Registered the driver ok, so let'f make a connection.");
        Connection c = null;

        try {

            String ip = ConfigApp.getValue("ORACLE_IP");
            String puerto = ConfigApp.getValue("ORACLE_PUERTO");
            String sid = ConfigApp.getValue("ORACLE_SID");
            String user = ConfigApp.getValue("ORACLE_USER");
            String psw = "usrGknSaexProd";
//            String psw = ConfigApp.getValue("ORACLE_PSW");

            //Producción
            String urll = "jdbc:oracle:thin:@" + ip + ":" + puerto + "/" + sid;
            c = DriverManager.getConnection(urll, user, psw);

            //Desarrollo
//			String urll = "jdbc:oracle:thin:@10.251.21.161:1521:gkndesa";
//			c = DriverManager.getConnection(urll, "eftglobp", "eftglobp");
        } catch (SQLException se) {
            logger.trace("Couldn't connect: print out a stack trace and exit.");
            se.printStackTrace();
        }

        if (c != null) {
//            logger.trace("We connected to the database! Oracle");
//            System.out.println("Conexión exitosa! Oracle");
        } else {
            logger.trace("We should never get here.");
        }
        return c;

    }

    public static void close(ResultSet rst, Statement stm, Connection cnn) {
        close(rst);
        close(stm);
        close(cnn);
    }

    public static void close(ResultSet rst) {
        try {
            if (rst != null) {
                rst.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void close(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void close(Connection cnn) {
        try {
            if (cnn != null) {
                cnn.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
