package office.sirion.suite.vendorHierarchy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import office.sirion.util.TestAuditLogUtil;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class VendorHierarchyAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;	
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(vendor_hierarchy_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(vendor_hierarchy_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void VendorHierarchyAuditLogTest (String Name, String Alias, String Address, String ECCID, String Functions, String Services,
			String ServiceCategory, String Regions, String Countries, String AdditionalTCV, String AdditionalACV, String AdditionalFACV, String Privacy,
			String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles, String ParentEntity
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Vendor Hierarchy Audit Log for -- " + Name);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("vh_quick_link"));

		locateElementBy("vh_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][./text()='"+ParentEntity+"']/preceding-sibling::td[1]/a")).click();
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[1]/a")).click();

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		// VENDOR HIERARCHY - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("vh_create_page_name_textbox"));

		Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage = TestAuditLogUtil.getEntityMapBeforeUpdate();

		// VENDOR HIERARCHY - EDIT PAGE - BASIC INFORMATION
		addFieldValue("vh_create_page_name_textbox", Name.trim());

		addFieldValue("vh_create_page_alias_textbox", Alias.trim());

		addFieldValue("vh_create_page_address_textarea", Address.trim());

		addFieldValue("vh_create_page_ecc_id_textbox", ECCID.trim());

		// VENDOR HIERARCHY - EDIT PAGE - GEOGRAPHY
		addFieldValue("vh_create_page_regions_multi_dropdown", Regions.trim());

		addFieldValue("vh_create_page_countries_multi_dropdown", Countries.trim());

		// VENDOR HIERARCHY - EDIT PAGE - FUNCTIONS
		addFieldValue("vh_create_page_functions_multi_dropdown", Functions.trim());

		addFieldValue("vh_create_page_services_multi_dropdown", Services.trim());

		addFieldValue("vh_create_page_service_category_multi_dropdown", ServiceCategory.trim());

		// VENDOR HIERARCHY - EDIT PAGE - FINANCIAL INFORMATION
		addFieldValue("vh_create_page_additional_tcv_numeric_box", AdditionalTCV.trim());

		addFieldValue("vh_create_page_additional_acv_numeric_box", AdditionalACV.trim());

		addFieldValue("vh_create_page_additional_facv_numeric_box", AdditionalFACV.trim());

		// VENDOR HIERARCHY - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Vendor Hierarchy\\" + UploadFiles);

			}

		Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = TestAuditLogUtil.getEntityMapAfterUpdate(entityMapBeforeUpdatePage);

		// VENDOR HIERARCHY - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// VENDOR HIERARCHY - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Vendor Hierarchy -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Vendor Hierarchy -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		// VENDOR HIERARCHY - EDIT PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element1 : elementErrors) {
				String entityErrors = element1.getText();

				if (entityErrors.contains("This name already exists in the system.")) {
					Logger.debug("Vendor Hierarchy Name -- " + Name + " already exists in the system.");
					}
				else if (entityErrors.contains("This alias already exists in the system.")) {
					Logger.debug("Vendor Hierarchy Alias -- " + Alias + " already exists in the system.");
			        }
				else if (entityErrors.contains("This Old System Id already exists in the system.")) {
					Logger.debug("Vendor Hierarchy ECC ID -- " + ECCID + " already exists in the system.");
			        }
				else if (entityErrors.contains("Please select services for all functions:")) {
					Logger.debug("For Vendor Hierarchy -- " + Name + " Please select services for all functions:");
			        }
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}

	    fluentWaitMethod(locateElementBy("vh_create_page_name_textbox"));

	    String entityShowPageName = locateElementBy("vh_create_page_name_textbox").getText();

	    Assert.assertEquals(entityShowPageName, Name);

		fluentWaitMethod(driver.findElement(By.linkText("AUDIT LOG")));
		driver.findElement(By.linkText("AUDIT LOG")).click();

	    Logger.debug("Test Case Vendor Hierarchy Audit Log for -- " + Name + " -- is STARTED");

		fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		WebElement elementOddRowSelected = driver.findElement(By.id("table_2011")).findElements(By.className("odd")).get(0);

		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

	    if (!RequestedBy.equalsIgnoreCase(""))
			Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), RequestedBy);
	    else
	    	Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    if (!Comments.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

	    if (!UploadFiles.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

	    elementOddRowSelected.findElement(By.cssSelector("td.col_type_fieldHistory.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
	    fluentWaitMethod(driver.findElement(By.id("historyDataTable")));


	    Map<String, Map<String, String>> auditLogDataMap = TestAuditLogUtil.getAuditLogViewHistoryData();

	    TestAuditLogUtil.isAuditLogWorking(entityMapAfterUpdatePage, auditLogDataMap);

	    Logger.debug("Test Case Vendor Hierarchy Audit Log for -- " + Name + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(vendor_hierarchy_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(vendor_hierarchy_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(vendor_hierarchy_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= vendor_hierarchy_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = vendor_hierarchy_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(vendor_hierarchy_suite_xls, "Test Cases", TestUtil.getRowNum(vendor_hierarchy_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(vendor_hierarchy_suite_xls, "Test Cases", TestUtil.getRowNum(vendor_hierarchy_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(vendor_hierarchy_suite_xls, this.getClass().getSimpleName());
		}
	}
