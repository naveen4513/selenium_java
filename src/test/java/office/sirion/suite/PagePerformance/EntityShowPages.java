package office.sirion.suite.PagePerformance;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class EntityShowPages extends TestSuiteBase {
    String result = null;
    String runmodes[] = null;
    static int count = -1;
    static boolean isTestPass = true;
    private String testCaseID;

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(obligation_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(PagePerformace_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void EntityShowPagesTest (String entityListing) throws InterruptedException, TestLinkAPIException {
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

        this.testCaseID = testCaseID;

        openBrowser();

        if(entityListing.equalsIgnoreCase("Login Page")){
            endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

            Logger.debug("Executing Test Case of capturing page load time of Login Page");

            driver.manage().timeouts().pageLoadTimeout(Long.parseLong(CONFIG.getProperty("timeout")), TimeUnit.SECONDS);

            try {
                driver.get(CONFIG.getProperty("endUserURL"));
                // verify element verification driver.findElementByXpath(verificationField);
            } catch (TimeoutException e) {
                System.out.println("Login Page did not load within" +CONFIG.getProperty("timeout")+ "seconds!");
                driver.get(CONFIG.getProperty("endUserURL"));

            }
        }

    }
    }
