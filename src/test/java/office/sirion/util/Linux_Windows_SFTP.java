package office.sirion.util; 
 
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.jcraft.jsch.*;

public class Linux_Windows_SFTP {
	static String hostname = "192.168.2.168";
    static String username = "tomcat7";
    static String password = "tomcat7@123";
    static String copyFrom = "/home/tomcat7/appProperties/trunkrc/integration/*";
    static File currentDir = new File("D:\\QASystemDetails"); 
    static String copyTo = "D:\\QASystemDetails";
    static String regex = "(^database.url=jdbc\\\\:postgresql(.*?)$)";
    static String destinationPath=null;
    static String ServerDetails= "D://SeleniumAutomation//QAServers.txt";
    static String excelFilePath = "D://SeleniumAutomation//QAServers_Updated_Properties.xlsx";
    static int RowCount = 1;
	public static void FileTransfer(String hostName,String filePath, String serverName){
    	 
        JSch jsch = new JSch();
        Session session = null;
        System.out.println("Trying to connect.....");
        try {
            session = jsch.getSession(username, hostName, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(); 
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel; 
            destinationPath=copyTo+"\\"+serverName+".properties";
            System.out.println(serverName + "  is now connected, copying files");
            sftpChannel.get(filePath, (destinationPath));
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }
        System.out.println("Done !!");
    }
	public static void FileRead(File dir) throws IOException, InterruptedException{
		
		Pattern patt = Pattern.compile(regex);
		File directory = new File("D:\\QASystemDetails");

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        Collections.sort(resultList,new FileComparator());
        for(File files: resultList){
        	System.out.println(files.getName());
        	BufferedReader r;
    		try {
    			r = new BufferedReader(new FileReader(files));
    	    String line;
    	    try {
    			while ((line = r.readLine()) != null) {
    			  Matcher m = patt.matcher(line);
    			  while (m.find()) {
    			   String applicationDatabase = line.replaceAll("(jdbc\\\\:postgresql\\\\:\\/\\/)", "");
    			   String[] ActualString =applicationDatabase.replaceAll("(\\\\:543.\\/)", "-->").split("-->");
    			   System.out.println(ActualString[0]);
    			   System.out.println(ActualString[1]);
    			   ExcelWrite(ActualString[1]);
    			   
    			 }
    			}
    			RowCount++;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		} catch (FileNotFoundException e1) {
    			e1.printStackTrace();
    		}
        
        }        
	}
	
	public static void ExcelWrite(String CellValue){
		
		try {
			FileInputStream fis = new FileInputStream(new File(excelFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook (fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell = null;
			cell = sheet.getRow(RowCount).getCell(5);
			cell.setCellValue(CellValue);
			fis.close();
			FileOutputStream fos =new FileOutputStream(new File(excelFilePath));
		        workbook.write(fos);
		        fos.close();
		        workbook.close();
			System.out.println("Done");
		}catch (IOException ex) {
            ex.printStackTrace();
        }
		
	}
	
	public static void ServerDetails() throws IOException{
		BufferedReader lineReader = new BufferedReader(new FileReader(ServerDetails));
		String lineText = null;
		List<String> listLines = new ArrayList<String>();
		while ((lineText = lineReader.readLine()) != null) {
		    listLines.add(lineText);
		}
		for(String line:listLines){
			String[] arr = line.split("-->");
			FileTransfer(arr[0],arr[1],arr[2]);	
		}
		lineReader.close();
	}
	
	public static class FileComparator implements Comparator<File> {
		  
		public int compare(File f0, File f1) {
		long date1 = f0.lastModified();
		long date2 = f1.lastModified();
		  
		if (date1 > date2)
		return 1;
		else if (date2 > date1)
		return -1;
		  
		return 0;
		}
		}
	
public static void main(String args[]) throws IOException, InterruptedException {
	ServerDetails();
     FileRead(currentDir);
    }
}