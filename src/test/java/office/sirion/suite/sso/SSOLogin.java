package office.sirion.suite.sso;

import office.sirion.suite.sso.TestSuiteBase;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.*;

public class SSOLogin  extends TestSuiteBase {
    String runmodes[] = null;
    static int count = -1;
    static boolean fail = true;
    static boolean skip = false;
    static boolean isTestPass = true;
    static String URL="http://qa.rc.office";
    static String Username="rc25";
    static String Password="admin123";

    // Runmode of test case in a suite
    @BeforeTest
    public void checkTestSkip() {

        if (!TestUtil.isTestCaseRunnable(sso_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// logs
            throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
        }
        // load the runmodes off the tests
        runmodes = TestUtil.getDataSetRunmodes(sso_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void SSOLoginTest(String testCaseID) throws InterruptedException {

        // test the runmode of current dataset
        count++;
        if (!runmodes[count].equalsIgnoreCase("Y")) {
            skip = true;
            throw new SkipException("Runmode for test set data set to no " + count);
        }

        openBrowser();
        driver.get(URL);


        driver.findElement(By.xpath("//a[@href='/saml?savedRequest=']")).click();
        /*driver.findElement(By.id("newTextUser")).sendKeys(Username);
        driver.findElement(By.id("newTextPassword")).sendKeys(Password);
        driver.findElement(By.id("newLoginButton")).click();
        //

        driver.findElement(By.xpath("//*[@id='headerNavbar']/div[3]/div[2]")).click();
        Thread.sleep(8000);
        driver.findElement(By.xpath("//*[@id='rt-contract']/a")).click();
        Thread.sleep(8000);
        fluentWaitMethod(driver.findElement(By.xpath("//select[@name='cr_length']"))));

        new Select(driver.findElement(By.xpath("//select[@name='cr_length']"))).selectByVisibleText("100");
        Thread.sleep(5000);

        //First Entry on Listing page
        String EntityID = driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[1]/td[1]/a")).getText();
        driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[1]/td[1]/a")).click();
        Thread.sleep(7000);

        try{
            driver.findElement(By.xpath("//*[@class='alertdialog-icon']"));

            if (driver.findElement(By.xpath("//*[@class='alertdialog-icon']")).getText().equalsIgnoreCase("Server Error")) {
                //
                driver.findElement(By.xpath("//button[@ng-click='cancel()']")).click();

                Logger.debug("Staging is Working, but Contract show page is not getting Open, Server Error is displayed");
                fluentWaitMethod(By.xpath("//*[@id='rt-contract']/a")));
                Thread.sleep(5000);
                driver.get(URL);
            }
        } catch (NoSuchElementException ex){
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='generalInfo']/div[2]/div/div[1]/div/label/span[2]")));
            String BatchID = driver.findElement(By.xpath("//*[@id='generalInfo']/div[2]/div/div[1]/div/label/span[2]")).getText();
            Assertion(EntityID, BatchID);

            Logger.debug("Staging is Working and User is able to view staging based Contract");
            driver.get(URL);
            driver.quit();
        }*/
    }

    @AfterMethod
    public void reportDataSetResult() {
        if (skip)
            TestUtil.reportDataSetResult(sso_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");
        else if (fail) {
            isTestPass = false;
            TestUtil.reportDataSetResult(sso_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
        } else
            TestUtil.reportDataSetResult(sso_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");

        skip = false;
        fail = true;

    }

    @AfterTest
    public void reportTestResult() {
        if (isTestPass)
            TestUtil.reportDataSetResult(sso_suite_xls, "Test Cases", TestUtil.getRowNum(sso_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(sso_suite_xls, "Test Cases", TestUtil.getRowNum(sso_suite_xls, this.getClass().getSimpleName()), "FAIL");

    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(sso_suite_xls, this.getClass().getSimpleName());
    }
}
