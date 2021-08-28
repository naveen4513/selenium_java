package office.sirion.suite.contract;

import java.io.IOException;
import java.util.List;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApplicationCreation extends TestSuiteBase {
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
	public void ApplicationCreationTest (String applicationName, String applicationActive, String applicationParentContract, String applicationParent
		  ) throws InterruptedException {

        count++;
        if (!runmodes[count].equalsIgnoreCase("Y"))
            throw new SkipException("Runmode for Test Data Set to NO -- " + count);

      

        openBrowser();
        endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Application Creation with Title -- " + applicationName);

        driver.get(CONFIG.getProperty("endUserURL"));
        locateElementBy("contracts_quick_link");

        locateElementBy("contracts_quick_link").click();
        locateElementBy("entity_listing_page_display_dropdown_link");

        new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


        if (!applicationParentContract.equalsIgnoreCase("")) {
            try {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + applicationParentContract + "']/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][./text()='" + applicationParentContract + "']/preceding-sibling::td[1]/a")).click();
            } catch (NoSuchElementException e) {
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + applicationParentContract + "']]/preceding-sibling::td[1]/a")).isDisplayed();
                driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[3][div/div[./text()='" + applicationParentContract + "']]/preceding-sibling::td[1]/a")).click();
            }
        } else {
            locateElementBy("global_bulk_listing_page_first_entry_link").click();
        }
		try {
            locateElementBy("co_application_group_tab_link");
        }catch (NoSuchElementException e){
            locateElementBy("co_application_group_tab_link");
        }
	    locateElementBy("co_application_group_tab_link").click();


		if (!applicationParent.equalsIgnoreCase(""))
			try{
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][./text()='"+applicationParent+"']/preceding-sibling::td[2]/a")).isDisplayed();
			driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][./text()='"+applicationParent+"']/preceding-sibling::td[2]/a")).click();
			} catch (NoSuchElementException e){
                driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][div/div[./text()='"+applicationParent+"']]/preceding-sibling::td[2]/a")).isDisplayed();
		    driver.findElement(By.xpath(".//*[@id='table_46']/tbody/tr/td[3][div/div[./text()='"+applicationParent+"']]/preceding-sibling::td[2]/a")).click();
		}
		else{
		    locateElementBy("ag_listing_page_first_entry_link").click();
		}
		locateElementBy("entity_show_page_plus_button");

		new Actions(driver).moveToElement(locateElementBy("entity_show_page_plus_button")).clickAndHold().build().perform();


		driver.findElement(By.id("drop")).findElement(By.linkText("Application")).click();
		locateElementBy("co_application_create_page_name_textbox");

		// APPLICATION - CREATE PAGE - BASIC INFORMATION
		addFieldValue("co_application_create_page_name_textbox", applicationName.trim());

		addFieldValue("co_application_create_page_active_checkbox", applicationActive.trim());

		// APPLICATION - CREATE PAGE - SAVE BUTTON
	    WebElement elementSave = locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
		Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")));
	    elementSave.findElement(By.xpath(".//button[contains(.,'Save')]")).click();


		// APPLICATION - CREATE PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath("//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Application -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Application -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		if (driver.findElements(By.className("success-icon")).size()!=0) {
	    	String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

	    	Logger.debug("Application created successfully with Entity ID " + entityID);

	    	driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			locateElementBy("co_application_show_page_id");
			String entityShowPageID = locateElementBy("co_application_show_page_id").getText();

			Assert.assertEquals(entityShowPageID, entityID);
			Logger.debug("Application ID on show page has been verified");
			}

		Logger.debug("Test Case Application Creation with Title -- " + applicationName + " -- is PASSED");
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
