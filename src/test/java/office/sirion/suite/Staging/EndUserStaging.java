package office.sirion.suite.Staging;

import office.sirion.suite.Staging.TestSuiteBase;
import office.sirion.util.TestUtil;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

public class EndUserStaging  extends TestSuiteBase {
    String runmodes[] = null;
    String result = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    static String URL="http://qavf.autodev.office";
    static String Username="anuvrata_user1";
    static String Password="admin123";

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {

        if (!TestUtil.isTestCaseRunnable(staging_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// logs
            throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
        }
        // load the runmodes off the tests
        runmodes = TestUtil.getDataSetRunmodes(staging_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void EndUserStagingTest() throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }


        openBrowser();

        driver.get(URL);
        Thread.sleep(5000);

        driver.findElement(By.id("newTextUser")).sendKeys(Username);
        driver.findElement(By.id("newTextPassword")).sendKeys(Password);
        driver.findElement(By.id("newLoginButton")).click();
        Thread.sleep(15000);

        //driver.findElement(By.xpath("//html//div[@ng-if='stagingAreaEnabled']/div[2]")).click();
        driver.get(URL+"/#/stagingdashboard");
        Thread.sleep(8000);
        driver.findElement(By.xpath("//*[@id='rt-contract']/a")).click();
        Thread.sleep(8000);
        fluentWaitMethod(driver.findElement(By.xpath("//select[@name='cr_length']")));

        new Select(driver.findElement(By.xpath("//select[@name='cr_length']"))).selectByVisibleText("100");
        Thread.sleep(5000);

        //First Entry on Listing page
        String EntityID = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[1]/td[1]/a")).getText();
        driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[1]/td[1]/a")).click();
        Thread.sleep(7000);

        try{
            driver.findElement(By.xpath("//*[@class='alertdialog-icon']"));

            if (driver.findElement(By.xpath("//*[@class='alertdialog-icon']")).getText().equalsIgnoreCase("Server Error")) {

                driver.findElement(By.xpath("//button[@ng-click='cancel()']")).click();

                Logger.debug("Staging is Working, but Contract show page is not getting Open, Server Error is displayed");
                fluentWaitMethod(driver.findElement(By.xpath("//*[@id='rt-contract']/a")));
                Thread.sleep(5000);
                driver.get(URL);
            }
        } catch (NoSuchElementException ex){
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='generalInfo']/div[2]/div/div[1]/div/label/span[2]")));
            String BatchID = driver.findElement(By.xpath("//*[@id='generalInfo']/div[2]/div/div[1]/div/label/span[2]")).getText();
            Assertion(EntityID, BatchID);

            Logger.debug("Staging is Working and User is able to view staging based Contract");
            driver.get(URL);
        }
    }

    @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(staging_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(staging_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(staging_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
			for (int i = 2; i <= staging_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = staging_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
            TestUtil.reportDataSetResult(staging_suite_xls, "Test Cases", TestUtil.getRowNum(staging_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(staging_suite_xls, "Test Cases", TestUtil.getRowNum(staging_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(staging_suite_xls, this.getClass().getSimpleName());
    }
}
