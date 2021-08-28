package office.sirion.suite.sl;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;

public class SLWorkflow extends TestSuiteBase {
    String result = null;
    String runmodes[] = null;
    static int count = -1;
    static boolean isTestPass = true;
    
    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(sl_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(sl_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void SLWorkflowTest(String slWorkflowSteps, String slParent
    ) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

       

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case SL Workflow with Name -- " + slParent);

        driver.get(CONFIG.getProperty("endUserURL"));

        fluentWaitMethod(locateElementBy("sl_quick_link"));

        locateElementBy("sl_quick_link").click();

        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!slParent.equalsIgnoreCase("") && slParent.length() <= 30)
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + slParent + "']/preceding-sibling::td[2]/a")).click();
            } catch (NoSuchElementException e) {
                locateElementBy("entity_listing_page_first_entry_link").click();
            }
        else if (!slParent.equalsIgnoreCase("") && slParent.length() > 30)
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + slParent + "']]/preceding-sibling::td[2]/a")).click();
            } catch (NoSuchElementException e) {
                locateElementBy("entity_listing_page_first_entry_link").click();
            }
        else {
            locateElementBy("global_bulk_listing_page_first_entry_link").click();

            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

            // SERVICE LEVELS - SHOW PAGE - CLONE BUTTON
            WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")).click();

            fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));

            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Create SL')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Create SL')]")));
            driver.findElement(By.xpath("//button[contains(.,'Create SL')]")).click();


            if (driver.findElements(By.className("success-icon")).size() != 0) {
                String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

                Logger.debug("SL cloned successfully with Entity ID " + entityID);

                driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
                fluentWaitMethod(locateElementBy("sl_show_page_id"));


                String entityShowPageID = locateElementBy("sl_show_page_id").getText();

                Assert.assertEquals(entityShowPageID, entityID);
                Logger.debug("SL ID on show page has been verified");
            }
        }

        try {
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

        }catch (NoSuchElementException e){

        }

        Logger.debug("Test Case SL Workflow with Name -- " + slParent + " -- is STARTED");
        if (!slWorkflowSteps.equalsIgnoreCase("")) {
            for (String entityData : slWorkflowSteps.split(";")) {
                WebElement elementWorkflow = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                try {
                    fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
                    fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'" + entityData.trim() + "')]")));
                    Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'" + entityData.trim() + "')]")));
                    elementWorkflow.findElement(By.xpath("//button[contains(.,'" + entityData.trim() + "')]")).click();

                } catch (NoSuchElementException e) {
                    Logger.debug("No Such Element with the given Key, --- " + entityData + " --- Moving onto Next Step");
                }

                if (entityData.trim().equalsIgnoreCase("Delete")) {
                    if (driver.findElements(By.className("success-icon")).size() != 0) {
                        String entityDeleteWFMessage = driver.findElement(By.className("success-icon")).getText();

                        if (entityDeleteWFMessage.equalsIgnoreCase("Are you sure you would like to delete this entity?")) {
                            Assert.assertEquals(entityDeleteWFMessage, "Are you sure you would like to delete this entity?");

                            Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Yes')]")));
                            driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();

                        }
                    }
                }

                if (entityData.trim().equalsIgnoreCase("Publish")) {
                    String entityShowPageStatus = locateElementBy("sl_show_page_status").getText();

                    Assert.assertEquals(entityShowPageStatus, "Active");
                    Logger.debug("SL Status on show page has been verified");
                }

                fluentWaitMethod(driver.findElement(By.id("h-analytics")));
            }
        }

        Logger.debug("Test Case SL Workflow with Name -- " + slParent + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));

    }

    @AfterMethod
   	public void reportDataSetResult(ITestResult testResult) throws IOException {
   		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

   		if(testResult.getStatus()==ITestResult.SKIP)
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
   		else if(testResult.getStatus()==ITestResult.FAILURE) {
   			isTestPass=false;
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
   			result= "Fail";
   			}
   		else if (testResult.getStatus()==ITestResult.SUCCESS) {
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
   			result= "Pass";
   			}
   		try {
             for (int i = 2; i <= sl_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                 String testCaseID = sl_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                 if (!testCaseID.equalsIgnoreCase("")) {
                     updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
                 }
             }
   	   }catch(Exception e){
             Logger.debug(e);
         }
   		}

    @AfterTest
    public void reportTestResult() {
        if (isTestPass)
            TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(sl_suite_xls, this.getClass().getSimpleName());
    }
}