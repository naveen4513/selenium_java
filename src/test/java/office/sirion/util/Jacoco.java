package office.sirion.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static org.apache.poi.hssf.record.cf.BorderFormatting.BORDER_THICK;

public class Jacoco {

    public static String repoURI="http://naveen.gupta@bit.sirionlabs.office/scm/sir/sirion.git";
    public static String ReleaseVersion="RC1.37";
    public static String branch= "release/1.37";
    public static String folderPath = "D://Jacoco1";
    public static Git git;
    public static WebDriver driver;
    public static JavascriptExecutor js;
    public static String user = "tomcat7";
    public static String password = "tomcat!@#";
    public static String host = "192.168.2.190";
    public static int port = 22;
    public static String copyFrom = "/home/tomcat7/jacoco/jacoco.exec";
    public static String copyToapiservice = "D:\\Jacoco1\\api-service\\target";
    public static String copyToCommon = "D:\\Jacoco1\\common\\target";
    public static String copyToEmail = "D:\\Jacoco1\\email\\target";
    public static String copyToEntity = "D:\\Jacoco1\\entity\\target";
    public static String copyToService = "D:\\Jacoco1\\service\\target";
    public static String copyToWeb = "D:\\Jacoco1\\web\\target";
    public static String copyToworkflow = "D:\\Jacoco1\\workflow\\target";
    public static String MissedInstructions;
    public static String TotalInstructions;
    public static String MissedLines;
    public static String TotalLines;
    public static String Percentage;
    public static  Logger LOGGER = Logger.getAnonymousLogger();
    public static  String SMTP = "smtp.office365.com";
    public static  int SMTP_PORT = 587;
    public static  String emailID = "naveen.gupta@sirionlabs.com";
    public static  String emailPassword = "Sirion@123";
    public static String from = "naveen.gupta@sirionlabs.com";
    public static String to = "kiran.malla@sirionlabs.com, anay.jyoti@sirionlabs.com, anshul.goel@sirionlabs.com";
    public static String subject = ReleaseVersion+" | Code Coverage Report";
    public static String[] modules = new String[]{"api-service","common","email","entity","web","service","workflow"};

