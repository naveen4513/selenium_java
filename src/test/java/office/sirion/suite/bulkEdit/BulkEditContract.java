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


public class BulkEditContract extends TestSuiteBase {

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
    public void BulkEditContractTest(String EntityName,String name,String title,String agreementno,String brief,String business_unit,String timezone,String contracting_entity,
                                               String delivery_countries,String supplier_access,String term_type,String ariba_cw_id,String contract_type,String comment_field_status,
                                               String number_of_renewals,String agreement_type,String contract_paper,String vendor_classification,String effective_date_original,
                                               String expiration_date_original,String expiration_notice_period_in_days,String notice_date,String notice_lead_days,String notice_lead_date,
                                               String contract_manager,String financial_impact,String projectid) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Contract Creation for -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //

        locateElementBy("contracts_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
        //

        if(!EntityName.equalsIgnoreCase("")) {
            for (String entityname : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + entityname + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='"+ entityname +"']/preceding-sibling::td[1]/a")).getText();
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

            locateElementBy("bulk_edit_contract_basic_information_checkbox").click();//Select All fields in Basic Information Section
            locateElementBy("bulk_edit_contract_information_checkbox").click();//Select All fields in Contract Information Section
            locateElementBy("bulk_edit_contract_important_dates_checkbox").click();//Select All fields in Important Dates Section
            locateElementBy("bulk_edit_contract_stakeholders_checkbox").click();//Select All fields in StakeHolders Section
            locateElementBy("bulk_edit_contract_financial_information_checkbox").click();//Select All fields in Financial Information Section
            locateElementBy("bulk_edit_contract_project_information_checkbox").click();//Select All fields in Project Information Section

            locateElementBy("bulk_edit_page_general_tab").click();

            if(!name.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_contract_name_textbox", name);
            }
            if(!title.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_contract_title_textbox", title);
            }
            if(!agreementno.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_agreementno_textbox", agreementno);
            }
            if(!brief.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_brief_textarea", brief);
            }
            if(!business_unit.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_business_unit_dropdown", business_unit);
            }
            if(!timezone.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_timezone_dropdown", timezone);
            }
            if(!contracting_entity.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_contracting_entity_dropdown", contracting_entity);
            }
            if(!delivery_countries.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_delivery_countries_multiselect", delivery_countries);
            }
            if(!supplier_access.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_supplier_access_checkbox", supplier_access);
            }
            if(!term_type.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_term_type_dropdown", term_type);
            }
            if(!ariba_cw_id.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_ariba_cw_id_textbox", ariba_cw_id);
            }
            if(!contract_type.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_contract_type_dropdown", contract_type);
            }
            if(!comment_field_status.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_comment_field_status_textarea", comment_field_status);
            }
            if(!number_of_renewals.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_number_of_renewals_textbox", number_of_renewals);
            }
            if(!agreement_type.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_agreement_type_dropdown", agreement_type);
            }
            if(!contract_paper.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_contract_paper_dropdown", contract_paper);
            }
            if(!vendor_classification.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_vendor_classification_dropdown", vendor_classification);
            }
            if(!effective_date_original.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(effective_date_original, "effectiveDateOriginal");
            }
            if(!expiration_date_original.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(expiration_date_original, "expirationDateOriginal");
            }
            if(!expiration_notice_period_in_days.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_expiration_notice_period_in_days_textbox", expiration_notice_period_in_days);
            }
            if(!notice_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(notice_date, "noticeDate");
            }
            if(!notice_lead_days.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_notice_lead_days_textbox", notice_lead_days);
            }
            if(!notice_lead_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(notice_lead_date, "noticeLeadDate");
            }
            /*if(!contract_manager.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_contract_manager_textbox", contract_manager);
            }*/
            if(!financial_impact.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_financial_impact_textbox", financial_impact);
            }
            if(!projectid.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_contract_projectid_multiselect", projectid);
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

        if(!name.equalsIgnoreCase("")){
            Assert.assertEquals(name,locateElementBy("bulk_edit_contract_name_showpage").getText());
        }
        if(!title.equalsIgnoreCase("")){
            Assert.assertEquals(title,locateElementBy("bulk_edit_contract_title_showpage").getText());
        }
        if(!agreementno.equalsIgnoreCase("")){
            Assert.assertEquals(agreementno,locateElementBy("bulk_edit_contract_agreementno_showpage").getText());
        }
        if(!brief.equalsIgnoreCase("")){
            Assert.assertEquals(brief,locateElementBy("bulk_edit_contract_brief_showpage").getText());
        }
        if(!business_unit.equalsIgnoreCase("")){
            Assert.assertEquals(business_unit,locateElementBy("bulk_edit_contract_business_unit_showpage").getText());
        }
        if(!timezone.equalsIgnoreCase("")){
            Assert.assertEquals(timezone,locateElementBy("bulk_edit_contract_timezone_showpage").getText());
        }
        if(!contracting_entity.equalsIgnoreCase("")){
            Assert.assertEquals(contracting_entity,locateElementBy("bulk_edit_contract_contracting_entity_showpage").getText());
        }
        if(!delivery_countries.equalsIgnoreCase("")){
            Assert.assertEquals(delivery_countries,locateElementBy("bulk_edit_contract_delivery_countries_showpage").getText());
        }
        if(!supplier_access.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_access,locateElementBy("bulk_edit_contract_supplier_access_showpage").getText());
        }
        if(!term_type.equalsIgnoreCase("")){
            Assert.assertEquals(term_type,locateElementBy("bulk_edit_contract_term_type_showpage").getText());
        }
        if(!ariba_cw_id.equalsIgnoreCase("")){
            Assert.assertEquals(ariba_cw_id,locateElementBy("bulk_edit_contract_ariba_cw_id_showpage").getText());
        }
        if(!contract_type.equalsIgnoreCase("")){
            Assert.assertEquals(contract_type,locateElementBy("bulk_edit_contract_contract_type_showpage").getText());
        }
        if(!comment_field_status.equalsIgnoreCase("")){
            Assert.assertEquals(comment_field_status,locateElementBy("bulk_edit_contract_comment_field_status_showpage").getText());
        }
        if(!number_of_renewals.equalsIgnoreCase("")){
            Assert.assertEquals(number_of_renewals,locateElementBy("bulk_edit_contract_number_of_renewals_showpage").getText());
        }
        if(!agreement_type.equalsIgnoreCase("")){
            Assert.assertEquals(agreement_type,locateElementBy("bulk_edit_contract_agreement_type_showpage").getText());
        }
        if(!contract_paper.equalsIgnoreCase("")){
            Assert.assertEquals(contract_paper,locateElementBy("bulk_edit_contract_contract_paper_showpage").getText());
        }
        if(!vendor_classification.equalsIgnoreCase("")){
            Assert.assertEquals(vendor_classification,locateElementBy("bulk_edit_contract_vendor_classification_showpage").getText());
        }
        if(!effective_date_original.equalsIgnoreCase("")){
            Assert.assertEquals(effective_date_original,locateElementBy("bulk_edit_contract_effective_date_original_showpage").getText());
        }
        if(!expiration_date_original.equalsIgnoreCase("")){
            Assert.assertEquals(expiration_date_original,locateElementBy("bulk_edit_contract_expiration_date_original_showpage").getText());
        }
        if(!expiration_notice_period_in_days.equalsIgnoreCase("")){
            Assert.assertEquals(expiration_notice_period_in_days,locateElementBy("bulk_edit_contract_expiration_notice_period_in_days_showpage").getText());
        }
        if(!notice_date.equalsIgnoreCase("")){
            Assert.assertEquals(notice_date,locateElementBy("bulk_edit_contract_notice_date_showpage").getText());
        }
        if(!notice_lead_days.equalsIgnoreCase("")){
            Assert.assertEquals(notice_lead_days,locateElementBy("bulk_edit_contract_notice_lead_days_showpage").getText());
        }
        if(!notice_lead_date.equalsIgnoreCase("")){
            Assert.assertEquals(notice_lead_date,locateElementBy("bulk_edit_contract_notice_lead_date_showpage").getText());
        }
        if(!contract_manager.equalsIgnoreCase("")){
            Assert.assertEquals(contract_manager,locateElementBy("bulk_edit_contract_contract_manager_showpage").getText());
        }
        if(!financial_impact.equalsIgnoreCase("")){
            Assert.assertEquals(financial_impact,locateElementBy("bulk_edit_contract_financial_impact_showpage").getText());
        }
        if(!projectid.equalsIgnoreCase("")){
            Assert.assertEquals(projectid,locateElementBy("bulk_edit_contract_projectid_showpage").getText());
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
