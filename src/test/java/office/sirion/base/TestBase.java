package office.sirion.base;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.TestRail.Projects;
import com.codepine.api.testrail.model.*;
import com.google.common.base.Function;
import java.net.MalformedURLException;
import java.net.URL;
import office.sirion.util.JSWaiter;
import office.sirion.util.Xls_Reader;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.AssertJUnit;
import org.testng.ITestResult;

import javax.mail.internet.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
//import static java.util.concurrent.TimeUnit.SECONDS;

public class TestBase {
    public static boolean isInitialized = false;
    public static boolean isBrowserOpened = false;
    public static boolean isSirionAdminLogin = false;
    public static boolean ispasswordpolicyclientAdminLogin = false;
    public static boolean isClientSetupAdminLogin = false;
    public static boolean isSirionUserAdminLogin = false;
    public static boolean isClientAdminLogin = false;
    public static boolean isEndUserLogin = false;
    public static org.apache.log4j.Logger Logger = null;
    public static Properties CONFIG = null;
    public static Properties OR = null;
    public static Xls_Reader suiteXls = null;
    public static Xls_Reader sirion_admin_suite_xls = null;
    public static Xls_Reader client_setup_admin_suite_xls = null;
    public static Xls_Reader user_admin_suite_xls = null;
    public static Xls_Reader client_admin_suite_xls = null;
    public static Xls_Reader client_user_admin_suite_xls = null;
    public static Xls_Reader client_workflow_configuration_suite_xls = null;
    public static Xls_Reader client_ui_content_setup_suite_xls = null;
    public static Xls_Reader vendor_hierarchy_suite_xls = null;
    public static Xls_Reader supplier_suite_xls = null;
    public static Xls_Reader contract_suite_xls = null;
    public static Xls_Reader sl_suite_xls = null;
    public static Xls_Reader child_sl_suite_xls = null;
    public static Xls_Reader obligation_suite_xls = null;
    public static Xls_Reader child_obligation_suite_xls = null;
    public static Xls_Reader action_suite_xls = null;
    public static Xls_Reader issue_suite_xls = null;
    public static Xls_Reader cr_suite_xls = null;
    public static Xls_Reader int_suite_xls = null;
    public static Xls_Reader dispute_suite_xls = null;
    public static Xls_Reader po_suite_xls = null;
    public static Xls_Reader inv_suite_xls = null;
    public static Xls_Reader wor_suite_xls = null;
    public static Xls_Reader governance_body_suite_xls = null;
    public static Xls_Reader child_gb_suite_xls = null;
    public static Xls_Reader clause_suite_xls = null;
    public static Xls_Reader contract_template_suite_xls = null;
    public static Xls_Reader cdr_suite_xls = null;
    public static Xls_Reader common_listing_suite_xls = null;
    public static Xls_Reader common_report_suite_xls = null;
    public static Xls_Reader common_dashboard_suite_xls = null;
    public static Xls_Reader common_insight_suite_xls = null;
    public static Xls_Reader insights_suite_xls = null;
    public static Xls_Reader dashboard_suite_xls = null;
    public static Xls_Reader calendar_suite_xls = null;
    public static Xls_Reader todo_suite_xls = null;
    public static Xls_Reader search_suite_xls = null;
    public static Xls_Reader staging_suite_xls = null;
    public static Xls_Reader mass_email_suite_xls = null;
    public static Xls_Reader sso_suite_xls = null;
    public static Xls_Reader bulkjobs_suite_xls = null;
    public static Xls_Reader manual_report_suite_xls = null;
    public static Xls_Reader my_profile_suite_xls = null;
    public static Xls_Reader bulk_update_suite_xls = null;
    public static Xls_Reader PagePerformace_suite_xls = null;
    public static Xls_Reader notification_alert_xls = null;
    public static Xls_Reader bulk_create_suite_xls = null;
    public static Xls_Reader bulk_action_suite_xls = null;
    public static Xls_Reader bulk_edit_suite_xls = null;
    public static Xls_Reader entity_show_suite_xls = null;
    public static Xls_Reader filters_suite_xls=null;
    public static Xls_Reader docusign_suite_xls=null;
    public static WebElement element = null;
    public static FluentWait<WebDriver> waitF = null;
    public static FirefoxBinary ffbinary;
    public static FirefoxProfile ffprofile;
    protected static DesiredCapabilities dCaps;
    //public static WebDriver driver;
    //public static PostgreSQLJDBC = new PostgreSQLJDBC();
    private static TestRail testRail;
    private static int projectId;
    private static int suiteId;
    public static Run run;
    public static Case testCase;
    public static int pid = 0;
    public static int sid = 0;
    public static Run rid = null;
    public static String runName = null;
    public static int implicitTimeOut = 30;
    public static List<Integer> caseIds = new ArrayList<Integer>();
    public static int getProjectId() {
        return projectId;
    }
    public static void setProjectId(int projectId) {
        TestBase.projectId = projectId;
    }
    public static int getSuiteId() {
        return suiteId;
    }
    public static void setSuiteId(int suiteId) {
        TestBase.suiteId = suiteId;
    }
    public static Run getRun() {
        return run;
    }
    public static void setRun(Run run) {
        TestBase.run = run;
    }
    private static WebDriver jsWaitDriver;
    private static WebDriverWait jsWait;
    private static JavascriptExecutor jsExec;
    public static String username = "kiran.malla";
    public static String accesskey = "897yrtqiRzpHL3JCj3A65jfayn48yKa2tqnvrslLhT0qkn4Lqf";
    public static RemoteWebDriver driver = null;
    public static String gridURL = "@hub.lambdatest.com/wd/hub";
    boolean status = false;

