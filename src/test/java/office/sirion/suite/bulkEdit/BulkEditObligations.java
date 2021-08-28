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


public class BulkEditObligations extends TestSuiteBase {

    String testCaseID;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    String runmodes[] = null;
    List<String> EntityIDs = new ArrayList<>();

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
    public void BulkEditObligationTest(String EntityID,String title,String description,String performance_type,String category,String sub_category,String timezone,
                                         String delivery_countries,String priority,String phase,String action_type,String triggered,String references,String frequency_type,String frequency,
                                         String week_type,String start_date,String end_date,String include_start_date,String include_end_date,String pattern_date,String effective_date,
                                         String vendor_contracting_party,String recipient_client_entity,String company_code,String master_obligations_manager,String responsibility,
                                         String financial_impact_applicable,String financial_impact_value ,String financial_impact_clause ,String impact_days,String impact_type,
                                         String credit_impact_applicable, String credit_impact_value ,String credit_impact_clause,String projectid,String project_levels,String initiatives)
                                        throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Obligations Creation with Title -- " + EntityID);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();
        //
        fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

        driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
        //

        if(!EntityID.equalsIgnoreCase("")) {
            for (String name : EntityID.split(";")) {
                driver.findElement(By.xpath("//a[contains(text(),'"+name+"')]/parent::td/parent::tr/td[1]/input")).click();
                String ids = driver.findElement(By.xpath("//a[contains(text(),'"+name+"')]/parent::td/parent::tr/td[3]/div/div/a")).getText();
                EntityIDs.add(ids);
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

            // OBLIGATIONS - EDIT PAGE - BASIC INFORMATION
            addFieldValue("bulk_edit_obligations_title_textbox", title.trim());

            addFieldValue("bulk_edit_obligations_description_textarea", description.trim());

            addFieldValue("bulk_edit_obligations_performance_type_dropdown", performance_type.trim());

            addFieldValue("bulk_edit_obligations_category_dropdown", category.trim());

            addFieldValue("bulk_edit_obligations_sub_category_dropdown", sub_category.trim());

            addFieldValue("bulk_edit_obligations_timezone_dropdown", timezone.trim());
            try {
                if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
                    driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
            } catch (Exception e) {
            }

            addFieldValue("bulk_edit_obligations_delivery_countries_multi_dropdown", delivery_countries.trim());

            // OBLIGATIONS - EDIT PAGE - OTHER INFORMATION
            addFieldValue("bulk_edit_obligations_priority_dropdown", priority.trim());

            addFieldValue("bulk_edit_obligations_phase_dropdown", phase.trim());

            addFieldValue("ob_create_page_triggered_checkbox", triggered.trim());

            addFieldValue("ob_create_page_references_dropdown", references.trim());

                // OBLIGATIONS - EDIT PAGE - IMPORTANT DATES
                addFieldValue("bulk_edit_obligations_frequency_type_dropdown", frequency_type.trim());

                addFieldValue("bulk_edit_obligations_frequency_dropdown", frequency.trim());

                addFieldValue("bulk_edit_obligations_week_type_dropdown", week_type.trim());

                addFieldValue("bulk_edit_obligations_start_date", start_date.trim());

                addFieldValue("bulk_edit_obligations_end_date", end_date.trim());

                addFieldValue("bulk_edit_obligations_include_start_date_checkbox", include_start_date.trim());

                addFieldValue("bulk_edit_obligations_include_end_date_checkbox", include_end_date.trim());

                addFieldValue("bulk_edit_obligations_pattern_date", pattern_date.trim());

                addFieldValue("bulk_edit_obligations_effective_date", effective_date.trim());

            // OBLIGATIONS - EDIT PAGE - CONTRACTING PARTY
            addFieldValue("bulk_edit_obligations_vendor_contracting_party_multi_dropdown", vendor_contracting_party.trim());

            addFieldValue("bulk_edit_obligations_recipient_client_entities_multi_dropdown", recipient_client_entity.trim());

            addFieldValue("bulk_edit_obligations_recipient_company_codes_multi_dropdown", company_code.trim());


            // OBLIGATIONS - EDIT PAGE - STAKEHOLDERS
            addFieldValue("bulk_edit_obligations_responsibility_dropdown", responsibility.trim());

            // OBLIGATIONS - EDIT PAGE - FINANCIAL INFORMATION
            addFieldValue("bulk_edit_obligations_financial_impact_applicable_checkbox", financial_impact_applicable.trim());
            if (financial_impact_applicable.equalsIgnoreCase("Yes")) {
                addFieldValue("bulk_edit_obligations_financial_impact_value_numeric_box", financial_impact_value.trim());

                addFieldValue("bulk_edit_obligations_financial_impact_clause_textarea", financial_impact_clause.trim());

                addFieldValue("bulk_edit_obligations_impact_days_numeric_box", impact_days.trim());

                addFieldValue("bulk_edit_obligations_impact_type_multi_dropdown", impact_type.trim());
            }

            addFieldValue("bulk_edit_obligations_credit_impact_applicable_checkbox", credit_impact_applicable.trim());
            if (credit_impact_applicable.equalsIgnoreCase("Yes")) {
                addFieldValue("bulk_edit_obligations_credit_impact_value_numeric_box", credit_impact_value.trim());

                addFieldValue("bulk_edit_obligations_credit_impact_clause_textarea", credit_impact_clause.trim());
            }

            // OBLIGATIONS - EDIT PAGE - PROJECT INFORMATION
            addFieldValue("bulk_edit_obligations_project_id_multi_dropdown", projectid.trim());

            addFieldValue("bulk_edit_obligations_project_levels_multi_dropdown", project_levels.trim());

            addFieldValue("bulk_edit_obligations_initiatives_multi_dropdown", initiatives.trim());

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
        for(String id: EntityIDs) {
            locateElementBy("end_user_quick_search_box").sendKeys(id);
            locateElementBy("end_user_quick_search_box").sendKeys(Keys.RETURN);
        }

        if(!title.equalsIgnoreCase("")){
            Assert.assertEquals(title,locateElementBy("bulk_edit_obligations_title_showpage").getText());
        }
        if(!description.equalsIgnoreCase("")){
            Assert.assertEquals(description,locateElementBy("bulk_edit_obligations_description_showpage").getText());
        }
        if(!performance_type.equalsIgnoreCase("")){
            Assert.assertEquals(performance_type,locateElementBy("bulk_edit_obligations_performance_type_showpage").getText());
        }
        if(!category.equalsIgnoreCase("")){
            Assert.assertEquals(category,locateElementBy("bulk_edit_obligations_category_showpage").getText());
        }
        if(!sub_category.equalsIgnoreCase("")){
            Assert.assertEquals(sub_category,locateElementBy("bulk_edit_obligations_sub_category_showpage").getText());
        }
        if(!timezone.equalsIgnoreCase("")){
            Assert.assertEquals(timezone,locateElementBy("bulk_edit_obligations_timezone_showpage").getText());
        }
        if(!delivery_countries.equalsIgnoreCase("")){
            Assert.assertEquals(delivery_countries,locateElementBy("bulk_edit_obligations_delivery_countries_multi_showpage").getText());
        }
        if(!priority.equalsIgnoreCase("")){
                Assert.assertEquals(priority,locateElementBy("bulk_edit_obligations_priority_showpage").getText());
        }
        if(!phase.equalsIgnoreCase("")){
            Assert.assertEquals(phase,locateElementBy("bulk_edit_obligations_phase_showpage").getText());
        }
        if(!references.equalsIgnoreCase("")){
            Assert.assertEquals(references,locateElementBy("bulk_edit_obligations_references_showpage").getText());
        }
        if(!triggered.equalsIgnoreCase("")){
            Assert.assertEquals(triggered,locateElementBy("bulk_edit_obligations_triggered_showpage").getText());
        }
        if(!frequency_type.equalsIgnoreCase("")){
            Assert.assertEquals(frequency_type,locateElementBy("bulk_edit_obligations_frequency_type_showpage").getText());
        }
        if(!frequency.equalsIgnoreCase("")){
            Assert.assertEquals(frequency,locateElementBy("bulk_edit_obligations_frequency_showpage").getText());
        }
        if(!week_type.equalsIgnoreCase("")){
            Assert.assertEquals(week_type,locateElementBy("bulk_edit_obligations_week_type_showpage").getText());
        }
        if(!start_date.equalsIgnoreCase("")){
            Assert.assertEquals(start_date,locateElementBy("bulk_edit_obligations_start_date_showpage").getText());
        }
        if(!end_date.equalsIgnoreCase("")){
            Assert.assertEquals(end_date,locateElementBy("bulk_edit_obligations_end_date_showpage").getText());
        }
        if(!include_start_date.equalsIgnoreCase("")){
            Assert.assertEquals(include_start_date,locateElementBy("bulk_edit_obligations_include_start_date_showpage").getText());
        }
        if(!include_end_date.equalsIgnoreCase("")){
            Assert.assertEquals(include_end_date,locateElementBy("bulk_edit_obligations_include_end_date_showpage").getText());
        }
        if(!pattern_date.equalsIgnoreCase("")){
            Assert.assertEquals(pattern_date,locateElementBy("bulk_edit_obligations_pattern_date_showpage").getText());
        }
        if(!effective_date.equalsIgnoreCase("")){
            Assert.assertEquals(effective_date,locateElementBy("bulk_edit_obligations_effective_date_showpage").getText());
        }
        if(!vendor_contracting_party.equalsIgnoreCase("")){
            Assert.assertEquals(vendor_contracting_party,locateElementBy("bulk_edit_obligations_vendor_contracting_party_multi_showpage").getText());
        }
        if(!recipient_client_entity.equalsIgnoreCase("")){
            Assert.assertEquals(recipient_client_entity,locateElementBy("bulk_edit_obligations_recipient_client_entities_multi_showpage").getText());
        }
        if(!company_code.equalsIgnoreCase("")){
            Assert.assertEquals(company_code,locateElementBy("bulk_edit_obligations_recipient_company_codes_multi_showpage").getText());
        }
        /*if(!master_obligations_manager.equalsIgnoreCase("")){
            Assert.assertEquals(master_obligations_manager,locateElementBy("bulk_edit_responsibility_showpage").getText());
        }*/
        if(!responsibility.equalsIgnoreCase("")){
            Assert.assertEquals(responsibility,locateElementBy("bulk_edit_obligations_responsibility_showpage").getText());
        }
        if(!financial_impact_applicable.equalsIgnoreCase("")){
            Assert.assertEquals(financial_impact_applicable,locateElementBy("bulk_edit_obligations_financial_impact_applicable_showpage").getText());
        }
        try {
            if (!financial_impact_value.equalsIgnoreCase("")) {
                Assert.assertEquals(financial_impact_value, locateElementBy("bulk_edit_obligations_financial_impact_value_showpage").getText());
            }
            if (!financial_impact_clause.equalsIgnoreCase("")) {
                Assert.assertEquals(financial_impact_clause, locateElementBy("bulk_edit_obligations_financial_impact_clause_showpage").getText());
            }
            if (!impact_days.equalsIgnoreCase("")) {
                Assert.assertEquals(impact_days, locateElementBy("bulk_edit_obligations_impact_days_showpage").getText());
            }
            if (!impact_type.equalsIgnoreCase("")) {
                Assert.assertEquals(impact_type, locateElementBy("bulk_edit_obligations_impact_type_multi_showpage").getText());
            }
        }catch (NoSuchElementException e){
            Logger.debug("Financial Impact Applicabel is False");
        }
        if(!credit_impact_applicable.equalsIgnoreCase("")){
            Assert.assertEquals(credit_impact_applicable,locateElementBy("bulk_edit_obligations_credit_impact_applicable_showpage").getText());
        }
        try {
            if (!credit_impact_value.equalsIgnoreCase("")) {
                Assert.assertEquals(credit_impact_value, locateElementBy("bulk_edit_obligations_credit_impact_value_showpage").getText());
            }
            if (!credit_impact_clause.equalsIgnoreCase("")) {
                Assert.assertEquals(credit_impact_clause, locateElementBy("bulk_edit_obligations_credit_impact_clause_showpage").getText());
            }
        }catch (NoSuchElementException e1){
            Logger.debug("Credit Impact Applicabel is False");
        }
        if(!projectid.equalsIgnoreCase("")){
            Assert.assertEquals(projectid,locateElementBy("bulk_edit_obligations_project_id_multi_showpage").getText());
        }
        if(!project_levels.equalsIgnoreCase("")){
            Assert.assertEquals(project_levels,locateElementBy("bulk_edit_obligations_project_levels_multi_showpage").getText());
        }
        if(!initiatives.equalsIgnoreCase("")){
            Assert.assertEquals(initiatives,locateElementBy("bulk_edit_obligations_initiatives_multi_showpage").getText());
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
