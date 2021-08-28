package office.sirion.suite.massEmail;

import java.io.IOException;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MassEmailCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static boolean fail = true;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(mass_email_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(mass_email_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void MassEmailCreationTest(String meUser, String	meDistributionList, String	meEmailId, String meSubject,
			String meScheduleNow, String meScheduleDate, String	meAttachment, String meDescription, String meDisclaimer
) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Mass Email creation for -- " + meSubject);

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
		
		fluentWaitMethod(locateElementBy("suppliers_quick_link"));
		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='gl-email']/a/span"))).click().build().perform();
		
		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='gl-email']/ul/li[2]/a"))).clickAndHold().build().perform();
		Thread.sleep(5000);
		driver.findElement(By.xpath(".//*[@id='gl-email']/ul/li[2]/a")).click();
		//
		//Opening Mass Email create page
	    driver.findElement(By.xpath("//div[@class='tabs-inner-sec-header']//div[@ng-show='plusButton']")).click();
		
	    //
		
		//addFieldValue("me_user_textarea", meUser);
	    driver.findElement(By.xpath(".//*[@id='allAutocomplete']")).sendKeys(meUser);
	    driver.findElement(By.xpath(".//*[@id='dlAutocomplete']")).sendKeys(meDistributionList);
		driver.findElement(By.xpath(".//*[@id='externalEmails']")).sendKeys(meEmailId);
		driver.findElement(By.xpath(".//*[@id='scheduleForm']/div[7]/input")).sendKeys(meSubject);
		//Not added schedule option for BVT

		driver.switchTo().frame("email-body_ifr");
		driver.findElement(By.xpath("//*[@id='tinymce']")).sendKeys(meDescription);
		Thread.sleep(3000);

		driver.switchTo().defaultContent();

		if(driver.findElements(By.xpath("me_disclaimer_options_drop_down")).size()!=0){
				addFieldValue("me_disclaimer_dropdown",meDisclaimer);
		}

		//driver.switchTo().defaultContent();
		//Click on send button
		try {
			locateElementBy("me_footer_buttons").click();
			driver.findElement(By.xpath("//html//div[@class='footerActBtns ng-scope']/input[1]")).click();

		}catch (Exception e){
			Logger.debug("Due to screen resolution, button is not visible");
			return;
		}
		try {
			driver.findElement(By.xpath("//div[@class='modal-content']"));
			if(driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
					equalsIgnoreCase("Either you do not have the required permissions or requested page does not exist anymore.") ||
					driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
							equalsIgnoreCase("Server Error")) {
				Logger.debug("Unable to create Mass Email or Might be Permission issue");
				locateElementBy("entity_show_page_permission_notification_pop_up_ok_button").click();
				fail=true;
				driver.get(CONFIG.getProperty("endUserURL"));
			}
		} catch (Exception exception) {
			if (driver.findElement(By.xpath("//div[@id='alertdialog']")).isDisplayed()) {
				System.out.println(driver.findElement(By.xpath("//div[@id='alertdialog']")).getText());
				fail = false;
				driver.get(CONFIG.getProperty("endUserURL"));
				//
			} else {
				System.out.println("Something happen, Check manually");
				fail = false;
				driver.get(CONFIG.getProperty("endUserURL"));
				//
			}
		}
        }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(mass_email_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(mass_email_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(mass_email_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
			for (int i = 2; i <= mass_email_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = mass_email_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(mass_email_suite_xls, "Test Cases", TestUtil.getRowNum(mass_email_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(mass_email_suite_xls, "Test Cases", TestUtil.getRowNum(mass_email_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(mass_email_suite_xls, this.getClass().getSimpleName());
		}
	}
