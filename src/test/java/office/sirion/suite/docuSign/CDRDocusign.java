package office.sirion.suite.docuSign;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;
import org.testng.annotations.*;

public class CDRDocusign extends TestSuiteBase {
	String testCaseID;
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(docusign_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(docusign_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CDRDocusignTest ()throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "+ count);
		}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    Logger.debug("Executing Test Case of Docusign on CDR Entity");
	    driver.get(CONFIG.getProperty("endUserURL"));

		//Thread.sleep(5000);
		fluentWaitMethod(locateElementBy("walk_mepopup_close_button"));
	    locateElementBy("walk_mepopup_close_button").click();
		fluentWaitMethod(driver.findElement(By.xpath("//*[@id='quick-nav']/div/ul[2]")));

		WebElement we = driver.findElement(By.xpath("//*[@id='quick-nav']/div/ul[2]"));

		Actions ac = new Actions(driver);
		ac.moveToElement(we).build().perform();

		locateElementBy("authoring_group_quick_link").click();
		locateElementBy("cdr_quick_link").click();
		locateElementBy("end_user_search_link").click();
		fluentWaitMethod(locateElementBy("cdr_first_entity_listing_page"));
	    
		locateElementBy("cdr_first_entity_listing_page").click();

		fluentWaitMethod(locateElementBy("cdr_entity_communication_icon"));
		locateElementBy("cdr_entity_communication_icon").click();

		locateElementBy("cdr_entity_comment_attach_file_button").click();
	    
		locateElementBy("cdr_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CDR\\ChangeRequestForm_MS_Minor_Release_02112019.docx");

		locateElementBy("cdr_file_comment_add_button").click();
		// DISPUTE - CREATE PAGE - BASIC INFORMATION


	}

	/*@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(docusign_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(docusign_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(docusign_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= docusign_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = docusign_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}
	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(docusign_suite_xls, "Test Cases", TestUtil.getRowNum(docusign_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(docusign_suite_xls, "Test Cases", TestUtil.getRowNum(docusign_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}*/

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(docusign_suite_xls, this.getClass().getSimpleName());
		}
	}
