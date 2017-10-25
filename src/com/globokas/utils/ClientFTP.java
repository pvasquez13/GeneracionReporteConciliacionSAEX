package com.globokas.utils;

/**
 *
 * @author pvasquez
 */
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientFTP {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClientFTP.class);

    public static void upLoadFileToServer(String server, String user, String password, String path, String filename)
            throws UnsupportedEncodingException {
        FTPClient client = new FTPClient();
        FileInputStream fis;

        try {
            client.connect(server);
            boolean login = client.login(user, password);
            if (login) {
                fis = new FileInputStream(path + filename);
                client.storeFile(filename, fis);
                boolean logout = client.logout();
                if (logout) {
                    logger.trace("Logout from FTP server...");
                }
            } else {
                logger.trace("Login fail..." + server);
            }

        } catch (Exception e) {
            try {
                String asuntoCorreo = "Error en Proceso Intercambio de Archivos Vía FTP - "+ filename;
                String bodyCorreo = "ERROR[" + e.getMessage() + "]";
                Mail m = new Mail();
                m.enviaCorreoPorGrupo(asuntoCorreo, bodyCorreo, 11);
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, e);
            } catch (SQLException ex) {
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                client.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void upLoadFileToServer2(String server, String user, String password, String ruta, String filename)
            throws UnsupportedEncodingException {

        FTPClient client = new FTPClient();
        client.setDataTimeout(2222222); // 100 minutes
        FileInputStream fis;

        try {
            client.connect(server);
            boolean login = client.login(user, password);
            if (login) {
                fis = new FileInputStream(ruta + filename);
                client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.storeFile(filename, fis);
                boolean logout = client.logout();
                if (logout) {
                    logger.trace("Logout from FTP server...");
                }
            } else {
                logger.trace("Login fail..." + server);
            }
        } catch (Exception e) {
            try {
                Mail m = new Mail();
                String asuntoCorreo = "Error en Proceso Intercambio de Archivos Vía FTP - "+filename;
                String bodyCorreo = "ERROR[" + e.getMessage() + "]";
                m.enviaCorreoPorGrupo(asuntoCorreo, bodyCorreo, 11);
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, e);
            } catch (SQLException ex) {
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                client.disconnect();
            } catch (IOException ex) {
                Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
