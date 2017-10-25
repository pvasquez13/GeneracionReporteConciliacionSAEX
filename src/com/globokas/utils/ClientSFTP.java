
package com.globokas.utils;

import com.jscape.inet.sftp.Sftp;
import com.jscape.inet.sftp.SftpException;
import com.jscape.inet.sftp.events.SftpAdapter;
import com.jscape.inet.sftp.events.SftpConnectedEvent;
import com.jscape.inet.sftp.events.SftpDisconnectedEvent;
import com.jscape.inet.ssh.util.SshParameters;
import java.io.File;

/**
 *
 * @author pvasquez
 */
public class ClientSFTP extends SftpAdapter {

    private Sftp sftp;

    public ClientSFTP(String hostname, String username, String password) {
        // initialize local SFTP instance with hostname, username and password.
        sftp = new Sftp(new SshParameters(hostname,
                username,
                password));

        // register the listener so this instance can recieve events.
        sftp.addSftpListener(this);
    }

    // open connection to the remote server.
    public void openConnection() throws SftpException {
        sftp.connect();
    }

    // disconnect from the remote server.
    public void closeConnection() {
        sftp.disconnect();
    }

    // set binary transfer mode
    public void setBinaryMode() {
        this.setAutoDetectFileMode(false);
        sftp.setBinary();
    }

    // set ASCII transfer mode
    public void setAsciiMode() {
        this.setAutoDetectFileMode(false);
        sftp.setAscii();
    }

    // automatically detect the transfer mode
    public void setAutoDetectFileMode(boolean value) {
        sftp.setAuto(value);
    }

	// provide a filename expression to upload
    // e.g. :
    // *.*       upload all files from in the current local directory.
    // .*\\.gif  upload all files with the GIF image extension.
    public void uploadFile(String expression) throws SftpException {
        sftp.mupload(expression);
    }

	// call this method if you want to append data to an existing file
    // e.g.
    // File src = new File("c:/tmp/logs.txt");
    // String dest = "all-logs.txt";
    // sftp.upload(src,dest,true);
    public void uploadFile(String source, String dest, boolean append) throws SftpException {
        sftp.upload(new File(source), dest, true);
    }

	// upload a directory
    // e.g. :
    // uploadDir(new File("e:/somedir"));
    // This call will upload "somedir" to the remote server
    public void uploadDir(File file) throws SftpException {
        sftp.uploadDir(file);
    }

	// upload from memory
    // e.g.
    // String someData = "data";
    // upload(someData.getBytes(), "data.txt");
    public void upload(byte[] b, String file) throws SftpException {
        sftp.upload(b, file);
    }

    // set local directory to upload from
    public void setLocalDir(String dir) {
        sftp.setLocalDir(new File(dir));
    }

    // set remote directory to upload to
    public void setRemoteDir(String dir) throws SftpException {
        sftp.setDir(dir);
    }

    // this method is invoked when when client connects to the remote server.
    public void connected(SftpConnectedEvent evt) {
        System.out.println("Connected to host: " + evt.getHostname());
    }

    // this method is invoked when client disconnects from the remote server.
    public void disconnected(SftpDisconnectedEvent evt) {
        System.out.println("Disconnected from host: " + evt.getHostname());
    }

}
