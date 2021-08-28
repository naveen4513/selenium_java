package office.sirion.suite.vendorHierarchy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VendorHierarchyCreation extends TestSuiteBase {
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
	public void VendorHierarchyCreationTest (String Name, String Alias, String Address, String ECCID, String Functions, String Services,
			String ServiceCategory, String Regions, String Countries, String AdditionalTCV, String AdditionalACV, String AdditionalFACV, String Privacy,
			String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles
			) throws Exception {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Vendor Hierarchy Creation for -- " + Name);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("vh_quick_link"));

		locateElementBy("vh_quick_link").click();

		fluentWaitMethod(locateElementBy("vh_listing_page_plus_button"));

		locateElementBy("vh_listing_page_plus_button").click();

		fluentWaitMethod(locateElementBy("vh_create_page_name_textbox"));

		// VENDOR HIERARCHY - CREATE PAGE - BASIC INFORMATION
		if (CONFIG.getProperty("entityDataCreation").equalsIgnoreCase("Yes")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ss");
			String datestr = sdf.format(date);
			
			addFieldValue("vh_create_page_name_textbox", Name.trim() + datestr);
			addFieldValue("vh_create_page_alias_textbox", Alias.trim() + datestr);
			addFieldValue("vh_create_page_ecc_id_textbox", ECCID.trim() + datestr);
			}
		else {
			addFieldValue("vh_create_page_name_textbox", Name.trim());
			addFieldValue("vh_create_page_alias_textbox", Alias.trim());
			addFieldValue("vh_create_page_ecc_id_textbox", ECCID.trim());
			}

		addFieldValue("vh_create_page_address_textarea", Address.trim());

		// VENDOR HIERARCHY - CREATE PAGE - GEOGRAPHY
		addFieldValue("vh_create_page_regions_multi_dropdown", Regions.trim());
		addFieldValue("vh_create_page_countries_multi_dropdown", Countries.trim());
		
		// VENDOR HIERARCHY - CREATE PAGE - FUNCTIONS
		addFieldValue("vh_create_page_functions_multi_dropdown", Functions.trim());
		addFieldValue("vh_create_page_services_multi_dropdown", Services.trim());
		addFieldValue("vh_create_page_service_category_multi_dropdown", ServiceCategory.trim());

		// VENDOR HIERARCHY - CREATE PAGE - FINANCIAL INFORMATION
		addFieldValue("vh_create_page_additional_tcv_numeric_box", AdditionalTCV.trim());
		addFieldValue("vh_create_page_additional_acv_numeric_box", AdditionalACV.trim());
		addFieldValue("vh_create_page_additional_facv_numeric_box", AdditionalFACV.trim());

		// VENDOR HIERARCHY - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());
		addFieldValue("entity_create_page_comments_textarea", Comments.trim());
		addFieldValue("entity_create_page_actual_date", ActualDate.trim());
		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());
		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Vendor Hierarchy\\" + UploadFiles);

			}

		// VENDOR HIERARCHY - CREATE PAGE - SAVE BUTTON
	    WebElement elementSave = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")));
	    elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")).click();


		// VENDOR HIERARCHY - CREATE PAGE - FIELD VALIDATIONS
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

		// VENDOR HIERARCHY - CREATE PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();

				if (entityErrors.contains("This name already exists in the system."))
					Logger.debug("Vendor Hierarchy Name -- " + Name + " already exists in the system.");
				else if (entityErrors.contains("This alias already exists in the system."))
					Logger.debug("Vendor Hierarchy Alias -- " + Alias + " already exists in the system.");
				else if (entityErrors.contains("This Old System Id already exists in the system."))
					Logger.debug("Vendor Hierarchy ECC ID -- " + ECCID + " already exists in the system.");
				else if (entityErrors.contains("Please select services for all functions:"))
					Logger.debug("For Vendor Hierarchy -- " + Name + " Please select services for all functions:");
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}
		fluentWaitMethod(driver.findElement(By.className("success-icon")));

		String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
		Logger.debug("Vendor Hierarchy created successfully with Entity ID " + entityID);

		driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();

		fluentWaitMethod(locateElementBy("vh_show_page_id"));

		String entityShowPageID = locateElementBy("vh_show_page_id").getText();

		Assert.assertEquals(entityShowPageID, entityID);
		

	    Logger.debug("Test Case Vendor Hierarchy Creation for -- " + Name + " is PASSED");
		driver.get(CONFIG.getProperty("endUserURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws Exception {
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
