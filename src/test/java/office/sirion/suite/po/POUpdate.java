package office.sirion.suite.po;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

public class POUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(po_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(po_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void PurchaseOrderTest(String poPONumber, String poName, String poRequisitionNumber, String poTrackingNumber, 
			String poDescription, String poContractingEntity, String poServiceSubCategory, String poTimeZone, 
			String poCurrency, String poActive, String poTier, String poBusinessUnit, String poDepartment, String poCostCenter, 
			String poStartDate, String poEndDate, String poCountries, String poStates, String poFunctions, String poServices, 
			String poPOTotal, String poExpectedBurn, String poBurn, String poAvailable, String poComments, String poRequestedBy,
			String poChangeRequest, String poUploadFile, String poSource, String poSourceName, String poParent)
			throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		
	    Logger.debug("Executing Test Case PO Update with Name --- " + poName);
	    
	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("po_quick_link"));


		locateElementBy("po_quick_link").click();
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!poParent.equalsIgnoreCase("")) {
			try {
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + poParent + "')]/preceding-sibling::td[2]/a")).click();
			} catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link_po").click();
			}
			} else {
				locateElementBy("entity_listing_page_first_entry_link_po").click();
				}

		
		try{
			driver.findElement(By.xpath("//button[contains(.,'Restore')]")).click();
			String entityWFMessage = driver.findElement(By.className("success-icon")).getText();
			if (entityWFMessage.equalsIgnoreCase("In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?")) {
				Assert.assertEquals(entityWFMessage, "In addition to the present contract, would you like to perform this action for its underlying contracts with the same status?");

				Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Yes')]")));
				driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();

				}

			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
		    fluentWaitMethod(locateElementBy("po_create_page_po_number_textbox"));

		}catch (NoSuchElementException e){
			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		    driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
		    fluentWaitMethod(locateElementBy("po_create_page_po_number_textbox"));
		}
		
		// Purchase Order - Create Page - BASIC INFORMATION		
		addFieldValue("po_create_page_po_number_textbox", poPONumber+getSaltString());
	    
		addFieldValue("po_create_page_name_textbox", poName+getSaltString());
		
		addFieldValue("po_create_page_requisition_number_textbox", poRequisitionNumber+getSaltString());
		
		addFieldValue("po_create_page_tracking_number_textbox", poTrackingNumber);
		
		addFieldValue("po_create_page_description_textarea", poDescription);
		
		addFieldValue("po_create_page_contracting_entity_dropdown", poContractingEntity);
		
		addFieldValue("po_create_page_service_sub_category_dropdown", poServiceSubCategory);
		
		addFieldValue("po_create_page_timezone_dropdown", poTimeZone);
		
		addFieldValue("po_create_page_currency_dropdown", poCurrency);
		
		addFieldValue("po_create_page_active_checkbox", poActive);
		
		addFieldValue("po_create_page_tier_dropdown", poTier);
		
		// Purchase Order - Create Page - ORGANIZATION INFORMATION		
		addFieldValue("po_create_page_business_unit_multiselect", poBusinessUnit);
		
		addFieldValue("po_create_page_department_multiselect", poDepartment);
		
		addFieldValue("po_create_page_cost_center_multiselect", poCostCenter);
		
		// Purchase Order - Create Page - IMPORTANT DATES
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		addFieldValue("po_create_page_start_date", poStartDate);
		date.selectCalendar(poEndDate,"endDate");
		
		// Purchase Order - Create Page - GEOGRAPHY
		addFieldValue("po_create_page_country_multiselect", poCountries);
		
		addFieldValue("po_create_page_state_multiselect", poStates);
		
		// Purchase Order - Create Page - FUNCTION
		addFieldValue("po_create_page_function_multiselect", poFunctions);
		
		addFieldValue("po_create_page_service_multiselect", poServices);
		
		// Purchase Order - Create Page - FINANCIAL INFORMATION
		addFieldValue("po_create_page_po_total_textbox", poPOTotal);
		
		addFieldValue("po_create_page_expected_po_burn_textbox", poExpectedBurn);
		
		addFieldValue("po_create_page_po_burn_textbox", poBurn);
		
		addFieldValue("po_create_page_po_available_textbox", poAvailable);

		// Purchase Order - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", poComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", poRequestedBy);
		    
		addFieldValue("entity_create_page_change_request_dropdown", poChangeRequest);
		
		if (!poUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\PO\\" + poUploadFile);

			}
		
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

		
		String entityShowPageName = locateElementBy("po_show_page_name").getText();

		Assertion(entityShowPageName.substring(0, entityShowPageName.length()-2), poName);
		Logger.debug("PO name on show page has been verified");

		fail = false;
		
        driver.get(CONFIG.getProperty("endUserURL"));
		}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
          for (int i = 2; i <= po_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
              String testCaseID = po_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(po_suite_xls, this.getClass().getSimpleName());
	}
}
