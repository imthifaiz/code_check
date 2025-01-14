package com.track.util.mail;
/**
 * Represents a email object
 *
 * @author Ravindra Gullapalli
 */
public class SMTPClient implements java.io.Serializable {

    /**
     * Sender email address
     */
    private Address senderAddress = null;
    /**
     * Set of recipient email addresses
     */
    private java.util.List<Address> recipientAddresses = null;
    /**
     * Set of email addresses to whom CC should be made
     */
    private java.util.List<Address> ccAddresses = null;
    /**
     * Set of email addresses to whom BCC should be made
     */
    private java.util.List<Address> bccAddresses = null;
    /**
     * Mail subject
     */
    private java.lang.String subject = null;
    /**
     * Message text
     */
    private java.lang.String content = null;
    /**
     * Set of attachments to be sent with email
     */
    private Attachment[] attachments = null;
    /**
     * This contentType of mail will be sent as a plain text
     */
    public static final java.lang.Integer TEXT_TYPE = 1;
    /**
     * This contentType of mail will be sent with MIME content
     */
    public static final java.lang.Integer MIME_TYPE = 2;
    /**
     * Type of content which can be either <code>TEXT_TYPE</code> or
     * <code>MIME_TYPE</code>. By default, it is <code>MIME_TYPE</code> and to
     * change, use <code>setContentType</code>
     *
     * @see #TEXT_TYPE
     * @see #MIME_TYPE
     * @see #setContentType(java.lang.Integer)
     */
    private java.lang.Integer contentType = MIME_TYPE;
    /**
     * Specifies whether the mail sending process to be debugged OR not. If set
     * to <code>true</code>, it the commands in mail sending will be printed on
     * the console. If set to <code>false</code>, nothing will be printed. By
     * default it is <code>false</code>. But in some situations, it will be
     * necessary to monitor the situation. For example, while testing, we may
     * want to see the commands sent by the system to the mail sending server.
     */
    private java.lang.Boolean debuggingRequired = java.lang.Boolean.FALSE;
    /**
     * Specifies whether the mail to be sent OR not. If set to
     * <code>false</code>, the mail will not be sent. If set to
     * <code>true</code>, the mail will be sent. By default it is
     * <code>true</code>. But in some situations, it will be necessary to
     * restrict the mail sending. For example, while testing, we may think not
     * to send email unnecessary.
     */
    private java.lang.Boolean toSendMail = java.lang.Boolean.TRUE;

    /**
     * Constructor that can be used when only one recipient email address is
     * present
     *
     * @param senderAddress Sender email address
     * @param recipientAddress Recipient email address
     * @param subject Mail subject
     * @param content Mail content
     * @throws Exception
     */
    public SMTPClient(java.lang.String senderAddress, java.lang.String recipientAddress, java.lang.String subject, java.lang.String content) throws Exception {
        this(new Address(senderAddress), new Address(recipientAddress), subject, content);
    }

    /**
     * Constructor that can be used when only one recipient email address is
     * present
     *
     * @param senderAddress Sender email address
     * @param recipientAddress Recipient email address
     * @param subject Mail subject
     * @param content Mail content
     */
    public SMTPClient(Address senderAddress, Address recipientAddress, java.lang.String subject, java.lang.String content) {
        /*
         Used new ArrayList(Arrays.asList()) to avoid java.lang.UnsupportedOperationException
         */
        this(senderAddress, new java.util.ArrayList(java.util.Arrays.asList(new Address[]{recipientAddress})), subject, content);
    }

    /**
     * Constructor that can be used when multiple recipient email address
     * present in TO field
     *
     * @param senderAddress Sender email address
     * @param recipientAddresses Set of recipient email addresses
     * @param subject Mail subject
     * @param content Mail content
     */
    public SMTPClient(Address senderAddress, java.util.List<Address> recipientAddresses, java.lang.String subject, java.lang.String content) {
        this(senderAddress, recipientAddresses, null, null, subject, content);
    }

    /**
     * Constructor that can be used when CC OR BCC recipients present
     *
     * @param senderAddress Sender email address
     * @param recipientAddresses Set of recipient email addresses
     * @param ccAddresses Set of recipient email addresses to whom CC should be
     * marked
     * @param bccAddresses Set of recipient email addresses to whom BCC should
     * be marked
     * @param subject Mail subject
     * @param content Mail content
     */
    public SMTPClient(Address senderAddress, java.util.List<Address> recipientAddresses, Address[] ccAddresses, Address[] bccAddresses, java.lang.String subject, java.lang.String content) {
        // By default display name will be the sender's email
        this(senderAddress, recipientAddresses, ccAddresses, bccAddresses, subject, content, null);
    }

