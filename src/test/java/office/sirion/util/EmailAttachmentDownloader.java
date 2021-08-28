package office.sirion.util;

import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;

public class EmailAttachmentDownloader {

    private static String saveDirectory = System.getProperty("user.dir") +"\\Attachments"; // directory to save the downloaded documents
    private static int daywindow = -1;
    static String host = "192.168.2.115";
    static String userName = "naveen@sirionqa.office"; //username for the mail you want to read
    static String password = "admin123"; //password

    public static void downloadEmailAttachments(String UploadedFile) {



        try {
            Properties properties = new Properties();
            // server setting
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", 143);
            properties.put("mail.pop3.starttls.enable", "false");
            Session session = Session.getDefaultInstance(properties);

            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(host,userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
				String from = fromAddress[0].toString();
				String subject = message.getSubject();
                TestEmailFeature TEF = new TestEmailFeature();
				String sentDate = TEF.getdateFromWhichRecordsNeedsToBeFilter(daywindow);

                String contentType = message.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";

                if(subject.equalsIgnoreCase("Bulk create request response")) {

                    if (contentType.contains("multipart")) {
                        // content may contain attachments
                        Multipart multiPart = (Multipart) message.getContent();
                        int numberOfParts = multiPart.getCount();
                        for (int partCount = 0; partCount < numberOfParts; partCount++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // this part is attachment
                                String fileName = part.getFileName();
                                attachFiles += fileName + ", ";
                                File f =new File(saveDirectory + File.separator + fileName);
                                if(fileName.compareTo(UploadedFile)==0){

                                    //print out details of each message
                                    System.out.println("Message #" + (i + 1) + ":");
                                    System.out.println("\t From: " + from);
                                    System.out.println("\t Subject: " + subject);
                                    System.out.println("\t Sent Date: " + sentDate);
                                    System.out.println("\t Message: " + messageContent);
                                    System.out.println("\t Attachments: " + attachFiles);

                                    f = new File(saveDirectory + File.separator + fileName);
                                    f.createNewFile(); //creating new file
                                }
                            } else {
                                // this part may be the message content
                                messageContent = part.getContent().toString();
                            }
                        }
                        if (attachFiles.length() > 1) {
                            attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                        }
                    } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                        Object content = message.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    }
                }
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for imap.");
            ex.printStackTrace();
            System.out.println(ex);
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
            System.out.println(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }
}