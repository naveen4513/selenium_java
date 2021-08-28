package office.sirion.suite.governanceBody;

import java.io.IOException;
import java.util.Date;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

public class GBCommunication extends TestSuiteBase {
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	String runmodes[] = null;
	Date date;
	Date date1;
	public static String result=null;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(governance_body_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(governance_body_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void GBCommunicationTest(String gbComments, String gbActualDate, String gbRequestedBy, String gbChangeRequest,
			String gbUploadFile, String gbParent) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
		Logger.debug("Executing Test Case Governance Body Creation with Title --- " + gbParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("gb_quick_link"));

	    
	    locateElementBy("gb_quick_link").click();

		fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

		driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();

	    

		if (!gbParent.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][contains(.,'" + gbParent + "')]/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                locateElementBy("entity_listing_page_first_entry_link").click();
            }
        }

		
		locateElementBy("gb_save_comment_attachment_button").click();
		
		fluentWaitMethod(locateElementBy("gb_quick_link"));
		

		
		if (driver.findElement(By.xpath("//*[@id='genericErrors']")).isDisplayed()){
			System.out.println("Error message is displayed");
		}else{
			System.out.println("Error message is not displayed");
		}
	}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		
			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}

		try {
			for (int i = 2; i <= governance_body_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = governance_body_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()), "FAIL");
			}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(governance_body_suite_xls, this.getClass().getSimpleName());
		}
}
