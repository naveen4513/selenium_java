package office.sirion.suite.supplier;

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

public class ECPAuditLog extends TestSuiteBase {
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
	public void ECPAuditLogTest (String Name, String ECCID, String SapSourcingID, String DUNSID, String VendorRegistrationNumber, String Replicated,
			String PurchasingHold, String SupplierCurrency, String TaxID, String TaxNumber, String Active, String AbbreviatedName, String AlternativeName,
			String CompanyCode, String SupplierType, String BlockDelete, String PurchasingOrganization, String PaymentTerms, String PurchasingOrganizationCurrency,
			String Address, String ContactDetailsPhone, String Fax, String Telex, String WebSiteURL, String PublishStatus, String PublishDate, String Privacy,
			String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles, String ParentSupplier, String ParentEntity
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);



		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case ECP Audit Log for -- " + ParentEntity);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("suppliers_quick_link"));

		locateElementBy("suppliers_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentSupplier.equalsIgnoreCase("") && ParentSupplier.length()<=30)try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+ParentSupplier+"']/preceding-sibling::td[2]/a")).click();
		}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}
		else if (!ParentSupplier.equalsIgnoreCase("") && ParentSupplier.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+ParentSupplier+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}

	    fluentWaitMethod(driver.findElement(By.linkText("EXTERNAL CONTRACTING PARTY")));

	    driver.findElement(By.linkText("EXTERNAL CONTRACTING PARTY")).click();

		fluentWaitMethod(locateElementBy("ecp_listing_page_display_dropdown_link"));

		new Select(locateElementBy("ecp_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='table_3609']/tbody/tr/td[2][./text()='"+ParentEntity+"']/preceding-sibling::td[1]/a")).click();
			} catch (NoSuchElementException e ){locateElementBy("ecp_listing_page_first_link").click();}
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='table_3609']/tbody/tr/td[2][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[1]/a")).click();
			} catch (NoSuchElementException e ){locateElementBy("ecp_listing_page_first_link").click();}


		// EXTERNAL CONTRACTING PARTY - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("ecp_create_page_name_textbox"));

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - BASIC INFORMATION
		addFieldValue("ecp_create_page_name_textbox", Name.trim());

		addFieldValue("ecp_create_page_ecc_id_textbox", ECCID.trim());

		addFieldValue("ecp_create_page_sap_sourcing_id_textbox", SapSourcingID.trim()+getSaltString());

		addFieldValue("ecp_create_page_duns_id_textbox", DUNSID.trim());

		addFieldValue("ecp_create_page_vendor_registration_number_textbox", VendorRegistrationNumber.trim());

		addFieldValue("ecp_create_page_replicated_checkbox", Replicated.trim());

		addFieldValue("ecp_create_page_purchasing_hold_checkbox", PurchasingHold.trim());

		addFieldValue("ecp_create_page_supplier_currency_dropdown", SupplierCurrency.trim());

		addFieldValue("ecp_create_page_tax_id_textbox", TaxID.trim());

		addFieldValue("ecp_create_page_tax_number_textbox", TaxNumber.trim());

		addFieldValue("ecp_create_page_active_checkbox", Active.trim());

		addFieldValue("ecp_create_page_abbreviated_name_textbox", AbbreviatedName.trim());

		addFieldValue("ecp_create_page_alternative_name_textbox", AlternativeName.trim());

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - COMPANY
		addFieldValue("ecp_create_page_company_code_dropdown", CompanyCode.trim());

		addFieldValue("ecp_create_page_supplier_type_dropdown", SupplierType.trim());

		addFieldValue("ecp_create_page_block_delete_checkbox", BlockDelete.trim());

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - PURCHASING ORGANIZATION INFORMATION
		addFieldValue("ecp_create_page_purchasing_organization_dropdown", PurchasingOrganization.trim());

		addFieldValue("ecp_create_page_payment_terms_dropdown", PaymentTerms.trim());

		addFieldValue("ecp_create_page_purchasing_organization_currency_dropdown", PurchasingOrganizationCurrency.trim());

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - SUPPLIER - CONTACT DETAILS
		addFieldValue("ecp_create_page_address_textarea", Address.trim());

		addFieldValue("ecp_create_page_contact_details_phone_textbox", ContactDetailsPhone.trim());

		addFieldValue("ecp_create_page_fax_textbox", Fax.trim());

		addFieldValue("ecp_create_page_telex_textbox", Telex.trim());

		addFieldValue("ecp_create_page_website_url_textbox", WebSiteURL.trim());

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - SUPPLIER - BUSINESS SYSTEM
		addFieldValue("ecp_create_page_publish_status_textbox", PublishStatus.trim());

		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(PublishDate,"publishDate");

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
		Acdate.selectCalendar(ActualDate,"actualDate");

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\ExternalContractingParty\\" + UploadFiles);

			}

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For ECP -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For ECP -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		// EXTERNAL CONTRACTING PARTY - EDIT PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();

				if (entityErrors.contains("This SapSourcing Id already exists in the system.")) {
					Logger.debug("ECP Sap Sourcing ID -- " + SapSourcingID + " already exists in the system.");
			        }
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}

	    fluentWaitMethod(locateElementBy("ecp_create_page_name_textbox"));

	    String entityShowPageName = locateElementBy("ecp_create_page_name_textbox").getText();

	    Assert.assertEquals(entityShowPageName, Name);

	    fluentWaitMethod(driver.findElement(By.linkText("AUDIT LOG")));
		driver.findElement(By.linkText("AUDIT LOG")).click();

	    Logger.debug("Test Case ECP Audit Log for -- " + ParentEntity + " -- is STARTED");

	    fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		WebElement elementOddRowSelected = driver.findElement(By.id("table_3601")).findElements(By.className("odd")).get(0);

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

		elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
	    fluentWaitMethod(driver.findElement(By.id("historyDataTable")));

	    List<WebElement> auditRowElementList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
	    	for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
	    		// EXTERNAL CONTRACTING PARTY - BASIC INFORMATION
	    		if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Name")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), Name);
	    			}

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("DUNS ID")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), DUNSID);
	    			}

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Vendor Registration Number")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), VendorRegistrationNumber);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Purchasing Hold")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), PurchasingHold);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Supplier Currency")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), SupplierCurrency);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Tax ID")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), TaxID);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Tax Number")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), TaxNumber);
	    			}
/*
	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Active")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), Active);
	    			}   
*/
	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Abbreviated Name")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), AbbreviatedName);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Alternative Name")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), AlternativeName);
	    			}   

	    		// EXTERNAL CONTRACTING PARTY - CREATE PAGE - SUPPLIER - CONTACT DETAILS
	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Address")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), Address);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Contact Details (Phone)")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), ContactDetailsPhone);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Fax")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), Fax);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Telex")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), Telex);
	    			}   

	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Web Site URL")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), WebSiteURL);
	    			}   

	    		// EXTERNAL CONTRACTING PARTY - CREATE PAGE - SUPPLIER - BUSINESS SYSTEM
	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Publish Status")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), PublishStatus);
	    			}   
	    		}
	    	}

	    Logger.debug("Test Case ECP Audit Log with Name -- " + ParentEntity + " -- is PASSED");
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
