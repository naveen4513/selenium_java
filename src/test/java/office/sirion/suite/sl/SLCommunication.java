package office.sirion.suite.sl;

import java.io.IOException;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SLCommunication extends TestSuiteBase {
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
	public void SLCommunicationTest (String slComments, String slActualDate, String slRequestedBy, String slChangeRequest,
			String slUploadFile, String slParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Service Level Communication Tab with Title -- " + slParent);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("sl_quick_link"));

		locateElementBy("sl_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!slParent.equalsIgnoreCase("") && slParent.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+slParent+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();}
		else if (!slParent.equalsIgnoreCase("") && slParent.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+slParent+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();}
		else
			locateElementBy("global_bulk_listing_page_first_entry_link").click();

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
		
		// SERVICE LEVELS - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", slComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", slRequestedBy);
		    
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(slActualDate,"actualDate");
		
		addFieldValue("entity_create_page_change_request_dropdown", slChangeRequest);
		
		if (!slUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\SL\\" + slUploadFile);

			}
		
		// SERVICE LEVELS - SHOW PAGE - Save Comment/Attachment BUTTON
	    WebElement elementSaveComment = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")).click();

	    fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));
		
		// SERVICE LEVELS - SHOW PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();

				if (entityErrors.contains("Please add a comment or upload files")) {
					Logger.debug("For Service Level -- " +slParent+ " -- Please add a comment or upload files");
			        }
				}

		    driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}

	    fluentWaitMethod(driver.findElement(By.linkText("COMMUNICATION")));
		driver.findElement(By.linkText("COMMUNICATION")).click();

	    Logger.debug("Test Case Service Level Communication Tab with Title -- " + slParent + " is STARTED");

	    verifyCommunicationTab(slComments, slUploadFile);

	    Logger.debug("Test Case Service Level Communication Tab with Title -- " + slParent + " is PASSED");
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
			TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(sl_suite_xls, this.getClass().getSimpleName());
		}
	}