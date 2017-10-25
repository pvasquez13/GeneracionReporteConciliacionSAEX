/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author pvasquez
 */
public class SqlConection {

    // TODO Auto-generated method stub
    public Connection SQLServerConnection(int tipoBD) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No encuentra driver");
            cnfe.printStackTrace();
            System.exit(1);
        }
        Connection c = null;

        try {

//            System.out.println("coneccion: " + ConfigApp.getValue("ip_bd_produccion"));

            String ipDataBase = ConfigApp.getValue("SQL_IP");
            String databaseNombre = ConfigApp.getValue(tipoBD==2?"SQL_BD":"SQL_BD_GESTION");
            String userDatabase = ConfigApp.getValue("SQL_USER");
//            String passDatabase = ConfigApp.getValue("SQL_PASSWORD");
            String passDatabase = "PassControl$";

            c = DriverManager.getConnection("jdbc:sqlserver://" + ipDataBase + ";databaseName=" + databaseNombre + ";user=" + userDatabase + ";password=" + passDatabase + ";");

        } catch (SQLException se) {
            System.out.println("No conecto!");
            se.printStackTrace();
            System.exit(1);
        }

        if (c != null) {
//            System.out.println("Conecto!");
        } else {
            System.out.println("No conecto!");
        }
        return c;

    }

}
