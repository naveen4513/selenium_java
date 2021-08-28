package office.sirion.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLBulkCreate {
	public static int j;
	public static int i;
    public static BufferedReader reader = null;
    public static BufferedReader reader1 = null;
    public static InputStream is = null;
    public static OutputStream os = null;

	public static void main(String args[]){

    	String oldString1="NAVEENBATCHID";
    	String oldString2="NAVEENID";
        FileWriter writer1 = null;

    	 File xmlToBeModified = new File("D:\\Staging Data\\NAVEENBATCHID-MA-NAVEENID.xml");
    	 File textfileToBeModified = new File("D:\\Staging Data\\MA-NAVEENBATCHID.txt");

         try{
             for(int batchNo = 326; batchNo <= 326; batchNo++){
                 List filenames  = new ArrayList<>();
            	 new File("D:\\XMLBulkData\\"+batchNo).mkdir();
                 String oldContent1 = "";
	             reader1 = new BufferedReader(new FileReader(textfileToBeModified));
	             String line1 = reader1.readLine();
	             while (line1 != null){
	                 oldContent1 = oldContent1 + line1 + System.lineSeparator();
	                 line1 = reader1.readLine();
	             }
	              for(int maID = 1000*batchNo + 1; maID <=1000*batchNo + 1; maID++){
	                  String oldContent = "";
	                  reader = new BufferedReader(new FileReader(xmlToBeModified));
	                  String line = reader.readLine();
	                  while (line != null){
	                      oldContent = oldContent + line + System.lineSeparator();
	                      line = reader.readLine();
	                  }
	            	  FileWriter writer = null;
	            	  String newContent = oldContent.replaceAll(oldString1, Integer.toString(batchNo).replaceAll("\\.0*$", "")).
	            			  replaceAll(oldString2, Integer.toString(maID).replaceAll("\\.0*$", ""));

	                  writer = new FileWriter("D:\\XMLBulkData\\"+batchNo + "\\"+ batchNo +"-MA-"+maID+".xml");
	                  writer.write(newContent);
	                  writer.close();

	             	  String newContent1 = oldContent1.replaceAll(oldString1, Integer.toString(batchNo).replaceAll("\\.0*$", "")).
	             			  replaceFirst(oldString2, Integer.toString(maID).replaceAll("\\.0*$", ""));


	             	  filenames.add(newContent1);

                      File pdfattachmentToBeModified = new File("D:\\Staging Data\\NAVEENID--NAVEENBATCHID-PPM 286875 & 242989$.pdf");
                      File xlsattachmentToBeModified = new File("D:\\Staging Data\\NAVEENID--NAVEENBATCHID-TEST DOCUMENTĐ#___ UAT.xlsx");
                      File msgattachmentToBeModified = new File("D:\\Staging Data\\NAVEENID--NAVEENBATCHID-Welcome to Sirion!.msg");

                      File newpdffileTOberenamed = new File("D:\\Attachments\\"+maID +"-"+maID+"2"+"-PPM 286875 & 242989$.pdf");
                      File newxlsfileTOberenamed = new File("D:\\Attachments\\"+maID +"-"+maID+"1"+"-TEST DOCUMENTĐ#___ UAT.xlsx");
                      File  newmsgfileTOberenamed = new File("D:\\Attachments\\"+maID +"-"+maID+"-Welcome to Sirion!.msg");

                      copyFileUsingStream(pdfattachmentToBeModified,newpdffileTOberenamed);
                      copyFileUsingStream(xlsattachmentToBeModified,newxlsfileTOberenamed);
                      copyFileUsingStream(msgattachmentToBeModified,newmsgfileTOberenamed);

	              }
                 writer1 = new FileWriter("D:\\XMLBulkData\\"+batchNo + "\\MA-"+batchNo+".txt");
                 for(int i = 0; i< filenames.size();i++){
                     writer1.write(filenames.get(i).toString());
                 }
                 writer1.close();
             }
             System.out.println("Done....");
         }


         catch (Exception e)
         {
             e.printStackTrace();
         }
         finally
         {
             try
             {
                reader.close();
                 reader1.close();

             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

}