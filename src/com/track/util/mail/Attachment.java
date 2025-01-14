package com.track.util.mail;
/**
 * Represents an email attachment
 * 
 * @author Ravindra Gullapalli
 */
public class Attachment implements java.io.Serializable{
	/**
	 * The file to be sent as an attachment
	 */
	private java.io.File attachment = null;
	/**
	 * The file name to be displayed at the recipient end.
	 */
	private java.lang.String displayName = null;
	/**
	 * Type of attachment that represents an attached file
	 */
	public static final java.lang.Integer ATTACHMENT = 1;
	/**
	 * Type of attachment that represents an in-line (embedded) attachment
	 */
	public static final java.lang.Integer INLINE = 2;
	/**
	 * Specifies the type of attachment
	 */
	private java.lang.Integer type = ATTACHMENT;
	/**
	 * Constructor which can be used when the attached file name can be displayed at recipient end
	 * @param attachment File to be attached
	 * @see #Attachment(java.io.File, java.lang.String)
	 * @see #Attachment(java.lang.String)
	 * @see #Attachment(java.lang.String, java.lang.String)
	 */
	public Attachment(java.io.File attachment) {
		this(attachment, attachment.getName());
	}
	/**
	 * Constructor which should be used when the display name of file should be different at recipient end
	 * @param attachment File to be attached
	 * @param displayName File name to be displayed at recipient end
	 * @see #Attachment(java.io.File)
	 * @see #Attachment(java.lang.String)
	 * @see #Attachment(java.lang.String, java.lang.String)
	 */
	public Attachment(java.io.File attachment, java.lang.String displayName) {
		setAttachment(attachment);
		setDisplayName(displayName);
	}
	/**
	 * Constructor
	 * @param attachedFileName The name of file which is attached
	 * @see #Attachment(java.io.File)
	 * @see #Attachment(java.lang.String, java.lang.String)
	 * @see #Attachment(java.io.File, java.lang.String)
	 */
	public Attachment(java.lang.String attachedFileName) {
		this(new java.io.File(attachedFileName), attachedFileName);
	}
	/**
	 * Constructor
	 * @param attachedFileName The name of file which is attached
	 * @param fileNameToShowInEmail The name that will show up in the email at the receiver
	 * @see #Attachment(java.lang.String)
	 * @see #Attachment(java.io.File)
	 * @see #Attachment(java.io.File, java.lang.String)
	 */
	public Attachment(String attachedFileName, String fileNameToShowInEmail) {
		this(new java.io.File(attachedFileName), fileNameToShowInEmail);
	}
	/**
	 * @return the attachment
	 */
	public java.io.File getAttachment() {
		return attachment;
	}
	/**
	 * @param attachment the attachment to set
	 */
	public final void setAttachment(java.io.File attachment) {
		this.attachment = attachment;
	}
	/**
	 * @return the displayName
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public final void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the type
	 */
	public java.lang.Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
}
