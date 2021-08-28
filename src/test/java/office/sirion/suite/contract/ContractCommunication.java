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

public class ContractCommunication extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ContractCommunicationTest (String coComments, String coActualDate, String coRequestedBy, String coChangeRequest,
			String coUploadFile, String coParent) throws Exception {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);



		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Contract Communication for -- " + coParent);

	    driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("contracts_quick_link");

		locateElementBy("contracts_quick_link").click();

		locateElementBy("entity_listing_page_display_dropdown_link");

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!coParent.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + coParent + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + coParent + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + coParent + "']]/preceding-sibling::td[1]/a")).click();
            }
        }else {
            locateElementBy("entity_listing_page_display_dropdown_link").click();
			}


		try{
		locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
		} catch (NoSuchElementException e){locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));}
		// CONTRACT - SHOW PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", coComments.trim());

		DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
		Acdate.selectCalendar(coActualDate,"actualDate");

		addFieldValue("entity_create_page_requested_by_dropdown", coRequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", coChangeRequest.trim());

		if (!coUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract\\" + coUploadFile);

			}

		// CONTRACT - SHOW PAGE - Save Comment/Attachment BUTTON
	    WebElement elementSaveComment = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
		Assert.assertNotNull(elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")).click();


		// CONTRACT - SHOW PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();

				if (entityErrors.contains("Please add a comment or upload files"))
					Logger.debug("For Contract -- " +coParent+ " -- Please add a comment or upload files");
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}

	    locateElementBy("co_create_page_name_textbox");


		locateElementBy("co_credit_earnback_communication_tab");
		locateElementBy("co_credit_earnback_communication_tab").click();

	    Logger.debug("Test Case Contract Communication for -- " + coParent + " -- is STARTED");

	    verifyCommunicationTab(coComments, coUploadFile);

	    Logger.debug("Test Case Contract Communication for -- " + coParent + " -- is PASSED");
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
		if(isTestPass)
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(contract_suite_xls, "Test Cases", TestUtil.getRowNum(contract_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_suite_xls, this.getClass().getSimpleName());
		}
	}
