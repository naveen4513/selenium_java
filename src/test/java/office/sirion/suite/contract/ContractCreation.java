package office.sirion.suite.contract;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContractCreation extends TestSuiteBase {
    String result = null;
    String runmodes[] = null;
    static int count = -1;
    static boolean isTestPass = true;
    

    @BeforeTest
    public void checkTestSkip() {
        if (!TestUtil.isTestCaseRunnable(contract_suite_xls, this.getClass().getSimpleName())) {
            Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
            throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
        }
        runmodes = TestUtil.getDataSetRunmodes(contract_suite_xls, this.getClass().getSimpleName());
    }

    @Test(dataProvider = "getTestData")
    public void ContractCreationTest(String coName, String coTitle, String coAgreementNo, String coContractNo, String coBrief, String coBusinessUnit,
                                     String coTimezone, String coContractingEntity, String coSpendType, String coCreatedFor, String coGovernanceBodies, String coDeliveryCountries, String coTier,
                                     String coSupplierAccess, String coTermType, String coAribaCWId, String coContractType, String coCommentField, String coNoOfRenewals, String coContractPaper,
                                     String coVendorClassification, String coVendorContractingParty, String coRecipientClientEntity, String coRecipientCompanyCode, String coContractingClientEntity,
                                     String coContractingCompanyCode, String coEffectiveDate, String coExpirationDate, String coEffectiveDateOriginal, String coExpirationDateOriginal,
                                     String coExpirationNoticePeriod, String coNoticeDate, String coNoticeLeadDays, String coNoticeLeadDate, String coFunctions, String coServices,
                                     String coServiceCategory, String coManagementRegions, String coManagementCountries, String coRegionCountry, String coContractRegions,
                                     String coContractCountries, String coCurrencies, String coReportingCurrency, String coConversionType, String coConversionMatrix, String coConversionMatrixFrom,
                                     String coConversionMatrixTo, String coAdditionalTCV, String coAdditionalACV, String coAdditionalFACV, String coFinancialImpact, String coProjectID,
                                     String coProjectLevels, String coInitiatives, String coDocumentName, String coViewer, String coSearch, String coDownload, String coFinancial, String coLegal,
                                     String coBusiness, String coCustomNumbering, String coComments, String coActualDate, String coRequestedBy, String coChangeRequest, String coUploadFile,
                                     String coDocumentType, String coParent, String coParentType) throws Exception {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

      

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Contract Creation for -- " + coTitle);

        driver.get(CONFIG.getProperty("endUserURL"));
       locateElementBy("suppliers_quick_link");

        if (coParentType.equalsIgnoreCase("Supplier")) {
            locateElementBy("suppliers_quick_link").click();

            locateElementBy("entity_listing_page_display_dropdown_link");

            new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));

            if (!coParent.equalsIgnoreCase("")) {
                try {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + coParent + "']/preceding-sibling::td[2]/a")).isDisplayed();
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='" + coParent + "']/preceding-sibling::td[2]/a")).click();
                } catch (NoSuchElementException e) {
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + coParent + "']]/preceding-sibling::td[2]/a")).isDisplayed();
                    driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='" + coParent + "']]/preceding-sibling::td[2]/a")).click();
                }
            }else {
                locateElementBy("global_bulk_listing_page_first_entry_link").click();
            }
        }else if (coParentType.equalsIgnoreCase("Contract")) {
                locateElementBy("contracts_quick_link").click();
               locateElementBy("entity_listing_page_display_dropdown_link");

                new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


                if (!coParent.equalsIgnoreCase("")) {
                    try {
                        driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + coParent + "']/preceding-sibling::td[1]/a")).isDisplayed();
                        driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + coParent + "']/preceding-sibling::td[1]/a")).click();
                    } catch (NoSuchElementException e1) {
                        driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                        driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")).click();
                    }
                }else {
                    locateElementBy("global_bulk_listing_page_first_entry_link").click();
                }
            }
                locateElementBy("entity_show_page_plus_button");

                new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

                new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='drop']/ul/li[1]/label"))).clickAndHold().build().perform();

                driver.findElement(By.className("childlist")).findElement(By.linkText(coDocumentType)).click();

                locateElementBy("co_create_page_name_textbox");

                // CONTRACT - CREATE PAGE - BASIC INFORMATION
                addFieldValue("co_create_page_name_textbox", coName.trim());

                addFieldValue("co_create_page_title_textbox", coTitle.trim());

                addFieldValue("co_create_page_agreement_no_textbox", coAgreementNo.trim());

                if (CONFIG.getProperty("entityDataCreation").equalsIgnoreCase("Yes")) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("ss");
                    String datestr = sdf.format(date);

                    addFieldValue("co_create_page_contract_no_textbox", coContractNo.trim() + datestr);
                } else
                    addFieldValue("co_create_page_contract_no_textbox", coContractNo.trim());

                if (locateElementBy("co_create_page_brief_textarea").findElements(By.tagName("textarea")).size() != 0)
                    addFieldValue("co_create_page_brief_textarea", coBrief.trim());
                else {
                    driver.switchTo().frame(locateElementBy("co_create_page_brief_textarea").findElement(By.tagName("iframe")));

                    driver.findElement(By.cssSelector("div.note-editable.panel-body")).clear();
                    driver.findElement(By.cssSelector("div.note-editable.panel-body")).sendKeys(coBrief.trim());

                    driver.switchTo().defaultContent();
                }

                addFieldValue("co_create_page_business_unit_dropdown", coBusinessUnit.trim());

                addFieldValue("co_create_page_timezone_dropdown", coTimezone.trim());
                try {
                    if (locateElementBy("entity_show_page_permission_notification_pop_up_text").getText().contains("Current Date is different for the selected Time Zone"))
                        locateElementBy("end_user_download_popup_ok_button").click();
                } catch (Exception e) {
                    Logger.info("User Time and Select Entity Time zone is same");
                }

                addFieldValue("co_create_page_contracting_entity_dropdown", coContractingEntity.trim());

                addFieldValue("co_create_page_spend_type_dropdown", coSpendType.trim());

                addFieldValue("co_create_page_created_for_dropdown", coCreatedFor.trim());

                addFieldValue("co_create_page_governance_bodies_multi_dropdown", coGovernanceBodies.trim());

                addFieldValue("co_create_page_delivery_countries_multi_dropdown", coDeliveryCountries.trim());

                addFieldValue("co_create_page_tier_dropdown", coTier.trim());

                addFieldValue("co_create_page_term_type_dropdown", coTermType.trim());

                addFieldValue("co_create_page_ariba_cw_id_textbox", coAribaCWId.trim());
                try {
                    addFieldValue("co_create_page_contract_type_dropdown", coContractType.trim());
                } catch (NoSuchElementException e) {
                    Logger.debug("Field is not accessible");
                }
                addFieldValue("co_create_page_comment_field_textarea", coCommentField.trim());

                addFieldValue("co_create_page_no_of_renewals_numeric_box", coNoOfRenewals.trim());

                // CONTRACT - CREATE PAGE - CONTRACT INFORMATION
                addFieldValue("co_create_page_contract_paper_dropdown", coContractPaper.trim());

                addFieldValue("co_create_page_vendor_classification_dropdown", coVendorClassification.trim());

                // CONTRACT - CREATE PAGE - CONTRACTING PARTY
                addFieldValue("co_create_page_vendor_contracting_party_multi_dropdown", coVendorContractingParty.trim());

                addFieldValue("co_create_page_recipient_client_entities_multi_dropdown", coRecipientClientEntity.trim());

                addFieldValue("co_create_page_recipient_company_codes_multi_dropdown", coRecipientCompanyCode.trim());

                addFieldValue("co_create_page_contracting_client_entities_multi_dropdown", coContractingClientEntity.trim());

                addFieldValue("co_create_page_contracting_company_codes_multi_dropdown", coContractingCompanyCode.trim());

                // CONTRACT - CREATE PAGE - IMPORTANT DATES
                DatePicker_Enhanced date = new DatePicker_Enhanced();
                date.selectCalendar(coEffectiveDate, "effectiveDate");
                date.selectCalendar(coExpirationDate, "expirationDate");
                date.selectCalendar(coEffectiveDateOriginal, "effectiveDateOriginal");
                date.selectCalendar(coExpirationDateOriginal, "expirationDateOriginal");
                date.selectCalendar(coNoticeDate, "noticeDate");
                date.selectCalendar(coNoticeLeadDate, "noticeLeadDate");

                addFieldValue("co_create_page_expiration_notice_period_numeric_box", coExpirationNoticePeriod.trim());
                addFieldValue("co_create_page_notice_lead_days_numeric_box", coNoticeLeadDays.trim());

                // CONTRACT - CREATE PAGE - FUNCTIONS
                addFieldValue("co_create_page_functions_multi_dropdown", coFunctions.trim());

                addFieldValue("co_create_page_services_multi_dropdown", coServices.trim());

                addFieldValue("co_create_page_service_category_multi_dropdown", coServiceCategory.trim());

                // CONTRACT - CREATE PAGE - GEOGRAPHY
                addFieldValue("co_create_page_management_regions_multi_dropdown", coManagementRegions.trim());

                addFieldValue("co_create_page_management_countries_multi_dropdown", coManagementCountries.trim());

                addFieldValue("co_create_page_region_country_dropdown", coRegionCountry.trim());

                addFieldValue("co_create_page_contract_regions_multi_dropdown", coContractRegions.trim());

                addFieldValue("co_create_page_contract_countries_multi_dropdown", coContractCountries.trim());

                // CONTRACT - CREATE PAGE - FINANCIAL INFORMATION
                addFieldValue("co_create_page_currencies_multi_dropdown", coCurrencies.trim());

                addFieldValue("co_create_page_reporting_currencies_dropdown", coReportingCurrency.trim());

                addFieldValue("co_create_page_conversion_type_dropdown", coConversionType.trim());

                addFieldValue("co_create_page_conversion_matrix_dropdown", coConversionMatrix.trim());

                date.selectCalendar(coConversionMatrixFrom, "rateCardFromDate");
                date.selectCalendar(coConversionMatrixTo, "rateCardToDate");

                addFieldValue("co_create_page_additional_tcv_numeric_box", coAdditionalTCV.trim());

                addFieldValue("co_create_page_additional_acv_numeric_box", coAdditionalACV.trim());

                addFieldValue("co_create_page_additional_facv_numeric_box", coAdditionalFACV.trim());

                addFieldValue("co_create_page_financial_impact_numeric_box", coFinancialImpact.trim());

                // CONTRACT - CREATE PAGE - PROJECT INFORMATION
                addFieldValue("co_create_page_project_id_multi_dropdown", coProjectID.trim());

                addFieldValue("co_create_page_project_levels_multi_dropdown", coProjectLevels.trim());

                addFieldValue("co_create_page_initiatives_multi_dropdown", coInitiatives.trim());

                // CONTRACT - CREATE PAGE - DOCUMENT UPLOAD TAB
                if (!coDocumentName.equalsIgnoreCase("")) {
                    locateElementBy("co_create_page_dialog_box").findElement(By.className("tablist_Container")).findElement(By.linkText("DOCUMENT UPLOAD")).click();
                    locateElementBy("co_create_page_upload_area").findElement(By.name("documentFileData")).sendKeys(System.getProperty("user.dir") + "\\file-upload\\Contract Documents\\" + coDocumentName);

                    try {
                        locateElementBy("co_create_page_upload_area_file_select");
                        String DocumentName = locateElementBy("co_create_page_upload_area_file_select").getText();
                        System.out.println(DocumentName);
                    } catch (NoSuchElementException e) {

                        String DocumentName = locateElementBy("co_create_page_upload_area_file_select").getText();
                        System.out.println(DocumentName);
                    }
                    addFieldValue("co_create_page_doc_viewer_checkbox", coViewer.trim());

                    addFieldValue("co_create_page_doc_search_checkbox", coSearch.trim());

                    addFieldValue("co_create_page_doc_download_checkbox", coDownload.trim());

                    addFieldValue("co_create_page_doc_financial_checkbox", coFinancial.trim());

                    addFieldValue("co_create_page_doc_legal_checkbox", coLegal.trim());

                    addFieldValue("co_create_page_doc_business_checkbox", coBusiness.trim());

                    addFieldValue("co_create_page_doc_custom_numbering_checkbox", coCustomNumbering.trim());

                    locateElementBy("co_create_page_dialog_box").findElement(By.className("tablist_Container")).findElement(By.linkText("GENERAL")).click();
                }

                // CONTRACT - CREATE PAGE - COMMENTS AND ATTACHMENTS
                addFieldValue("entity_create_page_comments_textarea", coComments.trim());

                DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
                Acdate.selectCalendar(coActualDate, "actualDate");

                addFieldValue("entity_create_page_requested_by_dropdown", coRequestedBy.trim());

                addFieldValue("entity_create_page_change_request_dropdown", coChangeRequest.trim());

                if (!coUploadFile.equalsIgnoreCase("")) {
                    locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\Contract\\" + coUploadFile);

                }

                // CONTRACT - CREATE PAGE - SAVE BUTTON
                WebElement elementSave = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
                Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Save Contract')]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Save Contract')]")));
                elementSave.findElement(By.xpath(".//button[contains(.,'Save Contract')]")).click();


                // CONTRACT - CREATE PAGE - FIELD VALIDATIONS
                List<WebElement> validationClassElementList = driver.findElements(By.xpath("//*[@class='errorMessageHolder']/ul/li"));
                if (!validationClassElementList.isEmpty()) {
                    for (WebElement validationClassElement : validationClassElementList) {
                        String validationMessage = validationClassElement.getText();

                        WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                        if (validationMessage.equalsIgnoreCase(""))
                            Logger.debug("For Contract -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                        else
                            Logger.debug("For Contract -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                    }

                    driver.get(CONFIG.getProperty("endUserURL"));
                    return;
                }

                // CONTRACT - CREATE PAGE - VALIDATIONS
                if (locateElementBy("entity_create_page_error_notifications") != null) {
                    List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
                    for (WebElement element : elementErrors) {
                        if (element.getText().contains("Contract Number is not Unique for this Supplier"))
                            Logger.debug("Contract Number -- " + coContractNo + " -- already exists in the system.");
                    }

                    driver.get(CONFIG.getProperty("endUserURL"));
                    return;
                }

                if (driver.findElements(By.className("success-icon")).size() != 0) {
                    String entityID = locateElementBy("csl_pop_up_id").findElement(By.id("hrefElemId")).getText();

                    Logger.debug("Contract created successfully with Entity ID " + entityID);

                    locateElementBy("csl_pop_up_id").findElement(By.id("hrefElemId")).click();


                    if (coDocumentType.equalsIgnoreCase("SOW") || coDocumentType.equalsIgnoreCase("Work Order")) {
                        fluentWaitMethod(locateElementBy("entity_show_page_plus_button"));
                        new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();


                        locateElementBy("co_child_entity_create_link").findElement(By.linkText("Map Countries")).click();


                        Assert.assertNotNull(locateElementBy("co_popup_proceed_button"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("co_popup_proceed_button"));
                        locateElementBy("co_popup_proceed_button").click();

                    }
                    locateElementBy("co_show_page_id");

                    String entityShowPageID = locateElementBy("co_show_page_id").getText();

                    Assert.assertEquals(entityShowPageID, entityID);
                }

                if (coDocumentType == "SOW" || coDocumentType == "Work Order") {
                    new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).click().build().perform();

                    new Actions(driver).moveToElement(locateElementBy("co_drop_down_menu")).clickAndHold().build().perform();


                    locateElementBy("co_childlist_menu").findElement(By.linkText("Map Countries")).click();

                    locateElementBy("co_proceed_button").click();

                }

                Logger.debug("Test Case Contract Creation for -- " + coTitle + " -- is PASSED");
                driver.get(CONFIG.getProperty("endUserURL"));
            }


    @AfterMethod
    public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(contract_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= contract_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = contract_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
            TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()), "FAIL");
    }

    @DataProvider
    public Object[][] getTestData() {
        return TestUtil.getData(contract_suite_xls, this.getClass().getSimpleName());
    }
}
