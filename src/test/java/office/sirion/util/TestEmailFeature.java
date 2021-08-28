package office.sirion.util;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import static office.sirion.base.TestBase.CONFIG;

/**
 * Created by Naveen Kumar Gupta on 08/06/18.
 */
public class TestEmailFeature {

    private final static Logger logger = LoggerFactory.getLogger(TestEmailFeature.class);
    SoftAssert softAssert;
    PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
    public static String finalContent = null;
    public static List<String> Password_policy_fields = null;
    public static List<String> values = null;

    /**
     * beforeMethod
     *
     * @param method
     */
    @BeforeMethod
    public void beforeMethod(Method method) {
        logger.info("***********************************************************************************************************************");
        logger.info("In Before Method");
        logger.info("method name is: {} ", method.getName());
        logger.info("................................................Test Starts Here................................................");

    }

    public String getdateFromWhichRecordsNeedsToBeFilter(int daywindow) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        //c.setTime(new Date()); // Now use today date.
        //String todayDate = String.valueOf(sdf.format(c.getTime()));

        c.setTime(new Date());
        c.add(Calendar.DATE, daywindow); //
        String dateFromWhichRecordsNeedsToBeFilter = String.valueOf(sdf.format(c.getTime()));


        return dateFromWhichRecordsNeedsToBeFilter;


    }


    /**
     * @throws Exception TS-80704:Verify that Failed Entity Report is working
     */
    //@Test(priority = 0)
    public void testTS80704(String testName, String tableName, List<String> columnName, List<String> comparator, List<String> columnValue) throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80704";
        try {


            logger.info("Verifying the Test Case : {}", testName);

            String filterRecordsOrderByQuery = "order by id desc limit 1";
            String parser = ", ";

            int daywindow = -1;

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }
                //columnNameValueMap.put("date_created::date", getdateFromWhichRecordsNeedsToBeFilter(daywindow));
                columnNameValueMap.put("date_created::date", getdateFromWhichRecordsNeedsToBeFilter(daywindow));

                String query = postgreSQLJDBC.getQueryClauses(tableName, columnNameValueMap, comparator, null) + " " + filterRecordsOrderByQuery + " ";

                logger.debug("query is {}", query);

                List<List<String>> result = postgreSQLJDBC.doSelect(query);

                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                }

            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }


        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }


        softAssert.assertAll();

    }

    /**
     * @throws Exception TS-80703:Verify that User Activity Report is working
     */
    //@Test(priority = 1)
    public void testTS80703(String testName, String tableName, List<String> columnName, List<String> comparator, List<String> columnValue) throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80703";
        try {

            logger.info("Verifying the Test Case : {}", testName);

            String filterRecordsOrderByQuery = "order by id desc limit 1";
            String parser = ", ";

            int daywindow = -1;


            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }
                columnNameValueMap.put("date_created", getdateFromWhichRecordsNeedsToBeFilter(daywindow));


                String query = postgreSQLJDBC.getQueryClauses(tableName, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";

                logger.debug("query is {}", query);

                List<List<String>> result = postgreSQLJDBC.doSelect(query);

                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                }

            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }


        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }


        softAssert.assertAll();

    }

    /**
     * @throws Exception TS-80699:Verify that Schedule Email is working
     */
    @Test(priority = 2)
    public void testTS80699(String testName, String reportName, String tableName, List<String> columnName, List<String> comparator, List<String> columnValue, List<String> tableColumnNameToSelect) throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80699";
        try {

            logger.info("Verifying the Test Case : {}", testName);

            String parser = ", ";


            int daywindow = -1;
            String filterRecordsOrderByQuery = "order by id desc limit 1";

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }
                columnNameValueMap.put("date_created", getdateFromWhichRecordsNeedsToBeFilter(daywindow));

                String query = postgreSQLJDBC.getQueryClauses(tableName, tableColumnNameToSelect, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";


                logger.debug("query is {}", query);

                List<List<String>> result = postgreSQLJDBC.doSelect(query);

                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                } else {

                    String htmlbodytext = result.get(0).get(result.get(0).size() - 1); //putting hardcoded 0 index because there is always going to be one record on which we will verify the body content

                    // these following assertion is for checking 'report title' , 'user name' who has schedule the report and 'additional comment' in email body
                    softAssert.assertTrue(htmlbodytext.contains("The report titled " + reportName + " has been scheduled"), "Email body don't have  Report Name");
                    softAssert.assertTrue(htmlbodytext.contains("scheduled by " + CONFIG.getProperty("endUserFullName") + " User for your use."), "Email body don't have  correct schedular User Name");
                    softAssert.assertTrue(htmlbodytext.contains("automation_" + reportName), "Email body don't have  correct additional comment ");


                    // TODO: 6/4/18 check for schedule date need to be done which is subjected to date pattern in email needed to be discussed

                }


            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }


        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }


        softAssert.assertAll();

    }


    /**
     * @throws Exception TS-80698:Verify that Schedule Large Report is working
     */
    //@Test(priority = 3)
    public void testTS80698(String testName, String reportName, String tableName, List<String> columnName, List<String> comparator, List<String> columnValue, List<String> tableColumnNameToSelect) throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80698";
        try {
            logger.info("Verifying the Test Case : {}", testName);

            String parser = ", ";


            int daywindow = -1;
            String filterRecordsOrderByQuery = "order by id desc limit 1";

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }
                columnNameValueMap.put("date_created", getdateFromWhichRecordsNeedsToBeFilter(daywindow));

                String query = postgreSQLJDBC.getQueryClauses(tableName, tableColumnNameToSelect, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";
                logger.debug("query is {}", query);
                List<List<String>> result = postgreSQLJDBC.doSelect(query);
                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                } else {

                    String htmlbodytext = result.get(0).get(result.get(0).size() - 1); //putting hardcoded 0 index because there is always going to be one record on which we will verify the body content

                    // this  assertion is for checking 'report title'
                    softAssert.assertTrue(htmlbodytext.contains(reportName), "Email body don't have Large Report Name");


                    // these following statements is for verifying link hypertext in the body
                    Document doc = Jsoup.parse(htmlbodytext);
                    Elements link = doc.select("a");
                    if (link.attr("href").toString().contains("http://" + CONFIG.getProperty("endUserURL") + "/scheduleLargeReport/downloadReport?id=")) {
                        // TODO: 9/4/18  this code is not verified yet since no entry with dft.auto environment was there in database
                     /*   APIUtils apiUtils = new APIUtils();
                        Boolean isLinkValid = apiUtils.isLinkValid(link.attr("href").split("/")[link.attr("href").split("/").length - 2] + "/" + link.attr("href").split("/")[link.attr("href").split("/").length - 1]);
                        softAssert.assertTrue(isLinkValid, "Link mentioned in the body of email is not valid ");*/
                    } else {
                        softAssert.assertTrue(false, "Link in the body of Message --> [ " + link.attr("href").toString() + "] is not correct it should be -->[ " + "http://" + CONFIG.getProperty("endUserURL") + "/scheduleLargeReport/downloadReport?id={reportId}" + "]");

                    }
                }
            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }


        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }


        softAssert.assertAll();

    }

    /**
     * @throws Exception TS-80700:Verify that Manual Notification Alert is working
     */
    //@Test(priority = 4)
    public void testTS80700(String entityName, String entityClientId, String tableName, List<String> columnName, List<String> comparator, List<String> columnValue, List<String> tableColumnNameToSelect) throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80700";
        try {
            //logger.info("Verifying the Test Case : {}", testName);

            String parser = ", ";
            int daywindow = -1;

            String filterRecordsOrderByQuery = "order by id desc limit 1";

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }
               // columnNameValueMap.put("date_created", getdateFromWhichRecordsNeedsToBeFilter(daywindow));

                columnNameValueMap.put("date_created::date", String.valueOf(LocalDate.now()));

                String query = postgreSQLJDBC.getQueryClauses(tableName, tableColumnNameToSelect, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";
                logger.debug("query is {}", query);
                List<List<String>> result = postgreSQLJDBC.doSelect(query);
                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                } else {

                    //Verifying Subject line
                    String subject = result.get(0).get(result.get(0).size() - 3);
                    logger.debug("Subject Line Contains: Notification Alert", subject.contains("Notification Alert"));
                    logger.debug(subject);

                    //Verifying To_Email
                    String to_Email = result.get(0).get(result.get(0).size() - 4);
                    logger.debug(to_Email);

                    //Verifying Client_ID
                    String client_id = result.get(0).get(result.get(0).size() - 1);
                    logger.debug(client_id);
                    client_id.contains("1001");

                    String htmlbodytext = result.get(0).get(result.get(0).size() - 2); //putting hardcoded 0 index because there is always going to be one record on which we will verify the body content

                    Document document = Jsoup.parse(htmlbodytext);
                    Elements links = document.select("a");

                    for (Element link : links) {

                        if (link.toString().contains("axigen")) // this hyperlink needed to be ignored
                            continue;

                        else {
                            if (link.attr("href").toString().contains(CONFIG.getProperty("endUserURL"))) {
                                continue;
                            } else {
                                softAssert.assertTrue(false, "Link in the body of Message --> [ " + link.attr("href").toString() + "] is not correct it should have link of this enviroment -->[ " + "http://" + CONFIG.getProperty("endUserURL") + "]");
                            }
                        }

                    }
                }
            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }


        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }


        softAssert.assertAll();

    }


    /**
     * @throws Exception TS-80701:Verify that Password Alert Email is working
     */
    //@Test(priority = 4)
    public Map<String, String> testts80701() throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80701";
        try {
            //logger.info("Verifying the Test Case : {}", testName);

            String tableName = "system_emails";
            List<String> columnName = Arrays.asList("subject", "client_id");
            List<String> comparator = Arrays.asList("ilike", "equal");
            List<Object> columnValue = Arrays.asList("Password Policy Update", "1019");
            List<String> tableColumnNameToSelect = Arrays.asList("subject", "body", "client_id");
            String parser = ", ";

            String filterRecordsOrderByQuery = "order by id desc limit 1";

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }

                String query = postgreSQLJDBC.getQueryClauses(tableName, tableColumnNameToSelect, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";
                logger.debug("query is {}", query);
                List<List<String>> result = postgreSQLJDBC.doSelect(query);
                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                } else {

                    String htmlbodytext = result.get(0).get(result.get(0).size() - 2); //putting hardcoded 0 index because there is always going to be one record on which we will verify the body content

                    Document doc = Jsoup.parse(htmlbodytext);
                    Elements content = doc.getElementsByClass("body-copy").tagName("td");
                    finalContent = content.toString().replace("<td class=\"body-copy\">", "").replace("</td>", "").
                            replace("<td class=\"body-copy\" width=\"37%\">Field Name", "").
                            replace("<td class=\"body-copy\" width=\"37%\">Modified Value", "");

                    Password_policy_fields = new ArrayList<String>();
                    Password_policy_fields.add("Password Never Expires");
                    Password_policy_fields.add("Password Expires In (Days)");
                    Password_policy_fields.add("Advance Password Expiry Notification (Days)");
                    Password_policy_fields.add("Temporary Password Expires in (Hours)");
                    Password_policy_fields.add("Enforce Password History");
                    Password_policy_fields.add("Past Passwords Count");
                    Password_policy_fields.add("Minimum Password Length");
                    Password_policy_fields.add("Password Complexity Requirement");
                    Password_policy_fields.add("Password Answer Requirement");
                    Password_policy_fields.add("Maximum Login Invalid Attempts");
                    Password_policy_fields.add("Unlock Account");
                    Password_policy_fields.add("Lockout Effective Period (Minutes)");
                    Password_policy_fields.add("Enforce 24 Hours as Minimum Lifetime of Password");
                    Password_policy_fields.add("Max Failed Security Question Attempts Allowed");
                    Password_policy_fields.add("Max Idle Time");
                    Password_policy_fields.add("Disable Save Password in Browser");

                    Map<String, String> map = new LinkedMap();
                    for (int i=0; i<Password_policy_fields.size(); i++) {
                        map.put(Password_policy_fields.get(i), FileWrite(finalContent).get(i));    // is there a clearer way?
                    }
                    return map;
                }
            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }
        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }
        return null;
    }

    //@Test(priority = 4)
    public String[] testts80702() throws Exception {
        softAssert = new SoftAssert();


        String scenarioName = "ts-80701";
        try {
            //logger.info("Verifying the Test Case : {}", testName);

            String tableName = "system_emails";
            List<String> columnName = Arrays.asList("to_mail","subject", "client_id");
            List<String> comparator = Arrays.asList("equal", "equal","equal");
            List<Object> columnValue = Arrays.asList("neetu_admin@sirionqa.office","Your password has been reset", "1019");
            List<String> tableColumnNameToSelect = Arrays.asList("to_mail","subject", "body", "client_id");
            String parser = ", ";

            String filterRecordsOrderByQuery = "order by id desc limit 1";

            if ((columnName.size() == comparator.size()) && (columnName.size() == columnValue.size())) {

                Map<String, Object> columnNameValueMap = new LinkedHashMap<>();
                for (int i = 0; i < columnName.size(); i++) {
                    columnNameValueMap.put(columnName.get(i), columnValue.get(i));
                }

                String query = postgreSQLJDBC.getQueryClauses(tableName, tableColumnNameToSelect, columnNameValueMap, comparator) + " " + filterRecordsOrderByQuery + " ";
                logger.debug("query is {}", query);
                List<List<String>> result = postgreSQLJDBC.doSelect(query);
                if (result.isEmpty()) {
                    softAssert.assertTrue(false, "There is no such entry in " + tableName + " table based on given filter in config file ");
                } else {

                    String htmlbodytext = result.get(0).get(result.get(0).size() - 2); //putting hardcoded 0 index because there is always going to be one record on which we will verify the body content

                    Document doc = Jsoup.parse(htmlbodytext);
                    Elements content = doc.getElementsByClass("body-copy").tagName("td");

                    finalContent = content.toString().replace("<td class=\"body-copy\">Your Sirion account's password has been reset. Your new password is <b>","")
                            .replace("</b><br>This password is valid till", ",").replace("</td>","").replace("<td class=\"body-copy\"> This is a temporary password and you will be prompted to change this password when you log in. </td>","");
                    System.out.println(finalContent);

                    return finalContent.split(",");
                }
            } else {
                softAssert.assertTrue(false, "Config File is incorrect for this test cast : Plz Check Again");
            }
        } catch (Exception e) {

            logger.debug("Error in Fetching the Config Details from config file-- {} ", e.getMessage());
        }
        return null;
    }

    /**
     * afterMethod
     *
     * @param result
     */
    @AfterMethod
    public void afterMethod(ITestResult result) {
        logger.info("................................................Test Ends Here................................................");
        logger.info("In After Method");
        logger.info("method name is: {}", result.getMethod().getMethodName());
        logger.info("***********************************************************************************************************************");

    }

    /**
     * afterClass
     */
    @AfterClass
    public void afterClass() {
        logger.info("In After Class method");
    }


    public List<String> FileWrite(String finalContent) throws FileNotFoundException {
        String line = null;
        Scanner scanner = null;


        File file = new File("D:\\SeleniumAutomation\\FileWriter.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(finalContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//Searching Text in File

        values = new ArrayList<String>();
        for (int i = 0; i < Password_policy_fields.size(); i++) {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linefromfile = scanner.nextLine();

                    if (linefromfile.contains((Password_policy_fields.get(i).toString()))) {
                        line = scanner.nextLine();
                        values.add(line);
                    }
            }

        }
        return values;
    }
}