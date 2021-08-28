package office.sirion.suite.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class ActionCommunication extends TestSuiteBase {
  String result = null;
  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date = new Date();
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(action_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(action_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void ActionCommunicationTest(String actionComments, String actionActualDate, String actionRequestedBy, String actionChangeRequest,
      String actionUploadFile, String actionTitle) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Action Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

    locateElementBy("actions_quick_link").click();

	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));

	try {
		if (!actionTitle.equalsIgnoreCase("") && actionTitle.length()<=30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='"+actionTitle+"']/preceding-sibling::td[2]/a")).click();
		else if (!actionTitle.equalsIgnoreCase("") && actionTitle.length()>30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='"+actionTitle+"']]/preceding-sibling::td[2]/a")).click();
		else
			locateElementBy("entity_listing_page_first_entry_link").click();
		} catch (Exception e) {
			locateElementBy("entity_listing_page_first_entry_link").click();				
			}

    // Actions - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("action_comment_box_textarea", actionComments);
    locateElementBy("action_blank_area").click();
	addFieldValue("action_requested_by_dropdown", actionRequestedBy);
	    
	DatePicker_Enhanced date = new DatePicker_Enhanced();
	date.selectCalendar(actionActualDate,"actualDate");
	    
	addFieldValue("action_change_request_dropdown", actionChangeRequest);
	
	if (!actionUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Action\\" + actionUploadFile);
		}
	
    Assert.assertNotNull(locateElementBy("action_comment_attachment_button"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        locateElementBy("action_comment_attachment_button"));
    locateElementBy("action_comment_attachment_button").click();

    if (getObject("actions_error_notifications") != null) {
      List<WebElement> e = driver.findElement(By.xpath(".//*[@id='genericErrors']/list")).findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);

        }
      }
      
    }

      locateElementBy("action_show_page_communication_tab").click();
    locateElementBy("action_show_page_communication_tab_inner_part");
	 
    Logger.debug("Test Case Supplier Communication with Name -- " + actionTitle + " -- is STARTED");

    verifyCommunicationTab(actionComments, actionUploadFile);

    Logger.debug("Test Case Supplier Communication with Name -- " + actionTitle + " -- is PASSED");

    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= action_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = action_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(action_suite_xls, this.getClass().getSimpleName());
  }
}