    public static void buildCheckout() throws GitAPIException, IOException, DetachedHeadException {

        if (Files.exists(Paths.get(folderPath))) {
            git = Git.open(new File(folderPath));
            System.out.println("Open Already existing codebase");
            PullResult call =  git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider("naveen.gupta", "naveen.gupta")).setRemoteBranchName(branch).call();
        } else {
            git = Git.cloneRepository()
                    .setURI(repoURI)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("naveen.gupta", "naveen.gupta"))
                    .setDirectory(new File(folderPath))
                    .setCloneAllBranches(true)
                    .call(); //cloning Repo
            git.open(new File(folderPath)).pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider("naveen.gupta", "naveen.gupta")).call();
            git.checkout().setStartPoint(branch); //Checkout at branch
        }
    }

    public static void executeMavenInstall() throws IOException, MavenInvocationException {

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( "D:\\Jacoco1\\pom.xml" ) );
        request.setGoals( Collections.singletonList( "clean install -DskipTests=true" ) );
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:\\apache-maven"));
        System.out.println(invoker.getMavenHome());
        InvocationResult result = invoker.execute( request );
        if ( result.getExitCode() != 0 )
        {
            throw new IllegalStateException( "Build failed." );
        }
    }

    public static void SFTPFile() {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                System.out.println("Establishing Connection...");
                session.connect();
                System.out.println("Connection established.");
                System.out.println("Creating SFTP Channel.");
                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                System.out.println("SFTP Channel created.");
                sftpChannel.get(copyFrom, copyToapiservice);
                sftpChannel.get(copyFrom, copyToCommon);
                sftpChannel.get(copyFrom, copyToEmail);
                sftpChannel.get(copyFrom, copyToEntity);
                sftpChannel.get(copyFrom, copyToService);
                sftpChannel.get(copyFrom, copyToWeb);
                sftpChannel.get(copyFrom, copyToworkflow);
                sftpChannel.exit();
                session.disconnect();
            } catch (Exception e) {
                System.err.print(e);
            }
        }

    public static void executeJacocoReport() throws IOException, MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( "D:\\Jacoco1\\pom.xml" ) );
        request.setGoals( Collections.singletonList( "jacoco:report" ) );
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:\\apache-maven"));
        System.out.println(invoker.getMavenHome());
        InvocationResult result = invoker.execute( request );
        if ( result.getExitCode() != 0 )
        {
            throw new IllegalStateException( "Build failed." );
        }
    }

    public static void browserInstantiate() {
        System.setProperty("webdriver.chrome.driver", "D:\\SirionSelenide\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js=(JavascriptExecutor)driver;
    }

    public static void getTextFromJquery() throws Exception{
        for(String M:modules) {
            String url = "D:\\Jacoco1\\"+M+"\\target\\site\\jacoco\\index.html";
            driver.get(url);
            //Thread.sleep(10000);
            JSWaiter jsw = new JSWaiter();
            jsw.setDriver(driver);
            jsw.waitAllRequest();
            js.executeScript("return document.querySelector('#coveragetable > tfoot > tr > td:nth-child(2)').innerText");
            String totalInstructions = String.valueOf(js.executeScript("return document.querySelector('#coveragetable > tfoot > tr > td:nth-child(2)').innerText"));
            String[] Insturctions = totalInstructions.split(" of ");
            MissedInstructions = Insturctions[0];
            TotalInstructions = Insturctions[1];
            MissedLines = String.valueOf(js.executeScript("return document.querySelector('#coveragetable > tfoot > tr > td:nth-child(8)').innerText"));
            TotalLines = String.valueOf(js.executeScript("return document.querySelector('#coveragetable > tfoot > tr > td:nth-child(9)').innerText"));
            Percentage= String.valueOf(js.executeScript("return document.querySelector('#coveragetable > tfoot > tr > td:nth-child(3)').innerText"));
            System.out.println("Percentage is: "+Percentage + ", Missed Intructions are : "+MissedInstructions + ", Total Instructions are: "+TotalInstructions + ", Missed Lines are: "+MissedLines + ", Total Lines are: "+TotalLines );

            if(M.equalsIgnoreCase("api-service")){
                modifyExistingWorkbook(1, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if (M.equalsIgnoreCase("common")){
                modifyExistingWorkbook(2, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if(M.equalsIgnoreCase("email")){
                modifyExistingWorkbook(3, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if(M.equalsIgnoreCase("entity")){
                modifyExistingWorkbook(4, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if (M.equalsIgnoreCase("service")){
                modifyExistingWorkbook(6, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if(M.equalsIgnoreCase("web")){
                modifyExistingWorkbook(7, 1,2,4,5,7);
                Thread.sleep(5000);
            }else if(M.equalsIgnoreCase("workflow")){
                modifyExistingWorkbook(8, 1,2,4,5,7);
                Thread.sleep(5000);
            }
        }
    }

    private static void modifyExistingWorkbook(int row ,int PC, int MIC, int TIC, int MLC, int TLC) throws InvalidFormatException, IOException {

        FileInputStream inputStream = new FileInputStream(new File("D:\\SirionSelenide\\Jacoco-RC1.37.xlsx"));

        Sheet sheet = null;
        List<String> sheetNames = new ArrayList<String>();;
        Workbook workbook = WorkbookFactory.create(inputStream);
        CellStyle style = workbook.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        // Get Sheet at index 0
        int noOfSheets = workbook.getNumberOfSheets();
        if (noOfSheets != 0) {
            for (int i = 0; i < noOfSheets; i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
        }

        if (!Arrays.asList(sheetNames).toString().contains("RC1.37_"+currentDate())){
           sheet= workbook.cloneSheet(0);
            workbook.setSheetName(workbook.getSheetIndex(sheet), "RC1.37_"+currentDate());
        }else{
            sheet=workbook.getSheet("RC1.37_"+currentDate());
        }

            //Updating Percentage value
        Row percentage = sheet.getRow(row);
            Cell cell = percentage.getCell(PC);

            if (cell == null){
                cell = percentage.createCell(PC);
            }else{
                percentage.removeCell(cell);
                cell = percentage.createCell(PC);
            }
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Percentage);
            cell.setCellStyle(style);

        //updating missed instructions
        Row missedInstructions = sheet.getRow(row);
            Cell cell1 = missedInstructions.getCell(MIC);

            if (cell1 == null){
                cell1 = missedInstructions.createCell(MIC);
            }else{
                missedInstructions.removeCell(cell1);
                cell1 = missedInstructions.createCell(MIC);
            }
            cell1.setCellType(CellType.NUMERIC);
            cell1.setCellValue(Integer.parseInt(MissedInstructions.replaceAll(",", "")));
            cell1.setCellStyle(style);

            //updating Total instructions
        Row totalInstructions = sheet.getRow(row);
            Cell cell2 = totalInstructions.getCell(TIC);

            if (cell2 == null){
                cell2 = totalInstructions.createCell(TIC);
            }else{
                totalInstructions.removeCell(cell2);
                cell2 = totalInstructions.createCell(TIC);
            }
            cell2.setCellType(CellType.NUMERIC);
            cell2.setCellValue(Integer.parseInt(TotalInstructions.replaceAll(",", "")));
            cell2.setCellStyle(style);

        //updating Missed Line
        Row missedLines = sheet.getRow(row);
        Cell cell3 = missedLines.getCell(MLC);

        if (cell3 == null){
            cell3 = missedLines.createCell(MLC);
        }else{
            missedLines.removeCell(cell3);
            cell3 = missedLines.createCell(MLC);
        }
        cell3.setCellType(CellType.NUMERIC);
        cell3.setCellValue(Integer.parseInt(MissedLines.replaceAll(",", "")));
        cell3.setCellStyle(style);

        //updating Total Lines
        Row totalLines = sheet.getRow(row);
        Cell cell4 = totalLines.getCell(TLC);

        if (cell4 == null){
            cell4 = totalLines.createCell(TLC);
        }else{
            totalLines.removeCell(cell4);
            cell4 = totalLines.createCell(TLC);
        }
        cell4.setCellType(CellType.NUMERIC);
        cell4.setCellValue(Integer.parseInt(TotalLines.replaceAll(",", "")));
        cell4.setCellStyle(style);

        try (FileOutputStream fileOut = new FileOutputStream("D:\\SirionSelenide\\Jacoco-RC1.37.xlsx")) {
            workbook.write(fileOut);
            workbook.close();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    public static String currentDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String strDate= formatter.format(date);
        return strDate;
    }

    public short setBorderThickness() {
        final short borderThick = BORDER_THICK;
        return borderThick;
    }

    public static void sendEmail() {

        // Get the Session object.
        final javax.mail.Session session = javax.mail.Session.getInstance(getEmailProperties(),
                new Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailID, emailPassword);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            // Set FromEmail.
            message.setFrom(new InternetAddress(from));
            // Set ToEmail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            // Set Subject
            message.setSubject(subject);
            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message

            //Replace text in message
            File log= new File("D:\\SirionSelenide\\Email_Body.txt");
            String Version = "${ReleaseVersion}";
            String currentDate = "${Date}";
            Date date1 = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            String strDate1= formatter1.format(date1);

            String text  = FileUtils.readFileToString(log, "utf8");
            text = text.replace(Version, ReleaseVersion).replace(currentDate, strDate1);
            //FileUtils.writeStringToFile(log, text);
            System.out.println(text);

            messageBodyPart.setText(text);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = "D:\\SirionSelenide\\Jacoco-RC1.37.xlsx";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Jacoco-"+ReleaseVersion+".xlsx");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getEmailProperties() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SMTP);
        config.put("mail.smtp.port", SMTP_PORT);
        return config;
    }

    public static void main(String arg[]) throws Exception {
        //buildCheckout();
        //executeMavenInstall();
        //SFTPFile();
        //executeJacocoReport();
        //browserInstantiate();
        //getTextFromJquery();
       sendEmail();

    }
}