    /**
     * Constructor that should be used when attachments present
     *
     * @param senderAddress Sender email address
     * @param recipientAddresses Set of recipient email addresses
     * @param ccAddresses Set of recipient email addresses to whom CC should be
     * marked
     * @param bccAddresses Set of recipient email addresses to whom BCC should
     * be marked
     * @param subject Mail subject
     * @param content Mail content
     * @param attachments Set of files to be sent as an attachment
     */
    public SMTPClient(Address senderAddress, java.util.List<Address> recipientAddresses, Address[] ccAddresses, Address[] bccAddresses, java.lang.String subject, java.lang.String content, Attachment[] attachments) {
        /*
         Used new ArrayList(Arrays.asList()) to avoid java.lang.UnsupportedOperationException
         */
        this(senderAddress, recipientAddresses, new java.util.ArrayList(java.util.Arrays.asList(ccAddresses == null ? new Address[]{} : ccAddresses)), new java.util.ArrayList(java.util.Arrays.asList(bccAddresses == null ? new Address[]{} : bccAddresses)), subject, content, attachments);
    }

    /**
     *
     * @param senderAddress
     * @param recipientAddresses
     * @param ccAddresses
     * @param bccAddresses
     * @param subject
     * @param content
     * @param attachments
     */
    public SMTPClient(Address senderAddress, java.util.List<Address> recipientAddresses, java.util.List<Address> ccAddresses, java.util.List<Address> bccAddresses, java.lang.String subject,
            java.lang.String content, Attachment[] attachments) {
        setSenderAddress(senderAddress);
        setRecipientAddresses(recipientAddresses);
        setCcAddresses(ccAddresses);
        setBccAddresses(bccAddresses);
        setSubject(subject);
        setContent(content);
        setAttachments(attachments);
    }

    /**
     * Sets the sender email address
     *
     * @param senderAddress The sender email address
     */
    public final void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * Provides the sender address
     *
     * @return Sender address
     */
    public Address getSenderAddress() {
        return senderAddress;
    }

    /**
     * Sets the recipient email addresses to whom the mail to be sent
     *
     * @param recipientAddresses List of email addresses of recipients to whom
     * the mail should be sent
     */
    public final void setRecipientAddresses(java.util.List<Address> recipientAddresses) {
        if (recipientAddresses != null) {
            this.recipientAddresses = recipientAddresses;
        }
    }

    /**
     * Provides list of recipient addresses
     *
     * @return List of recipient addresses
     */
    public java.util.List<Address> getRecipientAddresses() {
        return recipientAddresses;
    }

