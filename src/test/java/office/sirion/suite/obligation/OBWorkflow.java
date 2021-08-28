package office.sirion.suite.obligation;

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

public class OBWorkflow extends TestSuiteBase {
    String result = null;
    String runmodes[] = null;
    static int count = -1;
    static boolean isTestPass = true;
    String testCaseID;

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(obligation_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(obligation_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void OBWorkflowTest(String obWorkflowSteps, String obParent) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

          openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Obligation Workflow with Name -- " + obParent);

        driver.get(CONFIG.getProperty("endUserURL"));

        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();

        fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

        driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();

        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!obParent.equalsIgnoreCase("") && obParent.length() <= 30) {
            try {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/a[./text()='" + obParent + "']")).click();
            } catch (NoSuchElementException e) {
                locateElementBy("global_bulk_listing_page_first_entry_link").click();
            }
        } else if (!obParent.equalsIgnoreCase("") && obParent.length() > 30) {
            try {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/div/div/a[./text()='" + obParent + "']")).click();
            } catch (NoSuchElementException ex) {
                locateElementBy("global_bulk_listing_page_first_entry_link").click();
            }
        } else {
            locateElementBy("global_bulk_listing_page_first_entry_link").click();

            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
        }
        // OBLIGATIONS - SHOW PAGE - CLONE BUTTON
        try {
            driver.findElement(By.xpath(".//button[contains(.,'Restore')]")).click();
            WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")).click();

            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Create Obligation')]")));

            WebElement elementSave = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")));
            elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")).click();


        } catch (NoSuchElementException e) {
            WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")).click();

            fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Create Obligation')]")));

            WebElement elementSave = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")));
            elementSave.findElement(By.xpath(".//button[contains(.,'Create Obligation')]")).click();

        }


        if (driver.findElements(By.className("success-icon")).size() != 0) {
            String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

            Logger.debug("Obligation cloned successfully with Entity ID -- " + entityID);

            driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();

            fluentWaitMethod(locateElementBy("ob_show_page_id"));

            String entityShowPageID = locateElementBy("ob_show_page_id").getText();

            Assert.assertEquals(entityShowPageID, entityID);
            Logger.debug("Obligation ID on show page has been verified");
        }


        fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
        Logger.debug("Test Case Obligation Workflow with Name -- " + obParent + " -- is STARTED");

        if (!obWorkflowSteps.equalsIgnoreCase("")) {
            for (String entityData : obWorkflowSteps.split(";")) {
                WebElement elementWorkflow = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
                try {
                    fluentWaitMethod(locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze")));
                    fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'" + entityData.trim() + "')]")));
                    Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'" + entityData.trim() + "')]")));
                    elementWorkflow.findElement(By.xpath("//button[contains(.,'" + entityData.trim() + "')]")).click();

                } catch (NoSuchElementException e) {
                    Logger.debug("No Such Element with the given Key, -- " + entityData + " -- Moving onto Next Step");
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
                    String entityShowPageStatus = locateElementBy("ob_show_page_status").getText();

                    Assert.assertEquals(entityShowPageStatus, "Active");
                    Logger.debug("Obligation Status on show page has been verified");
                }

                try {
                    fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
                } catch (NoSuchElementException e) {
                    driver.get(CONFIG.getProperty("endUserURL"));
                }
            }

        }

        Logger.debug("Test Case Obligation Workflow with Name -- " + obParent + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
    }

    @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
          for (int i = 2; i <= obligation_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
              String testCaseID = obligation_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
            TestUtil.reportDataSetResult(obligation_suite_xls, "Test Cases", TestUtil.getRowNum(obligation_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(obligation_suite_xls, "Test Cases", TestUtil.getRowNum(obligation_suite_xls, this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(obligation_suite_xls, this.getClass().getSimpleName());
    }
}