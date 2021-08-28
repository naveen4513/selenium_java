package office.sirion.suite.bulkEdit;


import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class BulkEditActions extends TestSuiteBase {

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
    public void BulkEditActionsTest(String EntityName,String Type,String Priority,String Responsibility,String SupplierAccess,String RequestedOn, String DueDate,String IssuesManager)
            throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Action Creation with Titles ---- " + EntityName);

        driver.get(CONFIG.getProperty("endUserURL"));
        fluentWaitMethod(locateElementBy("actions_quick_link"));
        //

        locateElementBy("actions_quick_link").click();
        //
        fluentWaitMethod(locateElementBy("action_listing_page_plus_button"));


        if(!EntityName.equalsIgnoreCase("")) {
            for (String name : EntityName.split(";")) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='" + name + "']/preceding-sibling::td//input")).click();
                String ids = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][./text()='"+ name +"']/preceding-sibling::td[1]/a")).getText();
                EntityID.add(ids);
            }

            locateElementBy("bulk_edit_listing_page_button").click();
            try {
                if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Please select at least one entity")) {
                    driver.findElement(By.xpath("//span[contains(text(),'OK')]")).click();
                    driver.findElement(By.xpath("//tr[1]//td[1]//input[1]")).click();
                    locateElementBy("bulk_edit_listing_page_button").click();
                }
            }catch (NoSuchElementException e){
                Logger.debug("Entities are selected for Bulk Edit");
            }

            locateElementBy("bulk_edit_search_dropdown").click();

            driver.findElement(By.xpath("//div[@class='DDsearchList']//div[1]//span[1]//input[1]")).click();//Select All fields in Basic Information Section
            driver.findElement(By.xpath("//div[@class='DDsearchList']//div[2]//span[1]//input[1]")).click();//Select All fields in Important Dates Section
            driver.findElement(By.xpath("//div[@class='DDsearchList']//div[3]//span[1]//input[1]")).click();//Select All fields in StakeHolders Section

            driver.findElement(By.xpath("//a[contains(text(),'GENERAL')]")).click();

            if(!Type.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_actions_type_dropdown", Type);
            }
            if(!Priority.equalsIgnoreCase("")) {
                addFieldValue("bulk_edit_actions_priority_dropdown", Priority);
            }
            if(!SupplierAccess.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_actions_supplier_access_checkbox", SupplierAccess);
            }
            if(!RequestedOn.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(RequestedOn, "requestedOn");
            }
            if(!DueDate.equalsIgnoreCase("")){
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(DueDate, "plannedCompletionDate");
            }
            /*if(!IssuesManager.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_actions_manager_stakeholder", IssuesManager);
            }*/
            if(!Responsibility.equalsIgnoreCase("")){
                addFieldValue("bulk_edit_actions_responsibility_dropdown", Responsibility);
            }

        }
        driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
        Thread.sleep(7000);

        if(driver.findElement(By.className("success-icon")).getText().equalsIgnoreCase("Your request has been successfully submitted.")){
            driver.findElement(By.xpath("//button[@ng-click='cancel()']")).click();
        }

        try{
            driver.findElement(By.xpath("//div[@class='icon lock_icon']"));
            Logger.debug("Entities are locked for Bulk operations");
        }catch (NoSuchElementException e){
            Logger.debug("Entities are now unlocked for Bulk operations");
        }

        driver.navigate().refresh();

        driver.findElement(By.xpath("//input[@id='searchtext']")).click();
        for(String id: EntityID) {
            driver.findElement(By.xpath("//input[@id='searchtext']")).sendKeys(id);
            driver.findElement(By.xpath("//input[@id='searchtext']")).sendKeys(Keys.RETURN);
        }

        if(!Type.equalsIgnoreCase("")){
            Assert.assertEquals(Type,locateElementBy("bulk_edit_actions_type_showpage").getText());
        }

        if(!Priority.equalsIgnoreCase("")){
            Assert.assertEquals(Priority,locateElementBy("bulk_edit_actions_priority_showpage").getText());
        }

        if(!SupplierAccess.equalsIgnoreCase("")){
            Assert.assertEquals(SupplierAccess,locateElementBy("bulk_edit_actions_supplier_access_showpage").getText());
        }

        if(!RequestedOn.equalsIgnoreCase("")){
            Assert.assertEquals(RequestedOn,locateElementBy("bulk_edit_actions_date_showpage").getText());
        }

        if(!DueDate.equalsIgnoreCase("")){
            Assert.assertEquals(DueDate,locateElementBy("bulk_edit_actions_planned_completion_date_showpage").getText());
        }

            /*if(!IssuesManager.equalsIgnoreCase("")){
                Assert.assertEquals(IssuesManager,locateElementBy("bulk_edit_actions_manager_stakeholder_showpage").getText());
            }*/

        if(!Responsibility.equalsIgnoreCase("")){
            Assert.assertEquals(Responsibility,locateElementBy("bulk_edit_actions_responsibility_showpage").getText());
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
