package office.sirion.suite.bulkEdit;


import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import office.sirion.util.XLSUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BulkEditChildServiceLevels extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    List<String> EntityID = new ArrayList<>();

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(bulk_edit_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(bulk_create_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void BulkEditServiceLevelTest(String EntityName,String supplier_numerator,String actual_numerator,String supplier_denominator,String actual_denominator,
                                           String supplier_calculation,String actual_performance,String discrepancy,String discrepancyResolutionStatus,String final_numerator,
                                           String final_denominator,String final_performance,String slmet) throws InterruptedException, IOException, TestLinkAPIException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }
        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPasscsld"));

        driver.get(CONFIG.getProperty("endUserURL"));
        Thread.sleep(8000);
        String logged_in_user = locateElementBy("csl_communication_logged_in_user_full_name").getText();

        fluentWaitMethod(locateElementBy("sl_quick_link"));
        locateElementBy("sl_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("csl_link_listing_page"));

        locateElementBy("csl_link_listing_page").click();
        //


        if(!EntityName.equalsIgnoreCase("")) {
            for (String name : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[contains(text(),'"+name+"')]/preceding::td/input[@type='checkbox']")).click();
                String ids = driver.findElement(By.xpath("//*[contains(text(),'"+name+"')]/preceding::td[1]/a")).getText();
                EntityID.add(ids);
            }

            locateElementBy("bulk_edit_listing_page_button").click();
            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    locateElementBy("bulk_edit_alert_ok_button").click();
                    locateElementBy("bulk_edit_page_first_entity").click();
                    locateElementBy("bulk_edit_listing_page_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }

            locateElementBy("bulk_edit_search_dropdown").click();

            locateElementBy("bulk_edit_basic_information_checkbox").click();//Select All fields in Basic Information Section
            locateElementBy("bulk_edit_important_date_checkbox").click();//Select All fields in Important Dates Section
            locateElementBy("bulk_edit_stakeholders_checkbox").click();//Select All fields in StakeHolders Section

            locateElementBy("bulk_edit_page_general_tab").click();

            if(!supplier_numerator.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_csl_supplier_numerator_textbox", supplier_numerator);
            }
            if(!actual_numerator.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_csl_actual_numerator_textbox", actual_numerator);
            }
            if(!supplier_denominator.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_supplier_denominator_textbox", supplier_denominator);
            }
            if(!actual_denominator.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_actual_denominator_textbox", actual_denominator);
            }
            if(!supplier_calculation.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_supplier_calculation_textbox", supplier_calculation);
            }
            if(!actual_performance.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_actual_performance_textbox", actual_performance);
            }
            if(!discrepancy.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_discrepancy_textarea", discrepancy);
            }
            if(!discrepancyResolutionStatus.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_discrepancyResolutionStatus_textarea", discrepancyResolutionStatus);
            }
            if(!final_numerator.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_final_numerator_textbox", final_numerator);
            }
            if(!final_denominator.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_final_denominator_textbox", final_denominator);
            }
            if(!final_performance.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_final_performance_textbox", final_performance);
            }
            if(!slmet.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_csl_slmet_textbox", slmet);
            }
        }
        locateElementBy("bulk_edit_submit_button").click();
        Thread.sleep(7000);

        if(driver.findElement(By.className("success-icon")).getText().equalsIgnoreCase("Your request has been successfully submitted.")){
            locateElementBy("bulk_edit_popup_ok_button").click();
        }

        try{
            locateElementBy("bulk_edit_lock_icon");
            Logger.debug("Entities are locked for Bulk operations");
        }catch (NoSuchElementException e){
            Logger.debug("Entities are now unlocked for Bulk operations");
        }

        driver.navigate().refresh();

        locateElementBy("end_user_quick_search_box").click();
        for(String id: EntityID) {
            locateElementBy("end_user_quick_search_box").sendKeys(id);
            locateElementBy("end_user_quick_search_box").sendKeys(Keys.RETURN);
        }

        if(!supplier_numerator.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_numerator,locateElementBy("bulk_edit_csl_supplier_numerator_showpage").getText());
        }

        if(!actual_numerator.equalsIgnoreCase("")){
            Assert.assertEquals(actual_numerator,locateElementBy("bulk_edit_csl_actual_numerator_showpage").getText());
        }
        if(!supplier_denominator.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_denominator,locateElementBy("bulk_edit_csl_supplier_denominator_showpage").getText());
        }
        if(!actual_denominator.equalsIgnoreCase("")){
            Assert.assertEquals(actual_denominator,locateElementBy("bulk_edit_csl_actual_denominator_showpage").getText());
        }
        if(!supplier_calculation.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_calculation,locateElementBy("bulk_edit_csl_supplier_calculation_showpage").getText());
        }
        if(!actual_performance.equalsIgnoreCase("")){
            Assert.assertEquals(actual_performance,locateElementBy("bulk_edit_csl_actual_performance_showpage").getText());
        }
        if(!discrepancy.equalsIgnoreCase("")){
            Assert.assertEquals(discrepancy,locateElementBy("bulk_edit_csl_discrepancy_showpage").getText());
        }
        if(!discrepancyResolutionStatus.equalsIgnoreCase("")){
                Assert.assertEquals(discrepancyResolutionStatus,locateElementBy("bulk_edit_csl_discrepancyResolutionStatus_showpage").getText());
        }
        if(!final_numerator.equalsIgnoreCase("")){
            Assert.assertEquals(final_numerator,locateElementBy("bulk_edit_csl_final_numerator_showpage").getText());
        }
        if(!final_denominator.equalsIgnoreCase("")){
            Assert.assertEquals(final_denominator,locateElementBy("bulk_edit_csl_final_denominator_showpage").getText());
        }
        if(!final_performance.equalsIgnoreCase("")){
            Assert.assertEquals(final_performance,locateElementBy("bulk_edit_csl_final_performance_showpage").getText());
        }
        if(!slmet.equalsIgnoreCase("")){
            Assert.assertEquals(slmet,locateElementBy("bulk_edit_csl_slmet_showpage").getText());
        }
        fail=false;
        driver.get(CONFIG.getProperty("clientAdminURL"));

        return;
    }

    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
        takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

        if (testResult.getStatus() == ITestResult.SKIP)
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");

        else if (testResult.getStatus() == ITestResult.FAILURE) {
            isTestPass = false;
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
            result = "Fail";
        } else if (testResult.getStatus() == ITestResult.SUCCESS) {
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");
            result = "Pass";
        }

        try {
            if (!testCaseID.equalsIgnoreCase(""))
                TestlinkIntegration.updateTestLinkResult(testCaseID, "", result);
        } catch (Exception e) {

        }
    }

    @AfterTest
    public void reportTestResult() {

        if (isTestPass)
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_edit_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(bulk_edit_suite_xls, "Test Cases", TestUtil.getRowNum(bulk_edit_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(bulk_edit_suite_xls, this.getClass().getSimpleName());
    }
}
