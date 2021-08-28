package office.sirion.suite.common.listing;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class CommonListingOpen extends TestSuiteBase {
   static String result = null;
   static String runmodes[] = null;
    static int count = -1;
    static boolean isTestPass = true;
    String testCaseID;

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(common_listing_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(common_listing_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void CommonListingOpenTest(String entityName) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data -- " + entityName + " set to NO " + count);

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Listing Page Open of -- " + entityName);

        driver.get(CONFIG.getProperty("endUserURL"));

        if (entityName.equalsIgnoreCase("Vendor Hierarchy")) {
            //
            fluentWaitMethod(locateElementBy("vh_quick_link"));

            locateElementBy("vh_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Suppliers")) {
            //
            fluentWaitMethod(locateElementBy("suppliers_quick_link"));

            locateElementBy("suppliers_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Contracts")) {
            //
            fluentWaitMethod(locateElementBy("contracts_quick_link"));

            locateElementBy("contracts_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Service Levels")) {
            //
            fluentWaitMethod(locateElementBy("sl_quick_link"));

            locateElementBy("sl_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Child Service Levels")) {
            //
            fluentWaitMethod(locateElementBy("sl_quick_link"));

            locateElementBy("sl_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.linkText("VIEW CHILD SERVICE LEVELS")));

            driver.findElement(By.linkText("VIEW CHILD SERVICE LEVELS")).click();
            //
        } else if (entityName.equalsIgnoreCase("Obligations")) {
            //
            fluentWaitMethod(locateElementBy("ob_quick_link"));

            locateElementBy("ob_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.linkText("VIEW OBLIGATIONS")));

            driver.findElement(By.linkText("VIEW OBLIGATIONS")).click();
            //
        } else if (entityName.equalsIgnoreCase("Child Obligations")) {
            //
            fluentWaitMethod(locateElementBy("ob_quick_link"));

            locateElementBy("ob_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Governance Body")) {
            //
            fluentWaitMethod(locateElementBy("gb_quick_link"));

            locateElementBy("gb_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

            driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();
            //
        } else if (entityName.equalsIgnoreCase("Governance Body Meeting")) {
            //
            fluentWaitMethod(locateElementBy("gb_quick_link"));

            locateElementBy("gb_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Actions")) {
            //
            fluentWaitMethod(locateElementBy("actions_quick_link"));

            locateElementBy("actions_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Issues")) {
            //
            fluentWaitMethod(locateElementBy("issues_quick_link"));

            locateElementBy("issues_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Change Requests")) {
            //
            fluentWaitMethod(locateElementBy("cr_quick_link"));

            locateElementBy("cr_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Interpretations")) {
            //
            fluentWaitMethod(locateElementBy("ip_quick_link"));

            locateElementBy("ip_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Disputes")) {
            //
            fluentWaitMethod(locateElementBy("disputes_quick_link"));

            locateElementBy("disputes_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Invoices")) {
            //
            fluentWaitMethod(locateElementBy("inv_quick_link"));

            locateElementBy("inv_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Purchase Order")) {
            //
            fluentWaitMethod(locateElementBy("po_quick_link"));

            locateElementBy("po_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Work Order Requests")) {
            //
            fluentWaitMethod(locateElementBy("wor_quick_link"));

            locateElementBy("wor_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Contract Draft Request")) {
            //
            fluentWaitMethod(locateElementBy("cdr_quick_link"));

            locateElementBy("cdr_quick_link").click();
            //
        } else if (entityName.equalsIgnoreCase("Clause")) {
            //
            fluentWaitMethod(locateElementBy("cdr_quick_link"));

            locateElementBy("cdr_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
            new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();

            driver.findElement(By.linkText("Clauses")).click();
            //
            driver.findElement(By.xpath(".//*[@id='listViewTypeElementID']/span[2]")).click();

            driver.findElement(By.xpath(".//*[@id='listViewTypeElementID']/div/ul/li[1]")).click();
            //
        } else if (entityName.equalsIgnoreCase("Definition")) {
            //
            fluentWaitMethod(locateElementBy("cdr_quick_link"));

            locateElementBy("cdr_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
            new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();

            driver.findElement(By.linkText("Definitions")).click();
            //
            driver.findElement(By.xpath(".//*[@id='listViewTypeElementID']/span[2]")).click();

            driver.findElement(By.xpath(".//*[@id='listViewTypeElementID']/div/ul/li[1]")).click();
            //
        } else if (entityName.equalsIgnoreCase("Contract Template")) {
            //
            fluentWaitMethod(locateElementBy("cdr_quick_link"));

            locateElementBy("cdr_quick_link").click();
            //
            fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));
            new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();

            driver.findElement(By.linkText("Contract Templates")).click();
            //
        }

        driver.findElement(By.className("dataTables_scrollBody"));
        Logger.debug("Execution of Listing Page Open for -- " + entityName + " is = TRUE");

        String listingNameShowPage = driver.findElement(By.cssSelector("span.name.ng-scope")).getText();
        if (CommonTestFiltering.checkTableDataPresence()) {
            Logger.info("For " + entityName + " -- Listing there is No Corresponding Data Available");

            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }

        Thread.sleep(15000);
        String[] downloadStyleAttribute = driver.findElement(By.id("exportXLdownload")).getAttribute("style").split(Pattern.quote(": "));
        String[] downloadDisplayAttribute = null;

        if ("Clause".equalsIgnoreCase(entityName) || "Contract Draft Request".equalsIgnoreCase(entityName)
                || "Definition".equalsIgnoreCase(entityName)) {
            downloadDisplayAttribute = downloadStyleAttribute[2].split(Pattern.quote(";"));
        } else {
            downloadDisplayAttribute = downloadStyleAttribute[3].split(Pattern.quote(";"));
        }
        if ((downloadDisplayAttribute[0].trim().equalsIgnoreCase("list-item") || downloadDisplayAttribute[0].trim().equalsIgnoreCase("9"))) {
            File downloadedFileSrc = new File(System.getProperty("user.home") + "\\Downloads\\" + listingNameShowPage + ".xlsx");

            if (downloadedFileSrc.exists())
                downloadedFileSrc.delete();

            new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload"))).clickAndHold().build().perform();
            driver.findElement(By.id("selectedColumnsDownload")).click();
            //

            if (driver.findElements(By.className("alertdialog-icon")).size() != 0)
                driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

            if (downloadedFileSrc.exists()) {
                File downloadedSelectColumnFolderDest = new File(System.getProperty("user.dir") + "//Downloaded Data//" + new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Listing//Selected Columns");

                File downloadedSelectColumnFileDest = new File(downloadedSelectColumnFolderDest + "//" + listingNameShowPage + ".xlsx");

                if (downloadedSelectColumnFileDest.exists())
                    downloadedSelectColumnFileDest.delete();

                try {
                    FileUtils.moveFile(downloadedFileSrc, downloadedSelectColumnFileDest);

                    FileInputStream downloadedSelectColumnFileInputStream = new FileInputStream(downloadedSelectColumnFileDest);
                    @SuppressWarnings("resource")
                    XSSFWorkbook downloadedSelectColumnWorkbook = new XSSFWorkbook(downloadedSelectColumnFileInputStream);

                    Assert.assertEquals(downloadedSelectColumnWorkbook.getSheetName(0), "Data");

                    downloadedSelectColumnFileInputStream.close();
                    Logger.debug("Execution of Listing Page Selected Column Download for -- " + entityName + " is = TRUE");
                } catch (Exception e) {
                    Logger.debug("Execution of Listing Page Selected Column Download for -- " + entityName + " is = FAIL");
                    Logger.error(e.getMessage());
                }
            } else
                Logger.debug("Execution of Listing Page Selected Column Download for -- " + entityName + " is = FAIL");

            if (downloadedFileSrc.exists())
                downloadedFileSrc.delete();

            new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload"))).clickAndHold().build().perform();
            driver.findElement(By.id("exportXL")).click();
            //

            if (driver.findElements(By.className("alertdialog-icon")).size() != 0)
                driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

            if (downloadedFileSrc.exists()) {
                File downloadedAllColumnFolderDest = new File(System.getProperty("user.dir") + "//Downloaded Data//" + new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Listing//All Columns");

                File downloadedAllColumnFileDest = new File(downloadedAllColumnFolderDest + "//" + listingNameShowPage + ".xlsx");

                if (downloadedAllColumnFileDest.exists())
                    downloadedAllColumnFileDest.delete();

                try {
                    FileUtils.moveFile(downloadedFileSrc, downloadedAllColumnFileDest);

                    FileInputStream downloadedAllColumnFileInputStream = new FileInputStream(downloadedAllColumnFileDest);
                    @SuppressWarnings("resource")
                    XSSFWorkbook downloadedAllColumnWorkbook = new XSSFWorkbook(downloadedAllColumnFileInputStream);

                    Assert.assertEquals(downloadedAllColumnWorkbook.getSheetName(0), "Data");

                    downloadedAllColumnFileInputStream.close();
                    Logger.debug("Execution of Listing Page All Column Download for -- " + entityName + " is = TRUE");
                } catch (Exception e) {
                    Logger.debug("Execution of Listing Page All Column Download for -- " + entityName + " is = FAIL");
                    Logger.error(e.getMessage());
                }
            } else
                Logger.debug("Execution of Listing Page All Column Download for -- " + entityName + " is = FAIL");
        }

        Logger.debug("Execution of Listing Page Open and Download for -- " + entityName + " is = COMPLETED");
        driver.get(CONFIG.getProperty("endUserURL"));
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP)
            TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");

        else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(common_listing_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
            result = "Pass";
        }
        try {
            for (int i = 2; i <= common_listing_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = common_listing_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                if (!testCaseID.equalsIgnoreCase("")) {
                    updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
                }
            }
        }catch(Exception e){
            Logger.debug(e);
        }
    }

    @AfterTest
    public void reportTestResult() {
        if (isTestPass)
            TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(common_listing_suite_xls, "Test Cases", TestUtil.getRowNum(common_listing_suite_xls, this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(common_listing_suite_xls, this.getClass().getSimpleName());
    }
}