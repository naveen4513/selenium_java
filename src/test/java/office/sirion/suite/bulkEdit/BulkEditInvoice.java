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


public class BulkEditInvoice extends TestSuiteBase {

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
    public void BulkEditInvoiceTest(String EntityName,String currency,String bill_to_address,String ship_to_address,String supplier_address,String supplier_access,
                                      String email_address,String phone_number,String invoice_period_start,String invoice_period_end,String credit_period_in_days,String invoice_date,
                                      String payment_term,String expected_receipt_date,String system_receipt_date,String payment_due_date,String actual_payment_date,String actual_receipt_date,
                                      String invoice_manager) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Invoice Creation for -- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        //
        fluentWaitMethod(locateElementBy("inv_quick_link"));

        locateElementBy("inv_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");
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

            if(!currency.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_invoice_currency_dropdown", currency);
            }
            if(!bill_to_address.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_invoice_bill_to_address_textarea", bill_to_address);
            }
            if(!ship_to_address.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_ship_to_address_textarea", ship_to_address);
            }
            if(!supplier_address.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_supplier_address_textarea", supplier_address);
            }
            if(!supplier_access.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_supplier_access_checkbox", supplier_access);
            }
            if(!email_address.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_email_address_textbox", email_address);
            }
            if(!phone_number.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_phone_number_textbox", phone_number);
            }
            if(!invoice_period_start.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(invoice_period_start, "invoicePeriodFromDate");
            }
            if(!invoice_period_end.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(invoice_period_end, "invoicePeriodToDate");
            }
            if(!credit_period_in_days.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_credit_period_in_days_textbox", credit_period_in_days);
            }
            if(!invoice_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(invoice_date, "invoiceDate");
            }
            if(!payment_term.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_payment_term_textbox", payment_term);
            }
            if(!expected_receipt_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(expected_receipt_date, "expectedReceiptDate");
            }
            if(!system_receipt_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(system_receipt_date, "SystemReceiptDate");
            }
            if(!payment_due_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(payment_due_date, "paymentDueDate");
            }
            if(!actual_payment_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(actual_payment_date, "actualPaymentDate");
            }
            if(!actual_receipt_date.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(actual_receipt_date, "actualReceiptDate");
            }
            if(!invoice_manager.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_invoice_invoice_manager_textbox", invoice_manager);
            }

        }

        /*Map<String, String> BeforeBulkEdit = new HashMap<>();
        BeforeBulkEdit.put(EntityName,EntityName);*/

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

        if(!currency.equalsIgnoreCase("")){
            Assert.assertEquals(currency,locateElementBy("bulk_edit_invoice_currency_showpage").getText());
        }
        if(!bill_to_address.equalsIgnoreCase("")){
            Assert.assertEquals(bill_to_address,locateElementBy("bulk_edit_invoice_bill_to_address_showpage").getText());
        }
        if(!ship_to_address.equalsIgnoreCase("")){
            Assert.assertEquals(ship_to_address,locateElementBy("bulk_edit_invoice_ship_to_address_showpage").getText());
        }
        if(!supplier_address.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_address,locateElementBy("bulk_edit_invoice_supplier_address_showpage").getText());
        }
        if(!supplier_access.equalsIgnoreCase("")){
            Assert.assertEquals(supplier_access,locateElementBy("bulk_edit_invoice_supplier_access_showpage").getText());
        }
        if(!email_address.equalsIgnoreCase("")){
            Assert.assertEquals(email_address,locateElementBy("bulk_edit_invoice_email_address_showpage").getText());
        }
        if(!phone_number.equalsIgnoreCase("")){
            Assert.assertEquals(phone_number,locateElementBy("bulk_edit_invoice_phone_number_showpage").getText());
        }
        if(!invoice_period_start.equalsIgnoreCase("")){
            Assert.assertEquals(invoice_period_start,locateElementBy("bulk_edit_invoice_period_start_date_showpage").getText());
        }
        if(!invoice_period_end.equalsIgnoreCase("")){
            Assert.assertEquals(invoice_period_end,locateElementBy("bulk_edit_invoice_period_end_date_showpage").getText());
        }
        if(!credit_period_in_days.equalsIgnoreCase("")){
            Assert.assertEquals(credit_period_in_days,locateElementBy("bulk_edit_invoice_credit_period_in_days_showpage").getText());
        }
        if(!invoice_date.equalsIgnoreCase("")){
            Assert.assertEquals(invoice_date,locateElementBy("bulk_edit_invoice_date_showpage").getText());
        }
        if(!payment_term.equalsIgnoreCase("")){
            Assert.assertEquals(payment_term,locateElementBy("bulk_edit_invoice_payment_term_showpage").getText());
        }
        if(!expected_receipt_date.equalsIgnoreCase("")){
            Assert.assertEquals(expected_receipt_date,locateElementBy("bulk_edit_invoice_expected_reciept_date_showpage").getText());
        }
        if(!system_receipt_date.equalsIgnoreCase("")){
            Assert.assertEquals(system_receipt_date,locateElementBy("bulk_edit_invoice_system_reciept_date_showpage").getText());
        }
        if(!payment_due_date.equalsIgnoreCase("")){
            Assert.assertEquals(payment_due_date,locateElementBy("bulk_edit_invoice_payment_due_date_showpage").getText());
        }
        if(!actual_payment_date.equalsIgnoreCase("")){
            Assert.assertEquals(actual_payment_date,locateElementBy("bulk_edit_invoice_actual_payment_date_showpage").getText());
        }
        if(!actual_receipt_date.equalsIgnoreCase("")){
            Assert.assertEquals(actual_receipt_date,locateElementBy("bulk_edit_invoice_actual_reciept_date_showpage").getText());
        }
        if(!invoice_manager.equalsIgnoreCase("")){
            Assert.assertEquals(invoice_manager,locateElementBy("bulk_edit_invoice_manager_stakeholder_showpage").getText());
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
