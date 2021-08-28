package office.sirion.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import java.util.regex.*;  

public class ExceptionHandlingEmails {
	static Set<String> set = new HashSet<String>();
	static  String emails = "";
	static String mindate = "06/03/2018";
    static String maxdate = "07/03/2018";
    static String synopsis="";
    static String regex="";
    static String email=null;
    
   public static void check(String host, String user,String password)
   {
	   
      try {

      //create properties field
      Properties properties = new Properties();

      properties.put("mail.pop3.host", host);
      properties.put("mail.pop3.port", "143");
      properties.put("mail.pop3.starttls.enable", "false");
      Session emailSession = Session.getDefaultInstance(properties);
  
      //create the POP3 store object and connect with the pop server
      Store store = emailSession.getStore("imap");
      store.connect(host, user, password);

      //create the folder object and open it
      Folder emailFolder = store.getFolder("Exception-rc");
      emailFolder.open(Folder.READ_ONLY);

      // retrieve/search the messages from the inbox folder (within specific dates) in an array and print it
      
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

          Date minDate = formatter.parse(mindate);
          Date maxDate = formatter.parse(maxdate);
          System.out.println(minDate);
          System.out.println(maxDate);

      Message messages[] = emailFolder.getMessages();    //search on the imap server
      
      System.out.println("messages.length---" + messages.length);
      
      for (int i = 0; i < messages.length; i++) {
         Message message = messages[i];
         
         Address[] fromAddress = message.getFrom(); 
         String from = fromAddress[0].toString(); //From Address
        
         String sentDate = message.getSentDate().toString();
         
         if (message.getSentDate().after(minDate) && message.getSentDate().before(maxDate))
         {
        	 if (!message.getContent().toString().contains("ClientAbortException: java.io.IOException: Connection reset by peer"))
        	 {
        		 if (!message.getContent().toString().contains("workflow/download"))
            	 {
        			 if (message.getContent().toString().contains("qa.rc.office") && message.getSubject().contentEquals("Exception occurred!"))
        			 {
        	
         System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
         System.out.println("Email Number " + (i + 1));
         System.out.println("Subject: " + message.getSubject());
         System.out.println("From: " + from);
         System.out.println("To: " + message.getRecipients(Message.RecipientType.TO)[0].toString());
         System.out.println("Sent Date: "+ sentDate);

         
         //Pattern pattern = Pattern.compile("(..........)(rc\\.office\\/.*? )");
         Pattern pattern = Pattern.compile("(http:\\/\\/qa\\.rc\\.office\\/.*?\\ )");
         Matcher matcher = pattern.matcher(message.getContent().toString());
         synopsis = matcher.toString();
         String Exception="";
         while (matcher.find()) {
        	Exception = message.getContent().toString().replaceAll("(\\<html\\>)[\\s\\S]*(\\<td width\\=\\\"100\\%\\\"\\>\\<p class\\=\\\"body\\-copy\\\"\\>)", "");
        }
         readEmails(Exception);       
         
      
     try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("D:\\SeleniumAutomation\\Exceptions.txt"), "utf-8"))) {
    	  		writer.write(Exception);
    	  		ExceptionFiledinJIRA(Exception);
      			}
      }
      }
      }
      }
      }
      //close the store and folder objects
      emailFolder.close(false);
      store.close();

      
      }catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      }
   
   public static String readEmails(String fileData) throws InterruptedException {
	   
	   regex = "((..........)(rc\\.office\\/)([\\s\\S]*))(more)";
	   
	   Matcher m = Pattern.compile(regex).matcher(fileData);
	       while (m.find()) {
	           	           
	           if (!set.contains(fileData)) {
	              set.add(fileData);
	           }
	       }

	       return fileData;
	}
   
   public static void ExceptionFiledinJIRA(String email) throws InterruptedException{
	   
	   regex = "(..........)(rc\\.office\\/.*?\\ )";
	   
	   Pattern pattern = Pattern.compile(regex);
	   Matcher matcher = pattern.matcher(email);
	   
	   while (matcher.find()) {
		    System.out.println("Full match: " + matcher.group(0));
		    for (int i = 1; i <= matcher.groupCount(); i++) {
		        System.out.println("Group " + i + ": " + matcher.group(i));
		        JiraCreateAPI Jira = new JiraCreateAPI();
		        Jira.CreateIssue(matcher.group(0), email);
		    }
		}
	   
	 
   }
   
   public static void main(String[] args) {

      String host = "192.168.2.115";// change accordingly
      String username = "admin@sirionqa.office";// change accordingly
      String password = "admin123";// change accordingly

      check(host, username, password);

   }
}