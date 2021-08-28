package office.sirion.suite.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.JSWaiter;
import office.sirion.util.TestAuditLogUtil;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
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

public class ActionAuditLog extends TestSuiteBase {
  String runmodes[] = null;
  String result = null;
  static int count = -1;
  static boolean fail = true;
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
  public void ActionAuditLogTest(String actionTitle, String actionDescription, String actionType, String actionPriority, String actionResponsibility,
			String actionTimeZone, String actionCurrency, String actionSupplierAccess, String actionTier,String actionRequestedOn, String actionDueDate, String actionProcessAreaImpacted, 
			String actionActionTaken, String actionComments, String actionActualDate, String actionRequestedBy, String actionChangeRequest, String actionUploadFile,String actionParent) 
			throws InterruptedException, ClassNotFoundException, SQLException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Set Data Set to NO " + count);
    }

    Logger.debug("Executing Test Case of Action Audit Log");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

    locateElementBy("actions_quick_link").click();

	new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

	if (!actionParent.equalsIgnoreCase("")) {
		driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + actionParent + "')]/preceding-sibling::td[1]/a")).click();
		} else {
			locateElementBy("entity_listing_page_first_entry_link").click();
			}
    locateElementBy("entity_edit_button").click();
    

    Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage = TestAuditLogUtil.getEntityMapBeforeUpdate();

    // Actions - Edit Page - BASIC INFORMATION
	addFieldValue("action_create_page_title_textbox", actionTitle);
	
	addFieldValue("action_create_page_description_textarea", actionDescription);
	
	addFieldValue("action_create_page_type_dropdown", actionType);

	addFieldValue("action_create_page_priority_dropdown", actionPriority);

	addFieldValue("action_create_page_responsibility_dropdown", actionResponsibility);
	
	addFieldValue("action_create_page_timezone_dropdown", actionTimeZone);
	try {
		if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
			clickWebElement(driver.findElement(By.xpath(".//button[contains(.,'OK')]")));
		} catch (Exception e) {
			
		}
		
	addFieldValue("action_create_page_currency_dropdown", actionCurrency);

	addFieldValue("action_create_page_supplier_access_checkbox", actionSupplierAccess);
	
	
	addFieldValue("action_create_page_tier_dropdown", actionTier);
	
	
	// Actions - Edit Page - IMPORTANT DATES
	DatePicker_Enhanced date = new DatePicker_Enhanced();
	date.selectCalendar(actionRequestedOn,"requestedOn");
	date.selectCalendar(actionDueDate,"plannedCompletionDate");
		
	// Actions - Edit Page - COMMENTS AND ATTACHMENTS
	addFieldValue("entity_create_page_comments_textarea", actionComments);
	    
	addFieldValue("entity_create_page_requested_by_dropdown", actionRequestedBy);
	
	date.selectCalendar(actionActualDate,"actualDate");
	    
	addFieldValue("action_change_request_dropdown", actionChangeRequest);
	
	if (!actionUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"//file-upload//Action//" + actionUploadFile);

		}
	    
	addFieldValue("entity_create_page_change_request_dropdown", actionChangeRequest);
	
	if (!actionUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"//file-upload//Action//" + actionUploadFile);

		}
	
	Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = TestAuditLogUtil.getEntityMapAfterUpdate(entityMapBeforeUpdatePage);
	
	// ACTIONS - EDIT PAGE - UPDATE BUTTON
	
	locateElementBy("entity_update_button").click();

    
 // ACTIONS - EDIT PAGE - FIELD VALIDATIONS

 		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
 		if (!validationClassElementList.isEmpty()) {
 			for (WebElement validationClassElement : validationClassElementList) {
 				String validationMessage = validationClassElement.getText();

 				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

 				if (validationMessage.equalsIgnoreCase(""))
 					Logger.debug("For Supplier -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
 				else
 					Logger.debug("For Supplier -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
 				}

 			driver.get(CONFIG.getProperty("endUserURL"));
 			return;
 			}

	    //Navigate to Audit Log Tab

		locateElementBy("action_audit_log_tab").click();

	    Logger.debug("Test Case Supplier Audit Log for -- " + actionTitle + " -- is STARTED");
	    
	    //Audit Log Validations
	    
	locateElementBy("action_audit_log_tab").click();

      Assert.assertEquals(locateElementBy("action_audit_log_action_taken").getText(), "Updated");

    WebElement elementOddRowSelected = driver.findElement(By.id("table_525")).findElements(By.className("odd")).get(0);

    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

    if (!actionRequestedBy.equalsIgnoreCase(""))
		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), actionRequestedBy);
    else
    	Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

    if (!actionComments.equalsIgnoreCase(""))
	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
    else
	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

    if (!actionUploadFile.equalsIgnoreCase(""))
	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
    else
	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

    elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
    locateElementBy("action_audit_log_history_table");

    Map<String, Map<String, String>> auditLogDataMap = TestAuditLogUtil.getAuditLogViewHistoryData();

    TestAuditLogUtil.isAuditLogWorking(entityMapAfterUpdatePage, auditLogDataMap);

    Logger.debug("Test Case Supplier Audit Log for -- " + actionTitle + " -- is PASSED");
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
			System.out.println("test");
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
