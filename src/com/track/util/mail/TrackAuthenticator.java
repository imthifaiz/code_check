package com.track.util.mail;

/**
 * The email authenticator
 *
 * @author Ravindra Gullapalli
 */
public class TrackAuthenticator extends javax.mail.Authenticator implements java.io.Serializable {

    /**
     * Password authentication
     */
    private transient javax.mail.PasswordAuthentication passwordAuthentication = null;

    /**
     * Constructor
     *
     * @see TrackAuthenticator(java.lang.String, java.lang.String)
     */
    public TrackAuthenticator() {
        this("[User name]", "[Password]");//TODO : Set user name and password
    }    

    /**
     * Constructor
     *
     * @param userName User name
     * @param password Password
     * @see #TrackAuthenticator()
     */
    public TrackAuthenticator(java.lang.String userName, java.lang.String password) {
        passwordAuthentication = new javax.mail.PasswordAuthentication(userName, password);
    }

    /**
     * Provides the password authentication object with the given user name and
     * password
     *
     * @return Password authentication object
     */
    @Override
    public javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return passwordAuthentication;
    }
}