    /**
     * Sets the list of CC emails
     *
     * @param ccAddresses The list of emails to whome a CC to be marked
     */
    public final void setCcAddresses(java.util.List<Address> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /**
     * Provides list of CC emails
     *
     * @return CC email list
     */
    public java.util.List<Address> getCcAddresses() {
        return ccAddresses;
    }

    /**
     * Sets the list of BCC emails
     *
     * @param bccAddresses The list of emails to whome a BCC to be marked
     */
    public final void setBccAddresses(java.util.List<Address> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /**
     * Provides list of BCC emails
     *
     * @return BCC emails
     */
    public java.util.List<Address> getBccAddresses() {
        return bccAddresses;
    }

    /**
     * Provides the subject of mail content
     *
     * @return The subject of mail content
     */
    public java.lang.String getSubject() {
        return subject;
    }

    /**
     * Sets the subject to be set in mail content
     *
     * @param subject the subject to set
     */
    public final void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    /**
     * Provides the content of the mail
     *
     * @return The body of mail
     */
    public java.lang.String getContent() {
        return content;
    }

    /**
     * Sets the text to be sent in the mail
     *
     * @param content Mail body OR mail text OR mail content
     */
    public final void setContent(java.lang.String content) {
        this.content = content;
    }

    /**
     * Provides the list of attachments attached to this mail
     *
     * @return List of attachments
     */
    public Attachment[] getAttachments() {
        return attachments;
    }

    /**
     * Sets the list of attachments to be attached to this mail
     *
     * @param attachments List of attachments to be attached
     */
    public final void setAttachments(Attachment[] attachments) {
        if (attachments != null) {
            this.attachments = attachments.clone();
        }
    }

    /**
     * Provides the mail contentType that specifies whether it is a normal text
     * mail OR a mail having MIME content.
     *
     * @return The mail contentType
     * @see #TEXT_TYPE
     * @see #MIME_TYPE
     */
    public java.lang.Integer getContentType() {
        return contentType;
    }

    /**
     * Sets the type of content which will be sent in the mail. It can be a
     * normal text OR MIME content.
     *
     * @param contentType The mail content type
     * @see #TEXT_TYPE
     * @see #MIME_TYPE
     */
    public void setContentType(java.lang.Integer contentType) {
        this.contentType = contentType;
    }

    /**
     * Specifies whether the mail sending process commands will be monitored OR
     * not
     *
     * @return <code>true</code> if the mail sending process set to monitor and
     * <code>false</code> otherwise
     */
    public java.lang.Boolean isDebuggingRequired() {
        return debuggingRequired;
    }

    /**
     * Sets whether the mail sending process to be monitored OR not
     *
     * @param debuggingRequired Should be <code>true</code> if the mail sending
     * process to be monitored and <code>false</code> otherwises
     */
    public void setDebuggingRequired(java.lang.Boolean debuggingRequired) {
        this.debuggingRequired = debuggingRequired;
    }

    /**
     * Specifies whether the mail will be sent OR not
     *
     * @return <code>true</code> if the mail to be sent and <code>false</code>
     * otherwise
     */
    public java.lang.Boolean isToSendMail() {
        return toSendMail;
    }

    /**
     * Sets whether the mail to be sent OR not
     *
     * @param toSendMail Should be <code>true</code> if the mail to be sent and
     * <code>false</code> otherwise
     */
    public void setToSendMail(java.lang.Boolean toSendMail) {
        this.toSendMail = toSendMail;
    }

    /**
     * Sends email
     *
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.mail.MessagingException
     */
    public void send() throws javax.mail.MessagingException, java.io.UnsupportedEncodingException {// 10-Nov-2012 : Ravindra : Removed javax.mail.internet.AddressException,
        javax.mail.Message message = new javax.mail.internet.MimeMessage(getMailSession());
        message.addFrom(new javax.mail.internet.InternetAddress[]{new javax.mail.internet.InternetAddress(getSenderAddress().getEmailAddress(), getSenderAddress().getDisplayName())});
        // Set recipient addresses
        for (int recipientIndex = 0; recipientIndex < getRecipientAddresses().size(); recipientIndex++) {
            message.addRecipient(javax.mail.internet.MimeMessage.RecipientType.TO, new javax.mail.internet.InternetAddress(getRecipientAddresses().get(recipientIndex).getEmailAddress().trim()
                    .toLowerCase()));
        }
        // Set CC addresses (if any)
        for (int ccIndex = 0; getCcAddresses() != null && ccIndex < getCcAddresses().size(); ccIndex++) {
            message.addRecipient(javax.mail.internet.MimeMessage.RecipientType.CC, new javax.mail.internet.InternetAddress(getCcAddresses().get(ccIndex).getEmailAddress().trim().toLowerCase()));
        }
        // Set BCC address (if any)
        for (int bccIndex = 0; getBccAddresses() != null && bccIndex < getBccAddresses().size(); bccIndex++) {
            message.addRecipient(javax.mail.internet.MimeMessage.RecipientType.BCC, new javax.mail.internet.InternetAddress(getBccAddresses().get(bccIndex).getEmailAddress().trim().toLowerCase()));
        }
        ((javax.mail.internet.MimeMessage) message).setSubject(getSubject(), "UTF-8");
        if (getAttachments() != null) {
            javax.mail.Multipart multiPartMessage = new javax.mail.internet.MimeMultipart();
            // Add body text
            javax.mail.internet.MimeBodyPart textBodyPart = new javax.mail.internet.MimeBodyPart();
            textBodyPart.setContent(getContent(), getContentType().equals(MIME_TYPE) ? "text/html; charset=\"" + "UTF-8" + "\"" : "text/plain");
            multiPartMessage.addBodyPart(textBodyPart);
            // Add attachments
            for (int attachmentIndex = 0; attachmentIndex < getAttachments().length; attachmentIndex++) {
                javax.mail.BodyPart attachmentBodyPart = new javax.mail.internet.MimeBodyPart();
                javax.activation.DataSource fileDataSource = new javax.activation.FileDataSource(getAttachments()[attachmentIndex].getAttachment().getAbsolutePath());
                attachmentBodyPart.setDataHandler(new javax.activation.DataHandler(fileDataSource));
                attachmentBodyPart.setFileName(javax.mail.internet.MimeUtility.encodeText(getAttachments()[attachmentIndex].getDisplayName()));
                attachmentBodyPart.setDisposition(javax.mail.Part.ATTACHMENT);
                multiPartMessage.addBodyPart(attachmentBodyPart);
            }
            ((javax.mail.internet.MimeMessage) message).setContent(multiPartMessage, "UTF-8");
        } else {
            message.setContent(getContent(), getContentType().equals(MIME_TYPE) ? "text/html; charset=\"" + "UTF-8" + "\"" : "text/plain");
        }
        // Create transport
        javax.mail.Transport mailTransport = getMailSession().getTransport("smtp");
        // Save changes to message
        message.saveChanges();
        // Connect the transport
        mailTransport.connect();
        // Send message
        mailTransport.sendMessage(message, message.getAllRecipients());
        // Close the transport
        mailTransport.close();
        // Another way of sending mail but won't send properly
        // javax.mail.Transport.send(message);
    }

    /**
     * Provides the mail session
     *
     * @return Mail session
     */
    private javax.mail.Session getMailSession() {
    	TrackAuthenticator authenticator = getAuthenticator();
        // Set the properties
        java.util.Properties emailSessionProperties = new java.util.Properties();
        emailSessionProperties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
        emailSessionProperties.setProperty("mail.smtp.auth", "true");
        emailSessionProperties.setProperty("mail.debug", debuggingRequired.toString());
        emailSessionProperties.setProperty("mail.smtp.host", "email ip");//TODO:set email ip
        emailSessionProperties.setProperty("mail.smtp.timeout", "30000");
        emailSessionProperties.setProperty("mail.smtp.connectiontimeout", "30000");
        emailSessionProperties.setProperty("mail.transport.protocol", "smtp");
        emailSessionProperties.setProperty("mail.smtp.port", "[Port number]");//TODO: port number
        return javax.mail.Session.getInstance(emailSessionProperties, authenticator);
    }

    /**
     * Provides the authenticator
     *
     * @return
     */
    private TrackAuthenticator getAuthenticator() {
        return new TrackAuthenticator();
    }

    /**
     * Adds BCC address to the recipient list
     *
     * @param bccAddress
     * @throws Exception
     * @see {@link #addBccAddress(Address)}
     */
    public void addBccAddress(java.lang.String bccAddress) throws Exception {
        addBccAddress(new Address(bccAddress));
    }

    /**
     * Adds BCC address to the recipient list
     *
     * @param bccAddress
     * @see #addBccAddress(String)
     */
    public void addBccAddress(Address bccAddress) {
        java.util.List<Address> listCurrentBccAddress = getBccAddresses();
        if (listCurrentBccAddress == null) {
            listCurrentBccAddress = new java.util.ArrayList();
        }
        listCurrentBccAddress.add(bccAddress);
        setBccAddresses(listCurrentBccAddress);
    }

    /**
     * Adds CC address to the recipient list
     *
     * @param ccAddress
     * @throws Exception
     * @see {@link #addCcAddress(Address)}
     */
    public void addCcAddress(java.lang.String ccAddress) throws Exception {
        addCcAddress(new Address(ccAddress));
    }

    /**
     * Adds CC address to the recipient list
     *
     * @param ccAddress
     * @see #addCcAddress(String)
     */
    public void addCcAddress(Address ccAddress) {
        java.util.List<Address> listCurrentCcAddress = getCcAddresses();
        if (listCurrentCcAddress == null) {
            listCurrentCcAddress = new java.util.ArrayList();
        }
        listCurrentCcAddress.add(ccAddress);
        setCcAddresses(listCurrentCcAddress);
    }

    /**
     * Adds Recipient address to the recipient list
     *
     * @param recipientAddress
     * @throws Exception
     * @see {@link #addRecipientAddress(Address)}
     */
    public void addRecipientAddress(java.lang.String recipientAddress) throws Exception {
        addCcAddress(new Address(recipientAddress));
    }

    /**
     * Adds Recipient address to the recipient list
     *
     * @param recipientAddress
     * @see #addRecipientAddress(String)
     */
    public void addRecipientAddress(Address recipientAddress) {
        java.util.List<Address> listCurrentRecipientAddress = getRecipientAddresses();
        if (listCurrentRecipientAddress == null) {
            listCurrentRecipientAddress = new java.util.ArrayList();
        }
        listCurrentRecipientAddress.add(recipientAddress);
        setRecipientAddresses(listCurrentRecipientAddress);
    }
}
