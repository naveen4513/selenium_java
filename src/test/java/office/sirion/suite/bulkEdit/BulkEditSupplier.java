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


public class BulkEditSupplier extends TestSuiteBase {

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
    public void BulkEditClauseTest(String EntityName,String tier,String email,String taxid,String functions,String services,String regions,String region_countries,
                                     String suppliers_manager,String additional_FACV,String additional_TCV,String additional_ACV) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Supplier Creation for -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("vh_quick_link"));

        locateElementBy("vh_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
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

            if(!tier.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_supplier_tier_dropdown", tier);
            }
            if(!email.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_supplier_email_textbox", email);
            }
            if(!taxid.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_taxid_textbox", taxid);
            }
            if(!functions.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_functions_dropdown", functions);
            }
            if(!services.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_services_dropdown", services);
            }
            if(!regions.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_regions_dropdown",regions);
            }
            if(!region_countries.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_region_countries_dropdown",region_countries);
            }
            /*if(!suppliers_manager.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_suppliers_manager_textbox", suppliers_manager);
            }*/
            if(!additional_FACV.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_additional_FACV_textbox", additional_FACV);
            }
            if(!additional_TCV.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_additional_TCV_textbox", additional_TCV);
            }
            if(!additional_ACV.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_supplier_additional_ACV_textbox", additional_ACV);
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

        if(!tier.equalsIgnoreCase("")){
            Assert.assertEquals(tier,locateElementBy("bulk_edit_supplier_tier_showpage").getText());
        }

        if(!email.equalsIgnoreCase("")){
            Assert.assertEquals(email,locateElementBy("bulk_edit_supplier_email_showpage").getText());
        }

        if(!taxid.equalsIgnoreCase("")){
            Assert.assertEquals(taxid,locateElementBy("bulk_edit_supplier_taxid_showpage").getText());
        }

        if(!functions.equalsIgnoreCase("")){
            Assert.assertEquals(functions,locateElementBy("bulk_edit_supplier_functions_showpage").getText());
        }

        if(!services.equalsIgnoreCase("")){
            Assert.assertEquals(services,locateElementBy("bulk_edit_supplier_services_showpage").getText());
        }

        if(!regions.equalsIgnoreCase("")){
            Assert.assertEquals(regions,locateElementBy("bulk_edit_supplier_regions_showpage").getText());
        }

        if(!region_countries.equalsIgnoreCase("")){
            Assert.assertEquals(region_countries,locateElementBy("bulk_edit_supplier_region_countries_showpage").getText());
        }

        /*if(!suppliers_manager.equalsIgnoreCase("")){
                Assert.assertEquals(suppliers_manager,locateElementBy("bulk_edit_supplier_suppliers_manager_showpage").getText());
        }

        if(!additional_FACV.equalsIgnoreCase("")){
            Assert.assertEquals(additional_FACV,locateElementBy("bulk_edit_supplier_additional_FACV_showpage").getText());
        }
        if(!additional_TCV.equalsIgnoreCase("")){
            Assert.assertEquals(additional_TCV,locateElementBy("bulk_edit_supplier_additional_TCV_showpage").getText());
        }
        if(!additional_ACV.equalsIgnoreCase("")){
            Assert.assertEquals(additional_ACV,locateElementBy("bulk_edit_supplier_additional_ACV_showpage").getText());
        }*/

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
