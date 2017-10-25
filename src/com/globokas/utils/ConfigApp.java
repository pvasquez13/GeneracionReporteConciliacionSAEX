/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.globokas.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author pvasquez
 */
public class ConfigApp {
    
    private static final String PROPERTIES_FILENAME = "config_saex.properties";
    private static Properties config;

    static {
        try {
            config = new Properties();
            InputStream inputStream = ConfigApp.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
            config.load(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar el archivo " + PROPERTIES_FILENAME, e);
        }
    }

    public static String getValue(String key) {

        return config.getProperty(key);
    }

    public static Enumeration getPropertyNames() {

        return config.propertyNames();
    }
    
}
