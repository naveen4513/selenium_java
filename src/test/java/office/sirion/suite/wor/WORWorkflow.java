package office.sirion.suite.wor;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;

public class WORWorkflow extends TestSuiteBase {
    String result = null;
    String runmodes[] = null;
    static int count = -1;
    static boolean fail = false;
    static boolean skip = false;
    static boolean isTestPass = true;

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(wor_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(wor_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void WORWorkflowTest(String WorkflowSteps, String worParent) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);
        }

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case WOR Workflow with Name --- " + worParent);

        driver.get(CONFIG.getProperty("endUserURL"));

        fluentWaitMethod(locateElementBy("wor_quick_link"));


        locateElementBy("wor_quick_link").click();

        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

        try {
            driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + worParent + "')]/preceding-sibling::td[1]/a")).click();
        } catch (NoSuchElementException e) {
            locateElementBy("entity_listing_page_first_entry_link").click();
        }

        if (!WorkflowSteps.equalsIgnoreCase("")) {
            for (String entityData : WorkflowSteps.split(";")) {
                try {
                    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'" + entityData.trim() + "')]")));

                    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'" + entityData.trim() + "')]")));
                    driver.findElement(By.xpath("//button[contains(.,'" + entityData.trim() + "')]")).click();

                } catch (NoSuchElementException e) {
                    Logger.debug("No Such Element with the given Key, --- " + entityData + " --- Moving onto Next Step");
                }
            }
        }

        fail = false;

        driver.get(CONFIG.getProperty("endUserURL"));
    }

    @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= wor_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = wor_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}
    @AfterTest
    public void reportTestResult() {
        if (isTestPass)
            TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(wor_suite_xls, this.getClass().getSimpleName());
    }
}