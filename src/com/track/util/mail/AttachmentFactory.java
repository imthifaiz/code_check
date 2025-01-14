package com.track.util.mail;

/**
 * Represents the file attachment of email
 
 * @author Ravindra Gullapalli
 */
public final class AttachmentFactory {
    public static Attachment getAttachment(java.lang.String attachedFileName){
        return new Attachment(attachedFileName);
    }
    public static Attachment getAttachment(java.io.File attachedFile){
        return new Attachment(attachedFile);
    }
    public static Attachment getAttachment(java.lang.String attachedFileName, java.lang.String fileNameToShowInEmail){
        return new Attachment(attachedFileName, fileNameToShowInEmail);
    }
    public static Attachment getAttachment(java.io.File attachedFile, String fileNameToShowInEmail){
        return new Attachment(attachedFile, fileNameToShowInEmail);
    }
    public static Attachment[] getAttachments(java.lang.String[] attachedFileNames, java.lang.String[] fileNamesToShowInEmail) throws java.lang.Exception{
        //  Make sure that both arrays have same number of elements
        if (attachedFileNames != null 
            && fileNamesToShowInEmail != null 
            && attachedFileNames.length != fileNamesToShowInEmail.length){
            throw new java.lang.Exception("File name and display name array size mismatch");
        }
        if (attachedFileNames == null){
            return null;
        }
        if (fileNamesToShowInEmail == null){
            fileNamesToShowInEmail = attachedFileNames;
        }
        Attachment[] attachments = new Attachment[attachedFileNames.length];
        for(int fileIndex = 0; fileIndex < attachedFileNames.length; fileIndex ++){
            attachments[fileIndex] = getAttachment(attachedFileNames[fileIndex], fileNamesToShowInEmail[fileIndex]);
        }
        return attachments;
    }
}
