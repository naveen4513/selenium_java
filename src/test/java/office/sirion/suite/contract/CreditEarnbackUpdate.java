package office.sirion.suite.contract;

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

public class CreditEarnbackUpdate extends TestSuiteBase {
	String result = null;
    static boolean fail = true;
    String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void CreditEarnbackUpdateTest (String ceName, String ceDescription, String ceCurrency, String ceInvoiceDate, String ceInvoiceNo,
			String ceContractCreditValue, String ceActualCreditValue, String ceContractEarnbackValue, String ceActualEarnbackValue,
			String ceComments, String ceActualDate, String ceRequestedBy, String ceChangeRequest, String ceUploadFile, String ceParentContract, String ceParent
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

	

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Credit/Earnback Update with Title -- " + ceName);

        driver.get(CONFIG.getProperty("endUserURL"));

	    driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("contracts_quick_link");

		locateElementBy("contracts_quick_link").click();
		locateElementBy("entity_listing_page_display_dropdown_link");

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!ceParentContract.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + ceParentContract + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + ceParentContract + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + ceParentContract + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + ceParentContract + "']]/preceding-sibling::td[1]/a")).click();
            }
        }else{
            locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }

        locateElementBy("co_show_page_creditearnback_tab");

        locateElementBy("co_show_page_creditearnback_tab").click();


        new Select(locateElementBy("ce_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!ceParent.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='table_48']/tbody/tr/td[2][./text()='" + ceParent + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_48']/tbody/tr/td[2][./text()='" + ceParent + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e1) {
                driver.findElement(By.xpath(".//*[@id='table_48']/tbody/tr/td[2][div/div[./text()='" + ceParent + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='table_48']/tbody/tr/td[2][div/div[./text()='" + ceParent + "']]/preceding-sibling::td[1]/a")).click();
            }



            // CREDIT/EARNBACK - SHOW PAGE - EDIT BUTTON
            WebElement elementEdit = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
            elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
            locateElementBy("co_credit_earnback_create_page_name_textbox");

            // CREDIT/EARNBACK - CREATE PAGE - BASIC INFORMATION
            addFieldValue("co_credit_earnback_create_page_name_textbox", ceName.trim());

            addFieldValue("co_credit_earnback_create_page_description_textarea", ceDescription.trim());

            addFieldValue("co_credit_earnback_create_page_currency_dropdown", ceCurrency.trim());

            DatePicker_Enhanced date = new DatePicker_Enhanced();
            date.selectCalendar(ceInvoiceDate, "invoiceDate");

            addFieldValue("co_credit_earnback_create_page_invoice_number_textbox", ceInvoiceNo.trim());

            addFieldValue("co_credit_earnback_create_page_contract_credit_value_numeric_box", ceContractCreditValue.trim());

            addFieldValue("co_credit_earnback_create_page_actual_credit_value_numeric_box", ceActualCreditValue.trim());

            addFieldValue("co_credit_earnback_create_page_contract_earnback_value_numeric_box", ceContractEarnbackValue.trim());

            addFieldValue("co_credit_earnback_create_page_actual_earnback_value_numeric_box", ceActualEarnbackValue.trim());

            // CREDIT/EARNBACK - CREATE PAGE - COMMENTS AND ATTACHMENTS
            addFieldValue("entity_create_page_comments_textarea", ceComments.trim());

            DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
            Acdate.selectCalendar(ceActualDate, "actualDate");

            addFieldValue("entity_create_page_requested_by_dropdown", ceRequestedBy.trim());

            addFieldValue("entity_create_page_change_request_dropdown", ceChangeRequest.trim());

            if (!ceUploadFile.equalsIgnoreCase("")) {
                locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir") + "\\file-upload\\CreditEarnback\\" + ceUploadFile);

            }

            // CREDIT/EARNBACK - EDIT PAGE - UPDATE BUTTON
            WebElement elementUpdate = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
            Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
            elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


            // CREDIT/EARNBACK - EDIT PAGE - FIELD VALIDATIONS
            List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
            if (!validationClassElementList.isEmpty()) {
                for (WebElement validationClassElement : validationClassElementList) {
                    String validationMessage = validationClassElement.getText();

                    WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

                    if (validationMessage.equalsIgnoreCase(""))
                        Logger.debug("For Credit/Earnback -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
                    else
                        Logger.debug("For Credit/Earnback -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
                }

                driver.get(CONFIG.getProperty("endUserURL"));
                return;
            }
            locateElementBy("co_credit_earnback_create_page_name_textbox");

            String entityShowPageName = locateElementBy("co_credit_earnback_create_page_name_textbox").getText();

            Assert.assertEquals(entityShowPageName, ceName);

            Logger.debug("Test Case Credit/Earnback Update with Title -- " + ceName + " -- is PASSED");
        }else{
            Logger.debug("No Such Credit n Earnback available for selected Contract:"+ceParentContract);
            Logger.debug("Test Case Credit/Earnback Update with Title -- " + ceName + " -- is not Executed, as TestData is Incorrect");
            fail = false;
            driver.get(CONFIG.getProperty("endUserURL"));
        }
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
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_suite_xls, this.getClass().getSimpleName());
		}
	}
