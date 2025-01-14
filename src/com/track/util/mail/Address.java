package com.track.util.mail;

/**
 * Represents email address. This class is created to avoid confusion and conflict in using email address and display name if email address is string
 *
 * @author Ravindra Gullapalli
 */
public class Address implements java.io.Serializable{
	/**
	 * Email address
	 */
	private java.lang.String emailAddress = null;
	/**
	 * Display name for this email address
	 */
	private java.lang.String displayName = null;
	/**
	 * Constructor
         * @param eMailAddress Email address
         * @throws Exception  
	 */
	public Address(java.lang.String eMailAddress) throws Exception {
		this(eMailAddress, null);
	}
	/**
	 * Constructor to set display name along with email address
	 * @param eMailAddress
	 * @param displayName
	 * @throws Exception
	 */
	public Address(java.lang.String eMailAddress, java.lang.String displayName) throws Exception {
		this.emailAddress = eMailAddress;
		if ( ! isValid()) {
			throw new Exception("Invalid email : " + eMailAddress);
		}
		if (displayName != null) {
			this.displayName = displayName;
		} else {
			this.displayName = "[Display Name]";//TODO : Set display name
		}
	}
	/**
	 * Provides the email address
	 * @return Email address
	 */
	public java.lang.String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * Provides displayName associated with the e-mail address
	 * @return Display name associated with the e-mail address
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}
	/**
	 * Checks whether the given email address is valid or not.
	 * @return <code>true</code> if the given email address is valid else it will return <code>false</code>
	 */
	private java.lang.Boolean isValid() {
		int lastDotIndex = 3;
		int lastAtIndex = 5;
		if (emailAddress != null) {
			emailAddress = emailAddress.trim();
		} else {
			return java.lang.Boolean.FALSE;
		}
		if (!emailAddress.contains("@") || // Atleast one @ symbol should be there
				emailAddress.indexOf("@") == 0 || // Should not start with @
				emailAddress.indexOf(".") == 0 || // Should not start with .
                !emailAddress.contains(".") || // Atleast one . symbol should be there
                emailAddress.contains(" ") || // No space should be there
				emailAddress.lastIndexOf(".") < emailAddress.indexOf("@") || //	Atleast one . symbol should be there after @
				emailAddress.lastIndexOf("@") != emailAddress.indexOf("@") || //	Only one @ symbol should be present
				emailAddress.length() - emailAddress.lastIndexOf(".") < lastDotIndex || //	After last . atleast 2 characters should be present
				emailAddress.length() - emailAddress.lastIndexOf("@") < lastAtIndex) { //	After last @ atleast 4 characters should be present
			return java.lang.Boolean.FALSE;
		}
		return java.lang.Boolean.TRUE;
	}
	/**
	 * Provides the address information as string which includes the email address and the display name
	 * @return Address information
	 */
	public java.lang.String toString(){
		return "eMail : " + emailAddress + ", Display name : " + displayName;
	}
}
