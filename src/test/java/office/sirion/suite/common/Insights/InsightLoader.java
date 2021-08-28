package office.sirion.suite.common.Insights;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;

import testlink.api.java.client.TestLinkAPIResults;

public class InsightLoader extends TestSuiteBase {
    static int                count                    = -1;
    static boolean            isTestPass               = true;
    static LinkedList<String> listExpected             = null;
    String                    result                   = null;
    String                    runmodes[]               = null;
    public String[]           InsightfirstPrimaryCount = null;
    SoftAssert                s_assert                 = new SoftAssert();

    @Test(dataProvider = "getTestData")
    public void InsightLoaderTest() throws InterruptedException, IOException {
        count++;

        if (!runmodes[count].equalsIgnoreCase("Y")) {
            throw new SkipException("Runmode for Test Data is set to NO " + count);
        }

        //Opeming Browser
        openBrowser();
        
        //End-User logged-In
        endUserLogin(CONFIG.getProperty("endUserURL"),
                     CONFIG.getProperty("endUserUsername"),
                     CONFIG.getProperty("endUserPassword"));
        Logger.debug("Executing Test Case Insight Loader --- ");
        driver.get(CONFIG.getProperty("endUserURL"));
        fluentWaitMethod(driver.findElement(By.id("globalView")));
        Thread.sleep(6000);

        String originalHandle = driver.getWindowHandle();
        
        //Clicking Insights configure button

        driver.findElement(By.xpath("//*[@id='priorityListModernViewInsights']/a/span")).click();

        //Getting list of visible insights
        List<String>     InsightListID    = getInsightList();
        Iterator<String> itrInsightListID = InsightListID.iterator();

        //Set Insight as default one by one
        while (itrInsightListID.hasNext()) {
            deSelectChartList();

            String insightID           = itrInsightListID.next();
            String selectedInsightName = driver.findElement(By.id("priorityOverlayInsights"))
                                               .findElement(By.id(insightID))
                                               .getText();

            driver.findElement(By.className("chartSelectionBodyI"))
                  .findElement(By.id(insightID))
                  .findElement(By.className("chartListClassI"))
                  .click();
            driver.findElement(By.xpath("//input[contains(@id,'savePreferencesInsights')]")).click();
            //
            
            //Getting Insight Title, Data
            fluentWaitMethod(driver.findElement(By.className("titleBar")));

            try {
                takeScreenShotAs(this.getClass().getSimpleName(), selectedInsightName.toUpperCase().replace(" > ", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Assert.assertEquals(driver.findElement(By.id("topCharts"))
                                      .findElement(By.className("titleBar"))
                                      .getText()
                                      .toUpperCase(),
                                selectedInsightName.toUpperCase());
            Logger.debug("Test Case Insight Loader For --- " + selectedInsightName.toUpperCase()
                         + " --- is Completed Successfully");

            String[] InsightMainCount = driver.findElement(By.id("topCharts"))
                                              .findElement(By.className("bigText"))
                                              .getText()
                                              .split(" ");

            System.out.println(InsightMainCount + "-->" + selectedInsightName);

            try {
                if (driver.findElement(By.id("topCharts")).findElement(By.className("bottomBox")).isDisplayed()) {
                    InsightfirstPrimaryCount = driver.findElement(By.id("topCharts"))
                                                     .findElement(By.className("bottomBox"))
                                                     .getText()
                                                     .split(" ");
                    System.out.println(InsightfirstPrimaryCount + "-->" + selectedInsightName);
                }
            } catch (Exception e) {
                System.out.println("There is no such Secondary Insight is available");
            }

            try {
                if (driver.findElement(By.id("topCharts")).findElement(By.className("bottomBox2")).isDisplayed()) {
                    String InsightsecondPrimaryCount = driver.findElement(By.id("topCharts"))
                                                             .findElement(By.className("bottomBox2"))
                                                             .getText();

                    System.out.println(InsightsecondPrimaryCount + "-->" + selectedInsightName);
                }
            } catch (Exception e) {
                System.out.println("There is no such Secondary Insight is available");
            }

            //Navigating to Listing Page
            driver.findElement(By.id("topCharts")).findElement(By.className("bigText")).click();
            //

            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(originalHandle)) {
                    driver.switchTo().window(handle);
                }
            }

            String[] ListingCount = driver.findElement(By.id("cr_wrapper"))
                                          .findElement(By.className("dataTables_info"))
                                          .getText()
                                          .split(" ");

            //Asserting Insight data with listing data
            try {
                if (InsightMainCount[1].contains("%")) {
                    Assert.assertEquals(convertStringToInteger(InsightfirstPrimaryCount[0]),
                                        convertStringToInteger(ListingCount[4]));
                } else {
                    Assert.assertEquals(convertStringToInteger(InsightMainCount[0]),
                                        convertStringToInteger(ListingCount[4]));
                }
            } catch (Exception e) {
                System.out.println("Entity Count at Insight does not match with Entity Count visible at Listing page");
            }

            String FilterApplied = driver.findElement(By.id("preference"))
                                         .findElement(By.className("filterCount"))
                                         .getText();

            System.out.println("Default filters Applied are: " + FilterApplied);

            // Check for Listing Data
            if (CommonTestFiltering.checkTableDataPresence()) {
                Logger.info("For " + selectedInsightName + " --- Listing there is No Corresponding Data Available");
                driver.get(CONFIG.getProperty("endUserURL"));

                return;
            }

            int filtersCount = CommonTestFiltering.getFilterCount();

            Logger.info("Total Filters Count on -- " + selectedInsightName + " -- Listing Page is = " + filtersCount);


            //checking Filter visiblity
            if (filtersCount == 0) {
                org.testng.Assert.fail("For " + selectedInsightName
                                       + " --- Listing there are No Corresponding Filters Available");
                driver.get(CONFIG.getProperty("endUserURL"));

                return;
            } else {
            		
            	
            	//Downloading Listing: Selected as well All column data
                WebElement downloadlink = driver.findElement(By.id("exportXLdownload"));

                if (downloadlink != null) {
                    File downloadedFileSrc = new File(System.getProperty("user.home") + "\\Downloads\\"
                                                      + selectedInsightName + ".xlsx");

                    if (downloadedFileSrc.exists()) {
                        downloadedFileSrc.delete();
                    }

                    new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload")))
                    .clickAndHold()
                    .build()
                    .perform();
                    
                    if(driver.findElement(By.id("selectedColumnsDownload")).isDisplayed()){
                    driver.findElement(By.id("selectedColumnsDownload")).click();
                    //
                    }
                    if (driver.findElements(By.className("alertdialog-icon")).size() != 0) {
                        driver.findElement(
                            By.cssSelector(
                                "button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only"))
                              .click();
                    }

                    if (downloadedFileSrc.exists()) {
                        File downloadedSelectColumnFolderDest =
                            new File(
                                System.getProperty("user.dir") + "//Downloaded Data//"
                                + new SimpleDateFormat("dd_MMMM_yyyy").format(
                                    Calendar.getInstance().getTime()).toString() + "//Common Listing//Selected Columns");
                        File downloadedSelectColumnFileDest = new File(downloadedSelectColumnFolderDest + "//"
                                                                       + selectedInsightName + ".xlsx");

                        if (downloadedSelectColumnFileDest.exists()) {
                            downloadedSelectColumnFileDest.delete();
                        }

                        try {
                            FileUtils.moveFile(downloadedFileSrc, downloadedSelectColumnFileDest);

                            FileInputStream downloadedSelectColumnFileInputStream =
                                new FileInputStream(downloadedSelectColumnFileDest);
                            @SuppressWarnings("resource")
                            XSSFWorkbook downloadedSelectColumnWorkbook =
                                new XSSFWorkbook(downloadedSelectColumnFileInputStream);

                            Assert.assertEquals(downloadedSelectColumnWorkbook.getSheetName(0), "Data");
                            downloadedSelectColumnFileInputStream.close();
                            Logger.debug("Execution of Listing Page Selected Column Download for -- "
                                         + selectedInsightName + " is = TRUE");
                        } catch (Exception e) {
                            Logger.debug("Execution of Listing Page Selected Column Download for -- "
                                         + selectedInsightName + " is = FAIL");
                            Logger.error(e.getMessage());
                        }
                    } else {
                        Logger.debug("Execution of Listing Page Selected Column Download for -- " + selectedInsightName
                                     + " is = FAIL");
                    }

                    if (downloadedFileSrc.exists()) {
                        downloadedFileSrc.delete();
                    }

                    new Actions(driver).moveToElement(driver.findElement(By.id("exportXLdownload")))
                                       .clickAndHold()
                                       .build()
                                       .perform();
                    if (driver.findElement(By.id("exportXL")).isDisplayed()){
                    driver.findElement(By.id("exportXL")).click();
                    }
                    //

                    if (driver.findElements(By.className("alertdialog-icon")).size() != 0) {
                        driver.findElement(
                            By.cssSelector(
                                "button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only"))
                              .click();
                    }

                    if (downloadedFileSrc.exists()) {
                        File downloadedAllColumnFolderDest =
                            new File(
                                System.getProperty("user.dir") + "//Downloaded Data//"
                                + new SimpleDateFormat("dd_MMMM_yyyy").format(
                                    Calendar.getInstance().getTime()).toString() + "//Common Listing//All Columns");
                        File downloadedAllColumnFileDest = new File(downloadedAllColumnFolderDest + "//"
                                                                    + selectedInsightName + ".xlsx");

                        if (downloadedAllColumnFileDest.exists()) {
                            downloadedAllColumnFileDest.delete();
                        }

                        try {
                            FileUtils.moveFile(downloadedFileSrc, downloadedAllColumnFileDest);

                            FileInputStream downloadedAllColumnFileInputStream =
                                new FileInputStream(downloadedAllColumnFileDest);
                            @SuppressWarnings("resource")
                            XSSFWorkbook downloadedAllColumnWorkbook =
                                new XSSFWorkbook(downloadedAllColumnFileInputStream);

                            Assert.assertEquals(downloadedAllColumnWorkbook.getSheetName(0), "Data");
                            downloadedAllColumnFileInputStream.close();
                            Logger.debug("Execution of Listing Page All Column Download for -- " + selectedInsightName
                                         + " is = TRUE");
                        } catch (Exception e) {
                            Logger.debug("Execution of Listing Page All Column Download for -- " + selectedInsightName
                                         + " is = FAIL");
                            Logger.error(e.getMessage());
                        }
                    } else {
                        Logger.debug("Execution of Listing Page All Column Download for -- " + selectedInsightName
                                     + " is = FAIL");
                    }
                }

                //Validating Filter data against Listing Data
                Map<String, List<String>> filterContentMap = new HashMap<String, List<String>>();

                driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
                Thread.sleep(2000);

                for (int iCount = 0; iCount < filtersCount; iCount++) {
                    List<String>     selectedfilterOptions = new ArrayList<String>();
                    List<WebElement> filtersContent        = driver.findElement(By.id("filterDiv"))
                                                                   .findElement(By.id("filter"))
                                                                   .findElement(By.className("filter-content"))
                                                                   .findElements(By.className("left-filter"));

                    if (filtersContent.get(iCount).getAttribute("class").contains("left-filter selectedItem")) {
                        String filterName = filtersContent.get(iCount)
                                                          .findElement(By.tagName("button"))
                                                          .findElements(By.tagName("span"))
                                                          .get(1)
                                                          .getText();

                        System.out.println(filterName);
                        filtersContent.get(iCount).findElement(By.tagName("button")).click();
                        Thread.sleep(2785);

                        List<WebElement> element =
                            driver.findElement(
                                By.cssSelector(
                                    "div.ui-multiselect-menu.ui-widget.ui-widget-content.ui-corner-all.dynamicWidth"))
                                  .findElement(By.cssSelector("ul.ui-multiselect-checkboxes.ui-helper-reset"))
                                  .findElements(By.tagName("li"));

                        for (WebElement elementOptions : element) {
                            if (elementOptions.getAttribute("class").equalsIgnoreCase(" ")) {
                                if (elementOptions.findElement(By.tagName("label"))
                                                  .findElement(By.tagName("input"))
                                                  .getAttribute("checked") != null) {
                                    selectedfilterOptions.add(elementOptions.findElement(By.tagName("label"))
                                                                            .findElement(By.tagName("span"))
                                                                            .getText());
                                }
                            }
                        }

                        filterContentMap.put(filterName, selectedfilterOptions);
                        System.out.println(filterContentMap);
                        filterContentMap.clear();

                        // Get Data from column
                        driver.findElement(By.id("preference")).findElement(By.className("columnsList")).click();
                        driver.findElement(By.cssSelector(".ColVis_Button.TableTools_Button.ColVis_ShowAll")).click();
                        Thread.sleep(9500);
                        listExpected = TestSorting.getColumnData(CommonTestFiltering.getFilterColumnNumber(filterName));
                        Logger.info("For Filter -- " + filterName + " -- Column Data After Applying Filter is = "
                                    + listExpected);
                        listExpected.clear();
                        listExpected.containsAll(selectedfilterOptions);
                        driver.navigate().refresh();
                        //
                        fluentWaitMethod(
                                driver.findElement(By.className("dataTables_scrollBody")));
                        driver.findElement(By.className("listingPanelLeft"))
                              .findElement(By.className("filterToggle"))
                              .click();
                        Thread.sleep(2000);
                    }
                }

                //Validating Primary Insight data against Listing data
                try {
                    s_assert.assertEquals(filterContentMap.size(), FilterApplied);
                } catch (Exception ex) {
                    System.out.println("Count of Applied Filter and selected filters are not getting matched");
                }

                //closing Listing Page
                for (String handle : driver.getWindowHandles()) {
                    if (!handle.equals(originalHandle)) {
                        driver.switchTo().window(handle);
                        driver.close();
                    }
                }

                //Navigate back to Main Page
                driver.switchTo().window(originalHandle);
                
                //Again clicking Insight configure button
                driver.findElement(By.xpath("//*[@id='priorityListModernViewInsights']/a/span")).click();
            }
        }

        Logger.debug("Test Case Insight is Completed Successfully--- ");
        driver.get(CONFIG.getProperty("endUserURL"));
    }

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(insights_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");

            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
        }

        runmodes = TestUtil.getDataSetRunmodes(insights_suite_xls, this.getClass().getSimpleName());
    }

