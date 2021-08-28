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


public class BulkEditServiceData extends TestSuiteBase {

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
    public void BulkEditServiceDataTest(String EntityName,String service_category,String service_sub_category,String unit_type,String conversion_matrix,String currency_conversion_date,
                                          String rounding_rule,String no_of_decimals,String rounding_rule_slabs,String no_of_decimals_slabs) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Service Level Creation with Title -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("sl_quick_link"));

        locateElementBy("service_data_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
        //

        if(!EntityName.equalsIgnoreCase("")) {
            for (String name : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + name + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='"+ name +"']/preceding-sibling::td[1]/a")).getText();
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

            if(!service_category.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_service_category_dropdown", service_category);
            }
            if(!service_sub_category.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_service_sub_category_dropdown", service_sub_category);
            }
            if(!unit_type.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_unit_type_dropdown", unit_type);
            }
            if(!conversion_matrix.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_conversion_matrix_dropdown", conversion_matrix);
            }
            if(!currency_conversion_date.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_currency_conversion_date_dropdown", currency_conversion_date);
            }
            if(!rounding_rule.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_rounding_rule_dropdown", rounding_rule);
            }
            if(!no_of_decimals.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_no_of_decimals_textbox", no_of_decimals);
            }
            if(!rounding_rule_slabs.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_rounding_rule_slabs_dropdown", rounding_rule_slabs);
            }
            if(!no_of_decimals_slabs.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_no_of_decimals_slabs_textbox", no_of_decimals_slabs);
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
        if(!service_category.equalsIgnoreCase("")){
            Assert.assertEquals(service_category,locateElementBy("bulk_edit_service_category_showpage").getText());
        }
        if(!service_sub_category.equalsIgnoreCase("")){
            Assert.assertEquals(service_sub_category,locateElementBy("bulk_edit_service_sub_category_showpage").getText());
        }
        if(!unit_type.equalsIgnoreCase("")){
            Assert.assertEquals(unit_type,locateElementBy("bulk_edit_unit_type_showpage").getText());
        }
        if(!conversion_matrix.equalsIgnoreCase("")){
            Assert.assertEquals(conversion_matrix,locateElementBy("bulk_edit_conversion_matrix_showpage").getText());
        }
        if(!currency_conversion_date.equalsIgnoreCase("")){
            Assert.assertEquals(currency_conversion_date,locateElementBy("bulk_edit_currency_conversion_date_showpage").getText());
        }
        if(!rounding_rule.equalsIgnoreCase("")){
            Assert.assertEquals(rounding_rule,locateElementBy("bulk_edit_rounding_rule_showpage").getText());
        }
        if(!no_of_decimals.equalsIgnoreCase("")){
            Assert.assertEquals(no_of_decimals,locateElementBy("bulk_edit_no_of_decimals_showpage").getText());
        }
        if(!rounding_rule_slabs.equalsIgnoreCase("")){
                Assert.assertEquals(rounding_rule_slabs,locateElementBy("bulk_edit_rounding_rule_slabs_showpage").getText());
        }
        if(!no_of_decimals_slabs.equalsIgnoreCase("")){
            Assert.assertEquals(no_of_decimals_slabs,locateElementBy("bulk_edit_no_of_decimals_slabs_showpage").getText());
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
