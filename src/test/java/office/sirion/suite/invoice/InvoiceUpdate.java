package office.sirion.suite.invoice;

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

public class InvoiceUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(inv_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(inv_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void InvoiceUpdateTest (String InvoiceNumber, String Title, String PONumber, String Currency, String TimeZone, String ContractingEntity,
			String InvoiceAmount, String SupplierID, String ClientID, String SupplierName, String SirionSupplierID, String BilltoAddress, String ShiptoAddress,
			String CostCenterCode, String SupplierAddress, String SupplierAccess, String Tier, String EmailAddress, String PhoneNumber, String PreviousInvoiceID,
			String RecipientClientEntity, String RecipientCompanyCode, String ContractingClientEntity, String ContractingCompanyCode, String InvoicePeriodStart,
			String InvoicePeriodEnd, String CreditPeriodindays, String InvoiceDate, String PaymentTerm, String ExpectedReceiptDate, String SystemReceiptDate,
			String PaymentDueDate, String ActualPaymentDate, String ActualReceiptDate, String Functions, String Services, String ServiceCategory, String Regions,
			String Countries, String ContractRegions, String ContractCountries, String AmountApproved, String SupplierTAXID, String TAXPercentage, String TAXAmount,
			String SupplierBankAccount, String DiscrepancyAmount, String NoOfLineItems, String NoOfLineItemsWithDiscrepancy, String PaidAmount, String ResolvedDiscrepancy,
			String DisputeAmount, String DiscrepancyOvercharge, String DiscrepancyUndercharge, String NetSavings, String ProjectID, String Initiatives,
			String ProjectLevels, String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles, String ParentEntity
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

	

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Invoice Update for -- " + Title);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("inv_quick_link"));

		locateElementBy("inv_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+ParentEntity+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_display_dropdown_link").click();}
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_display_dropdown_link").click();}

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		// INVOICES - SHOW PAGE - EDIT BUTTON
		try{
			driver.findElement(By.xpath("//button[contains(.,'Restore')]")).click();
			WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
			Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
			elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
		}catch(NoSuchElementException e) {
			WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
			Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
			elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

			fluentWaitMethod(locateElementBy("inv_create_page_title_textbox"));
		}

	    fluentWaitMethod(locateElementBy("inv_create_page_title_textbox"));

	    // INVOICES - EDIT PAGE - BASIC INFORMATION
	    addFieldValue("inv_create_page_title_textbox", Title.trim());

	    addFieldValue("inv_create_page_invoice_number_textbox", InvoiceNumber.trim());

	    addFieldValue("inv_create_page_po_number_textbox", PONumber.trim());

	    addFieldValue("inv_create_page_currency_dropdown", Currency.trim());

	    addFieldValue("inv_create_page_time_zone_dropdown", TimeZone.trim());
	    try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    		}

	    addFieldValue("inv_create_page_contracting_entity_dropdown", ContractingEntity.trim());

	    addFieldValue("inv_create_page_invoice_amount_numeric", InvoiceAmount.trim());

	    addFieldValue("inv_create_page_supplier_id_textbox", SupplierID.trim());

	    addFieldValue("inv_create_page_client_id_textbox", ClientID.trim());

	    addFieldValue("inv_create_page_supplier_name_textbox", SupplierName.trim());

	    addFieldValue("inv_create_page_sirion_supplier_id_textbox", SirionSupplierID.trim());

	    addFieldValue("inv_create_page_bill_to_address_textarea", BilltoAddress.trim());

	    addFieldValue("inv_create_page_ship_to_address_textarea", ShiptoAddress.trim());

	    addFieldValue("inv_create_page_cost_center_code_textbox", CostCenterCode.trim());

	    addFieldValue("inv_create_page_supplier_address_textarea", SupplierAddress.trim());

	    addFieldValue("inv_create_page_supplier_access_checkbox", SupplierAccess.trim());

	    addFieldValue("inv_create_page_tier_dropdown", Tier.trim());

	    addFieldValue("inv_create_page_email_address_textbox", EmailAddress.trim());

	    addFieldValue("inv_create_page_phone_number_textbox", PhoneNumber.trim());

	    addFieldValue("inv_create_page_previous_invoice_id_textbox", PreviousInvoiceID.trim());
	    
	    // INVOICES - EDIT PAGE - CONTRACTING PARTY
		addFieldValue("inv_create_page_recipient_client_entity_multi_dropdown", RecipientClientEntity.trim());
		if (!RecipientClientEntity.equalsIgnoreCase(""))
			addFieldValue("inv_create_page_recipient_company_code_multi_dropdown", RecipientCompanyCode.trim());

		addFieldValue("inv_create_page_contracting_client_entity_multi_dropdown", ContractingClientEntity.trim());
		if (!ContractingClientEntity.equalsIgnoreCase(""))
			addFieldValue("inv_create_page_contracting_company_code_multi_dropdown", ContractingCompanyCode.trim());

		// INVOICES - EDIT PAGE - IMPORTANT DATES
		
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		
		date.selectCalendar(InvoicePeriodStart,"invoicePeriodFromDate");
		date.selectCalendar(InvoicePeriodEnd,"invoicePeriodToDate");
		addFieldValue("inv_create_page_credit_period_in_days_numeric", CreditPeriodindays.trim());
		date.selectCalendar(InvoiceDate,"invoiceDate");
		addFieldValue("inv_create_page_payment_term_numeric", PaymentTerm.trim());
		date.selectCalendar(ExpectedReceiptDate,"expectedReceiptDate");
		date.selectCalendar(SystemReceiptDate,"SystemReceiptDate");
		date.selectCalendar(PaymentDueDate,"paymentDueDate");
		date.selectCalendar(ActualPaymentDate,"actualPaymentDate");
		date.selectCalendar(ActualReceiptDate,"actualReceiptDate");
		
	    // INVOICES - EDIT PAGE - FUNCTIONS
		addFieldValue("inv_create_page_functions_multi_dropdown", Functions.trim());
		if (!Functions.equalsIgnoreCase(""))
			addFieldValue("inv_create_page_services_multi_dropdown", Services.trim());
        try{
		addFieldValue("inv_create_page_service_category_multi_dropdown", ServiceCategory.trim());
        }catch (NoSuchElementException e1){
            new Select(driver.findElement(By.xpath("//span[@id='elem_11476']/select"))).selectByIndex(0);
        }
		// INVOICES - EDIT PAGE - GEOGRAPHY
		addFieldValue("inv_create_page_regions_multi_dropdown", Regions.trim());
		if (!Regions.equalsIgnoreCase(""))
			addFieldValue("inv_create_page_countries_multi_dropdown", Countries.trim());

		addFieldValue("inv_create_page_contract_regions_multi_dropdown", ContractRegions.trim());
		if (!ContractRegions.equalsIgnoreCase(""))
			addFieldValue("inv_create_page_contract_countries_multi_dropdown", ContractCountries.trim());

		// INVOICES - EDIT PAGE - FINANCIAL INFORMATION
		addFieldValue("inv_create_page_amount_approved_numeric", AmountApproved.trim());

		addFieldValue("inv_create_page_supplier_tax_id_textbox", SupplierTAXID.trim());

		addFieldValue("inv_create_page_tax_percentage_numeric", TAXPercentage.trim());

		addFieldValue("inv_create_page_tax_amount_numeric", TAXAmount.trim());

		addFieldValue("inv_create_page_supplier_bank_account_textbox", SupplierBankAccount.trim());

		addFieldValue("inv_create_page_discrepancy_amount_numeric", DiscrepancyAmount.trim());

		addFieldValue("inv_create_page_no_of_line_items_numeric", NoOfLineItems.trim());

		addFieldValue("inv_create_page_no_of_line_items_with_discrepancy_numeric", NoOfLineItemsWithDiscrepancy.trim());

		addFieldValue("inv_create_page_paid_amount_numeric", PaidAmount.trim());

		addFieldValue("inv_create_page_resolved_discrepancy_numeric", ResolvedDiscrepancy.trim());

		addFieldValue("inv_create_page_dispute_amount_numeric", DisputeAmount.trim());

		addFieldValue("inv_create_page_discrepancy_overcharge_numeric", DiscrepancyOvercharge.trim());

		addFieldValue("inv_create_page_discrepancy_undercharge_numeric", DiscrepancyUndercharge.trim());

		addFieldValue("inv_create_page_net_savings_numeric", NetSavings.trim());

		// INVOICES - EDIT PAGE - PROJECT INFORMATION
		addFieldValue("inv_create_page_project_id_multi_dropdown", ProjectID.trim());

		addFieldValue("inv_create_page_project_levels_multi_dropdown", ProjectLevels.trim());

		addFieldValue("inv_create_page_initiatives_multi_dropdown", Initiatives.trim());

		// INVOICES - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Invoice\\" + UploadFiles);

			}

		locateElementBy("entity_create_page_actual_date").findElement(By.tagName("input")).click();
		// SERVICE LEVELS - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// INVOICES - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Invoice -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Invoice -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		// INVOICES - EDIT PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();
				System.out.println(entityErrors);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}

	    fluentWaitMethod(locateElementBy("inv_create_page_title_textbox"));

		String entityShowPageName = locateElementBy("inv_create_page_title_textbox").getText();

		if (!Title.equalsIgnoreCase("")) {
			Assert.assertEquals(entityShowPageName, Title);
		}else{
			Assert.assertEquals(entityShowPageName, ParentEntity);
		}
	    Logger.debug("Test Case Invoice Update with Title -- " + Title + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(inv_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(inv_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(inv_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= inv_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = inv_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(inv_suite_xls, "Test Cases", TestUtil.getRowNum(inv_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(inv_suite_xls, "Test Cases", TestUtil.getRowNum(inv_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(inv_suite_xls, this.getClass().getSimpleName());
		}
	}
