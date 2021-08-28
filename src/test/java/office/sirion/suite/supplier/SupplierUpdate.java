package office.sirion.suite.supplier;

import java.io.IOException;
import java.util.List;
import office.sirion.util.TestUtil;
import org.openqa.selenium.*;
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

public class SupplierUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;	
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(supplier_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(supplier_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void SupplierUpdateTest (String Name, String Alias, String Tier, String Address, String ShareId, String Email, String TaxId, String ECCID,
			String Functions, String Services, String ServiceCategory, String Regions, String Countries, String AdditionalFACV, String AdditionalTCV, String AdditionalACV,
			String ProjectID, String Initiatives, String ProjectLevels, String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest,
			String UploadFiles, String ParentEntity
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);



		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Supplier Update for -- " + Name);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("suppliers_quick_link"));

		locateElementBy("suppliers_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+ParentEntity+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}

        try{
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
        }catch (NoSuchElementException e){
            fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
        }
		// SUPPLIER - SHOW PAGE - EDIT BUTTON


        try{
            driver.findElement(By.xpath(".//button[contains(.,'Restore')]")).click();

            WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            try {
                fluentWaitMethod(driver.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
                elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
            } catch (StaleElementReferenceException e){
                fluentWaitMethod(driver.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            }


            fluentWaitMethod(locateElementBy("supplier_create_page_name_textbox"));

        }catch (NoSuchElementException e){
            WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
            try{
                Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            }catch (NoSuchElementException exec){
                Assert.assertNotNull(driver.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

            fluentWaitMethod(locateElementBy("supplier_create_page_name_textbox"));
        }

	    // SUPPLIER - EDIT PAGE - BASIC INFORMATION
		addFieldValue("supplier_create_page_name_textbox", Name.trim());

		addFieldValue("supplier_create_page_alias_textbox", Alias.trim());

		addFieldValue("supplier_create_page_tier_multi_dropdown", Tier.trim());

		addFieldValue("supplier_create_page_address_textarea", Address.trim());

		addFieldValue("supplier_create_page_share_id_textbox", ShareId.trim());

		addFieldValue("supplier_create_page_email_textbox", Email.trim());

		addFieldValue("supplier_create_page_tax_id_textbox", TaxId.trim());

		addFieldValue("supplier_create_page_ecc_id_textbox", ECCID.trim());

		// SUPPLIER - EDIT PAGE - FUNCTIONS
		addFieldValue("supplier_create_page_functions_multi_dropdown", Functions.trim());

		addFieldValue("supplier_create_page_services_multi_dropdown", Services.trim());

		addFieldValue("supplier_create_page_service_category_multi_dropdown", ServiceCategory.trim());

		// SUPPLIER - EDIT PAGE - GEOGRAPHY
		addFieldValue("supplier_create_page_regions_multi_dropdown", Regions.trim());

		addFieldValue("supplier_create_page_countries_multi_dropdown", Countries.trim());

		// SUPPLIER - EDIT PAGE - FINANCIAL INFORMATION
		addFieldValue("supplier_create_page_additional_tcv_numeric_box", AdditionalTCV.trim());

		addFieldValue("supplier_create_page_additional_acv_numeric_box", AdditionalACV.trim());

		addFieldValue("supplier_create_page_additional_facv_numeric_box", AdditionalFACV.trim());

		// SUPPLIER - EDIT PAGE - PROJECT INFORMATION
		addFieldValue("supplier_create_page_project_id_multi_dropdown", ProjectID.trim());

		addFieldValue("supplier_create_page_project_levels_multi_dropdown", ProjectLevels.trim());

		addFieldValue("supplier_create_page_initiatives_multi_dropdown", Initiatives.trim());

		// SUPPLIER - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Supplier\\" + UploadFiles);

			}

		// SUPPLIER - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();

		// SUPPLIER - EDIT PAGE - FIELD VALIDATIONS
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

		// SUPPLIER - EDIT PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();

				if (entityErrors.contains("This Old System Id already exists in the system."))
					Logger.debug("Supplier ECC ID -- " + ECCID + " already exists in the system.");
				else if (entityErrors.contains("Invalid domain found for emails, " + Email))
					Logger.debug("Invalid Domain Found For Supplier Email -- " + Email);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}
		//
	    fluentWaitMethod(locateElementBy("supplier_create_page_name_textbox"));

	    String entityShowPageName = locateElementBy("supplier_show_page_name").getText();

	    Assert.assertEquals(entityShowPageName, Name);

	    Logger.debug("Test Case Supplier Update with Name -- " + Name + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= supplier_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = supplier_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(supplier_suite_xls, this.getClass().getSimpleName());
		}
	}