    public static void deSelectChartList() {
        List<WebElement> selectedInsightsList = driver.findElement(By.id("priorityOverlayInsights"))
                                                      .findElement(By.tagName("ul"))
                                                      .findElements(By.tagName("li"));

        if (!selectedInsightsList.isEmpty()) {
            Iterator<WebElement> itrSelectedInsightsList = selectedInsightsList.iterator();

            while (itrSelectedInsightsList.hasNext()) {
                WebElement selectedInsightElement = itrSelectedInsightsList.next();

                if (selectedInsightElement.findElement(By.tagName("input")).isSelected()) {
                    selectedInsightElement.findElement(By.tagName("input")).click();
                }
            }
        }
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP) {
            TestUtil.reportDataSetResult(insights_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");
        } else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(insights_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(insights_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
            result = "Pass";
        }
    }

    @AfterTest
    public void reportTestResult() {
        if (isTestPass) {
            TestUtil.reportDataSetResult(insights_suite_xls,
                                         "Test Cases",
                                         TestUtil.getRowNum(insights_suite_xls, this.getClass().getSimpleName()),
                                         "PASS");
        } else {
            TestUtil.reportDataSetResult(insights_suite_xls,
                                         "Test Cases",
                                         TestUtil.getRowNum(insights_suite_xls, this.getClass().getSimpleName()),
                                         "FAIL");
        }
    }

    public static List<String> getInsightList() {
        List<String>     insightIDList          = new ArrayList<String>();
        List<WebElement> deselectedInsightsList = driver.findElement(By.className("chartSelectionBodyI"))
                                                        .findElement(By.tagName("ul"))
                                                        .findElements(By.tagName("li"));
        Iterator<WebElement> itrDeselectedInsightsList = deselectedInsightsList.iterator();

        while (itrDeselectedInsightsList.hasNext()) {
            WebElement insightElement = itrDeselectedInsightsList.next();
            String     insightListID  = insightElement.getAttribute("id");

            insightIDList.add(insightListID);
        }

        return insightIDList;
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(insights_suite_xls, this.getClass().getSimpleName());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