    public static void initialize() throws Exception {
        System.out.println("Calling Test Base");

        if (!isInitialized) {
            Logger = LogManager.getRootLogger();
            Logger.debug("Loading Property Files");

            CONFIG = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "//src//test//java//office//sirion//config/config.properties");
            CONFIG.load(ip);

            OR = new Properties();
            ip = new FileInputStream(System.getProperty("user.dir") + "//src//test//java//office//sirion//config/OR.properties");
            OR.load(ip);

            Logger.debug("Loaded Property Files Successfully");
            Logger.debug("Loading XLS Files");


            suiteXls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Suite.xlsx");
            sirion_admin_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Sirion Admin Suite.xlsx");
            client_setup_admin_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Client Setup Admin Suite.xlsx");
            user_admin_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Sirion User Admin Suite.xlsx");
            client_admin_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Client Admin Suite.xlsx");
            client_user_admin_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Client User Admin Suite.xlsx");
            client_workflow_configuration_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Client Workflow Configuration Suite.xlsx");
            client_ui_content_setup_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Client UI Content Setup Suite.xlsx");
            vendor_hierarchy_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Vendor Hierarchy Suite.xlsx");
            supplier_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Supplier Suite.xlsx");
            contract_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Contract Suite.xlsx");
            sl_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//SL Suite.xlsx");
            child_sl_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Child SL Suite.xlsx");
            obligation_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Obligation Suite.xlsx");
            child_obligation_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Child Obligation Suite.xlsx");
            governance_body_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//GB Suite.xlsx");
            child_gb_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Child GB Suite.xlsx");
            action_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Action Suite.xlsx");
            issue_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Issue Suite.xlsx");
            int_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Interpretation Suite.xlsx");
            cr_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//CR Suite.xlsx");
            dispute_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Dispute Suite.xlsx");
            clause_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Clause Suite.xlsx");
            contract_template_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Contract Template Suite.xlsx");
            cdr_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//CDR Suite.xlsx");
            po_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//PO Suite.xlsx");
            inv_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Invoice Suite.xlsx");
            wor_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//WOR Suite.xlsx");
            common_listing_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Common Listing Suite.xlsx");
            common_report_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Common Report Suite.xlsx");
            common_dashboard_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Common Dashboard Suite.xlsx");
            insights_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Insights Suite.xlsx");
            calendar_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Calendar Suite.xlsx");
            todo_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//ToDo Suite.xlsx");
            search_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Search Suite.xlsx");
            staging_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Staging Suite.xlsx");
            mass_email_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Mass Email Suite.xlsx");
            sso_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//SSO Suite.xlsx");
            bulkjobs_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//BulkJobs Suite.xlsx");
            manual_report_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Manual Report Suite.xlsx");
            my_profile_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//My Profile Suite.xlsx");
            entity_show_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Entity Show Suite.xlsx");
            notification_alert_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Notification Alert.xlsx");
            my_profile_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//My Profile.xlsx");
            bulk_create_suite_xls=new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Bulk Create Suite.xlsx");
            bulk_action_suite_xls=new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Bulk Action Suite.xlsx");
            bulk_edit_suite_xls=new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Bulk Edit Suite.xlsx");
            bulk_update_suite_xls=new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Bulk Update.xlsx");
            PagePerformace_suite_xls =new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Page Performance.xlsx");
            filters_suite_xls =new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Filters Suite.xlsx");
            docusign_suite_xls = new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//DocuSign Suite.xlsx");
            //new Xls_Reader(System.getProperty("user.dir") + "//src//test//java//office//sirion//xls//Manual Report Suite.xlsx");
            Logger.debug("Loaded XLS Files Successfully");
            isInitialized = true;
            //createTestRailInstance();
        }
    }
    
	/*
	 * public void test1() throws IOException { Document resultsPage =
	 * Jsoup.connect("").get(); resultsPage. }
	 */
    

    // Opening a Browser
 
   
	public static void openBrowser() {
        if (!isBrowserOpened) {
            String os = CONFIG.getProperty("os.name").toLowerCase();


            if (CONFIG.getProperty("browserType").equals("MOZILLA")) {
                //ffbinary = new FirefoxBinary(path);
                ffprofile = new FirefoxProfile();
              
              driver = new FirefoxDriver();
            } else if (CONFIG.getProperty("browserType").equals("IE")) {
                if (os.contains("win"))
                    System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "//IEDriverServer.exe");
                else if (os.contains("nux") || os.contains("nix"))
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//IEDriverServer");
                driver = new InternetExplorerDriver();
            } else if (CONFIG.getProperty("browserType").equals("CHROME")) {
                if (os.contains("win")) {
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    try {
                        driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
                    } catch (MalformedURLException e) {
                        System.out.println("Invalid grid URL");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    System.setProperty("webdriver.chrome.driver", "D:\\SirionSelenide\\Drivers\\chromedriver.exe");
                     ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    //chromeOptions.addArguments("--headless");

                    driver = new ChromeDriver(chromeOptions);
                    driver.manage().window().maximize();
                    driver.manage().deleteAllCookies();
                    JSWaiter jsWaiter = new JSWaiter();
                    jsWaiter.setDriver(driver);


                }
             else if (os.contains("nux") || os.contains("nix")) {
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//Drivers//chromedriver");
                driver = new ChromeDriver();
                }
            } else if (CONFIG.getProperty("browserType").equals("GECKO")) {
                if (os.contains("win"))
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//Drivers//geckodriver.exe");
                else if (os.contains("nux") || os.contains("nix"))
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//Drivers//geckodriver");
                driver = new FirefoxDriver();
            } else if (CONFIG.getProperty("browserType").equals("GHOST")) {
                dCaps = new DesiredCapabilities();
                dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, System.getProperty("user.dir") + "//phantomjs//bin//phantomjs.exe");
                dCaps.setJavascriptEnabled(true);
                dCaps.setCapability("takesScreenshot", false);
                driver = new PhantomJSDriver(dCaps);
            }

            isBrowserOpened = true;


        }
    }

    // Closing a Browser
    public static  void closeBrowser() {
        driver.close();
        isInitialized = false;
        isBrowserOpened = false;
        isSirionAdminLogin = false;
        isClientSetupAdminLogin = false;
        ispasswordpolicyclientAdminLogin=false;
        isClientAdminLogin = false;
        isEndUserLogin = false;
        isSirionUserAdminLogin = false;
    }

    // Gets an Object with the given Xpath Key
    public static  WebElement getObject(String xpathKey) {
        try {
            driver.findElement(By.xpath(OR.getProperty(xpathKey)));

        } catch (Throwable t) {
            Logger.debug("Cannot Find Object with Key -- " + xpathKey);
            return null;
        }
        return driver.findElement(By.xpath(OR.getProperty(xpathKey)));
    }

    // Login as Sirion Super Admin
    public static void sirionAdminLogin(String sirionAdminURL, String sirionAdminUsername, String sirionAdminPassword) {
        if (!isSirionAdminLogin) {
            driver.get(CONFIG.getProperty("sirionAdminURL"));
            getObject("username").sendKeys(CONFIG.getProperty("sirionAdminUsername"));
            getObject("password").sendKeys(CONFIG.getProperty("sirionAdminPassword"));
            getObject("login_button").click();

            Logger.debug(CONFIG.getProperty("sirionAdminUsername") + " logged in Successfully");
        }
        isSirionAdminLogin = true;
    }

    // Login as Sirion Client Setup Admin
    public static void clientSetupAdminLogin(String ClientSetupAdminURL, String ClientSetupAdminUserName, String ClientSetupAdminPassword) {
        if (!isClientSetupAdminLogin) {
            driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
            getObject("username").sendKeys(CONFIG.getProperty("ClientSetupAdminUserName"));
            getObject("password").sendKeys(CONFIG.getProperty("ClientSetupAdminPassword"));
            getObject("login_button").click();

            Logger.debug(CONFIG.getProperty("ClientSetupAdminUserName") + " logged in Successfully");
        }
        isClientSetupAdminLogin = true;
    }

    // Login as Sirion User Admin
    public static void sirionUserAdminLogin(String sirionUserAdminURL, String sirionUserAdminUsername, String sirionUserAdminPassword) {
        if (!isSirionUserAdminLogin) {
            driver.get(CONFIG.getProperty("sirionuserAdminURL"));
            getObject("username").sendKeys(CONFIG.getProperty("sirionuserAdminUsername"));
            getObject("password").sendKeys(CONFIG.getProperty("sirionuserAdminPassword"));
            getObject("login_button").click();

            Logger.debug(CONFIG.getProperty("sirionuserAdminUsername") + " logged in successfully");
        }
        isSirionUserAdminLogin = true;
    }

    // Login as Client Admin
    public static  void clientAdminLogin(String clientAdminURL, String clientAdminUsername, String clientAdminPassword) {
        if (!isClientAdminLogin) {
            driver.get(CONFIG.getProperty("clientAdminURL"));

            if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                driver.findElement(By.name("username_login")).sendKeys(CONFIG.getProperty("clientAdminUsername"));
                driver.findElement(By.name("password_login")).sendKeys(CONFIG.getProperty("clientAdminPassword"));
                driver.findElement(By.id("loginButton")).click();
            } else {
                getObject("username").sendKeys(CONFIG.getProperty("clientAdminUsername"));
                getObject("password").sendKeys(CONFIG.getProperty("clientAdminPassword"));
                getObject("login_button").click();
            }

            Logger.debug(CONFIG.getProperty("clientAdminUsername") + " logged in Successfully");
        }
        isClientAdminLogin = true;
    }

    // Login as Client Admin for Password Policy Testing
    public static void passwordpolicyclientAdminLogin(String passwordpolicyclientadminURL, String passwordpolicyclientadminUsername, String passwordpolicyclientadminPassword) {
        if (!ispasswordpolicyclientAdminLogin) {
            driver.get(CONFIG.getProperty("passwordpolicyclientadminURL"));

            if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                getObject("sso_username").clear();
                getObject("sso_username").sendKeys(CONFIG.getProperty("passwordpolicyclientadminUsername"));
                getObject("sso_password").clear();
                getObject("sso_password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                getObject("sso_login_button").click();
            } else {
                getObject("username").clear();
                getObject("username").sendKeys(CONFIG.getProperty("passwordpolicyclientadminUsername"));
                getObject("password").clear();
                getObject("password").sendKeys(CONFIG.getProperty("passwordpolicyclientadminPassword"));
                getObject("login_button").click();
            }

            Logger.debug(CONFIG.getProperty("passwordpolicyclientadminUsername") + " logged in Successfully");
        }
        ispasswordpolicyclientAdminLogin = true;
    }

    // Login as Client User
    public static void endUserLogin(String endUserURL, String endUserUsername, String endUserPassword) {
        if (!isEndUserLogin) {
            driver.get(CONFIG.getProperty("endUserURL"));

            if (CONFIG.getProperty("clientLoginType").equalsIgnoreCase("SSO")) {
                locateElementBy("sso_username").sendKeys(CONFIG.getProperty("endUserUsername"));
                locateElementBy("sso_password").sendKeys(CONFIG.getProperty("endUserPassword"));
                locateElementBy("sso_login_button").click();
            } else {
                locateElementBy("username").sendKeys(CONFIG.getProperty("endUserUsername"));
                locateElementBy("password").sendKeys(CONFIG.getProperty("endUserPassword"));
                locateElementBy("login_button").click();
            }

            Logger.debug(CONFIG.getProperty("endUserUsername") + " logged in Successfully");
        }
        isEndUserLogin = true;
    }

    // Converts String To Integer To String
    public static  String convertStringToInteger(String data) {
        try {

            String temp_data = data.replace(",", "");
            Double double_data_temp = Double.parseDouble(temp_data);
            int int_data_temp = double_data_temp.intValue();
            String string_data_temp = Integer.toString(int_data_temp);

            return string_data_temp.replaceAll(",", "");
        } catch (Throwable t) {
            Logger.debug("Unable to Convert given String -- " + data);
            return null;
        }
    }

    public static  Integer getIntegerFromString(String data) {
        try {

            String temp_data = data.replace(",", "");
            Double double_data_temp = Double.parseDouble(temp_data);
            int int_data_temp = double_data_temp.intValue();

            return int_data_temp;
        } catch (Throwable t) {
            Logger.debug("Unable to Convert given String -- " + data);
            return null;
        }
    }

    public static boolean Assertion(String Actualvalue, String Expectedvalue) {

        try {
            AssertJUnit.assertEquals(Actualvalue, Expectedvalue, "Actual: " + Actualvalue + " & Expected: " + Expectedvalue + "does not Matched");
        } catch (AssertionError err) {
        }
        return true;
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 3) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    // Selects Future Date
    public static void selectDate(String dateValue) {
        try {
            String[] entityDate = dateValue.split("-");
            String entityMonth = driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-month")).getText();

            String nextElementClassAttribute = driver.findElement(By.cssSelector("a.ui-datepicker-next.ui-corner-all")).getAttribute("class");
            String previousElementClassAttribute = driver.findElement(By.cssSelector("a.ui-datepicker-prev.ui-corner-all")).getAttribute("class");
            WebElement buttonElement = null;

            while (!entityMonth.equalsIgnoreCase(entityDate[0])) {
                if (!nextElementClassAttribute.contains("ui-state-disabled"))
                    buttonElement = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span"));
                else if (!previousElementClassAttribute.contains("ui-state-disabled"))
                    buttonElement = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[1]/span"));

                buttonElement.click();
                entityMonth = driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-month")).getText();
            }

            new Select(driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-year"))).selectByVisibleText(entityDate[2]);

            driver.findElement(By.linkText(entityDate[1])).click();
        } catch (Throwable t) {
            Logger.debug("Unable to Select with the given Value -- " + dateValue);
        }
    }

    public static void selectDate_Enhanced(String dateValue, String monthValue, int yearValue) {
        try {

            String entityMonth = driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-month")).getText();

            String nextElementClassAttribute = driver.findElement(By.cssSelector("a.ui-datepicker-next.ui-corner-all")).getAttribute("class");
            String previousElementClassAttribute = driver.findElement(By.cssSelector("a.ui-datepicker-prev.ui-corner-all")).getAttribute("class");
            WebElement buttonElement = null;

            while (!entityMonth.equalsIgnoreCase(monthValue)) {
                if (!nextElementClassAttribute.contains("ui-state-disabled"))
                    buttonElement = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[2]/span"));
                else if (!previousElementClassAttribute.contains("ui-state-disabled"))
                    buttonElement = driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div/a[1]/span"));

                buttonElement.click();
                entityMonth = driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-month")).getText();
            }

            new Select(driver.findElement(By.className("ui-datepicker-title")).findElement(By.className("ui-datepicker-year"))).selectByVisibleText(String.valueOf(yearValue));

            driver.findElement(By.linkText(dateValue)).click();
        } catch (Throwable t) {
            Logger.debug("Unable to Select with the given Value -- " + dateValue + "/" + monthValue + "/" + yearValue);
        }
    }


    // Return Element with the given Locator
    public static WebElement locateElementBy(String locatorKey) {
        WebElement element = null;

        JSWaiter jsWaiter = new JSWaiter();


        String[] str = OR.getProperty(locatorKey).split("-->");
        String locatorType = str[0];
        String locatorValue = str[1];

        try {
            if (locatorType.trim().equalsIgnoreCase("id")) {
                jsWaiter.waitAllRequest();
                element = driver.findElement(By.id(locatorValue.trim()));
            } else if (locatorType.trim().equalsIgnoreCase("className")) {
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.className(locatorValue.trim()));
            } else if (locatorType.trim().equalsIgnoreCase("css")) {
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.cssSelector(locatorValue.trim()));
            } else if (locatorType.trim().equalsIgnoreCase("link")) {
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.linkText(locatorValue.trim()));
            } else if (locatorType.trim().equalsIgnoreCase("name")) {
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.name(locatorValue.trim()));
            } else if (locatorType.trim().equalsIgnoreCase("tagName")){
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.tagName(locatorValue.trim()));
            }else if (locatorType.trim().equalsIgnoreCase("xpath")){
                jsWaiter.waitAllRequest();
            	 element = driver.findElement(By.xpath(locatorValue.trim()));
            }else {
                Logger.error("There is no such locator Type as -- " + locatorType.trim());
            }
            return element;
        } catch (Throwable t) {
            Logger.debug("Cannot Find Object with Key -- " + locatorKey.trim());
            return null;
        }
    }

    public static void fluentWaitMethod(WebElement Element){

        waitForPageSpinnerToDisappear();

        FluentWait<RemoteWebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        Function<WebDriver, Boolean> function = arg0 -> {
            WebElement element = Element;
            if (element.isDisplayed()) {
                return true;
            }
            return false;
        };

        wait.until(function);
    }

    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }





    // Input Entity Field Value
    public static void addFieldValue(String locatorKey, String data) throws InterruptedException {
        Actions actions = new Actions(driver);
        if (data.equalsIgnoreCase(""))
            return;

        WebElement element = locateElementBy(locatorKey);

        if (element == null)
            return;

        String[] str = OR.getProperty(locatorKey).split("-->");
        String locatorType = str[0];
        String elementType = str[2];

        if (locatorType.equalsIgnoreCase("id")) {
            if (elementType.equalsIgnoreCase("textbox") || elementType.equalsIgnoreCase("checkbox") || elementType.equalsIgnoreCase("numeric") || elementType.equalsIgnoreCase("date")) {
                if (element.findElements(By.tagName("input")).size() != 0) {
                    if (elementType.equalsIgnoreCase("textbox")) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                        element.findElement(By.tagName("input")).clear();
                        element.findElement(By.tagName("input")).sendKeys(data.trim());
                    } else if (elementType.equalsIgnoreCase("checkbox")) {
                        if (data.equalsIgnoreCase("Yes")) {
                            if (!element.findElement(By.tagName("input")).isSelected())
                                element.findElement(By.tagName("input")).click();
                        } else if (data.equalsIgnoreCase("No")) {
                            if (element.findElement(By.tagName("input")).isSelected())
                                element.findElement(By.tagName("input")).click();
                        }
                    } else if (elementType.equalsIgnoreCase("numeric")) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                        element.findElement(By.tagName("input")).clear();
                        element.findElement(By.tagName("input")).sendKeys(convertStringToInteger(data).trim());
                    } else if (elementType.equalsIgnoreCase("date")) {
                        element.findElement(By.tagName("input")).click();

                        selectDate(data);
                    }
                } else {
                    Logger.debug("Cannot Find Object with Key -- " + locatorKey.trim());
                    return;
                }
            } else if (elementType.equalsIgnoreCase("textarea")) {
                if (element.findElements(By.tagName("textarea")).size() != 0) {
                    element.findElement(By.tagName("textarea")).clear();
                    element.findElement(By.tagName("textarea")).sendKeys(data.trim());
                } else {
                    Logger.debug("Cannot Find Object with Key -- " + locatorKey.trim());
                    return;
                }
            } else if (elementType.equalsIgnoreCase("dropdown")) {
                if (element.findElements(By.tagName("select")).size() != 0) {
                    if (new Select(element.findElement(By.tagName("select"))).isMultiple()) {
                        for (String entityData : data.split(";")) {
                            new Select(element.findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
                        }
                    } else {
                        ((JavascriptExecutor)driver).executeScript("return window.title;");
                        Point hoverItem =element.getLocation();
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,"+(hoverItem.getY())+");");
                        Thread.sleep(500);
                        new Select(element.findElement(By.tagName("select"))).selectByVisibleText(data.trim());
                    }
                } else {
                    Logger.debug("Cannot Find Object with Key -- " + locatorKey.trim());
                    return;
                }
            }
        } else if (locatorType.equalsIgnoreCase("xpath")) {
            if (elementType.equalsIgnoreCase("textbox")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                element.clear();
                element.sendKeys(data.trim());
            } else if (elementType.equalsIgnoreCase("textarea")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                element.clear();
                element.sendKeys(data.trim());
            } else if (elementType.equalsIgnoreCase("dropdown")) {
                if (new Select(element).isMultiple()) {
                    for (String entityData : data.split(";")) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                        new Select(element).selectByVisibleText(entityData.trim());
                    }
                } else
                    new Select(element).selectByVisibleText(data.trim());
            } else if (elementType.equalsIgnoreCase("checkbox")) {
                if (data.equalsIgnoreCase("Yes")) {
                    if (!element.isSelected())
                        element.click();
                } else if (data.equalsIgnoreCase("No")) {
                    if (element.isSelected())
                        element.click();
                }
            } else if (elementType.equalsIgnoreCase("numeric")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                element.clear();
                element.sendKeys(convertStringToInteger(data).trim());
            } else if (elementType.equalsIgnoreCase("date")) {
                element.click();

                selectDate(data);
            }
        } else {
            Logger.debug("Cannot Find Object with Key -- " + locatorKey.trim());
            return;
        }
    }
    
    public static WebElement pages (String quicklink) {
    	JSWaiter.waitUntilAngularReady();
    	 WebElement elem =  locateElementBy(quicklink);
    	 return elem;
    }
    
    
    // Take Screenshot on Failure
    public static void takeScreenShotOnFailure(int i, String className) throws IOException {
        if (i == ITestResult.FAILURE) {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("Screenshots//FAILURES//" + new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString()
                    + "//" + className + "//" + className + "_"
                    + new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()).toString() + ".png"));
        }
    }

    // Take Screenshot on As
    public static void takeScreenShotAs(String className, String data) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("Screenshots//INFORMATION//" + new SimpleDateFormat("dd_MMM_yyyy").format(Calendar.getInstance().getTime()).toString()
                + "//" + className + "//" + data + "_"
                + new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()).toString() + ".png"));
    }

    // Get the Mandatory Field Validation
    public static boolean fieldValidationMandatory(String locatorKey) {
        WebElement errorElement = null;
        String entityAttributeSplitString;

        WebElement element = locateElementBy(locatorKey);
        if (element != null) {
            String[] str = OR.getProperty(locatorKey).split("-->");
            String elementType = str[2];

            if (elementType.equalsIgnoreCase("textbox") || elementType.equalsIgnoreCase("numeric") || elementType.equalsIgnoreCase("date")) {
                if (element.findElements(By.tagName("input")).size() != 0)
                    errorElement = element.findElement(By.tagName("input"));
            } else if (elementType.equalsIgnoreCase("textarea")) {
                if (element.findElements(By.tagName("textarea")).size() != 0)
                    errorElement = element.findElement(By.tagName("textarea"));
            } else if (elementType.equalsIgnoreCase("dropdown")) {
                if (element.findElements(By.tagName("select")).size() != 0)
                    errorElement = element.findElement(By.tagName("select"));
            }

            try {
                String entityMandatoryAttribute = errorElement.getAttribute("ismandatory");

                if (entityMandatoryAttribute != null) {
                    String entityErrorAttributeValue = errorElement.getAttribute("class");
                    String[] entityAttributeSplit = entityErrorAttributeValue.split(" ");
                    if (!elementType.equalsIgnoreCase("date"))
                        entityAttributeSplitString = entityAttributeSplit[3];
                    else
                        entityAttributeSplitString = entityAttributeSplit[4];

                    Assertion(entityAttributeSplitString, "errorClass");

                    return true;
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    //Comparison of 2 integers
    public static void compareIntegerVals(int x, int y) {

        // compares two Integer objects numerically
        Integer obj1 = new Integer(x);
        Integer obj2 = new Integer(y);
        if (obj1 != null && obj2 != null) {
            int retval = obj1.compareTo(obj2);
            if (retval > 0) {
                System.out.println(x + "is greater than " + y);
            } else if (retval < 0) {
                System.out.println(y + " is greater than " + x);
            } else {
                System.out.println(x + " and " + y + " are equal");
            }
        } else {
            System.out.println(x + " or " + y + " is null, cannot compare");
        }
    }

    //Auto-complete common method
    public static void selectOptionWithText(String textToSelect) {
        try {
            WebElement autoOptions = driver.findElement(By.xpath("//span[@class='select2-dropdown select2-dropdown--below']//span[@class='select2-results']/ul[@class='select2-results__options']"));
            waitF.until(ExpectedConditions.visibilityOf(autoOptions));

            List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
            for(WebElement option : optionsToSelect){
                if(option.getText().equals(textToSelect)) {
                    System.out.println("Trying to select: "+textToSelect);
                    option.click();
                    break;
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println(e.getStackTrace());
        }
        catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void verifyCommunicationTab(String entityComments, String entityUploadFile) throws InterruptedException {
        WebElement elementDetailed = driver.findElement(By.xpath("//div[@class='detailedContent ng-scope']"));
        WebElement elementOddRowSelected = driver.findElement(By.cssSelector("tr.odd.selectedRow"));

        elementOddRowSelected.click();

        Assertion(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_status_name")).getText(), "Comment/Attachment");

        if (!entityComments.equalsIgnoreCase("")) {
            AssertJUnit.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText().toLowerCase(), entityComments.trim().toLowerCase());

            driver.switchTo().frame(elementDetailed.findElement(By.className("detailComment")).findElement(By.tagName("iframe")));
            //
            AssertJUnit.assertEquals(driver.findElement(By.tagName("body")).getText().toLowerCase(), entityComments.trim().toLowerCase());

            driver.switchTo().defaultContent();
        }

        AssertJUnit.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText().toLowerCase(), CONFIG.getProperty("endUserFullName").toLowerCase());
        AssertJUnit.assertEquals(elementDetailed.findElement(By.className("detailUsername")).getText().toLowerCase(), "completed by: " + CONFIG.getProperty("endUserFullName").toLowerCase());

        if (!entityUploadFile.equalsIgnoreCase("")) {
            AssertJUnit.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText().toLowerCase(), entityUploadFile.toLowerCase());

            File file = new File(System.getProperty("user.home") + "//Downloads//" + entityUploadFile);

            if (file.exists())
                file.delete();

            elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).findElement(By.tagName("a")).click();
            //

            if (file.exists())
                Logger.debug("Entity Communication File Download PASSED");
            else
                AssertJUnit.fail("Entity Communication File Download FAILED");

            AssertJUnit.assertEquals(elementDetailed.findElement(By.className("detailAttachment")).findElement(By.tagName("a")).getText().toLowerCase(), entityUploadFile.toLowerCase());

            if (file.exists())
                file.delete();

            elementDetailed.findElement(By.className("detailAttachment")).findElement(By.tagName("a")).click();
            //

            if (file.exists())
                Logger.debug("Entity Communication File Download from Detailed Tab PASSED");
            else
                AssertJUnit.fail("Entity Communication File Download from Detailed Tab FAILED");
        }
    }

    public static void createTestRailInstance() throws ParseException {
        if (testRail == null) {
            testRail = TestRail.builder("http://testrail.sirionlabs.office", "naveen.gupta@sirionlabs.com", "admin123").build();
        }

        setProjectSuiteId();
    }

          public static void setProjectSuiteId() throws ParseException {
                        try{
                            Projects projects = testRail.projects();
                            java.util.List<Project> projectList = projects.list().execute();
                            
                            for(Project project : projectList){
                                if(project.getName().equals("Sirion")){
                                    pid = project.getId();
                                    setProjectId(pid);
                                    break;
                                }
            }

                            if(pid !=0){
                            	if(CONFIG.getProperty("Suite_Creation").equalsIgnoreCase("Yes")) {
                                testRail.suites().add(pid, new Suite().setName(CONFIG.getProperty("Suite_Name"))).execute();
                                java.util.List<Suite> suiteList = testRail.suites().list(pid).execute();
                                for(Suite s : suiteList){
                                    String sName = s.getName();
                                    if(sName.equals(CONFIG.getProperty("Suite_Name")))
                                    {
                                        sid = s.getId();
                                        setSuiteId(sid);
                                        System.out.println(sid);
                                    }
                                }
                            	}else {
                            		 java.util.List<Suite> suiteList = testRail.suites().list(pid).execute();
                                     for(Suite s : suiteList){
                                         String sName = s.getName();
                                         if(sName.equals(CONFIG.getProperty("Suite_Name")))
                                         {
                                             sid = s.getId();
                                             setSuiteId(sid);
                                             System.out.println(sid);
                                         }
                                     }
                            	}
                            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        createRun();


    }



    public static void createRun() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        String dateString = format.format(date);
        runName = "Sanity Automation " + dateString;
        if(run==null) {
        	try{
                run = new Run();
                run = testRail.runs().add(getProjectId(), run.setSuiteId(sid).setName(runName).setIncludeAll(false)).execute();
                setRun(run);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }else {
        	try{
        	java.util.List<Run> runlist = testRail.runs().list(pid).execute();
        	for(Run r : runlist) {
        		String rName = r.getName();
        		if(rName.equalsIgnoreCase(runName)) {
        			rid.setId(r.getId());
        			setRun(rid);
        			
        		}
        	}
        	}
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }

    /*Update Run: To add test case ids at run time
    * Owner: Naveen Kr. Gupta
    * Date: 18/01/2019
    */
    public static void updateRun(Object caseIdString,String comment,String statusId){
        try{
            if(null != caseIdString){
                Integer caseId = Integer.parseInt(caseIdString.toString());

                caseIds.add(caseId);
                getRun().setCaseIds(caseIds);
                testRail.runs().update(getRun()).execute().toString();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        addResult(comment,testCase);
        List<CaseField> customCaseFields = testRail.caseFields().list().execute();
        Case testCase = testRail.cases().get(Integer.parseInt(caseIdString.toString()), customCaseFields).execute();
        addStatusForCase(statusId,testCase);

    }

    /*Add Result for test case in current run
    * Owner: Naveen Kr. Gupta
    * Date: 18/01/2019
    */

    public static void addResult(String comment, Case testCase){
        try{
            if(null != testRail ){
                List<ResultField> customResultFields = testRail.resultFields().list().execute();
                testRail.results().addForCase(getRun().getId(), testCase.getId(), new Result().setComment(comment),customResultFields).execute();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /*Add final result for a test case i.e. status like pass/fail/skipped, etc
    * Owner: Naveen Kr. Gupta
    * Date 18/01/2019
    */
    public static void addStatusForCase(String statusId, Case testCase){
        if(statusId.equalsIgnoreCase("Pass")) {
            try {
                List<ResultField> customResultFields = testRail.resultFields().list().execute();
                testRail.results().addForCase(getRun().getId(), testCase.getId(), new Result().setStatusId(1), customResultFields).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(statusId.equalsIgnoreCase("Fail")) {
            try {
                List<ResultField> customResultFields = testRail.resultFields().list().execute();
                testRail.results().addForCase(getRun().getId(), testCase.getId(), new Result().setStatusId(5),customResultFields).execute();
                } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Finally Close run
    * Owner: Naveen Kr. Gupta
    */
	/*
	 * public static void closeRun(){ try{
	 * testRail.runs().close(getRun().getId()).execute(); } catch(Exception e){
	 * e.printStackTrace(); } }
	 */
    
    public static void extractStackTrace(Exception exception, Logger _logger) {
    	 StackTraceElement[] elements = exception.getStackTrace();
    	 String trace = null;
    	 for (StackTraceElement element: elements) {
    	  trace = (trace == null) ? element.toString() : trace + ",\n" + element.toString();
    	 }
    	 _logger.error(trace);
    	}
    
    public static WebElement clickBy (By by) {
        //Asynchronous wait
        JSWaiter.waitUntilAngularReady();
        //Explicit wait
        element = waitF.until(ExpectedConditions.elementToBeClickable(by));
        return element;
        
    }

    public static WebElement clickWebElement (WebElement element) {
        //Asynchronous wait
        JSWaiter.waitUntilAngularReady();
        //Explicit wait
       return  waitF.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    public static void handlePopup (String showPageIDPath)  {
    	String entityID = null;
        if (driver.findElements(By.className("success-icon")).size()!=0) {
		entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
		Logger.debug("Entity Cloned successfully with Entity ID " + entityID);
            driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
		
		String entityShowPageID = locateElementBy(showPageIDPath).getText();

		 Assert.assertEquals(entityShowPageID, entityID);
		 Logger.debug("Entity ID on show page has been verified");
    
        }
    }

    /**
     * Wait for page spinner to disappear
     */
    public static boolean waitForPageSpinnerToDisappear() {
        boolean bFlag = false;
        try {
            waitForPageSync();
            nullifyImplicitWait();
            Thread.sleep(1000);
            if ((driver.findElements(By.xpath("//*[@id=overlay]"))).size()!=0) {
                waitF = new FluentWait<>(driver);
                waitF.withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofMillis(100)).ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class).until(ExpectedConditions
                        .invisibilityOf(driver.findElement(By.xpath("//*[@id=overlay]"))));
            }
            bFlag = true;
        } catch (Exception e) {
            Logger.debug("Unable to Find the element due to the following : " + e.getMessage());
        }
        setImplicitWait();
        return bFlag;
    }

    public static boolean waitForPageSync() {
        boolean bFlag = false;
        nullifyImplicitWait();
        waitForAngular();
        try {
            waitF = new FluentWait<>(driver);
            if (driver.getCurrentUrl().contains("http")) {
                waitF.withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofMillis(100))
                        .until((ExpectedCondition<Boolean>) webDriver -> (Boolean) executeJavaScript("return window.jQuery != undefined && jQuery.active === 0")
                        );
                bFlag = true;
            }
        } catch (Exception e) {
            Logger.debug("Wait for Page Load Failed due to :" + e.getMessage());
        }
        setImplicitWait();
        return bFlag;
    }

    public static void nullifyImplicitWait() {
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        } catch (Exception e) {
            Logger.debug("Unable to set implicit time driver value " + driver);
        }
    }

    public static void setImplicitWait() {
        try {
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            Logger.debug("Unable to set implicit time driver value " + driver + " Time Value " + implicitTimeOut);
        }
    }

    public static String waitForAngular(){
        try{
            driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
            executeJavaScript("var callback=arguments[arguments.length-1];"+" if (!window.angular) { throw new Error('angular could not be found on the window');}if (angular.getTestability) {angular.getTestability(angular.element(document.body)).whenStable(callback); } else { if (!angular.element(angular.element(document.body)).injector()) { throw new Error('root element (' + 'body' + ') has no injector.' +' this may mean it is not inside ng-app.'); }}"+"angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
            return"Pass:";
        } catch(Throwable e){
            System.err.println(e.getMessage());
            return"Fail:"+e.getMessage();
        }
    }

    private static boolean executeJavaScript(String s) {
        jsWaitDriver = driver;
        jsWait = new WebDriverWait(jsWaitDriver, 10);
        jsExec = (JavascriptExecutor) jsWaitDriver;
        return true;
    }

}