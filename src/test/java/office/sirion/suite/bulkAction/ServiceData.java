package office.sirion.suite.bulkAction;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;

public class ServiceData extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(bulk_action_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(bulk_action_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void BulkActionServiceDataTest(String entitytitles, String obCurrentstate, String obNextState, String obActionToBePerformed,String obPrivacy, String obComments,
                                         String obActualDate,String obRequestedBy, String obUploadedFiles) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for test set data set to no " + count);

        skip = true;

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Obligations Bulk creation with Titles -- " + entitytitles);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("service_data_quick_link"));

        locateElementBy("service_data_quick_link").click();
        //

        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        locateElementBy("bulk_action_button_listing_page").click();

        try {
            addFieldValue("bulk_action_current_task_drop_down", obCurrentstate);
            if (!obCurrentstate.equalsIgnoreCase("")) {
                addFieldValue("bulk_action_next_task_drop_down", obNextState);
            }
        } catch (NoSuchElementException e) {
            Logger.debug("No Such option is visible under Current Task drop-down");

            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }
        locateElementBy("bulk_action_popup_apply_button").click();

        try {
            driver.findElement(By.xpath("//*[@class='dataTables_empty']"));
            Logger.debug("After applying bulk action current & next task, no such entities are applicable");
            driver.get(CONFIG.getProperty("endUserURL"));
            return;

        } catch (NoSuchElementException e1) {

            if (!entitytitles.equalsIgnoreCase("All")) {

                for (String titles : entitytitles.split(";")) {

                    Thread.sleep(5000);
                    fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

                    new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
                    //

                    if (!titles.equalsIgnoreCase("") && titles.length() <= 30) {
                        try {
                            driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/a[./text()='" + titles + "']")).click();
                        } catch (NoSuchElementException e) {
                            Logger.debug("No Such Obligation is visible under listing page");

                            driver.get(CONFIG.getProperty("endUserURL"));
                            return;
                        }
                    } else if (!titles.equalsIgnoreCase("") && titles.length() > 30) {
                        try {
                            driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/div/div/a[./text()='" + titles + "']")).click();
                        } catch (NoSuchElementException ex) {
                            Logger.debug("No Such Obligation is visible under listing page");

                            driver.get(CONFIG.getProperty("endUserURL"));
                            return;
                        }
                    }
                }
            } else {
                locateElementBy("bulk_action_first_box_in_listing_page").click();
                Thread.sleep(8000);
                locateElementBy("bulk_action_select_all").click();
            }
            if (obActionToBePerformed.equalsIgnoreCase("Y")) {
                locateElementBy("bulk_action_task_tobe_performed").click();
            } else {
                locateElementBy("bulk_action_cancel_button").click();
            }

            locateElementBy("bulk_action_comment_attachment_popup_submit_button").click();

            // OBLIGATIONS - EDIT PAGE - COMMENTS AND ATTACHMENTS
            addFieldValue("entity_create_page_comments_textarea", obComments.trim());

            addFieldValue("entity_create_page_requested_by_dropdown", obRequestedBy.trim());
            DatePicker_Enhanced date = new DatePicker_Enhanced();
            date.selectCalendar(obActualDate, "actualDate");

            if (!obUploadedFiles.equalsIgnoreCase("")) {
                locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Obligation\\" + obUploadedFiles);
                //
            }
            if (obPrivacy.equalsIgnoreCase("Y"))
                locateElementBy("entity_create_page_privacy_checkbox").findElement(By.tagName("input")).click();

            locateElementBy("bulk_action_final_ok_button").click();
        }

    }


    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if(testResult.getStatus()==ITestResult.SKIP)
            TestUtil.reportDataSetResult(bulk_action_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

        else if(testResult.getStatus()==ITestResult.FAILURE) {
            isTestPass=false;
            TestUtil.reportDataSetResult(bulk_action_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
            result= "Fail";
        }

        else if (testResult.getStatus()==ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(bulk_action_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
            result= "Pass";
        }

        try {
            if (!testCaseID.equalsIgnoreCase(""))
                TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
        } catch (Exception e) {

        }
    }
    @AfterTest
    public void reportTestResult() {

        if (isTestPass)
            TestUtil.reportDataSetResult(bulk_action_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_action_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(bulk_action_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_action_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(bulk_action_suite_xls, this.getClass().getSimpleName());
    }
}