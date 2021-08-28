package office.sirion.suite.contract;

import java.io.IOException;
import java.util.List;
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

public class ApplicationGroupAuditLog extends TestSuiteBase {
	String result = null;
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
	public void ApplicationGroupAuditLogTest (String agName, String agActive, String agParentContract, String agParent
		  ) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

	

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Application Group Audit Log with Title -- " + agName);

	    driver.get(CONFIG.getProperty("endUserURL"));

		locateElementBy("contracts_quick_link");

		locateElementBy("contracts_quick_link").click();

		locateElementBy("entity_listing_page_display_dropdown_link");

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));

        if (!agParentContract.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + agParentContract + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + agParentContract + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][div/div[./text()='"+agParentContract+"']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3][div/div[./text()='"+agParentContract+"']]/preceding-sibling::td[1]/a")).click();
            }
        }else{
            locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }

		locateElementBy("co_application_group_tab_link");

        locateElementBy("co_application_group_tab_link").click();


		if(!agParent.equalsIgnoreCase("")){

            try{
                driver.findElement(By.xpath("//*[@id='table_46']/tbody/tr[@role='row']/td[3][./text()='"+agParent+"']/preceding-sibling::td[2]/a")).isDisplayed();
                driver.findElement(By.xpath("//*[@id='table_46']/tbody/tr[@role='row']/td[3][./text()='"+agParent+"']/preceding-sibling::td[2]/a")).click();
            }catch (NoSuchElementException e){
                driver.findElement(By.xpath("//*[@id='table_46']/tbody/tr[@role='row']/td[3][div/div[./text()='"+agParent+"']]/preceding-sibling::td[2]/a")).isDisplayed();
                driver.findElement(By.xpath("//*[@id='table_46']/tbody/tr[@role='row']/td[3][div/div[./text()='"+agParent+"']]/preceding-sibling::td[2]/a")).click();
            }

        }else{
            locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }

		locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));

		// APPLICATION GROUP - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
	    locateElementBy("co_application_group_create_page_name_textbox");

		// APPLICATION GROUP - EDIT PAGE - BASIC INFORMATION
		addFieldValue("co_application_group_create_page_name_textbox", agName.trim());

		addFieldValue("co_application_group_create_page_active_checkbox", agActive.trim());

		// APPLICATION GROUP - EDIT PAGE - SAVE BUTTON
	    WebElement elementSave = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
		Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementSave.findElement(By.xpath(".//button[contains(.,'Update')]")).click();

		locateElementBy("co_application_group_create_page_name_textbox");

	    String entityShowPageName = locateElementBy("co_application_group_create_page_name_textbox").getText();

	    Assert.assertEquals(entityShowPageName, agName);

	    locateElementBy("co_show_page_creditearnback_audit_log_tab");
        locateElementBy("co_show_page_creditearnback_audit_log_tab").click();

	    Logger.debug("Test Case Application Group Audit Log with Title -- " + agName + " -- is STARTED");

	    locateElementBy("co_data_scroll_body");
		WebElement elementOddRowSelected = locateElementBy("co_application_group_audit_log_tab_inner_part").findElements(By.className("odd")).get(0);

		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

		elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
	    locateElementBy("action_audit_log_history_table");

	    List<WebElement> auditRowElementList = locateElementBy("action_audit_log_history_table").findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
	    	for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
	    		// APPLICATION GROUP - BASIC INFORMATION
	    		if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Name")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), agName);
	    			}
	    		else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Active")) {
	    			List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
	    			Assert.assertEquals(elementDataList.get(3).getText(), agActive);
	    			}
	    		}
	    	}

	    Logger.debug("Test Case Application Group Audit Log with Title -- " + agName + " -- is PASSED");
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
